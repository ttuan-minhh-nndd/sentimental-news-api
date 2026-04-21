package com.example.uit_mobileapp_lab03.data.paging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource; // Hoặc dùng ListenableFuturePagingSource

import com.example.uit_mobileapp_lab03.data.api.NewsApiService;
import com.example.uit_mobileapp_lab03.data.model.Article;
import com.example.uit_mobileapp_lab03.data.model.ArticleWithSentiment;
import com.example.uit_mobileapp_lab03.data.model.NewsResponse;
import com.example.uit_mobileapp_lab03.logic.SentimentAnalyzer;
import com.example.uit_mobileapp_lab03.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewsPagingSource extends RxPagingSource<Integer, ArticleWithSentiment> {

    private final NewsApiService apiService;
    private final SentimentAnalyzer sentimentAnalyzer;
    private final String query;

    public NewsPagingSource(NewsApiService apiService, SentimentAnalyzer sentimentAnalyzer, String query) {
        this.apiService = apiService;
        this.sentimentAnalyzer = sentimentAnalyzer;
        this.query = query; // Truyền query vào constructor
    }

    @NonNull
    @Override
    public Single<PagingSource.LoadResult<Integer, ArticleWithSentiment>> loadSingle(@NonNull PagingSource.LoadParams<Integer> params) {
        // 1. Xác định trang hiện tại (mặc định là trang 1)
        int page = (params.getKey() != null) ? params.getKey() : 1;

        // 2. Gọi API thông qua Retrofit
        return apiService.getArticles("android", params.getLoadSize(), page, BuildConfig.NEWS_API_KEY)
                .subscribeOn(Schedulers.io())
                .map(response -> toLoadResult(response, page))
                .onErrorReturn(LoadResult.Error::new);
    }

    private PagingSource.LoadResult<Integer, ArticleWithSentiment> toLoadResult(NewsResponse response, int page) {
        List<ArticleWithSentiment> articlesWithSentiment = new ArrayList<>();

        // 3. Vòng lặp kết hợp dữ liệu bài báo với AI Sentiment
        for (Article article : response.getArticles()) {
            // Lấy nội dung để phân tích, nếu không có thì dùng tiêu đề
            String textToAnalyze = (article.getContent() != null) ? article.getContent() : article.getTitle();

            // Chạy model AI đã nạp từ Giai đoạn 1
            float[] scores = sentimentAnalyzer.analyze(textToAnalyze);

            // Đóng gói vào model Wrapper
            articlesWithSentiment.add(new ArticleWithSentiment(article, scores[0], scores[1]));
        }

        // 4. Trả về kết quả phân trang (Page)
        return new LoadResult.Page<>(
                articlesWithSentiment,
                (page == 1) ? null : page - 1, // prevKey
                (articlesWithSentiment.isEmpty()) ? null : page + 1 // nextKey
        );
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, ArticleWithSentiment> state) {
        // Try to find the page key of the closest item to the anchor position
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        var anchorPage = state.closestPageToPosition(anchorPosition);
        if (anchorPage == null) return null;

        return anchorPage.getPrevKey() != null ? anchorPage.getPrevKey() + 1 : anchorPage.getNextKey() != null ? anchorPage.getNextKey() - 1 : null;
    }
}
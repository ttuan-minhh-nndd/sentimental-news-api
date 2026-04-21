package com.example.uit_mobileapp_lab03.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;
import androidx.paging.rxjava3.PagingRx;

import com.example.uit_mobileapp_lab03.data.api.NewsApiService;
import com.example.uit_mobileapp_lab03.data.model.ArticleWithSentiment;
import com.example.uit_mobileapp_lab03.data.paging.NewsPagingSource;
import com.example.uit_mobileapp_lab03.logic.SentimentAnalyzer;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class NewsViewModel extends AndroidViewModel {

    private final NewsApiService apiService;
    private final SentimentAnalyzer sentimentAnalyzer;

    public NewsViewModel(@NonNull Application application, NewsApiService apiService) {
        super(application);
        this.apiService = apiService;
        this.sentimentAnalyzer = new SentimentAnalyzer(application);
    }

    public Flowable<PagingData<ArticleWithSentiment>> getNewsFlowable(String query) {
        // Configure the Pager
        Pager<Integer, ArticleWithSentiment> pager = new Pager<>(
                new PagingConfig(
                        20, // pageSize
                        5,  // prefetchDistance
                        false // enablePlaceholders
                ),
                () -> new NewsPagingSource(apiService, sentimentAnalyzer, query)
        );

        // Convert Pager to Flowable for RxJava3 support
        return PagingRx.getFlowable(pager);
    }

    // Optional: If you prefer LiveData in your UI
    public LiveData<PagingData<ArticleWithSentiment>> getNewsLiveData(String query) {
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, ArticleWithSentiment> pager = new Pager<>(
                new PagingConfig(20),
                () -> new NewsPagingSource(apiService, sentimentAnalyzer, query)
        );

        return PagingLiveData.getLiveData(pager);
    }
}
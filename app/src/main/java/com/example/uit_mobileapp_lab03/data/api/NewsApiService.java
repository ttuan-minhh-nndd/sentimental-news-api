package com.example.uit_mobileapp_lab03.data.api;
import com.example.uit_mobileapp_lab03.data.model.NewsResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("v2/everything") // Endpoint lấy tất cả tin tức
    Single<NewsResponse> getArticles(
            @Query("q") String query,         // Từ khóa tìm kiếm
            @Query("pageSize") int pageSize,  // Số lượng item mỗi trang
            @Query("page") int page,          // Trang hiện tại
            @Query("apiKey") String apiKey    // API Key của em
    );
}
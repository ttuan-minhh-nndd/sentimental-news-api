package com.example.uit_mobileapp_lab03.data.model;
import java.util.List;

public class NewsResponse {
    private List<Article> articles; // Danh sách các bài báo [cite: 413]
    private int totalResults;       // Tổng số bài báo tìm thấy để phục vụ phân trang [cite: 414]

    public List<Article> getArticles() { return articles; }
    public int getTotalResults() { return totalResults; }
}
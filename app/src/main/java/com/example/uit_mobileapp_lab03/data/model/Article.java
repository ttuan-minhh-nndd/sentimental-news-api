package com.example.uit_mobileapp_lab03.data.model;

public class Article {
    private String title;          // Tiêu đề bài báo [cite: 417]
    private String content;        // Nội dung chi tiết [cite: 418]
    private String urlToImage;     // Link ảnh minh họa [cite: 419]
    private String publishedAt;    // Thời gian đăng [cite: 420]

    // Getter và Setter (Em có thể dùng phím tắt Alt+Insert trong Android Studio để tạo nhanh)
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getUrlToImage() { return urlToImage; }
    public String getPublishedAt() { return publishedAt; }
}
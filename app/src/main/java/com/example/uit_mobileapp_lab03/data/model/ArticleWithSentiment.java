package com.example.uit_mobileapp_lab03.data.model;

public class ArticleWithSentiment {
    private Article article;
    private float positiveScore;
    private float negativeScore;

    public ArticleWithSentiment(Article article, float pos, float neg) {
        this.article = article;
        this.positiveScore = pos;
        this.negativeScore = neg;
    }

    // Getters
    public Article getArticle() { return article; }
    public float getPositiveScore() { return positiveScore; }
    public float getNegativeScore() { return negativeScore; }
}
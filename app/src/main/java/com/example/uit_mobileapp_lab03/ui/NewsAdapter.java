package com.example.uit_mobileapp_lab03.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uit_mobileapp_lab03.R;
import com.example.uit_mobileapp_lab03.data.model.Article;
import com.example.uit_mobileapp_lab03.data.model.ArticleWithSentiment;

public class NewsAdapter extends PagingDataAdapter<ArticleWithSentiment, NewsAdapter.NewsViewHolder> {

    public NewsAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        ArticleWithSentiment item = getItem(position);
        if (item != null) {
            Article article = item.getArticle();
            holder.tvTitle.setText(article.getTitle());
            holder.tvContent.setText(article.getContent());

            // Sentiment Logic
            if (item.getPositiveScore() > item.getNegativeScore()) {
                // Color.GREEN with low alpha (e.g., 0x33 green)
                holder.itemView.setBackgroundColor(Color.argb(50, 0, 255, 0));
            } else {
                // Color.RED with low alpha
                holder.itemView.setBackgroundColor(Color.argb(50, 255, 0, 0));
            }

            // Load Image with Glide
            Glide.with(holder.ivNews.getContext())
                    .load(article.getUrlToImage())
                    .placeholder(android.R.color.darker_gray)
                    .into(holder.ivNews);
        }
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNews;
        TextView tvTitle, tvContent;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNews = itemView.findViewById(R.id.ivNews);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }

    private static final DiffUtil.ItemCallback<ArticleWithSentiment> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ArticleWithSentiment>() {
                @Override
                public boolean areItemsTheSame(@NonNull ArticleWithSentiment oldItem, @NonNull ArticleWithSentiment newItem) {
                    return oldItem.getArticle().getTitle().equals(newItem.getArticle().getTitle());
                }

                @Override
                public boolean areContentsTheSame(@NonNull ArticleWithSentiment oldItem, @NonNull ArticleWithSentiment newItem) {
                    return oldItem.getArticle().getTitle().equals(newItem.getArticle().getTitle()) &&
                            oldItem.getPositiveScore() == newItem.getPositiveScore() &&
                            oldItem.getNegativeScore() == newItem.getNegativeScore();
                }
            };
}
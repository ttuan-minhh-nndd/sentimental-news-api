package com.example.uit_mobileapp_lab03.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uit_mobileapp_lab03.R;
import com.example.uit_mobileapp_lab03.data.api.NewsApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private NewsViewModel viewModel;
    private NewsAdapter adapter;
    private RecyclerView rvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvNews = findViewById(R.id.rvNews);
        adapter = new NewsAdapter();
        rvNews.setAdapter(adapter);

        // Simple Retrofit setup (usually done in a repository or module)
        NewsApiService apiService = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(NewsApiService.class);

        // Using a basic Factory to pass apiService to NewsViewModel
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @Override
            public <T extends androidx.lifecycle.ViewModel> T create(Class<T> modelClass) {
                return (T) new NewsViewModel(getApplication(), apiService);
            }
        }).get(NewsViewModel.class);

        // Observe the PagingData
        viewModel.getNewsLiveData("android").observe(this, pagingData -> {
            adapter.submitData(getLifecycle(), pagingData);
        });
    }
}
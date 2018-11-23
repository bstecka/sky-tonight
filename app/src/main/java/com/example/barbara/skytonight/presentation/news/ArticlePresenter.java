package com.example.barbara.skytonight.presentation.news;
import android.content.Context;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import kevenchen.utils.WebImageView;
import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.data.NewsDataSource;
import com.example.barbara.skytonight.data.remote.ArticleFetchService;
import com.example.barbara.skytonight.data.remote.NewsRemoteDataSource;
import com.example.barbara.skytonight.data.repository.NewsRepository;
import com.example.barbara.skytonight.entity.ArticleContentWrapper;

import java.util.ArrayList;

public class ArticlePresenter implements ArticleContract.Presenter {

    private final NewsRepository mNewsRepository;
    private final ArticleContract.View mArticleView;

    public ArticlePresenter(ArticleContract.View mArticleView) {
        this.mArticleView = mArticleView;
        Context context = mArticleView.getContext().getApplicationContext();
        this.mNewsRepository = NewsRepository.getInstance(NewsRemoteDataSource.getInstance(context), new ArticleFetchService(context));
    }

    @Override
    public void start() {
        if (mArticleView.getArticleUrl() != null)
            getNewsArticle(mArticleView.getArticleUrl());
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        this.mNewsRepository.setBaseUrl(baseUrl);
    }

    private ArrayList<View> getArticleContentViews(ArrayList<ArticleContentWrapper> articleChunks, String url) {
        final ArrayList<View> views = new ArrayList<>();
        for (ArticleContentWrapper chunk : articleChunks) {
            if (chunk.hasText())
                views.add(makeTextView(chunk.getText()));
            else if (chunk.hasImage())
                views.add(makeImageView(chunk.getImageUrl()));
        }
        views.add(makeTextView(url));
        return views;
    }

    private WebImageView makeImageView(String url) {
        WebImageView imageView = new WebImageView(mArticleView.getContext());
        imageView.showImageUrl(url);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
        layoutParams.setMargins(0, 10, 0, 20);
        imageView.setLayoutParams(layoutParams);
        imageView.reload();
        return imageView;
    }

    private TextView makeTextView(String text) {
        TextView textView = new TextView(mArticleView.getContext());
        textView.setPadding(0, 0, 0, 10);
        textView.setText(text);
        textView.setTextAppearance(R.style.ArticleText);
        Linkify.addLinks(textView, Linkify.WEB_URLS);
        return textView;
    }

    private void getNewsArticle(final String url) {
        mNewsRepository.getNewsArticle(url, new NewsDataSource.GetNewsArticleCallback() {
            @Override
            public void onDataLoaded(ArrayList<ArticleContentWrapper> articleChunks) {
                mArticleView.addArticleViews(getArticleContentViews(articleChunks, url));
            }

            @Override
            public void onDataNotAvailable() {
                Log.e("ArticlePresenter", "data not available");
            }
        });
    }
}

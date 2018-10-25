package com.example.barbara.skytonight.news;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.data.NewsRepository;
import com.example.barbara.skytonight.data.remote.ArticleFetchService;
import com.example.barbara.skytonight.data.remote.NewsRemoteDataSource;
import com.example.barbara.skytonight.notes.NoteFragment;
import com.example.barbara.skytonight.notes.NotePresenter;
import com.example.barbara.skytonight.util.AppConstants;

public class ArticleActivity extends AppCompatActivity {

    private String baseUrl = AppConstants.NEWS_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        String articleUrl = null, articleTitle = null, articlePubDate = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            articleUrl = bundle.getString("articleUrl");
            articleTitle = bundle.getString("articleTitle");
            articlePubDate = bundle.getString("articlePubDate");
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.articleFragment);
        ArticleFragment articleFragment = (ArticleFragment) currentFragment;
        if (articleFragment == null) {
            articleFragment = new ArticleFragment();
            NewsRepository newsRepository = NewsRepository.getInstance(NewsRemoteDataSource.getInstance(this), new ArticleFetchService(this));
            newsRepository.setBaseUrl(baseUrl);
            articleFragment.setPresenter(new ArticlePresenter(newsRepository, articleFragment));
            articleFragment.setArticleUrl(articleUrl);
            articleFragment.setTitle(articleTitle);
            articleFragment.setPubDate(articlePubDate);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.articleFragment, articleFragment);
            transaction.commit();
        }
        else {
            NewsRepository newsRepository = NewsRepository.getInstance(NewsRemoteDataSource.getInstance(this), new ArticleFetchService(this));
            newsRepository.setBaseUrl(baseUrl);
            articleFragment.setPresenter(new ArticlePresenter(newsRepository, articleFragment));
            articleFragment.setArticleUrl(articleUrl);
            articleFragment.setTitle(articleTitle);
            articleFragment.setPubDate(articlePubDate);
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}

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

public class ArticleActivity extends AppCompatActivity {

    private ArticleFragment articleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        String articleUrl = null, articleTitle = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            articleUrl = bundle.getString("articleUrl");
            articleTitle = bundle.getString("articleTitle");
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.articleFragment);
        articleFragment = (ArticleFragment) currentFragment;
        if (articleFragment == null) {
            articleFragment = new ArticleFragment();
            articleFragment.setPresenter(new ArticlePresenter(NewsRepository.getInstance(NewsRemoteDataSource.getInstance(this), new ArticleFetchService(this)), articleFragment));
            articleFragment.setArticleUrl(articleUrl);
            articleFragment.setTitle(articleTitle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.articleFragment, articleFragment);
            transaction.commit();
        }
        else {
            articleFragment.setPresenter(new ArticlePresenter(NewsRepository.getInstance(NewsRemoteDataSource.getInstance(this), new ArticleFetchService(this)), articleFragment));
            articleFragment.setArticleUrl(articleUrl);
            articleFragment.setTitle(articleTitle);
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}

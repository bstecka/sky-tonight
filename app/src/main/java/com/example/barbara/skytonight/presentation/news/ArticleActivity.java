package com.example.barbara.skytonight.presentation.news;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.AppConstants;
import com.example.barbara.skytonight.presentation.util.LocaleHelper;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

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
            articleFragment.setPresenter(new ArticlePresenter(articleFragment));
            articleFragment.setArticleUrl(articleUrl);
            articleFragment.setTitle(articleTitle);
            articleFragment.setPubDate(articlePubDate);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.articleFragment, articleFragment);
            transaction.commit();
        }
        else {
            articleFragment.setPresenter(new ArticlePresenter(articleFragment));
            articleFragment.setArticleUrl(articleUrl);
            articleFragment.setTitle(articleTitle);
            articleFragment.setPubDate(articlePubDate);
            articleFragment.setBaseUrlForLanguage(getBaseUrlForLanguage());
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private String getBaseUrlForLanguage() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String language = preferences.getString(AppConstants.PREF_KEY_LANG, AppConstants.LANG_EN);
        if (language.equals(AppConstants.LANG_PL))
            return AppConstants.NEWS_URL_PL;
        else
            return AppConstants.NEWS_URL_EN;
    }

}

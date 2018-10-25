package com.example.barbara.skytonight.news;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.notes.NoteContract;

import java.util.Calendar;

public class ArticleFragment extends Fragment implements ArticleContract.View {

    private ArticleContract.Presenter mPresenter;
    private View view;
    private String articleUrl;

    @Override
    public void setPresenter(ArticleContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_article, container, false);
        return view;
    }

    @Override
    public Context getContext() { return view.getContext(); }

    public void setText(String text) {
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(text);
        textView.refreshDrawableState();
    }

    public void setTitle(String title) {
        TextView textView = view.findViewById(R.id.titleTextView);
        textView.setText(title);
        textView.refreshDrawableState();
    }

    public void setPubDate(String pubDate) {
        TextView textView = view.findViewById(R.id.dateTextView);
        textView.setText(pubDate);
        textView.refreshDrawableState();
    }

    @Override
    public Activity getViewActivity() { return getActivity(); }

    @Override
    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String url) {
        this.articleUrl = url;
    }

}

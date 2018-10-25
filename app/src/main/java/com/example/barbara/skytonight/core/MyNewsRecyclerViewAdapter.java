package com.example.barbara.skytonight.core;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.core.NewsFragment.OnListFragmentInteractionListener;
import com.example.barbara.skytonight.news.ArticleActivity;
import com.example.barbara.skytonight.notes.NoteActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MyNewsRecyclerViewAdapter extends RecyclerView.Adapter<MyNewsRecyclerViewAdapter.ViewHolder> {

    private final List<NewsHeadline> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyNewsRecyclerViewAdapter(List<NewsHeadline> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final NewsHeadline newsObject = mValues.get(position);
        holder.mItem = newsObject;
        holder.mTitleView.setText(newsObject.getTitle());
        final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm:dd", Locale.ENGLISH);
        holder.mDateView.setText(sdf.format(newsObject.getPubDate().getTime()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("articleUrl", newsObject.getUrl());
                intent.putExtra("articleTitle", newsObject.getTitle());
                intent.putExtra("articlePubDate", sdf.format(newsObject.getPubDate().getTime()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTitleView;
        final TextView mDateView;
        public NewsHeadline mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.newsTitleTextView);
            mDateView = view.findViewById(R.id.newsPubDateTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'";
        }
    }
}

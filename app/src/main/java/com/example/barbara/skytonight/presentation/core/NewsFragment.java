package com.example.barbara.skytonight.presentation.core;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.AstroEvent;
import com.example.barbara.skytonight.entity.NewsHeadline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NewsFragment extends Fragment implements NewsContract.View {

    private OnListFragmentInteractionListener mListener;
    private NewsRecyclerViewAdapter mAdapter;
    private NewsContract.Presenter mPresenter;
    ArrayList<NewsHeadline> list;

    public NewsFragment() {
        list = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public void setBaseUrlForLanguage() {
        mPresenter.setUrlsForLanguage();
    }

    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAdapter = new NewsRecyclerViewAdapter(list, mListener);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void showErrorText() {
        if (getView() != null && getContext() != null) {
            View rView = getView().findViewById(R.id.newsRecyclerView);
            rView.setVisibility(View.INVISIBLE);
            TextView textView = getView().findViewById(R.id.errorTextView);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideErrorText() {
        if (getView() != null && getContext() != null) {
            View rView = getView().findViewById(R.id.newsRecyclerView);
            rView.setVisibility(View.VISIBLE);
            TextView textView = getView().findViewById(R.id.errorTextView);
            textView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void clearList() {
        this.list.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateList(ArrayList<NewsHeadline> list) {
        this.list.clear();
        Collections.sort(list, new Comparator<NewsHeadline>() {
            @Override
            public int compare(NewsHeadline o1, NewsHeadline o2) {
                if (o1.getPubDate().getTimeInMillis() == o2.getPubDate().getTimeInMillis())
                    return 0;
                return o1.getPubDate().getTimeInMillis() > o2.getPubDate().getTimeInMillis() ? -1 : 1;
            }
        });
        this.list.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(NewsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(AstroEvent event);
    }
}

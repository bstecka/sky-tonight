package com.example.barbara.skytonight.presentation.details;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barbara.skytonight.R;

import kevenchen.utils.WebImageView;

public class SolarDetailsFragment extends Fragment implements SolarDetailsContract.View {

    private SolarDetailsContract.Presenter mPresenter;
    private View view;

    @Override
    public void setPresenter(SolarDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_solar_details, container, false);
        return view;
    }

    @Override
    public void setImage(String imageUrl) {
        WebImageView webImageView = view.findViewById(R.id.webImageView);
        webImageView.loadUrl(imageUrl);
        view.findViewById(R.id.title2).setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(String name, String date) {
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        Context context = dateTextView.getContext();
        try {
            int eclipseTypeStringId = context.getResources().getIdentifier(name, "string", context.getPackageName());
            titleTextView.setText(context.getResources().getString(eclipseTypeStringId));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        dateTextView.setText(date);
    }

    @Override
    public void setTimeLine(String greatestEclipse){
        view.findViewById(R.id.title1).setVisibility(View.VISIBLE);
        TextView greatestTextView = view.findViewById(R.id.greatestEclipse);
        Context context = view.getContext();
        greatestTextView.setText(context.getString(R.string.greatest_eclipse, greatestEclipse));
    }

    @Override
    public Context getContext() { return view.getContext(); }

    @Override
    public Activity getViewActivity() { return getActivity(); }

}


package com.example.barbara.skytonight.presentation.details;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barbara.skytonight.R;

import java.util.Calendar;

public class LunarDetailsFragment extends Fragment implements LunarDetailsContract.View {

    private LunarDetailsContract.Presenter mPresenter;
    private View view;

    @Override
    public void setPresenter(LunarDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_lunar_details, container, false);
        return view;
    }

    @Override
    public Context getContext() { return view.getContext(); }

    @Override
    public Activity getViewActivity() { return getActivity(); }

}


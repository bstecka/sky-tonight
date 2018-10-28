package com.example.barbara.skytonight.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.news.ArticleContract;
import com.example.barbara.skytonight.util.AppConstants;

import java.util.ArrayList;

public class SettingsFragment extends Fragment implements SettingsContract.View {

    private SettingsContract.Presenter mPresenter;
    private View view;

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setLanguageSelected(int language) {
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        if (language == AppConstants.LANG_EN)
            radioGroup.check(R.id.radioButtonEn);
        else if (language == AppConstants.LANG_PL)
            radioGroup.check(R.id.radioButtonPl);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }

    @Override
    public Context getContext() { return view.getContext(); }

    @Override
    public Activity getViewActivity() { return getActivity(); }

}

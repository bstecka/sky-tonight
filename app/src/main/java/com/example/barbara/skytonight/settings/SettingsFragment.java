package com.example.barbara.skytonight.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.news.ArticleContract;
import com.example.barbara.skytonight.util.AppConstants;

import java.util.ArrayList;

public class SettingsFragment extends Fragment implements SettingsContract.View {

    private SettingsContract.Presenter mPresenter;
    private boolean languageChanged = false;
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

    public boolean wasLanguageChanged() {
        return languageChanged;
    }

    @Override
    public void setLanguageSelected(String language) {
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        if (language.equals(AppConstants.LANG_EN))
            radioGroup.check(R.id.radioButtonEn);
        else if (language.equals(AppConstants.LANG_PL))
            radioGroup.check(R.id.radioButtonPl);
    }

    @Override public void setUserChoiceListener() {
        final Context context = view.getContext();
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonEn) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    prefs.edit().putString(AppConstants.PREF_KEY_LANG, AppConstants.LANG_EN).commit();
                    Toast.makeText(context, R.string.language_changed_en, Toast.LENGTH_SHORT).show();
                }
                else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    prefs.edit().putString(AppConstants.PREF_KEY_LANG, AppConstants.LANG_PL).commit();
                    Toast.makeText(context, R.string.language_changed_pl, Toast.LENGTH_SHORT).show();
                }
                languageChanged = true;
            }
        });
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

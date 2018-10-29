package com.example.barbara.skytonight.settings;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.barbara.skytonight.util.AppConstants;

public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View settingsView;

    public SettingsPresenter(SettingsContract.View settingsView) {
        this.settingsView = settingsView;
    }

    @Override
    public void start() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsView.getContext());
        settingsView.setLanguageSelected(preferences.getString(AppConstants.PREF_KEY_LANG, AppConstants.LANG_EN));
        settingsView.setUserChoiceListener();
    }
}

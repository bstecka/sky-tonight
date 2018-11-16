package com.example.barbara.skytonight.presentation.settings;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.barbara.skytonight.AppConstants;

public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View settingsView;

    public SettingsPresenter(SettingsContract.View settingsView) {
        this.settingsView = settingsView;
    }

    @Override
    public void start() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(settingsView.getContext());
        settingsView.setUserSelection(preferences.getString(AppConstants.PREF_KEY_LANG, AppConstants.LANG_EN));
        settingsView.setUserChoiceListeners();
    }
}

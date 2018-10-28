package com.example.barbara.skytonight.settings;

import com.example.barbara.skytonight.util.AppConstants;

public class SettingsPresenter implements SettingsContract.Presenter {

    private final SettingsContract.View settingsView;

    public SettingsPresenter(SettingsContract.View settingsView) {
        this.settingsView = settingsView;
    }

    @Override
    public void start() {
        settingsView.setLanguageSelected(AppConstants.LANG_EN);
    }
}

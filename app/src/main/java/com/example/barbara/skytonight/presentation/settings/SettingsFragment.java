package com.example.barbara.skytonight.presentation.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.AppConstants;

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
    public void setUserSelection(String language) {
        setLanguageSelected(language);
        setNotificationSwitches();
    }

    @Override
    public void setUserChoiceListeners() {
        setLanguageListeners();
        setNotificationListeners();
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

    private void setNotificationSwitches() {
        Switch switchEvent = view.findViewById(R.id.switchEvent);
        Switch switchISS = view.findViewById(R.id.switchISS);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        switchEvent.setChecked(preferences.getBoolean(AppConstants.PREF_KEY_NOTIF_EVENTS, true));
        switchISS.setChecked(preferences.getBoolean(AppConstants.PREF_KEY_NOTIF_ISS, true));
    }

    private void setLanguageSelected(String language) {
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        if (language.equals(AppConstants.LANG_EN))
            radioGroup.check(R.id.radioButtonEn);
        else if (language.equals(AppConstants.LANG_PL))
            radioGroup.check(R.id.radioButtonPl);
        Switch switchEn = view.findViewById(R.id.switchEn);
        Switch switchPl = view.findViewById(R.id.switchPl);
        if (language.equals(AppConstants.LANG_EN))
            switchEn.setChecked(true);
        else if (language.equals(AppConstants.LANG_PL))
            switchPl.setChecked(true);
    }

    private void setNotificationListeners() {
        final Context context = view.getContext();
        Switch switchEvent = view.findViewById(R.id.switchEvent);
        Switch switchISS = view.findViewById(R.id.switchISS);
        switchEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                prefs.edit().putBoolean(AppConstants.PREF_KEY_NOTIF_EVENTS, isChecked).commit();
                Toast.makeText(context, R.string.notif_changed, Toast.LENGTH_SHORT).show();
            }
        });
        switchISS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                prefs.edit().putBoolean(AppConstants.PREF_KEY_NOTIF_ISS, isChecked).commit();
                Toast.makeText(context, R.string.notif_changed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLanguageListeners() {
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
        final Switch switchEn = view.findViewById(R.id.switchEn);
        final Switch switchPl = view.findViewById(R.id.switchPl);
        switchEn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    prefs.edit().putString(AppConstants.PREF_KEY_LANG, AppConstants.LANG_EN).commit();
                    Toast.makeText(context, R.string.language_changed_en, Toast.LENGTH_SHORT).show();
                    switchPl.setChecked(false);
                }
                else {
                    switchPl.setChecked(true);
                }
            }
        });
        switchPl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    prefs.edit().putString(AppConstants.PREF_KEY_LANG, AppConstants.LANG_PL).commit();
                    Toast.makeText(context, R.string.language_changed_en, Toast.LENGTH_SHORT).show();
                    switchEn.setChecked(false);
                }
                else {
                    switchEn.setChecked(true);
                }
            }
        });
    }

}

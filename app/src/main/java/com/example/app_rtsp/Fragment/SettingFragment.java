package com.example.app_rtsp.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.app_rtsp.Activity.About;
import com.example.app_rtsp.R;

public class SettingFragment extends PreferenceFragment {
    private String ABOUT = "key_infor";
    private String keyNumber = "";

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private float keyTheme;
    private float keyOrientation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        sharedPreferences = getActivity().getSharedPreferences("pref", 0);
        keyTheme = sharedPreferences.getFloat("keyTheme", -1);
        editor = sharedPreferences.edit();

        Preference myPref = (Preference) findPreference(ABOUT);
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), About.class);
                startActivity(intent);
                return false;
            }
        });

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key) {
                    case "key_themes":
                        keyNumber = sharedPreferences.getString(key, "");
                        switch (keyNumber) {
                            case "0":
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                                editor.putFloat("keyTheme", -1);
                                break;
                            case "1":
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                editor.putFloat("keyTheme", 2);
                                break;
                            case "2":
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                editor.putFloat("keyTheme", 1);
                                break;
                        }
                        break;
                    case "key_screen_orientation":
                        keyNumber = sharedPreferences.getString(key, "");
                        switch (keyNumber) {
                            case "0":
                                editor.putFloat("keyOrientation", ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                                break;
                            case "1":
                                editor.putFloat("keyOrientation", ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                break;
                        }
                        break;
                    case "keyorien_mode":
                        keyNumber = sharedPreferences.getString(key, "");
                        switch (keyNumber) {
                            case "0":
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
                                break;
                            case "1":
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                                break;
                            case "2":
                                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                break;
                        }
                        break;
                }
                editor.commit();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}

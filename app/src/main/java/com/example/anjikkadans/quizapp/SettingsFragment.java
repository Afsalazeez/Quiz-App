package com.example.anjikkadans.quizapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

/**
 * Created by Anjikkadan's on 5/4/2018.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_quiz);

        SharedPreferences preferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        int count = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < count ; i++) {
            Preference p = preferenceScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference)) {
                String value = preferences.getString(p.getKey(),"");
                setPreferenceSummary(p,value);
            }

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);
        if (null != preference) {
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(preference.getKey(),"");
                setPreferenceSummary(preference,value);
                Log.e("preference",value);
            }
        }

    }

    private void setPreferenceSummary(Preference preference,String summary){
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(summary);

            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}

package iu9.bmstu.ru.lab7.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import java.util.HashSet;
import java.util.Set;

import iu9.bmstu.ru.lab7.R;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

//        if (preference instanceof ListPreference) {
//            ListPreference listPreference = (ListPreference) preference;
//            String value = sharedPreferences.getString(preference.getKey(), "");
//            int index = listPreference.findIndexOfValue(value);
//
//            if (index >= 0) {
//                preference.setSummary(listPreference.getEntries()[index]);
//            }
//        }
//        else if (preference instanceof MultiSelectListPreference) {
//            MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) preference;
//            Set<String> values = sharedPreferences.getStringSet(preference.getKey(),null);
//
//            String summary = "";
//
//            if (values != null) {
//                for (String value : values) {
//                    if (!value.isEmpty())
//                        summary += ", ";
//                    summary += multiSelectListPreference.getEntries()[multiSelectListPreference.findIndexOfValue(value)];
//                }
//            }
//
//            if (!summary.isEmpty())
//                preference.setSummary(summary);
//        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_fragment);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        sharedPreferences = preferenceScreen.getSharedPreferences();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}

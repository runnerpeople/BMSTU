package iu9.bmstu.ru.lab7.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SeekBarPreference;

import java.util.Locale;

import iu9.bmstu.ru.lab7.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences sharedPreferences;

    private void setSummary(Preference preference) {
        if (preference instanceof SeekBarPreference) {
            SeekBarPreference seekBarPreference = (SeekBarPreference) preference;
            int value = sharedPreferences.getInt(preference.getKey(), 1);

            String days;
            if (Locale.getDefault().equals(new Locale("ru","RU"))) {
                if (value % 20 == 1)
                    days = "день";
                else if (value % 20 < 5)
                    days = getString(R.string.day);
                else
                    days = getString(R.string.days);
            }
            else
                days = getString(R.string.days);

            preference.setSummary(seekBarPreference.getValue() + " " + days);

        }
        else if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            String value = sharedPreferences.getString(preference.getKey(),null);
            int index = listPreference.findIndexOfValue(value);

            if (index >= 0) {
                preference.setSummary(listPreference.getEntries()[index]);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        setSummary(preference);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_fragment);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        Preference periodPreference = findPreference(getString(R.string.pref_period_key));
        Preference currencyPreference = findPreference(getString(R.string.pref_currency_key));

        setSummary(periodPreference);
        setSummary(currencyPreference);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}

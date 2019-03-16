package iu9.bmstu.ru.lab6;


import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    private SharedPreferences mSharedPreferences;

    private static final String TAG = SettingsFragment.class.getName();

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        addPreferencesFromResource(R.xml.pref_settings);

        Preference switchPreference = findPreference(getString(R.string.pref_switch_key));
        Preference editPreference = findPreference(getString(R.string.pref_edit_key));
        editPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (preference.getKey().equals(getString(R.string.pref_edit_key))) {
                    try {
                        String value = (String)newValue;
                        if (value.matches(".*\\d+.*")) {
                            Toast.makeText(getActivity().getApplicationContext(),"Имя не должно содержать цифр",Toast.LENGTH_LONG).show();
                            return false;
                        }
                        if (value.trim().length() == 0) {
                            Toast.makeText(getActivity().getApplicationContext(),"Имя должно содержать хотя бы одну букву",Toast.LENGTH_LONG).show();
                            return false;
                        }
                        else
                            return true;
                    }
                    catch (ClassCastException ex) {
                        Log.e(TAG, "onPreferenceChange: " + ex.getMessage());
                        return false;
                    }
                }
                return true;
            }

        });
        Preference checkPreference = findPreference(getString(R.string.pref_check_key));
        setSummary(switchPreference);
        setSummary(editPreference);
        setSummary(checkPreference);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);

        if (preference != null) {

            if (preference instanceof SwitchPreference) {

                Boolean torchEnable = mSharedPreferences.getBoolean(preference.getKey(),false);

                CameraManager cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
                if (torchEnable) {
                    try {
                        String cameraId = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraId, true);
                    } catch (CameraAccessException e) {
                    }
                }
                else {
                    try {
                        String cameraId = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraId,false);
                    } catch (CameraAccessException e) {
                    }
                }

            }
            else if (preference instanceof CheckBoxPreference) {

                Boolean isVibrate = mSharedPreferences.getBoolean(preference.getKey(),false);

                if (isVibrate) {
                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(500);
                    }
                }
            }
        }

        setSummary(preference);
    }

    private void setSummary(Preference preference) {
        if (preference != null) {

            if (preference instanceof SwitchPreference) {
                Boolean torchEnable = mSharedPreferences.getBoolean(preference.getKey(),false);
                preference.setSummary(torchEnable ? "Фонарик включен" : "Фонарик выключен");
            }

            if (preference instanceof EditTextPreference) {
                String string = mSharedPreferences.getString(preference.getKey(), "123");
                preference.setSummary(string);
            }

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                String string = mSharedPreferences.getString(preference.getKey(), "");
                int index = listPreference.findIndexOfValue(string);

                if (index >= 0) {
                    preference.setSummary(listPreference.getEntries()[index]);
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
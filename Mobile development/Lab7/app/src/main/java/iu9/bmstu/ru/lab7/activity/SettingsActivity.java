package iu9.bmstu.ru.lab7.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import iu9.bmstu.ru.lab7.R;
import iu9.bmstu.ru.lab7.fragment.SettingsFragment;


public class SettingsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }

}

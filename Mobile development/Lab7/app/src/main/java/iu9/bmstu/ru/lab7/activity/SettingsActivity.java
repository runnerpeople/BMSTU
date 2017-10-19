package iu9.bmstu.ru.lab7.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import iu9.bmstu.ru.lab7.R;
import iu9.bmstu.ru.lab7.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();

    }

}

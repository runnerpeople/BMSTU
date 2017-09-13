package iu9.bmstu.ru.application;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Spinner spinnerMagazine;
    private TextView textJson;

    private final static String apiKey = "d05c0860bb624b348cefaf0f3a97cfba";

    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("locale")) {
                restartActivity();
            }
        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String language = sharedPreferences.getString("locale","en");
        Context context = ContextWrapper.wrap(newBase, new Locale(language));
        super.attachBaseContext(context);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bar_update: {
                int index = (spinnerMagazine.getSelectedItemPosition() == -1) ? 0 : spinnerMagazine.getSelectedItemPosition();
                new NewsAsyncTask(MainActivity.this).execute(makeUri(getResources().getStringArray(R.array.magazine)[index]));
                break;
            }
            case R.id.action_bar_change_language:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.choose_language))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.russian), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("locale", "ru");
                                editor.apply();

                            }
                        })
                        .setNegativeButton(getString(R.string.english), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("locale", "en");
                                editor.apply();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.setTitle(getString(R.string.change_language));
                alert.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Uri makeUri(String magazine) {
        return Uri.parse("https://newsapi.org/v1/articles").buildUpon()
                .appendQueryParameter("source", magazine)
                .appendQueryParameter("sortBy", "top")
                .appendQueryParameter("apiKey",apiKey)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);


        imageView = (ImageView)findViewById(R.id.imageView);
        spinnerMagazine = (Spinner)findViewById(R.id.spinner_magazine);
        textJson = (TextView)findViewById(R.id.text_json);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.magazine,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMagazine.setAdapter(adapter);

        spinnerMagazine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] array = getResources().getStringArray(R.array.magazine);
                if (position != -1)
                    new NewsAsyncTask(MainActivity.this).execute(makeUri(array[position]));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // code here //
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void restartActivity() {
        Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
}

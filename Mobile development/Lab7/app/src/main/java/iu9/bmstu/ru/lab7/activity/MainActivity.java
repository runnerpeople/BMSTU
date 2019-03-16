package iu9.bmstu.ru.lab7.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import iu9.bmstu.ru.lab7.R;
import iu9.bmstu.ru.lab7.adapter.CoinAdapter;
import iu9.bmstu.ru.lab7.model.Coin;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private static final Integer LOADER_ID = UUID.randomUUID().hashCode();
    private static final String TAG = MainActivity.class.getName();

    private LinearLayout layout;
    private EditText editTextBitcoin;
    private ProgressBar progressBar;
    private CoinAdapter coinAdapter;

    private Bundle params;
    private List<Coin> coins = new ArrayList<>();

    private Uri makeUri(String nameBitcoin, String currency, int days) {
        return Uri.parse("https://min-api.cryptocompare.com/data/histoday").buildUpon()
                .appendQueryParameter("fsym", nameBitcoin)
                .appendQueryParameter("tsym", currency)
                .appendQueryParameter("aggregate",String.valueOf(days))
                .build();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.action_bar_update: {
                initLoader(editTextBitcoin.getText().toString());
                break;
            }
            case R.id.action_bar_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initLoader(String bitcoin) {
        Log.d(TAG,"onInitLoader()");
        params = new Bundle();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int days = sharedPreferences.getInt(getString(R.string.pref_period_key),1);
        String currency = sharedPreferences.getString(getString(R.string.pref_currency_key), null);

        if (currency == null) {
            Snackbar.make(layout,"Не указано значение валюты",Toast.LENGTH_LONG).show();
            return;
        }

        params.putString("URL",makeUri(bitcoin,currency,days).toString());

        String url = params.getString("URL");
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<JSONObject> loader = loaderManager.getLoader(LOADER_ID);

        progressBar.setVisibility(View.VISIBLE);

        if (loader == null) {
            loaderManager.initLoader(LOADER_ID,params,this);
        }
        else {
            loaderManager.restartLoader(LOADER_ID,params,this);
        }
    }

    @Override
    public Loader<JSONObject> onCreateLoader(int id, final Bundle args) {
        Log.d(TAG,"onCreateLoader()");
        return new AsyncTaskLoader<JSONObject>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                forceLoad();
            }

            @Override
            public JSONObject loadInBackground() {

                try {
                    URL url = new URL(args.getString("URL",null));
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                    }
                    in.close();
                    return new JSONObject(sb.toString());
                } catch (JSONException | IOException e){
                    Log.e(TAG,e.getMessage());
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
        Log.d(TAG,"onLoadFinished()");
        progressBar.setVisibility(View.GONE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            JSONObject coinsObj;
            JSONArray coinsArr;
            String URL = params.getString("URL");
            if (data == null) {
                Snackbar.make(layout, "Нет данных", Toast.LENGTH_LONG).show();
                return;
            }
            coins = new ArrayList<>();
            if (URL != null) {
                coinsArr = data.getJSONArray("Data");
                for (int i = 0; i < coinsArr.length(); i++) {
                    JSONObject coinsInfo = (JSONObject) coinsArr.get(i);
                    Coin coin = new Coin();
                    coin.setTime(new Date(coinsInfo.getLong("time") * 1000));

                    coin.setLow(coinsInfo.getDouble("low"));
                    coin.setHigh(coinsInfo.getDouble("high"));
                    coin.setOpen(coinsInfo.getDouble("open"));
                    coin.setClose(coinsInfo.getDouble("close"));

                    coins.add(coin);
                }
            }
        }
        catch (JSONException ex) {
            Log.e(TAG,ex.getMessage());
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String currency = sharedPreferences.getString(getString(R.string.pref_currency_key), null);
        Coin.setCurrency(currency);

        coinAdapter.setData(coins);
        coinAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {
        Log.d(TAG,"onLoaderReset()");
        // TODO: Not implemented
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate()");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.main_activity);
        editTextBitcoin = findViewById(R.id.bitcoinEditText);
        RecyclerView recyclerViewBitcoin = findViewById(R.id.bitcoinRecyclerView);
        progressBar = findViewById(R.id.bitcoinProgressBar);

        progressBar.setVisibility(View.GONE);

        coinAdapter = new CoinAdapter(coins);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerViewBitcoin.setLayoutManager(gridLayoutManager);
        recyclerViewBitcoin.setAdapter(coinAdapter);

    }
}

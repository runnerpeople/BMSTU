package iu9.bmstu.ru.lab7.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import iu9.bmstu.ru.lab7.R;
import iu9.bmstu.ru.lab7.adapter.CoinAdapter;
import iu9.bmstu.ru.lab7.model.Coin;
import iu9.bmstu.ru.lab7.util.URLUtil;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private static final Integer LOADER_ID = UUID.randomUUID().hashCode();
    private static final String TAG = MainActivity.class.getName();

    private EditText editTextBitcoin;
    private RecyclerView recyclerViewBitcoin;
    private ProgressBar progressBar;
    private CoinAdapter coinAdapter;

    private Bundle params;
    private List<Coin> coins = new ArrayList<>();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        params = new Bundle();
        params.putString("URL",URLUtil.makeUri(bitcoin,14).toString());

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
        progressBar.setVisibility(View.GONE);

        SimpleDateFormat dateFormat;
        try {
            JSONObject coinsObj;
            JSONArray coinsArr;
            String URL = params.getString("URL");
            dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            if (data == null) {
                Toast.makeText(getApplicationContext(), "Null data", Toast.LENGTH_LONG).show();
                return;
            }

            if (!URL.contains("days")) {
                coinsArr = data.getJSONArray("history");
                Locale locale = new Locale("ru");
                Coin.setLocale(locale);
                Coin.setValue_symbol(Currency.getInstance("rub"));
                for (int i = 0; i < coinsArr.length(); i++) {
                    JSONObject coinsInfo = (JSONObject) coinsArr.get(i);
                    Coin coin = new Coin();
                    coin.setValue(coinsInfo.getJSONObject("price").getDouble("rub"));
                    coin.setDate(dateFormat.parse(coinsInfo.getString("date")));
                    coins.add(coin);
                }
            }
            else {
                coinsObj = data.getJSONObject("history");
                Locale locale = new Locale("ru");
                Coin.setLocale(locale);
                Coin.setValue_symbol(Currency.getInstance("rub"));
                dateFormat = new SimpleDateFormat("HH-dd-MM-yyyy");
                Iterator<?> keys = coinsObj.keys();
                while (keys.hasNext()) {
                    String key = (String)keys.next();
                    if (coinsObj.get(key) instanceof JSONObject) {
                        JSONObject coinsInfo = (JSONObject)coinsObj.get(key);
                        Coin coin = new Coin();
                        coin.setValue(coinsInfo.getJSONObject("price").getDouble("rub"));
                        coin.setDate(dateFormat.parse(key));
                        coins.add(coin);
                    }
                }
            }
            coinAdapter.setData(coins);
            coinAdapter.notifyDataSetChanged();
        }
        catch (JSONException | ParseException ex) {
            Log.e(TAG,ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onLoaderReset(Loader<JSONObject> loader) {
        // TODO: Not implemented
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        CoinAdapter.font = Typeface.createFromAsset(getAssets(),"Roboto-ThinItalic.ttf");

        editTextBitcoin = (EditText) findViewById(R.id.bitcoinEditText);
        recyclerViewBitcoin = (RecyclerView) findViewById(R.id.bitcoinRecyclerView);
        progressBar = (ProgressBar) findViewById(R.id.bitcoinProgressBar);

        progressBar.setVisibility(View.GONE);

        coinAdapter = new CoinAdapter(coins);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewBitcoin.setLayoutManager(linearLayoutManager);
        recyclerViewBitcoin.setAdapter(coinAdapter);

    }
}

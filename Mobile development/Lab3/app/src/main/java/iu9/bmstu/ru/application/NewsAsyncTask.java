package iu9.bmstu.ru.application;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.List;

public class NewsAsyncTask extends AsyncTask<Uri, Void, JSONObject> {

    private static final String TAG = "NewAsyncTask";

    private ProgressDialog progressDialog;
    private MainActivity activity;
    private List<String> data;
    private NewsAdapter newsAdapter;

    public NewsAsyncTask(MainActivity activity, List<String> data, NewsAdapter newsAdapter) {
        this.data = data;
        this.activity = activity;
        this.newsAdapter = newsAdapter;
        progressDialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog.setMessage("Getting news...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected JSONObject doInBackground(Uri... params) {
        try {
            URL url = new URL(params[0].toString());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            in.close();
            return new JSONObject(sb.toString());
        } catch (JSONException | IOException e){
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);

        progressDialog.dismiss();

        try {
            JSONArray articles = jsonObject.getJSONArray("articles");
            for (int i=0;i<articles.length();i++) {
                JSONObject article = (JSONObject) articles.get(0);
                data.add(article.getString("author"));
                data.add(article.getString("title"));
                data.add(article.getString("description"));
                data.add(article.getString("url"));
                data.add(article.getString("urlToImage"));
                data.add(article.getString("publishedAt"));

                Log.d(TAG,Integer.valueOf(data.size()).toString());
                Log.d(TAG,Integer.valueOf(newsAdapter.data.size()).toString());

                Log.d(TAG,Integer.valueOf(data.hashCode()).toString());
                Log.d(TAG,Integer.valueOf(newsAdapter.data.hashCode()).toString());

            }
            //newsAdapter.setData(data);
            newsAdapter.notifyDataSetChanged();
        }
        catch (JSONException ex) {
            Log.e(TAG,ex.getMessage());
        }

    }
}
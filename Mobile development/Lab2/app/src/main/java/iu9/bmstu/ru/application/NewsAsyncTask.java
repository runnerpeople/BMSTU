package iu9.bmstu.ru.application;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NewsAsyncTask extends AsyncTask<Uri, Void, JSONObject> {

    private static final String TAG = "NewAsyncTask";
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private ProgressDialog progressDialog;
    private MainActivity activity;

    public NewsAsyncTask(MainActivity activity) {
        this.activity = activity;
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
            JSONObject article = (JSONObject)articles.get(0);
            News news = new News(article.getString("author"),article.getString("title"),
                                 article.getString("description"),new URL(article.getString("url")),
                                 new URL(article.getString("urlToImage")),FORMATTER.parse(article.getString("publishedAt")));
            TextView textView = (TextView)activity.findViewById(R.id.text_json);
            textView.setText(news.toString());
        }
        catch (JSONException | MalformedURLException | ParseException ex) {
            Log.e(TAG,ex.getMessage());
        }

    }
}
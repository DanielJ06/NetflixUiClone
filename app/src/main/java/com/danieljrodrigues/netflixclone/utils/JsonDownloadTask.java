package com.danieljrodrigues.netflixclone.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.danieljrodrigues.netflixclone.model.Category;
import com.danieljrodrigues.netflixclone.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class JsonDownloadTask extends AsyncTask<String, Void, List<Category>> {

    private final WeakReference<Context> context;
    ProgressDialog dialog;

    public JsonDownloadTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();
        if(context != null) {
            dialog = ProgressDialog.show(context, "Loading", "", true);
        }
    }

    @Override
    protected List<Category> doInBackground(String... params) {
        String url = params[0];

        try {
            URL req = new URL(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) req.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode > 400) {
                throw new IOException("Connection error");
            }

            InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String jsonAsString = toString(in);

            List<Category> categories = getCategories(new JSONObject(jsonAsString));

            in.close();

            return categories;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Category> getCategories(JSONObject jsonObject) throws JSONException {
        List<Category> categories = new ArrayList<>();

        JSONArray categoryArr = jsonObject.getJSONArray("category");
        for (int i = 0; i < categoryArr.length(); i++) {
            JSONObject category = categoryArr.getJSONObject(i);
            String title = category.getString("title");

            List<Movie> movies = new ArrayList<>();
            JSONArray movieArr = category.getJSONArray("movie");
            for (int j = 0; j < movieArr.length(); j++) {
                JSONObject movie =  movieArr.getJSONObject(j);
                String coverUrl = movie.getString("cover_url");

                Movie movieObj = new Movie();
                movieObj.setCoverUrl(coverUrl);
                movies.add(movieObj);
            }

            Category categoryObj = new Category();
            categoryObj.setTitle(title);
            categoryObj.setMovies(movies);
            categories.add(categoryObj);
        }


        return categories;
    }

    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);
        dialog.dismiss();
    }

    private String toString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int received;
        while ((received = is.read(bytes)) > 0) {
            baos.write(bytes, 0, received);
        }

        return new String(baos.toByteArray());
    }
}

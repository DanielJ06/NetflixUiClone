package com.danieljrodrigues.netflixclone.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.danieljrodrigues.netflixclone.model.Category;
import com.danieljrodrigues.netflixclone.model.Movie;
import com.danieljrodrigues.netflixclone.model.MovieDetail;

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

public class MovieDetailTask extends AsyncTask<String, Void, MovieDetail> {

    private WeakReference<Context> context;
    private ProgressDialog dialog;
    private MovieDetailLoader movieDetailLoader;

    public MovieDetailTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void setMovieDetailLoader(MovieDetailLoader movieDetailLoader) {
        this.movieDetailLoader = movieDetailLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();
        if (context != null) {
            dialog = ProgressDialog.show(context, "Loading", "", true);
        }
    }

    @Override
    protected MovieDetail doInBackground(String... params) {
        String url = params[0];
        try {
            URL req = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) req.openConnection();

            if (urlConnection.getResponseCode() > 200) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();

            BufferedInputStream in = new BufferedInputStream(inputStream);
            String jsonAsString = toString(in);

            MovieDetail movieDetail = getMovieDetail(new JSONObject(jsonAsString));
            in.close();

            return movieDetail;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private MovieDetail getMovieDetail(JSONObject json) throws JSONException {
        int id = json.getInt("id");
        String title = json.getString("title");
        String desc = json.getString("desc");
        String cast = json.getString("cast");
        String coverUrl = json.getString("cover_url");

        List<Movie> movies = new ArrayList<>();
        JSONArray movieArr = json.getJSONArray("movie");
        for (int i = 0; i < movieArr.length(); i++) {
            JSONObject movie = movieArr.getJSONObject(i);

            String cover = movie.getString("cover_url");
            int idSimilar = movie.getInt("id");

            Movie similar = new Movie();
            similar.setId(idSimilar);
            similar.setCoverUrl(cover);

            movies.add(similar);
        }

        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setDesc(desc);
        movie.setCast(cast);
        movie.setCoverUrl(coverUrl);

        return new MovieDetail(movie, movies);
    }

    @Override
    protected void onPostExecute(MovieDetail movieDetail) {
        super.onPostExecute(movieDetail);
        dialog.dismiss();

        if (movieDetailLoader != null) {
            Log.i("TESTE", String.valueOf(movieDetail));
            movieDetailLoader.onResult(movieDetail);
        }
    }

    public interface MovieDetailLoader {
        void onResult(MovieDetail movieDetail);
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

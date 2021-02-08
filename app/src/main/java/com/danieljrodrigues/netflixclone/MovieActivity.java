package com.danieljrodrigues.netflixclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danieljrodrigues.netflixclone.model.Movie;
import com.danieljrodrigues.netflixclone.model.MovieDetail;
import com.danieljrodrigues.netflixclone.utils.ImageDownloadTask;
import com.danieljrodrigues.netflixclone.utils.MovieDetailTask;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity implements MovieDetailTask.MovieDetailLoader {

    private RecyclerView rvSimilar;
    private MovieAdapter movieAdapter;

    private TextView movieTitle;
    private TextView movieDesc;
    private TextView movieCast;
    private ImageView imgCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieTitle = findViewById(R.id.movie_title);
        movieDesc = findViewById(R.id.movie_synopsis);
        movieCast = findViewById(R.id.movie_cast);
        imgCover = findViewById(R.id.coverImage);

        Toolbar movieToolbar = findViewById(R.id.movie_toolbar);
        setSupportActionBar(movieToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        List<Movie> movies = new ArrayList<>();
        movieAdapter = new MovieAdapter(movies);

        rvSimilar = findViewById(R.id.similar_rv);
        rvSimilar.setLayoutManager(new GridLayoutManager(this, 3));
        rvSimilar.setAdapter(movieAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");
            MovieDetailTask movieDetailTask = new MovieDetailTask(this);
            movieDetailTask.setMovieDetailLoader(this);
            movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/" + id);
        }
    }

    @Override
    public void onResult(MovieDetail movieDetail) {
        movieTitle.setText(movieDetail.getMovie().getTitle());
        movieDesc.setText(movieDetail.getMovie().getDesc());
        movieCast.setText(movieDetail.getMovie().getCast());

        ImageDownloadTask imageDownloadTask = new ImageDownloadTask(imgCover);
        imageDownloadTask.setShadowEnabled(true);
        imageDownloadTask.execute(movieDetail.getMovie().getCoverUrl());

        movieAdapter.setSimilarMovies(movieDetail.getMoviesSimilar());
        movieAdapter.notifyDataSetChanged();
    }

    private class MovieHolder extends RecyclerView.ViewHolder {
        final ImageView imageViewCover;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.coverImage);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private List<Movie> movies;

        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(
                LayoutInflater.from(MovieActivity.this).inflate(R.layout.movie_item_similar, null)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
            new ImageDownloadTask(holder.imageViewCover).execute(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        void setSimilarMovies(List<Movie> moviesSimilar) {
            this.movies.clear();
            this.movies.addAll(moviesSimilar);
        }
    }
}
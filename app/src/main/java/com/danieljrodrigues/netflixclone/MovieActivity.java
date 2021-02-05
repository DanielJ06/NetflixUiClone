package com.danieljrodrigues.netflixclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.danieljrodrigues.netflixclone.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {

    private RecyclerView rvSimilar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Toolbar movieToolbar = findViewById(R.id.movie_toolbar);
        setSupportActionBar(movieToolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        List<Movie> movies = new ArrayList<>();
        for(int i=0; i < 12; i++) {
            Movie movie = new Movie();
            movies.add(movie);
        }

        rvSimilar = findViewById(R.id.similar_rv);
        rvSimilar.setLayoutManager(new GridLayoutManager(this, 3));
        rvSimilar.setAdapter(new MovieAdapter(movies));
    }

    private class MovieHolder extends RecyclerView.ViewHolder {
        final ImageView similarCover;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            similarCover = itemView.findViewById(R.id.similar_movie_cover);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private final List<Movie> movies;

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
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}
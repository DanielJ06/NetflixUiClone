package com.danieljrodrigues.netflixclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danieljrodrigues.netflixclone.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView categoryRecyclerView = findViewById(R.id.rv_category);

        categoryRecyclerView.setLayoutManager(
            new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false)
        );

        List<Movie> movies = new ArrayList<Movie>();
        for (int i = 0; i < 10; i++) {
            Movie movie = new Movie();
            movie.setCoverUrl("Teste " + i);
            movies.add(movie);
        }

        CategoryAdapter adapter = new CategoryAdapter(movies);
        categoryRecyclerView.setAdapter(adapter);
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {

        final TextView title;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.movie_title);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<Movie> movieData;

        private CategoryAdapter(List<Movie> movieData) {
            this.movieData = movieData;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.movie_item, null));
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Movie movie = movieData.get(position);
            holder.title.setText(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movieData.size();
        }
    }
}
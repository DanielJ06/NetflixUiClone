package com.danieljrodrigues.netflixclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danieljrodrigues.netflixclone.model.Category;
import com.danieljrodrigues.netflixclone.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView categoryRecyclerView = findViewById(R.id.rv_category);

        List<Category> categories = new ArrayList<>();
        for (int j = 0; j < 10; j++) {
            Category category = new Category();
            category.setTitle("Category" + j);

            List<Movie> movies = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Movie movie = new Movie();
//                movie.setCoverUrl(R.drawable.movie);
                movies.add(movie);
            }
            category.setMovies(movies);
            categories.add(category);
        }
        categoryRecyclerView.setLayoutManager(
            new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false)
        );
        CategoryAdapter adapter = new CategoryAdapter(categories);
        categoryRecyclerView.setAdapter(adapter);
    }

    private class MovieHolder extends RecyclerView.ViewHolder {
        ImageView coverUrl;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            coverUrl = itemView.findViewById(R.id.movie_cover);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private List<Movie> movieData;

        private MovieAdapter(List<Movie> movies) {
            this.movieData = movies;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(
                LayoutInflater.from(MainActivity.this).inflate(R.layout.movie_item, null)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movieData.get(position);
            //holder.coverUrl.setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movieData.size();
        }
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {
        RecyclerView movies;
        TextView title;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            movies = itemView.findViewById(R.id.categoryMovies);
            title = itemView.findViewById(R.id.categoryTitle);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<Category> categoryData;

        private CategoryAdapter(List<Category> categoryData) {
            this.categoryData = categoryData;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.category_item, null));
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Category category = categoryData.get(position);
            holder.title.setText(category.getTitle());
            holder.movies.setLayoutManager(
                new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false)
            );
            holder.movies.setAdapter(new MovieAdapter(category.getMovies()));
        }

        @Override
        public int getItemCount() {
            return categoryData.size();
        }
    }
}
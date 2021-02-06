package com.danieljrodrigues.netflixclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.danieljrodrigues.netflixclone.model.Category;
import com.danieljrodrigues.netflixclone.model.Movie;
import com.danieljrodrigues.netflixclone.utils.CategoryTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryTask.CategoryLoader {
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView categoryRecyclerView = findViewById(R.id.rv_category);

        List<Category> categories = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(categories);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        CategoryTask categoryTask = new CategoryTask(this);
        categoryTask.setCategoryLoader(this);
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home");
    }

    @Override
    public void onResult(List<Category> categories) {
        Log.i("TESTE", String.valueOf(categories));
        categoryAdapter.setCategories(categories);
        categoryAdapter.notifyDataSetChanged();
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

        void setCategories(List<Category> categories) {
            this.categoryData = categories;
        }
    }
}
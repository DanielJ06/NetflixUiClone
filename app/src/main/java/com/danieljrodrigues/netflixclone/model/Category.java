package com.danieljrodrigues.netflixclone.model;

import java.util.List;

public class Category {
    private String title;
    private List<Movie> movies;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

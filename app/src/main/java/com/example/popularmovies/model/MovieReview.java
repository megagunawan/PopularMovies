package com.example.popularmovies.model;

import java.util.ArrayList;

public class MovieReview {
    private final long movieId;
    private final ArrayList<String> authors;
    private final ArrayList<String> contents;

    public MovieReview(long movieId, ArrayList<String> authors, ArrayList<String> contents) {
        this.movieId = movieId;
        this.authors = authors;
        this.contents = contents;
    }

    public long getMovieId() {
        return movieId;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public ArrayList<String> getContents() {
        return contents;
    }
}

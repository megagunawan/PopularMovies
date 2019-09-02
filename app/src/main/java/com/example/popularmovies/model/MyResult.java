package com.example.popularmovies.model;

import com.example.popularmovies.model.Movie;

import java.util.ArrayList;

public class MyResult {
    private final ArrayList<Movie> movieList;
    private final ArrayList<String> imageURLs;
    private final ArrayList<String> movieTitles;
    private final ArrayList<Long> movieIDs;

    public MyResult(ArrayList<Movie> movieList, ArrayList<String> imageURLs, ArrayList<String> movieTitles, ArrayList<Long> movieIDs) {
        this.movieList = movieList;
        this.imageURLs = imageURLs;
        this.movieTitles = movieTitles;
        this.movieIDs = movieIDs;
    }
    public ArrayList<Movie> getMovieList() {
        return movieList;
    }

    public ArrayList<String> getImageURLs() {
        return imageURLs;
    }

    public ArrayList<String> getMovieTitles() {
        return movieTitles;
    }

    public ArrayList<Long> getMovieIDs() { return movieIDs; }
}

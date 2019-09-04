package com.example.popularmovies.model;

import android.net.Uri;
import java.util.ArrayList;

public class MovieTrailer {
    private final long movieId;
    private final ArrayList<String> movieKey;
    private final ArrayList<String> trailerName;

    public MovieTrailer(long movieId, ArrayList<String> movieKey, ArrayList<String> trailerName) {
        this.movieId = movieId;
        this.movieKey = movieKey;
        this.trailerName = trailerName;
    }

    public long getMovieId() {
        return movieId;
    }

    public ArrayList<String> getMovieKey() {
        return movieKey;
    }

    public ArrayList<String> getTrailerName() {
        return trailerName;
    }
}

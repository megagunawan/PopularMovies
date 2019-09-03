package com.example.popularmovies.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

import com.example.popularmovies.model.Movie;

@Entity(tableName = "movie")
public class MovieEntry {
    @PrimaryKey
    private long id;
    private String imageURL;
    private String movieTitle;

    @Ignore
    public MovieEntry(long id, String imageURL, String movieTitle) {
        this.id = id;
        this.imageURL = imageURL;
        this.movieTitle = movieTitle;
    }

    public MovieEntry() { }

    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
}

package com.example.popularmovies.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "movie")
public class MovieEntry {
    @PrimaryKey
    private long id;
    private String imageURL;

    @Ignore
    public MovieEntry(long id, String image) {
        this.id = id;
        this.imageURL = image;
    }

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
}

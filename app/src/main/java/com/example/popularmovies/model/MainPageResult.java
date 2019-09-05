package com.example.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MainPageResult {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("main_page_movies")
    @Expose
    private List<MainPageMovies> mainPageMovies = new ArrayList<MainPageMovies>();
    @SerializedName("total_main_page_movies")
    @Expose
    private Integer totalMainPageMovies;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<MainPageMovies> getMainPageMovies() {
        return mainPageMovies;
    }

    public void setMainPageMovies(List<MainPageMovies> movies) {
        this.mainPageMovies = movies;
    }

    public Integer getTotalMainPageMovies() {
        return totalMainPageMovies;
    }

    public void setTotalMainPageMovies(Integer totalMainPageMovies) {
        this.totalMainPageMovies = totalMainPageMovies;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}

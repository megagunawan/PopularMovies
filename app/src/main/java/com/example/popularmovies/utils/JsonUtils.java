package com.example.popularmovies.utils;

import android.os.Bundle;
import android.util.Log;

import com.example.popularmovies.model.DetailedMovie;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.model.MovieReview;
import com.example.popularmovies.model.MovieTrailer;
import com.example.popularmovies.model.MyResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static MyResult parseMovie(String json) {
        JSONObject obj;
        JSONArray resultsArr;
        JSONObject resultsObj;
        String IMAGE_URL = "http://image.tmdb.org/t/p/w185";

        ArrayList<String> imageURLs = new ArrayList<>();
        ArrayList<String> movieTitles = new ArrayList<>();
        ArrayList<Long> movieIDs = new ArrayList<>();

        Movie movie = null;
        ArrayList<Movie> movieList = new ArrayList<>();

        double popularity;
        long id;
        boolean video;
        long vote_count;
        double vote_average;
        String title;
        String release_date;
        String original_language;
        String original_title;
        JSONArray genre_ids;
        String backdrop_path;
        boolean adult;
        String overview;
        String poster_path;

        try {
            obj = new JSONObject(json);
            resultsArr = obj.getJSONArray("results");

            for (int i = 0; i < resultsArr.length(); i++) {
                resultsObj = resultsArr.getJSONObject(i);

                popularity = resultsObj.getDouble("popularity");
                id = resultsObj.getLong("id");
                video = resultsObj.getBoolean("video");
                vote_count = resultsObj.getLong("vote_count");
                vote_average = resultsObj.getDouble("vote_average");
                title = resultsObj.getString("title");
                release_date = resultsObj.getString("release_date");
                original_language = resultsObj.getString("original_language");
                original_title = resultsObj.getString("original_title");
                genre_ids = resultsObj.getJSONArray("genre_ids");
                backdrop_path = IMAGE_URL + resultsObj.getString("backdrop_path");
                adult = resultsObj.getBoolean("adult");
                overview = resultsObj.getString("overview");
                poster_path = IMAGE_URL + resultsObj.getString("poster_path");

                List<Long> genreIdsList = new ArrayList<>();
                for (int j = 0; j < genre_ids.length(); j++) {
                    genreIdsList.add(genre_ids.getLong(j));
                }

                movieTitles.add(title);
                imageURLs.add(poster_path);
                movieIDs.add(id);

                Log.v("title", title);

                movie = new Movie(popularity, id, video, vote_count, vote_average, title,
                        release_date, original_language, original_title, genreIdsList, backdrop_path,
                        adult, overview, poster_path);
                movieList.add(movie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MyResult(movieList, imageURLs, movieTitles, movieIDs);
    }

    public static DetailedMovie parseDetailedMovie(String json) {
        JSONObject obj;
        JSONArray genresArr;
        JSONArray companiesArr;
        JSONArray countriesArr;
        JSONArray languagesArr;
        String IMAGE_URL = "http://image.tmdb.org/t/p/w185";

        DetailedMovie detailedMovie = null;

        boolean adult;
        String backdrop_path;
        String belongs_to_collection;
        long budget;
        ArrayList<Bundle> genres = new ArrayList<>();
        String homepage;
        long id;
        String imdb_id;
        String original_language;
        String original_title;
        String overview;
        long popularity;
        String poster_path;
        ArrayList<Bundle> production_companies = new ArrayList<>();
        ArrayList<Bundle> production_countries = new ArrayList<>();
        String release_date;
        long revenue;
        long runtime;
        ArrayList<Bundle> spoken_languages = new ArrayList<>();
        String status;
        String tagline;
        String title;
        boolean video;
        double vote_average;
        long vote_count;

        try {
            obj = new JSONObject(json);
            adult = obj.getBoolean("adult");
            backdrop_path = obj.getString("backdrop_path");
            belongs_to_collection = obj.getString("belongs_to_collection");
            budget = obj.getLong("budget");
            genresArr = obj.getJSONArray("genres");
            homepage = obj.getString("homepage");
            id = obj.getLong("id");
            imdb_id = obj.getString("imdb_id");
            original_language = obj.getString("original_language");
            original_title = obj.getString("original_title");
            overview = obj.getString("overview");
            popularity = obj.getLong("popularity");
            poster_path = IMAGE_URL + obj.getString("poster_path");
            companiesArr = obj.getJSONArray("production_companies");
            countriesArr = obj.getJSONArray("production_countries");
            release_date = obj.getString("release_date");
            revenue = obj.getLong("revenue");
            if (obj.has("runtime")) {
                runtime = 0;
            } else {
                runtime = obj.getLong("runtime");
            }
            languagesArr = obj.getJSONArray("spoken_languages");
            status = obj.getString("status");
            tagline = obj.getString("tagline");
            title = obj.getString("title");
            video = obj.getBoolean("video");
            vote_average = obj.getDouble("vote_average");
            vote_count = obj.getLong("vote_count");

            JSONObject genreObj = null;
            for (int i = 0; i < genresArr.length(); i++) {
                genreObj = genresArr.getJSONObject(i);
                Bundle temp = new Bundle();
                temp.putInt("id", genreObj.getInt("id"));
                temp.putString("name", genreObj.getString("name"));
                genres.add(temp);
            }

            JSONObject companyObj = null;
            for (int i = 0; i < companiesArr.length(); i++) {
                companyObj = companiesArr.getJSONObject(i);
                Bundle temp = new Bundle();
                temp.putInt("id", companyObj.getInt("id"));
                temp.putString("logo_path", companyObj.getString("logo_path"));
                temp.putString("name", companyObj.getString("name"));
                temp.putString("origin_country", companyObj.getString("origin_country"));
                production_companies.add(temp);
            }

            JSONObject countryObj = null;
            for (int i = 0; i < countriesArr.length(); i++) {
                countryObj = countriesArr.getJSONObject(i);
                Bundle temp = new Bundle();
                temp.putString("iso_3166_1", countryObj.getString("iso_3166_1"));
                temp.putString("name", countryObj.getString("name"));
                production_countries.add(temp);
            }

            JSONObject langObj = null;
            for (int i = 0; i < languagesArr.length(); i++) {
                langObj = languagesArr.getJSONObject(i);
                Bundle temp = new Bundle();
                temp.putString("iso_639_1", langObj.getString("iso_639_1"));
                temp.putString("name", langObj.getString("name"));
                spoken_languages.add(temp);
            }

            detailedMovie = new DetailedMovie(adult, backdrop_path, belongs_to_collection,
                    budget, genres, homepage, id, imdb_id, original_language, original_title,
                    overview, popularity, poster_path, production_companies, production_countries,
                    release_date, revenue, runtime, spoken_languages, status, tagline, title, video,
                    vote_average, vote_count);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return detailedMovie;
    }

    public static MovieTrailer parseTrailer(String json) {
        JSONObject obj;
        JSONArray resultsArr;
        JSONObject resultsObj;

        long id = 0;

        ArrayList<String> key = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        try {
            obj = new JSONObject(json);
            id = obj.getLong("id");

            resultsArr = obj.getJSONArray("results");

            for (int i = 0; i < resultsArr.length(); i++) {
                resultsObj = resultsArr.getJSONObject(i);

                key.add(resultsObj.getString("key"));
                name.add(resultsObj.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MovieTrailer(id, key, name);
    }

    public static MovieReview parseReview(String json) {
        JSONObject obj;
        JSONArray resultsArr;
        JSONObject resultsObj;

        long id = 0;

        ArrayList<String> authors = new ArrayList<>();
        ArrayList<String> contents = new ArrayList<>();
        try {
            obj = new JSONObject(json);
            id = obj.getLong("id");

            resultsArr = obj.getJSONArray("results");

            for (int i = 0; i < resultsArr.length(); i++) {
                resultsObj = resultsArr.getJSONObject(i);

                authors.add(resultsObj.getString("author"));
                contents.add(resultsObj.getString("content"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new MovieReview(id, authors, contents);
    }
}

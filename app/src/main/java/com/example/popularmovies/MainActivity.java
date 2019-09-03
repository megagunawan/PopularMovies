package com.example.popularmovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.MovieEntry;
import com.example.popularmovies.model.Movie;
import com.example.popularmovies.utils.JsonUtils;
import com.example.popularmovies.model.MyResult;
import com.example.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.MyAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private String userMenuChoice = "top_rated";
    private ArrayList<String> imageURLs = new ArrayList<>();
    private ArrayList<String> movieTitles = new ArrayList<>();
    private ArrayList<Long> movieIDs = new ArrayList<>();
    MyAdapter myAdapter;
    private AppDatabase mDatabase;
    private ArrayList<MovieEntry> movieEntries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = AppDatabase.getsInstance(getApplicationContext());

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);

        myAdapter = new MyAdapter(this);
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        loadMovies();
    }

    private void loadMovies() {
        if (userMenuChoice != "favorite") {
            new MyAsyncTask().execute(userMenuChoice);
        } else {
            LiveData<List<MovieEntry>> movies = mDatabase.movieDao().getAllMovies();
            movies.observe(this, new Observer<List<MovieEntry>>() {
                @Override
                public void onChanged(List<MovieEntry> movieEntries) {
                    myAdapter.setMovies(movieEntries);
                }
            });
        }
    }

    @Override
    public void onClick(long movieId) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        String movieIdString = Long.toString(movieId);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieIdString);
        startActivity(intentToStartDetailActivity);
    }

    public class MyAsyncTask extends AsyncTask<String, Void, MyResult> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected MyResult doInBackground(String... params) {
            URL myURL;
            String result = null;
            try {
                myURL = NetworkUtils.buildUrl(params[0]);
                result = NetworkUtils.getResponseFromHttpUrl(myURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MyResult myResult = JsonUtils.parseMovie(result);
            return myResult;
        }

        @Override
        protected void onPostExecute(MyResult result) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            movieTitles = result.getMovieTitles();
            imageURLs = result.getImageURLs();
            movieIDs = result.getMovieIDs();
            showData(imageURLs, movieTitles, movieIDs);
        }
    }

    private void showData(ArrayList<String> imageURLs, ArrayList<String> movieTitles, ArrayList<Long> movieIDs) {
        myAdapter.setData(imageURLs, movieTitles, movieIDs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.first_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.top_rated && userMenuChoice != "top_rated") {
            userMenuChoice = "top_rated";
            loadMovies();
        } else if (id == R.id.popular && userMenuChoice != "popular") {
            userMenuChoice = "popular";
            loadMovies();
        } else if (id == R.id.favorite && userMenuChoice != "favorite") {
            userMenuChoice = "favorite";
            loadMovies();
        }

        return super.onOptionsItemSelected(item);
    }
}



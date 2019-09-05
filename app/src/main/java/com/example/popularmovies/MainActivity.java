package com.example.popularmovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.MovieEntry;
import com.example.popularmovies.utils.JsonUtils;
import com.example.popularmovies.model.MyResult;
import com.example.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainAdapter.MyAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private String MENU_CHOICE = "menu_choice";
    private String userMenuChoice = "top_rated";
    private boolean changedChoice = false;
    private ArrayList<String> imageURLs = new ArrayList<>();
    private ArrayList<String> movieTitles = new ArrayList<>();
    private ArrayList<Long> movieIDs = new ArrayList<>();
    MainAdapter myAdapter;
    private AppDatabase mDatabase;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("test", "test");

        if (savedInstanceState != null) {
            userMenuChoice = savedInstanceState.getString(MENU_CHOICE);
        }

        setContentView(R.layout.activity_main);

        mDatabase = AppDatabase.getsInstance(getApplicationContext());

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);

        myAdapter = new MainAdapter(this);

        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        loadMovies();
    }
    private void loadMovies() {
        Log.v("changedChoice", ""+changedChoice);
        Log.v("userMenuChoice", "" + userMenuChoice);
        Log.v("currentpage", "" + currentPage);
        if (changedChoice == false && currentPage == 0 && userMenuChoice != "favorite") {
            currentPage = 1;
            new MyAsyncTask().execute(userMenuChoice);
        }

        if (changedChoice == true && userMenuChoice != "favorite") {
            currentPage = 1;
            changedChoice = false;
            myAdapter = new MainAdapter(this);
            mRecyclerView.setAdapter(myAdapter);
            new MyAsyncTask().execute(userMenuChoice);
        }

        if (changedChoice == false && userMenuChoice != "favorite") {
            Log.v("nextpage", "" + currentPage);
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Log.v("test", "test");
                    //currentPage++;
                    if (!recyclerView.canScrollVertically(1)) {
                        Log.v("currentPage", ""+currentPage);
                        currentPage++;
                        new MyAsyncTask().execute(userMenuChoice);
                    }
                }
            });
        }

        if(userMenuChoice == "favorite" && changedChoice == true) {
            currentPage = 1;
            changedChoice = false;
            myAdapter = new MainAdapter(this);
            mRecyclerView.setAdapter(myAdapter);
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
                myURL = NetworkUtils.buildUrl(params[0], currentPage);
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
        myAdapter.setData(imageURLs, movieTitles, movieIDs, currentPage);
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
            changedChoice = true;
            loadMovies();
        } else if (id == R.id.popular && userMenuChoice != "popular") {
            userMenuChoice = "popular";
            changedChoice = true;
            loadMovies();
        } else if (id == R.id.favorite && userMenuChoice != "favorite") {
            userMenuChoice = "favorite";
            changedChoice = true;
            loadMovies();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MENU_CHOICE, userMenuChoice);
    }
}



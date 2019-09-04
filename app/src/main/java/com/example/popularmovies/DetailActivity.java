package com.example.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.popularmovies.database.AppDatabase;
import com.example.popularmovies.database.MovieEntry;
import com.example.popularmovies.databinding.ActivityDetailBinding;
import com.example.popularmovies.model.DetailedMovie;
import com.example.popularmovies.utils.AppExecutors;
import com.example.popularmovies.utils.JsonUtils;
import com.example.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String movieId = null;
    private ActivityDetailBinding mBinding;
    private AppDatabase mDatabase;
    private DetailedMovie detailedMovie;
    private boolean[] inFavorite = new boolean[1];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mDatabase = AppDatabase.getsInstance(getApplicationContext());
        mBinding.favoriteButton.setOnClickListener(this);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        movieId = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (movieId == null) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }
        Log.v("movieIdInDetailActivity", movieId);

        loadDetailedMovie();
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void loadDetailedMovie() {
        new MyAsyncTask().execute(movieId);
    }

    @Override
    public void onClick(View view) {
        long id = Long.parseLong(movieId);
        String image = detailedMovie.getPoster_path();
        String title = detailedMovie.getTitle();
        final MovieEntry movie = new MovieEntry(id, image, title);
        if (view.getId() == R.id.favorite_button) {
            if (inFavorite[0]) {
                inFavorite[0] = false;
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.movieDao().deleteMovie(movie);
                    }
                });
                mBinding.favoriteButton.setText(R.string.add_to_favorite);
                Toast.makeText(this, R.string.unfavorite_success, Toast.LENGTH_SHORT).show();
            } else {
                inFavorite[0] = true;
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDatabase.movieDao().insertMovie(movie);
                    }
                });
                mBinding.favoriteButton.setText(R.string.unfavorite);
                Toast.makeText(this, R.string.add_to_favorite_success, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class MyAsyncTask extends AsyncTask<String, Void, DetailedMovie> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBinding.pbLoadingIndicatorDetail.setVisibility(View.VISIBLE);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    Log.v("findifexsits", ""+mDatabase.movieDao().findIfExistsInDatabase(Long.parseLong(movieId)));
                    if (mDatabase.movieDao().findIfExistsInDatabase(Long.parseLong(movieId)) != 0) {
                        inFavorite[0] = true;
                    } else {
                        inFavorite[0] = false;
                    }
                }
            });
        }

        @Override
        protected DetailedMovie doInBackground(String... params) {
            URL myURL;
            String result = null;
            try {
                myURL = NetworkUtils.buildUrl(params[0]);
                result = NetworkUtils.getResponseFromHttpUrl(myURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            detailedMovie = JsonUtils.parseDetailedMovie(result);
            return detailedMovie;
        }

        @Override
        protected void onPostExecute(DetailedMovie result) {
            mBinding.pbLoadingIndicatorDetail.setVisibility(View.INVISIBLE);
            mBinding.ratingText.setVisibility(View.VISIBLE);
            mBinding.releaseDateText.setVisibility(View.VISIBLE);
            mBinding.favoriteButton.setVisibility(View.VISIBLE);
            Log.v("ratingreleasedatetext", "visible");
            mBinding.detailTitle.setText(result.getTitle());


            Log.v("inFavorite value", ""+inFavorite[0]);
            if (inFavorite[0]) {
                mBinding.favoriteButton.setText(R.string.unfavorite);
            }

            Picasso.with(getApplicationContext())
                    .load(result.getPoster_path())
                    .into(mBinding.detailImage);

            mBinding.releaseDateTv.setText(result.getRelease_date());
            mBinding.ratingTv.setText(Double.toString(result.getVote_average()) + " / 10");
            mBinding.summaryTv.setText(result.getOverview());

            Log.v("id", Long.toString(result.getId()));
        }

    }
}

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

import com.example.popularmovies.model.DetailedMovie;
import com.example.popularmovies.utils.JsonUtils;
import com.example.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTextView;
    private ImageView imageView;
    private TextView releaseDateTextView;
    private TextView ratingTextView;
    private TextView summaryTextView;
    private ProgressBar mLoadingIndicator;
    private TextView releaseDateText;
    private TextView ratingText;
    private String movieId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleTextView = findViewById(R.id.detail_title);
        imageView = findViewById(R.id.detail_image);
        releaseDateTextView = findViewById(R.id.release_date_tv);
        ratingTextView = findViewById(R.id.rating_tv);
        summaryTextView = findViewById(R.id.summary_tv);
        mLoadingIndicator = (ProgressBar)findViewById(R.id.pb_loading_indicator_detail);
        releaseDateText = findViewById(R.id.release_date_text);
        ratingText = findViewById(R.id.rating_text);

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

    public class MyAsyncTask extends AsyncTask<String, Void, DetailedMovie> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
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
            DetailedMovie detailedMovie = JsonUtils.parseDetailedMovie(result);
            return detailedMovie;
        }

        @Override
        protected void onPostExecute(DetailedMovie result) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            ratingText.setVisibility(View.VISIBLE);
            releaseDateText.setVisibility(View.VISIBLE);
            Log.v("id", Long.toString(result.getId()));
            titleTextView.setText(result.getTitle());
            Picasso.with(getApplicationContext())
                    .load(result.getPoster_path())
                    .into(imageView);
            releaseDateTextView.setText(result.getRelease_date());
            ratingTextView.setText(Double.toString(result.getVote_average()) + " / 10");
            summaryTextView.setText(result.getOverview());
        }
    }

}

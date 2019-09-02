package com.example.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String API_KEY = "d5d3565fafb69b9ac4920bc1bc168880";

    private static final String API_PARAM = "api_key";

    public static URL buildUrl(String locationQuery) {
        Uri myUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(locationQuery)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        try {
            URL newURL = new URL(myUri.toString());
            return newURL;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

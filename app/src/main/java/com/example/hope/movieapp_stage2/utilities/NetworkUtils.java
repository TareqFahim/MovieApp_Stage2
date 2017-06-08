package com.example.hope.movieapp_stage2.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie";
    final static String API_KEY_PARAM = "api_key";
    final static String api_key = "INSERT API KEY!";

    public static URL buildMoviesUrl(String sortBy) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL + "/" + sortBy + "?").buildUpon()
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildTrailersUrl(String movieID) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL + "/" + movieID + "/" + "videos" + "?").buildUpon()
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildReviewsUrl(String movieID){
        Uri builtUri = Uri.parse(MOVIE_BASE_URL + "/" + movieID + "/" + "reviews" + "?").buildUpon()
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();
        URL  url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        String jsonStr = "";
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                jsonStr += scanner.next();
            } else {
                return null;
            }
            return jsonStr;
        } finally {
            urlConnection.disconnect();
        }
    }
}

package com.example.hope.movieapp_stage2.utilities;

import android.content.res.Resources;

import com.example.hope.movieapp_stage2.BuildConfig;
import com.example.hope.movieapp_stage2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hope on 6/5/2017.
 */

public class MovieParser {
    private static final String LOG_TAG = MovieParser.class.getSimpleName();

    public MoviesData parseJson(String movieJsonStr) throws JSONException {

        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_RESULTS = "results";
        final String OWM_TITLE = "original_title";
        final String OWM_OVERVIEW = "overview";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_RATE = "vote_average";
        final String OWM_ID = "id";
        final String API_KEY = BuildConfig.API_KEY;

        MoviesData moviesData = new MoviesData();

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);

        for (int i = 0; i < movieArray.length(); i++) {

            String overview, year, rate , mTitle , id, posterUrl;


            JSONObject movieObject = movieArray.getJSONObject(i);

            posterUrl = movieObject.getString(OWM_POSTER_PATH);
            moviesData.postersURL.add(i, "http://image.tmdb.org/t/p/w185" + posterUrl + "?api_key=" + API_KEY);

            overview = movieObject.getString(OWM_OVERVIEW);
            moviesData.movieOverview.add(i, overview);   // ADD movies' overview to movieOverview list

            year = movieObject.getString(OWM_RELEASE_DATE);
            year = year.substring(0, 4)+"\n120 min"; // Year of release
            moviesData.movieReleaseDate.add(i, year);        // ADD movies' release date to movieTitle list

            rate = movieObject.getString(OWM_RATE);
            Double x = Double.parseDouble(rate);
            x = (Math.round(x*10))/10.0;
            rate = Double.toString(x);
            moviesData.movieRate.add(i, rate+"/10");        // Add movies' rate to movieRate list

            mTitle = movieObject.getString(OWM_TITLE);
            moviesData.movieTitle.add(i, mTitle);          // ADD movies' titles to movieTitle list

            id = movieObject.getString(OWM_ID);
            moviesData.movieID.add(i ,id);
        }
        return moviesData;
    }
}

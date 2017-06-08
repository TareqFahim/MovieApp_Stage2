package com.example.hope.movieapp_stage2.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hope on 6/6/2017.
 */

public class MovieTrailersParser implements JSONParser<String> {
    private static final String LOG_TAG = MovieTrailersParser.class.getSimpleName();

    List<String> movieTrailers;

    @Override
    public List<String> parseJson(String jsonStr) throws JSONException {
        movieTrailers = new ArrayList<String>();

        final String OWM_RESULTS = "results";
        final String OWM_VIDEO_KEY = "key";

        JSONObject videoJson = new JSONObject(jsonStr);
        JSONArray videoArray = videoJson.getJSONArray(OWM_RESULTS);

        for (int i = 0; i < videoArray.length(); i++) {

            String trailerKey;
            JSONObject videoObject = videoArray.getJSONObject(i);

            trailerKey = videoObject.getString(OWM_VIDEO_KEY);

            movieTrailers.add(i, "https://www.youtube.com/watch?v=" + trailerKey);
        }

        for (String s : movieTrailers) {
            Log.v(LOG_TAG, "Movie Trailers: " + s);
        }
        return movieTrailers;
    }
}

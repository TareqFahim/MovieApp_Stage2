package com.example.hope.movieapp_stage2.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hope on 6/5/2017.
 */

public class MovieReviewsParser implements JSONParser<String> {
    private static final String LOG_TAG = MovieReviewsParser.class.getSimpleName();

    @Override
    public List<String> parseJson(String jsonStr) throws JSONException {
        List<String> Reviews = new ArrayList<String>();

        final String OWM_RESULTS = "results";
        final String OWM_CONTENT = "content";
        final String OWM_AUTHOR = "author";

        JSONObject reviewJson = new JSONObject(jsonStr);
        JSONArray reviewArray = reviewJson.getJSONArray(OWM_RESULTS);

        for (int i = 0; i < reviewArray.length(); i++) {

            String review, author;
            JSONObject reviewObject = reviewArray.getJSONObject(i);
            author = reviewObject.getString(OWM_AUTHOR);
            review = reviewObject.getString(OWM_CONTENT);

            Reviews.add(author + "\n\n" + review + "\n\n-------------------------------\n");
        }

        for (String s : Reviews) {
            Log.v(LOG_TAG, "Movie Reviews: " + s);
        }
        return Reviews;
    }
}

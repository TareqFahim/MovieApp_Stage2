package com.example.hope.movieapp_stage2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hope.movieapp_stage2.utilities.MovieReviewsParser;
import com.example.hope.movieapp_stage2.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Reviews extends AppCompatActivity implements FetchJsonAsyncTask.JsonCallBack {

    ListView reviewList;
    ArrayAdapter reviewAdapter;
    String movieID;
    URL url;
    List<String> movieReviews = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        reviewList = (ListView) findViewById(R.id.reviews_list);
        Intent intent = this.getIntent();
        if (intent != null && intent.hasExtra("movieID")) {
            movieID = intent.getStringExtra("movieID");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FetchJsonAsyncTask fetchReviews = new FetchJsonAsyncTask();
        url = NetworkUtils.buildReviewsUrl(movieID);
        fetchReviews.setCallBackContext(this);
        fetchReviews.execute(url);
    }

    @Override
    public void setJson(String json) {
        callJsonParser(json);
        setReviewsAdapter();
    }

    private void callJsonParser(String json) {
        try {
            movieReviews = new MovieReviewsParser().parseJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setReviewsAdapter() {
        reviewAdapter = new ArrayAdapter<String>(
                this
                , R.layout.list_item_reviews
                , R.id.reviews_list_textview
                , new ArrayList<String>());

        for (String review : movieReviews) {
            if (movieReviews != null) {
                reviewAdapter.add(review);
            }
            reviewList.setAdapter(reviewAdapter);
        }
    }
}

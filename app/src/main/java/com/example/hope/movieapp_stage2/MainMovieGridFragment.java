package com.example.hope.movieapp_stage2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hope.movieapp_stage2.adapters.MovieGridAdapter;
import com.example.hope.movieapp_stage2.data.MovieDbContract;
import com.example.hope.movieapp_stage2.utilities.MovieParser;
import com.example.hope.movieapp_stage2.utilities.MoviesData;
import com.example.hope.movieapp_stage2.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainMovieGridFragment extends Fragment implements FetchJsonAsyncTask.JsonCallBack, MovieGridAdapter.GridItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = MainMovieGridFragment.class.getSimpleName();

//    List posterURL = new ArrayList();

    MovieGridAdapter mAdapter;
    MoviesData moviesData;
    SelectedMovieData mSelectedMovieData;
    Bundle savedInstanceState;

    String movieDisplayOrder = null;
    URL moviesUrl;

    View rootView;
    RecyclerView mMovieGrid;


    @Override
    public void onStart() {
        super.onStart();
        FetchJsonAsyncTask task = new FetchJsonAsyncTask();
        task.setCallBackContext(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        movieDisplayOrder = preferences.getString(getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_default_value));

        if (savedInstanceState != null){
            getSavedState();
            setMovieGridAdapter();
        } else if (movieDisplayOrder.equals("favorite")) {
            displayFavoriteMovies();
        } else {
            moviesUrl = NetworkUtils.buildMoviesUrl(movieDisplayOrder);
            task.execute(moviesUrl);
        }
    }

    public MainMovieGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main_movie_grid, container, false);
        this.savedInstanceState = savedInstanceState;
        int noOfColumns = 2;
        mMovieGrid = (RecyclerView) rootView.findViewById(R.id.rv_movie_grid);
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), noOfColumns);
        mMovieGrid.setLayoutManager(layoutManager);
        mMovieGrid.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(getString(R.string.saved_state_posters), (ArrayList<String>) moviesData.postersURL);
        outState.putStringArrayList(getString(R.string.saved_state_date), (ArrayList<String>) moviesData.movieReleaseDate);
        outState.putStringArrayList(getString(R.string.saved_state_movie_id), (ArrayList<String>) moviesData.movieID);
        outState.putStringArrayList(getString(R.string.saved_state_overview), (ArrayList<String>) moviesData.movieOverview);
        outState.putStringArrayList(getString(R.string.saved_state_title), (ArrayList<String>) moviesData.movieTitle);
        outState.putStringArrayList(getString(R.string.saved_state_rate), (ArrayList<String>) moviesData.movieRate);
    }

    private void getSavedState(){
        moviesData = new MoviesData();
        moviesData.postersURL = (ArrayList) savedInstanceState.getStringArrayList(getString(R.string.saved_state_posters));
        moviesData.movieID = (ArrayList) savedInstanceState.getStringArrayList(getString(R.string.saved_state_movie_id));
        moviesData.movieOverview = (ArrayList) savedInstanceState.getStringArrayList(getString(R.string.saved_state_overview));
        moviesData.movieRate = (ArrayList) savedInstanceState.getStringArrayList(getString(R.string.saved_state_rate));
        moviesData.movieReleaseDate = (ArrayList) savedInstanceState.getStringArrayList(getString(R.string.saved_state_date));
        moviesData.movieTitle = (ArrayList) savedInstanceState.getStringArrayList(getString(R.string.saved_state_title));

    }

    @Override
    public void setJson(String json) {
        callJsonParser(json);
        setMovieGridAdapter();
    }

    private void callJsonParser(String json) {
        try {
            moviesData = new MovieParser().parseJson(json);
        } catch (JSONException ex) {
            Log.e(LOG_TAG, "JSON EXCEPTION");
        }
    }

    private void setMovieGridAdapter() {
        mAdapter = new MovieGridAdapter(getActivity(), moviesData.postersURL, this);
        mMovieGrid.setAdapter(mAdapter);
    }

    private void displayFavoriteMovies(){
        Cursor favMoviesCursor = getActivity().getContentResolver().query(MovieDbContract.FavoriteMovies.CONTENT_URI
                , null
                , null
                , null
                , null);
        moviesData = new MoviesData();
        fillMovieData(favMoviesCursor);
        mAdapter = new MovieGridAdapter(getActivity(), moviesData.postersURL, this);
        mMovieGrid.setAdapter(mAdapter);
    }

    private void fillMovieData(Cursor c){
        while (c.moveToNext()){
            moviesData.postersURL .add(c.getString(c.getColumnIndex(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_POSTER)));
            moviesData.movieOverview.add(c.getString(c.getColumnIndex(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_OVERVIEW)));
            moviesData.movieID.add(c.getString(c.getColumnIndex(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_ID)));
            moviesData.movieRate.add(c.getString(c.getColumnIndex(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_RATE)));
            moviesData.movieReleaseDate.add(c.getString(c.getColumnIndex(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_DATE)));
            moviesData.movieTitle.add(c.getString(c.getColumnIndex(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_TITLE)));
        }
    }

    @Override
    public void onGridItemClick(int itemIndex) {
        mSelectedMovieData = new SelectedMovieData(moviesData, itemIndex);
        Intent startMovieDetails = new Intent(getActivity(), MovieDetails.class);
        startMovieDetails.putExtra("xx", mSelectedMovieData);
        startActivity(startMovieDetails);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_sorting_key))) {
            movieDisplayOrder = sharedPreferences.getString(key, getResources().getString(R.string.pref_sorting_default_value));
        }
    }


}

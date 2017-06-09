package com.example.hope.movieapp_stage2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hope.movieapp_stage2.data.MovieDbContract;
import com.example.hope.movieapp_stage2.utilities.MovieTrailersParser;
import com.example.hope.movieapp_stage2.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment implements FetchJsonAsyncTask.JsonCallBack {

    View rootView;
    View header;
    Button reviewBtn;
    Button favoriteBtn;

    List<String> movieTrailers = new ArrayList<String>();
    ArrayAdapter trailersAdapter;
    String movieID;
    ListView trailersListView;
    URL url = null;
    Cursor moviesInDb;
    boolean isMovieinDb = false;
    Bundle savedInstanceState;

    SelectedMovieData mSelectedMovieData;

    public MovieDetailsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (savedInstanceState != null) {
            getSavedState();
            setTrailersListAdapter();
        } else {
            FetchJsonAsyncTask fetchTrailers = new FetchJsonAsyncTask();
            url = NetworkUtils.buildTrailersUrl(movieID);
            fetchTrailers.setCallBackContext(this);
            fetchTrailers.execute(url);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        this.savedInstanceState = savedInstanceState;
        trailersListView = (ListView) rootView.findViewById(R.id.trailers_list);
        header = inflater.inflate(R.layout.details_fragment_header, null, false);

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("xx")) {
            mSelectedMovieData = intent.getParcelableExtra("xx");

            ImageView iview = (ImageView) header.findViewById(R.id.detail_imageView);

            Picasso.with(getActivity()).load(mSelectedMovieData.moviePoster).into(iview);
            ((TextView) header.findViewById(R.id.detail_textView_title)).setText(mSelectedMovieData.movieTitle);
            ((TextView) header.findViewById(R.id.detail_textView_date)).setText(mSelectedMovieData.movieReleaseDate);
            ((TextView) header.findViewById(R.id.detail_textView_overview)).setText(mSelectedMovieData.movieOverview);
            ((TextView) header.findViewById(R.id.detail_textView_rate)).setText(mSelectedMovieData.movieRate);

            reviewBtn = ((Button) header.findViewById(R.id.reviews_btn));
            reviewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent reviewIntent = new Intent(getActivity(), Reviews.class).putExtra("movieID", movieID);
                    startActivity(reviewIntent);
                }
            });

            favoriteBtn = (Button) header.findViewById(R.id.favourite_btn);
            setFavBtnText();
            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isMovieinDb) {
                        ContentValues cv = new ContentValues();

                        cv.put(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_ID, mSelectedMovieData.movieID);
                        cv.put(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_DATE, mSelectedMovieData.movieReleaseDate);
                        cv.put(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_OVERVIEW, mSelectedMovieData.movieOverview);
                        cv.put(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_POSTER, mSelectedMovieData.moviePoster);
                        cv.put(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_TITLE, mSelectedMovieData.movieTitle);
                        cv.put(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_RATE, mSelectedMovieData.movieRate);

                        Uri uri = getActivity().getContentResolver().insert(MovieDbContract.FavoriteMovies.CONTENT_URI, cv);

                        if (uri != null) {
                            Toast.makeText(getActivity(), "Movie is Added to Favorite", Toast.LENGTH_SHORT).show();
                            favoriteBtn.setText(R.string.remove_from_favorite);
                            isMovieinDb = true;
                        }
                    } else {
                        moviesInDb = getActivity().getContentResolver().query(MovieDbContract.FavoriteMovies.CONTENT_URI, null, null, null, null);
                        Uri itemToDeleteUri = MovieDbContract.FavoriteMovies.CONTENT_URI;
                        int itemToDeleteID = -1;
                        int idColIndex = moviesInDb.getColumnIndex(MovieDbContract.FavoriteMovies._ID);
                        moviesInDb.moveToFirst();
                        do {
                            String movID = moviesInDb.getString(moviesInDb.getColumnIndex(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_ID));
                            if (movID.equals(mSelectedMovieData.movieID)) {
                                itemToDeleteID = moviesInDb.getInt(idColIndex);
                                break;
                            }
                        } while (moviesInDb.moveToNext());
                        itemToDeleteUri = itemToDeleteUri.buildUpon().appendPath(Integer.toString(itemToDeleteID)).build();
                        int noOfDeletedItems = getActivity().getContentResolver().delete(itemToDeleteUri, null, null);
                        if (noOfDeletedItems > 0) {
                            favoriteBtn.setText(getString(R.string.add_to_favorite));
                            isMovieinDb = false;
                            Toast.makeText(getActivity(), "Movie is Removed from Favorite", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            trailersListView.addHeaderView(header);
            movieID = mSelectedMovieData.movieID;

            trailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(movieTrailers.get(position - 1)).buildUpon().build();
                    intent.setData(uri);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null)
                        startActivity(intent);
                }
            });
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(getString(R.string.saved_state_trailers), (ArrayList<String>) movieTrailers);
    }

    private void getSavedState() {
        movieTrailers = this.savedInstanceState.getStringArrayList(getString(R.string.saved_state_trailers));
    }

    private void setFavBtnText() {
        moviesInDb = getActivity().getContentResolver().query(MovieDbContract.FavoriteMovies.CONTENT_URI, null, null, null, null);
        int movieIDColumnIndex = moviesInDb.getColumnIndex(MovieDbContract.FavoriteMovies.COLUMN_MOVIE_ID);
        while (moviesInDb.moveToNext()) {
            if (mSelectedMovieData.movieID.equals(moviesInDb.getString(movieIDColumnIndex))) {
                favoriteBtn.setText(getString(R.string.remove_from_favorite));
                isMovieinDb = true;
                moviesInDb.close();
                break;
            } else {
                favoriteBtn.setText(getString(R.string.add_to_favorite));
                isMovieinDb = false;
            }
        }
    }

    @Override
    public void setJson(String json) {
        callJsonParser(json);
        setTrailersListAdapter();
    }

    private void setTrailersListAdapter() {
        trailersAdapter = new ArrayAdapter<String>(
                // The current context (this fragment's parent )
                getActivity()
                // ID of the list item layout
                , R.layout.list_item_trailers
                // ID of the textView populate
                , R.id.trailers_list_textview
                // Forecast data
                , new ArrayList<String>());

        String s = "Trailer ";
        for (String trailer : movieTrailers) {
            trailersAdapter.add(s + (movieTrailers.indexOf(trailer) + 1));
        }
        trailersListView.setAdapter(trailersAdapter);
    }

    private void callJsonParser(String json) {
        try {
            movieTrailers = new MovieTrailersParser().parseJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

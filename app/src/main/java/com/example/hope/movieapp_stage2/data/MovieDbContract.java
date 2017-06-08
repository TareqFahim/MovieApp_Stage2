package com.example.hope.movieapp_stage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Hope on 6/7/2017.
 */

public class MovieDbContract {

    public static final String AUTHORITY = "com.example.hope.movieapp_stage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = FavoriteMovies.TABLE_NAME;

    public static final class FavoriteMovies implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_RATE = "rate";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_DATE = "date";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
}

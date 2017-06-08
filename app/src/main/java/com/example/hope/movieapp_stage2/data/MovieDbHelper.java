package com.example.hope.movieapp_stage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hope on 6/7/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite_movies.db";
    private static final int DATABASE_VERSION = 4;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_FAVOURITE_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieDbContract.FavoriteMovies.TABLE_NAME + " (" +
                MovieDbContract.FavoriteMovies._ID + " INTEGER PRIMARY KEY, " +
                MovieDbContract.FavoriteMovies.COLUMN_MOVIE_POSTER + " TEXT, " +
                MovieDbContract.FavoriteMovies.COLUMN_MOVIE_DATE + "  TEXT, " +
                MovieDbContract.FavoriteMovies.COLUMN_MOVIE_TITLE + " TEXT, " +
                MovieDbContract.FavoriteMovies.COLUMN_MOVIE_RATE + " TEXT, " +
                MovieDbContract.FavoriteMovies.COLUMN_MOVIE_OVERVIEW + " TEXT, " +
                MovieDbContract.FavoriteMovies.COLUMN_MOVIE_ID + " TEXT" +
                ");";
        db.execSQL(SQL_CREATE_FAVOURITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieDbContract.FavoriteMovies.TABLE_NAME + ";");
        onCreate(db);
    }
}

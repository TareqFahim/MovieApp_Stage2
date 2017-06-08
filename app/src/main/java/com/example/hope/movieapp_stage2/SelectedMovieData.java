package com.example.hope.movieapp_stage2;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.hope.movieapp_stage2.utilities.MovieParser;
import com.example.hope.movieapp_stage2.utilities.MoviesData;

/**
 * Created by Hope on 6/6/2017.
 */
public class SelectedMovieData implements Parcelable {
    String moviePoster, movieReleaseDate, movieOverview, movieRate, movieTitle, movieID;

    public SelectedMovieData(MoviesData mData, int index){
        moviePoster = (String) mData.postersURL.get(index);
        movieReleaseDate = (String) mData.movieReleaseDate.get(index);
        movieOverview = (String) mData.movieOverview.get(index);
        movieRate = (String) mData.movieRate.get(index);
        movieTitle = (String) mData.movieTitle.get(index);
        movieID = (String) mData.movieID.get(index);
    }

    protected SelectedMovieData(Parcel in) {
        moviePoster = in.readString();
        movieReleaseDate = in.readString();
        movieOverview = in.readString();
        movieRate = in.readString();
        movieTitle = in.readString();
        movieID = in.readString();
    }

    public static final Creator<SelectedMovieData> CREATOR = new Creator<SelectedMovieData>() {
        @Override
        public SelectedMovieData createFromParcel(Parcel in) {
            return new SelectedMovieData(in);
        }

        @Override
        public SelectedMovieData[] newArray(int size) {
            return new SelectedMovieData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(moviePoster);
        dest.writeString(movieReleaseDate);
        dest.writeString(movieOverview);
        dest.writeString(movieRate);
        dest.writeString(movieTitle);
        dest.writeString(movieID);
    }
}

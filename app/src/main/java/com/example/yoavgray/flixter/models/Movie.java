package com.example.yoavgray.flixter.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public static final String MOVIE_POSTER_URL_PREFIX = "https://image.tmdb.org/t/p/";
    public static final String MOVIE_POSTER_SIZE = "w500";
    public static final String MOVIE_BACKDROP_SIZE = "w780";

    private String poster_path;
    private String overview;
    private String release_date;
    private String title;
    private String backdrop_path;
    private Double popularity;
    private Double vote_average;

    public Movie(String poster_path, String overview, String release_date, String title, String backdrop_path, Double popularity, Double vote_average) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_average = vote_average;
    }

    public String getPosterPath() {
        return MOVIE_POSTER_URL_PREFIX + MOVIE_POSTER_SIZE + poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return MOVIE_POSTER_URL_PREFIX + MOVIE_BACKDROP_SIZE + backdrop_path;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Double getVoteAverage() {
        return vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeDouble(popularity);
        dest.writeDouble(vote_average);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        poster_path     = in.readString();
        overview        = in.readString();
        release_date    = in.readString();
        title           = in.readString();
        backdrop_path   = in.readString();
        popularity      = in.readDouble();
        vote_average    = in.readDouble();
    }
}

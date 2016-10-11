package com.example.yoavgray.flixter.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String poster_path;
    private String overview;
    private String release_date;
    private String original_title;
    private String title;
    private String backdrop_path;
    private Double popularity;

    public Movie(String poster_path, String overview, String release_date, String original_title, String title, String backdrop_path, Double popularity) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.original_title = original_title;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public void setBackdropPath(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
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
        dest.writeString(original_title);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeDouble(popularity);
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
        original_title  = in.readString();
        title           = in.readString();
        backdrop_path   = in.readString();
        popularity      = in.readDouble();
    }
}

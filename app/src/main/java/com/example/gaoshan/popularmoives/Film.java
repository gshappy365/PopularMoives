package com.example.gaoshan.popularmoives;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaoshan on 2016/10/2.
 */

public class Film implements Parcelable {

    private String poster_path;
    private String overview;
    private String title;
    private int vote_average;
    private String release_date;

    public Film() {

    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVote_average() {
        return vote_average;
    }

    public void setVote_average(int vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    protected Film(Parcel in) {
        poster_path = in.readString();
        overview = in.readString();
        title = in.readString();
        vote_average = in.readInt();
        release_date = in.readString();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeString(overview);
        parcel.writeString(title);
        parcel.writeInt(vote_average);
        parcel.writeString(release_date);
    }
}

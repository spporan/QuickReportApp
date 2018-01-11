package com.example.android.quakereport;

/**
 * Created by poran on 10/1/2017.
 */

public class Earthquake {

    // @param magnitude earth quake magnitude
    private double magnitude;

    // @param city location of earthquake
    private String location;

    // @param date , the  date of the earthquake
    private long mTimeInMilliseconds;
    private String urls;

    public Earthquake(double magnitude, String location, long mTimeInMilliseconds,String urls) {
        this.magnitude = magnitude;
        this.location = location;
        this.mTimeInMilliseconds = mTimeInMilliseconds;
        this.urls=urls;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }


    public long getmTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }
    public String getUrls(){
        return urls;
    }


}
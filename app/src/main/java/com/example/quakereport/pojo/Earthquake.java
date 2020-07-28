package com.example.quakereport.pojo;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Earthquake {
    double magnitude;
    String location;
    long dateMilliseconds;
    String url;

    public Earthquake(double magnitude, String location, long dateMilliseconds, String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.dateMilliseconds = dateMilliseconds;
        this.url = url;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getNear(){
        int end = location.indexOf("of");
        return (end != -1) ? location.substring(0, end+3) : "Near of";
    }

    public String getLocation() {
        int end = location.indexOf("of");
        return (end != -1) ? location.substring(end+3) : location;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM DD, yyyy");
        return formatter.format(new Date(dateMilliseconds));
    }

    public String getTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a");
        return formatter.format(new Date(dateMilliseconds));
    }

    public String getURL() {
        return url;
    }
}

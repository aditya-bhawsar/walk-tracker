package com.aditya.walktracker.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.aditya.walktracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "walk")
public class WalkEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String startTime;
    private String endTime;
    private String date;
    private String distance;

    public WalkEntity(int id, String startTime, String endTime, String date, String distance) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.distance = distance;
    }

    @Ignore
    public WalkEntity(String startTime, String endTime, String date, String distance) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.distance = distance;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getDistance() { return distance; }
    public void setDistance(String distance) { this.distance = distance; }


    public int getWalkType(){
        float velocity = getVelocity();
        if(velocity>=0 && velocity<=1){ return R.drawable.ic_walk_slow; }
        if(velocity>2){ return R.drawable.ic_walk_fast; }
        return R.drawable.ic_walk_med;
    }

    public float getVelocity(){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(this.startTime);
            d2 = format.parse(this.endTime);
        }
        catch (Exception e){e.printStackTrace();}
        long diff = d2.getTime() - d1.getTime();
        long diffHours = diff/ (60*60*1000);

        return (Float.parseFloat(this.distance)/1000)/diffHours;
    }
}
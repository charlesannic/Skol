package com.android.charl.skol.java;

/**
 * Created by charl on 09/11/2016.
 */

public class Schedule {

    private long id;
    private long itsCourse;
    private int day;
    private String startTime;
    private String endTime;
    private String location;

    public Schedule(long id, long itsCourse, int day, String startTime, String endTime, String location) {
        this.id = id;
        this.itsCourse = itsCourse;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    public Schedule(int day, String startTime, String endTime, String location) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItsCourse() {
        return itsCourse;
    }

    public void setItsCourse(long itsCourse) {
        this.itsCourse = itsCourse;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}


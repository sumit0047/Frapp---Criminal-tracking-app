package com.vss.frapp;

public class Model_Location {

    String time;
    String date;
    String loc;
    String city;

    public Model_Location() {
    }

    public Model_Location(String time, String date, String loc, String city) {
        this.time = time;
        this.date = date;
        this.loc = loc;
        this.city = city;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

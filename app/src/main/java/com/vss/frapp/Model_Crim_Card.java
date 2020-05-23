package com.vss.frapp;

public class Model_Crim_Card {

    String photo;
    String id;
    String full_name;

    public Model_Crim_Card() {
    }

    public Model_Crim_Card(String photo, String id, String full_name) {
        this.photo = photo;
        this.id = id;
        this.full_name = full_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}

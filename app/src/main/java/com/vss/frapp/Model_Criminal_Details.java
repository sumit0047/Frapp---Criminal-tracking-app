package com.vss.frapp;

public class Model_Criminal_Details {

    String full_name;
    String id;
    String photo;
    String address;
    String desc;
    String age;

    public Model_Criminal_Details()
    {

    }

    public Model_Criminal_Details(String full_name, String id, String photo, String address, String desc, String age) {
        this.full_name = full_name;
        this.id = id;
        this.photo = photo;
        this.address = address;
        this.desc = desc;
        this.age = age;
    }



    public String getfull_name() {
        return full_name;
    }

    public void setfull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

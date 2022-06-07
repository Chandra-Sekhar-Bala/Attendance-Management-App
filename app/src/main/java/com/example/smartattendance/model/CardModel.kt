package com.example.smartattendance.model;

public class CardModel {
    private String  name, Class,  image;
    private  int roll;
    public CardModel(String name, int roll, String Class, String image){
        this.name = name;
        this.roll = roll;
        this.Class = Class;
        this.image = image;
    }

    public String getMyClass() {
        return Class;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getRoll() {
        return roll;
    }
}

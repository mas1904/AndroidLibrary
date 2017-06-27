package com.example.lukasz.wypozyczalnia.models;

import android.graphics.Bitmap;

/**
 * Created by Łukasz on 2017-01-06.
 */
public class CategoryModel {
    String name;
    int id;
    Bitmap image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public CategoryModel(String name, int id, Bitmap image) {

        this.name = name;
        this.id = id;
        this.image = image;
    }
}

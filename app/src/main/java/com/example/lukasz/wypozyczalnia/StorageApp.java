package com.example.lukasz.wypozyczalnia;

import android.app.Application;
import android.util.Log;
import android.view.MenuItem;

import com.example.lukasz.wypozyczalnia.models.BookModel;

import java.util.HashMap;

/**
 * Created by Łukasz on 2017-01-07.
 */

public class StorageApp extends Application {
    private int item=0;
    private int pre_item=0;
    private int user_id=0;
    private String name = "";
    private String surname = "";

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getPre_item() {
        return pre_item;
    }

    public void setPre_item(int pre_item) {
        this.pre_item = pre_item;
    }


    HashMap<Long,BookModel> inbin = new HashMap<>();

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
        Log.d("cc", "setItem: "+item);
    }
    @Override
    public void onCreate() {
        Log.d("app", "onCreate: ");

    }

    public HashMap<Long, BookModel> getInbin() {
        return inbin;
    }
    public void setpre(){
        pre_item=item;
    }

    public void setInbin(HashMap<Long, BookModel> inbin) {
        this.inbin = inbin;
    }

    public void logout() {
        user_id=0;
        name="";
        surname="";
    }
}

package com.example.lukasz.wypozyczalnia.models;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.Date;

/**
 * Created by Łukasz on 2016-12-28.
 */

public class NewsModel {
    String title;
    String desc;
    Date pub_date;

    public NewsModel(String title, String desc, Date pub_date) {
        this.title = title;
        this.desc = desc;
        this.pub_date = pub_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getPub_date() {
        return pub_date;
    }

    public void setPub_date(Date pub_date) {
        this.pub_date = pub_date;
    }

}
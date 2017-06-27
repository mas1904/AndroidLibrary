package com.example.lukasz.wypozyczalnia.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lukasz.wypozyczalnia.models.NewsModel;
import com.example.lukasz.wypozyczalnia.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Łukasz on 2016-12-28.
 */

public class NewsAdapter extends ArrayAdapter<NewsModel> {
    private final Activity context;
    List<NewsModel> news;

    public NewsAdapter(Activity context, List<NewsModel> news) {
        super(context, R.layout.news_list_item_layout, news);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.news = news;
    }


    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.news_list_item_layout, null,true);

        TextView Title = (TextView) rowView.findViewById(R.id.news_title);
        TextView Desc = (TextView) rowView.findViewById(R.id.news_desc);
        TextView Date = (TextView) rowView.findViewById(R.id.news_date);
        SimpleDateFormat dat = new SimpleDateFormat("dd-MM-yyyy");
        Date.setText(dat.format(news.get(position).getPub_date()));
        Title.setText(news.get(position).getTitle());

        Desc.setText(news.get(position).getDesc());

        return rowView;

    };
}

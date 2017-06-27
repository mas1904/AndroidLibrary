package com.example.lukasz.wypozyczalnia.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lukasz.wypozyczalnia.R;
import com.example.lukasz.wypozyczalnia.models.BookModel;
import com.example.lukasz.wypozyczalnia.models.LendDates;
import com.example.lukasz.wypozyczalnia.models.NewsModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Łukasz on 2016-12-29.
 */

public class LendAdapter extends ArrayAdapter<BookModel> {
    private final Activity context;
    List<BookModel> books;
    List<LendDates> dates;

    public LendAdapter(Activity context, List<BookModel> books, List<LendDates> dates) {
        super(context, R.layout.lend_item, books);
        this.context = context;
        this.books = books;
        this.dates = dates;
    }


    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.lend_item, null,true);

        TextView Title = (TextView) rowView.findViewById(R.id.book_title);
        TextView LendDate = (TextView) rowView.findViewById(R.id.lend_date);
        TextView TermDate = (TextView) rowView.findViewById(R.id.term_date);
        ImageView img = (ImageView) rowView.findViewById(R.id.book_img);
        if(books!=null) {
            Title.setText(books.get(position).getTitle());
            SimpleDateFormat form = new SimpleDateFormat("dd-MM-yyyy");
            boolean lol=false;
            if(dates.get(position).getLend()!=null) {
                LendDate.setText(getContext().getResources().getText(R.string.lendedin).toString()+" "+form.format(dates.get(position).getLend()));
                lol = true;
            }
            else {
                LendDate.setText(getContext().getResources().getText(R.string.readytoget).toString());
            }
            if(dates.get(position).getTerm()!=null)
                TermDate.setText(getContext().getResources().getText(R.string.handedin).toString()+" "+form.format(dates.get(position).getTerm()));
            else if(lol){
                Calendar c = Calendar.getInstance();
                c.setTime(dates.get(position).getLend());
                c.add(Calendar.DAY_OF_MONTH,14);
                TermDate.setText(getContext().getResources().getText(R.string.term).toString()+" "+form.format(c.getTime()));
            }


            img.setImageBitmap(books.get(position).getImage());
        }
        return rowView;

    };
}


package com.example.lukasz.wypozyczalnia.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lukasz.wypozyczalnia.R;
import com.example.lukasz.wypozyczalnia.models.BookModel;
import com.example.lukasz.wypozyczalnia.models.NewsModel;

import java.util.List;
import java.util.Set;

/**
 * Created by Łukasz on 2016-12-29.
 */

public class BookAdapter extends ArrayAdapter<BookModel> {
    private final Activity context;
    List<BookModel> books;
    Set<Long> inbin;

    public BookAdapter(Activity context, List<BookModel> books, Set<Long> inbin) {
        super(context, R.layout.book_list_item, books);
        this.context = context;
        this.books = books;
        this.inbin = inbin;
    }


    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.book_list_item, null,true);

        TextView Title = (TextView) rowView.findViewById(R.id.book_title);
        TextView Authors = (TextView) rowView.findViewById(R.id.book_authors);
        TextView Publisher = (TextView) rowView.findViewById(R.id.book_publisher);
        TextView Category = (TextView) rowView.findViewById(R.id.book_category);
        ImageView img = (ImageView) rowView.findViewById(R.id.book_img);
        final View sf = rowView.findViewById(R.id.sf);
        if(inbin.contains(books.get(position).getIsbn())) {
            sf.setBackgroundColor(context.getResources().getColor(R.color.colorAdded));
        }
        else {
            sf.setBackground(null);
        }
        Title.setText(books.get(position).getTitle());
        String aut="";
        if(books.get(position).getAuthors().size()>0)
            aut=books.get(position).getAuthors().get(0).getName()+" "+books.get(position).getAuthors().get(0).getSurname();;
        for(int i=1;i<books.get(position).getAuthors().size();i++)
            aut+=", "+books.get(position).getAuthors().get(i).getName()+" "+books.get(position).getAuthors().get(i).getSurname();
        Authors.setText(aut);
        Publisher.setText(books.get(position).getPublisher());
        Category.setText(books.get(position).getCategory());
        img.setImageBitmap(books.get(position).getImage());
        return rowView;

    };
}


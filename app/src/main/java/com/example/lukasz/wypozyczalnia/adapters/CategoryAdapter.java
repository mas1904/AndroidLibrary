package com.example.lukasz.wypozyczalnia.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lukasz.wypozyczalnia.R;
import com.example.lukasz.wypozyczalnia.models.BookModel;
import com.example.lukasz.wypozyczalnia.models.CategoryModel;

import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Łukasz on 2017-01-06.
 */
public class CategoryAdapter extends ArrayAdapter<CategoryModel> {
    private final Activity context;
    List<CategoryModel> category;

    public CategoryAdapter(Activity context, List<CategoryModel> category) {
        super(context, R.layout.category_list_item, category);
        this.context = context;
        this.category = category;
    }


    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.category_list_item, null,true);

        TextView Title = (TextView) rowView.findViewById(R.id.category_name);


        Title.setText(category.get(position).getName());

        return rowView;

    };
}

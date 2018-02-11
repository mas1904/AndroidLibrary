package com.example.lukasz.wypozyczalnia;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lukasz.wypozyczalnia.models.BookModel;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Łukasz on 2016-12-30.
 */
@TargetApi(23)
public class PopupBook extends DialogFragment{

    private BookModel books;
    private ArrayAdapter adapter;
    private HashMap bin;
    boolean abin = false;
    private List boo;

    public static PopupBook newInstance(int title, final List<BookModel> boo, final BookModel books, final ArrayAdapter adapter, final HashMap bin, boolean abin) {
        PopupBook frag = new PopupBook();
        frag.books = books;
        frag.adapter = adapter;
        frag.bin = bin;
        frag.abin = abin;
        frag.boo = boo;
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder pop = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

        final View rowView = inflater.inflate(R.layout.book_popup,
                (ViewGroup) getActivity().findViewById(R.id.layout_root));

        TextView desc = (TextView) rowView.findViewById(R.id.book_desc);

        //final SurfaceView sf = (SurfaceView) rowView.findViewById(R.id.book_sur);
        final TextView Title = (TextView) rowView.findViewById(R.id.book_title);
        TextView Authors = (TextView) rowView.findViewById(R.id.book_authors);
        TextView Publisher = (TextView) rowView.findViewById(R.id.book_publisher);
        TextView Category = (TextView) rowView.findViewById(R.id.book_category);
        TextView ISBN = (TextView) rowView.findViewById(R.id.book_isbn);


        final View sf = (View) rowView.findViewById(R.id.sf);
        if(bin!=null) {
            final CheckBox imgb = (CheckBox) rowView.findViewById(R.id.addToCart);
            if (bin.keySet().contains(books.getIsbn())) {
                sf.setBackgroundColor(getResources().getColor(R.color.colorAdded));
                imgb.setChecked(true);
            } else {
                sf.setBackground(null);
                imgb.setChecked(false);
            }
            imgb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bin.keySet().contains(books.getIsbn())) {
                        Toast.makeText(getActivity(), R.string.remove_from_cart,
                                Toast.LENGTH_SHORT).show();
                        bin.remove(books.getIsbn());
                        sf.setBackground(null);
                    } else {
                        Toast.makeText(getActivity(), R.string.add_to_cart,
                                Toast.LENGTH_SHORT).show();

                        bin.put(books.getIsbn(), books);
                        sf.setBackgroundColor(getResources().getColor(R.color.colorAdded));
                    }
                    adapter.notifyDataSetChanged();
                    //((CustomAdapter) list.getAdapter()).notifyDataSetChanged();
                }

            });

            final ImageView img = (ImageView) rowView.findViewById(R.id.book_img);

            img.setImageBitmap(books.getImage());

            desc.setText(books.getDescription());
            Title.setText(books.getTitle());
            String aut = "";
            if (books.getAuthors().size() > 0)
                aut = books.getAuthors().get(0).getName() + " " + books.getAuthors().get(0).getSurname();
            ;
            for (int i = 1; i < books.getAuthors().size(); i++)
                aut += ", " + books.getAuthors().get(i).getName() + " " + books.getAuthors().get(i).getSurname();
            Authors.setText(aut);
            Publisher.setText(getResources().getText(R.string.publisher).toString()+" "+ books.getPublisher());
            Category.setText(books.getCategory());
            ISBN.setText("ISBN: " + books.getIsbn());
            pop.setView(rowView);

            pop.setPositiveButton(getResources().getString(R.string.dismis), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        pop.create();

        return pop.show();

    }
    @Override
    public void onDetach(){
        super.onDetach();
        //if(abin==false) {
            if (abin == true && bin.get(books.getIsbn()) == null) {
                boo.remove(books);
                adapter.notifyDataSetChanged();
            }

        //}
    }

}

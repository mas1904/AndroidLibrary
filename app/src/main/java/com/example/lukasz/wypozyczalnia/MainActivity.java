package com.example.lukasz.wypozyczalnia;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.core.deps.guava.collect.Maps;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lukasz.wypozyczalnia.adapters.BookAdapter;
import com.example.lukasz.wypozyczalnia.adapters.NewsAdapter;
import com.example.lukasz.wypozyczalnia.models.BookModel;
import com.example.lukasz.wypozyczalnia.models.NewsModel;
import com.example.lukasz.wypozyczalnia.models.SerializableBitmap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MainActivity extends BaseActivity {

    static final int GET_BIN = 1;
    ListView news_list;
    ListView book_list;
    NewsAdapter adapter;
    BookAdapter b_adapter;
    List<NewsModel> news = new LinkedList<NewsModel>();
    List<BookModel> books = new LinkedList<BookModel>();
    HashMap<Long,BookModel> inbin;

    RESTHelper helper;
    private int mId=10;

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = new RESTHelper(getApplicationContext());
        adapter = new NewsAdapter(this, news);
        helper.queryNews(0,5,news,adapter);

        inbin = ((StorageApp)getApplicationContext()).getInbin();

        news_list = (ListView) findViewById(R.id.news_list);
        news_list.setAdapter(adapter);

        b_adapter = new BookAdapter(this, books,inbin.keySet());
        helper.queryBooks("?_sort=add_date&_order=DESC&_limit=3",books,b_adapter);
        book_list = (ListView) findViewById(R.id.newbook_list);
        book_list.setAdapter(b_adapter);
        book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // TODO Auto-generated method stub
                DialogFragment newFragment = PopupBook.newInstance(
                        R.string.dismis,books,books.get(+position),b_adapter,inbin,false);
                newFragment.show(getFragmentManager(), "dialog");

                // /books.get(+position);
                //AlertDialog.Builder a = pop_helper.PopProduct(books.get(+position),getApplicationContext(),(ViewGroup) findViewById(R.id.layout_root));
                //final AlertDialog alertDialog = a.create();

                //alertDialog.show();


            }
        });

    }


    @Override
    public void onResume() {
        super.nav =  R.id.nav_news;
        super.onResume();
        inbin = ((StorageApp)getApplicationContext()).getInbin();
        b_adapter.notifyDataSetChanged();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                Intent next = new Intent(MainActivity.this, SearchActivity.class);
                next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                next.putExtra("title",query);
                startActivity(next);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent next = new Intent(MainActivity.this, SettingsActivity.class);
            next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(next);
            return true;
        } else if (id == R.id.action_bin) {
            Intent next = new Intent(MainActivity.this, BinActivity.class);
            next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(next);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

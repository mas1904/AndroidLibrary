package com.example.lukasz.wypozyczalnia;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lukasz.wypozyczalnia.adapters.CategoryAdapter;
import com.example.lukasz.wypozyczalnia.models.BookModel;
import com.example.lukasz.wypozyczalnia.models.CategoryModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.lukasz.wypozyczalnia.MainActivity.GET_BIN;

public class CategoryActivity extends BaseActivity{

    ListView category_list;
    CategoryAdapter c_adapter;
    List<CategoryModel> category = new ArrayList<>();
    RESTHelper helper;
    HashMap<Long,BookModel> inbin = new HashMap<>();
    Context context;
    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        context = this;
        helper = new RESTHelper(getApplicationContext());
        c_adapter = new CategoryAdapter(this,category);
        helper.getCategory(category,c_adapter);
        category_list = (ListView) findViewById(R.id.list_category);
        category_list.setAdapter(c_adapter);

        category_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                StartActivityQuery(+position);
            }
        });}

    private void StartActivityQuery(int id) {
        Intent next = new Intent(CategoryActivity.this, CategoryBooksActivity.class);
        next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        int idd =+category.get(id).getId();
        next.putExtra("category_id",idd);
        startActivity(next);
    }

    @Override
    public void onResume(){
        super.nav =  R.id.nav_category;
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.binback, menu);
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
                Intent next = new Intent(CategoryActivity.this, SearchActivity.class);
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
            Intent next = new Intent(this, SettingsActivity.class);
            next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(next);
            return true;
        } else if (id == R.id.action_back) {
            onBackPressed();
        } else if (id == R.id.action_bin) {
            Intent next = new Intent(this, BinActivity.class);
            next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(next);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

package com.example.lukasz.wypozyczalnia;

import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lukasz.wypozyczalnia.adapters.BookAdapter;
import com.example.lukasz.wypozyczalnia.adapters.NewsAdapter;
import com.example.lukasz.wypozyczalnia.models.BookModel;
import com.example.lukasz.wypozyczalnia.models.NewsModel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    ListView book_list;
    BookAdapter b_adapter;
    List<BookModel> books = new LinkedList<BookModel>();
    HashMap<Long,BookModel> inbin;
    RESTHelper helper;

    String title="";
    Context context;

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }

    @Override
    public void onResume() {
        super.nav =  R.id.nav_category;
        super.onResume();
        inbin = ((StorageApp)getApplicationContext()).getInbin();

        String title2=getIntent().getStringExtra("title");
        if(title2!=title) {
            title=title2;
            helper.queryBooks("?title_like="+title,books,b_adapter);
        }
        b_adapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        helper = new RESTHelper(getApplicationContext());

        inbin = ((StorageApp)getApplicationContext()).getInbin();
        b_adapter = new BookAdapter(this, books,inbin.keySet());
        title=getIntent().getStringExtra("title");

        helper.queryBooks("?title_like="+title,books,b_adapter);
        book_list = (ListView) findViewById(R.id.list_search_books);
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
                title=query;
                books.clear();
                getIntent().putExtra("title",query);
                helper.queryBooks("?title_like="+title,books,b_adapter);
                b_adapter.notifyDataSetChanged();
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

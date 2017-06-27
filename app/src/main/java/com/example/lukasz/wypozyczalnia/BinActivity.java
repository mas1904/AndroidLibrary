package com.example.lukasz.wypozyczalnia;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lukasz.wypozyczalnia.adapters.BookAdapter;
import com.example.lukasz.wypozyczalnia.adapters.NewsAdapter;
import com.example.lukasz.wypozyczalnia.models.BookModel;
import com.example.lukasz.wypozyczalnia.models.NewsModel;

import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.example.lukasz.wypozyczalnia.MainActivity.GET_BIN;

public class BinActivity extends BaseActivity{

    ListView book_list;
    Button lend,delete;
    BookAdapter b_adapter;
    List<BookModel> books;
    RESTHelper helper;
    HashMap<Long,BookModel> inbin;
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

        setContentView(R.layout.activity_bin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        context = this;
        helper = new RESTHelper(getApplicationContext());

        inbin = ((StorageApp)getApplicationContext()).getInbin();

        books = new ArrayList(inbin.values());
        Log.d("tag", inbin.keySet()+"");

        b_adapter = new BookAdapter(this, books,inbin.keySet());
        b_adapter.notifyDataSetChanged();
        book_list = (ListView) findViewById(R.id.list_bin);
        book_list.setAdapter(b_adapter);
        book_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // TODO Auto-generated method stub
                DialogFragment newFragment = PopupBook.newInstance(
                        R.string.dismis,books,books.get(+position),b_adapter,inbin,true);
                newFragment.show(getFragmentManager(), "dialog");


                // /books.get(+position);
                //AlertDialog.Builder a = pop_helper.PopProduct(books.get(+position),getApplicationContext(),(ViewGroup) findViewById(R.id.layout_root));
                //final AlertDialog alertDialog = a.create();

                //alertDialog.show();

            }
        });
        lend = (Button) findViewById(R.id.lend_button);
        delete = (Button) findViewById(R.id.delete_all_button);
        lend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(books.size()>0){
                    if(((StorageApp) getApplicationContext()).getUser_id()!=0) {
                        AlertDialog.Builder ifLend = new AlertDialog.Builder(context);
                        String mes = "";
                        for (BookModel o : books)
                            mes += o.getTitle() + "\n";

                        ifLend.setMessage(mes).setTitle(R.string.wantlend);
                        ifLend.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        ifLend.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                helper.addLend(books, inbin, b_adapter, context);


                            }
                        });
                        ifLend.show();
                    } else {
                        Toast.makeText(BinActivity.this, R.string.notloged, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(books.size()>0){
                    AlertDialog.Builder ifLend = new AlertDialog.Builder(context);

                    ifLend.setTitle(R.string.wantdelete);
                    ifLend.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    ifLend.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            inbin.clear();
                            books.clear();
                            b_adapter.notifyDataSetChanged();

                        }
                    });
                    ifLend.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bin, menu);
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
                Intent next = new Intent(BinActivity.this, SearchActivity.class);
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
    public void onResume(){
        super.nav = 0;
        b_adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent next = new Intent(BinActivity.this, SettingsActivity.class);
            next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(next);
            return true;
        } else if (id == R.id.action_back) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}

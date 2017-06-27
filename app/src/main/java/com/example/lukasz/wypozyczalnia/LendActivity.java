package com.example.lukasz.wypozyczalnia;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lukasz.wypozyczalnia.adapters.BookAdapter;
import com.example.lukasz.wypozyczalnia.adapters.LendAdapter;
import com.example.lukasz.wypozyczalnia.models.BookModel;
import com.example.lukasz.wypozyczalnia.models.LendDates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.id.list;
import static com.example.lukasz.wypozyczalnia.MainActivity.GET_BIN;

public class LendActivity extends BaseActivity{


    Context context;

    private LendActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lend);

        mSectionsPagerAdapter = new LendActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        context = this;

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String ARG_SECTION_NUMBER = "section_number";
        //private static LendAdapter l_adapter;
        //private static final List<BookModel> books= new ArrayList<>();
        LendAdapter lad;

        public void setAdapter(final LendAdapter adapter){
            lad=adapter;
        }




        public PlaceholderFragment(final LendAdapter adapter) {
            lad=adapter;
        }

        public PlaceholderFragment() {

        }
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static LendActivity.PlaceholderFragment newInstance(int sectionNumber, final LendAdapter adapter) {
            LendActivity.PlaceholderFragment fragment = new LendActivity.PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            fragment.setAdapter(adapter);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.content_lend, container, false);

            ListView lv = (ListView) rootView.findViewById(R.id.list_lend);
            lv.setAdapter(lad);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private RESTHelper helper;
        private LendAdapter l_adapter;
        private LendAdapter l_adapter2;
        private LendAdapter l_adapter3;

        List<BookModel> toget = new ArrayList<>();
        List<LendDates> dates_to_get = new ArrayList<>();
        List<BookModel> lend = new ArrayList<>();
        List<LendDates> dates_lend = new ArrayList<>();
        List<BookModel> handed = new ArrayList<>();
        List<LendDates> dates_handed = new ArrayList<>();

        public void notifyy(){

            helper.getLend(toget,dates_to_get,l_adapter,1,((StorageApp) getApplicationContext()).getUser_id());
            helper.getLend(lend,dates_lend,l_adapter2,2,((StorageApp) getApplicationContext()).getUser_id());
            helper.getLend(handed,dates_handed,l_adapter3,3,((StorageApp) getApplicationContext()).getUser_id());
            l_adapter.notifyDataSetChanged();
            l_adapter2.notifyDataSetChanged();
            l_adapter3.notifyDataSetChanged();

        }
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            helper = new RESTHelper(getApplicationContext());
            l_adapter = new LendAdapter(LendActivity.this,toget,dates_to_get);
            l_adapter2 = new LendAdapter(LendActivity.this,lend,dates_lend);
            l_adapter3 = new LendAdapter(LendActivity.this,handed,dates_handed);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if(position==0)
                return LendActivity.PlaceholderFragment.newInstance(position + 1,l_adapter);
            else if(position==1)
                return LendActivity.PlaceholderFragment.newInstance(position + 1,l_adapter2);
            else
                return LendActivity.PlaceholderFragment.newInstance(position + 1,l_adapter3);

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.canget);
                case 1:
                    return getResources().getString(R.string.lend);
                case 2:
                    return getResources().getString(R.string.handed);
            }
            return null;
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("lol", "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume(){
        super.nav =  R.id.nav_lends;
        mSectionsPagerAdapter.notifyy();
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
                Intent next = new Intent(LendActivity.this, SearchActivity.class);
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

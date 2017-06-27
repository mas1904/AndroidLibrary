package com.example.lukasz.wypozyczalnia;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lukasz.wypozyczalnia.models.BookModel;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    public int nav=-1;
    TextView name,surname;

    protected void onCreateDrawer()
    {
        // R.id.drawer_layout should be in every activity with exactly the same id.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);



        toggle.syncState();
        toolbar.setSelected(false);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = (View) navigationView.getHeaderView(0);
        name=(TextView)header.findViewById(R.id.name);
        name.setText(R.string.notlogedheader);


        Log.d("sada", "onCreateDrawer: ");


    }
    @Override
    public void onResume(){
        if(((StorageApp)getApplicationContext()).getUser_id()!=0){
            navigationView.getMenu().getItem(4).setTitle(getResources().getString(R.string.logout));
            name.setText(((StorageApp)getApplicationContext()).getName()+" "+((StorageApp)getApplicationContext()).getSurname());
        } else {
            navigationView.getMenu().getItem(4).setTitle(getResources().getString(R.string.login));
            name.setText(R.string.notlogedheader);
        }
        if(nav!=-1)
            navigationView.setCheckedItem(nav);

        super.onResume();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        ((StorageApp)getApplicationContext()).setItem(id);

        Log.d("act", "onNavigationItemSelected: "+this);
        drawer.closeDrawer(GravityCompat.START);
        if (id == R.id.nav_news) {
            Intent next = new Intent(this, MainActivity.class);
            next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(next);
            return true;
        } else if (id == R.id.nav_category) {
            Intent next = new Intent(this, CategoryActivity.class);
            next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(next);
            return true;

        } else if (id == R.id.nav_lends) {
            if(((StorageApp) getApplicationContext()).getUser_id()!=0) {
                Intent next = new Intent(this, LendActivity.class);
                next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(next);
            }
            else {
                Toast.makeText(this, R.string.notloged, Toast.LENGTH_SHORT).show();
            }
            return true;

        } else if (id == R.id.nav_manage) {
            Intent next = new Intent(this, SettingsActivity.class);
            next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(next);
            return true;
        } else if (id == R.id.nav_login) {
            if(((StorageApp) getApplicationContext()).getUser_id()!=0) {
                ((StorageApp) getApplicationContext()).logout();
                Toast.makeText(BaseActivity.this, R.string.logoutyes, Toast.LENGTH_SHORT).show();
                onResume();
            }
            else {
                Intent next = new Intent(this, LoginActivity.class);
                next.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(next);
            }
            return true;
        }


        return true;
    }

    @Override
    public void onBackPressed() {
        ((StorageApp)getApplicationContext()).setpre();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

}

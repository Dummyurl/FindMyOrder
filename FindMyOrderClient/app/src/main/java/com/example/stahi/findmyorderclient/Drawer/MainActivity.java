package com.example.stahi.findmyorderclient.Drawer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.stahi.findmyorderclient.API.RestService;
import com.example.stahi.findmyorderclient.LoginActivities.LoginActivity;
import com.example.stahi.findmyorderclient.R;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.mapboxsdk.Mapbox;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener {

    DrawerLayout drawer;
    public NavigationView navigationView;
    RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setDrawerListener(this);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
        String email = sharedPref.getString("email", "email");
        String name = sharedPref.getString("name", "name");

        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.personName)).setText(name);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.personEmail)).setText(email);


        Mapbox.getInstance(getApplicationContext(), "pk.eyJ1Ijoic3RhaGllIiwiYSI6ImNqZnZjcWMydTM5NWszM252aXpzMnJtbTgifQ.h403qCXrUbgzM8zibqusdA");

        restService = new RestService();

        //Home = default fragment
        FragmentManager ft = getFragmentManager();
        FragmentTransaction transaction = ft.beginTransaction();
        transaction.add(R.id.flMain, new HomeFragment());
        transaction.commit();
        navigationView.setCheckedItem(R.id.nav_home);

    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            FragmentManager ft = getFragmentManager();
            FragmentTransaction transaction = ft.beginTransaction();
            transaction.replace(R.id.flMain, new HomeFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_logout) {
            LogoutUser();
        } else if (id == R.id.nav_contact) {
            FragmentManager ft = getFragmentManager();
            FragmentTransaction transaction = ft.beginTransaction();
            transaction.replace(R.id.flMain, new ContactFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_update_info) {
            FragmentManager ft = getFragmentManager();
            FragmentTransaction transaction = ft.beginTransaction();
            transaction.replace(R.id.flMain, new UpdateInfoFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_reset_password) {
            FragmentManager ft = getFragmentManager();
            FragmentTransaction transaction = ft.beginTransaction();
            transaction.replace(R.id.flMain, new ResetPasswordFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_chat) {
            FragmentManager ft = getFragmentManager();
            FragmentTransaction transaction = ft.beginTransaction();
            transaction.replace(R.id.flMain, new ChatUserListFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_doc_picture) {
            FragmentManager ft = getFragmentManager();
            FragmentTransaction transaction = ft.beginTransaction();
            transaction.replace(R.id.flMain, new PictureDocFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_doc_picture_list) {
            FragmentManager ft = getFragmentManager();
            FragmentTransaction transaction = ft.beginTransaction();
            transaction.replace(R.id.flMain, new DocPhotosListFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void LogoutUser(){
        Intent backToLoginIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(backToLoginIntent);

        SharedPreferences preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        finish();

        FirebaseAuth.getInstance().signOut();
    }



    //DRAWER METHODS ON

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        hideKeyboard(true, MainActivity.this);
    }

    public static void hideKeyboard(boolean val, Activity activity) {
        View view;
        view = activity.getWindow().getCurrentFocus();
        if (val == true) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        hideKeyboard(true, MainActivity.this);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        hideKeyboard(true, MainActivity.this);
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

}

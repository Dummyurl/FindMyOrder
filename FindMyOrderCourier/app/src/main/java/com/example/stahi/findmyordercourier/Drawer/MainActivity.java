package com.example.stahi.findmyordercourier.Drawer;

import android.Manifest;
import java.util.Date;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.stahi.findmyordercourier.API.RestService;
import com.example.stahi.findmyordercourier.AsyncSaveLocToDb;
import com.example.stahi.findmyordercourier.LoginActivities.LoginActivity;
import com.example.stahi.findmyordercourier.Models.MyLocation;
import com.example.stahi.findmyordercourier.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.mapbox.mapboxsdk.Mapbox;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DrawerLayout.DrawerListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener{

    DrawerLayout drawer;
    public NavigationView navigationView;
    RestService restService;

    private Location mylocation;
    private GoogleApiClient googleApiClient;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
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

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.personName)).setText(name);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.personEmail)).setText(email);


        Mapbox.getInstance(getApplicationContext(), "pk.eyJ1Ijoic3RhaGllIiwiYSI6ImNqZnZjcWMydTM5NWszM252aXpzMnJtbTgifQ.h403qCXrUbgzM8zibqusdA");

        restService = new RestService();
        setUpGClient();


        //Home = default fragment
        FragmentManager ft = getFragmentManager();
        FragmentTransaction transaction = ft.beginTransaction();
        transaction.add(R.id.flMain, new HomeFragment());
        transaction.commit();
        navigationView.setCheckedItem(R.id.nav_home);
    }


    // LOCATIONS METHODS ON

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (mylocation != null) {
            double latitude = mylocation.getLatitude();
            double longitude = mylocation.getLongitude();

            SharedPreferences sharedPref = MainActivity.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String senderStringId = sharedPref.getString("id", "0");
            int userId = Integer.parseInt(senderStringId);

            MyLocation myLocation = new MyLocation(latitude, longitude, userId);


            AsyncSaveLocToDb task = new AsyncSaveLocToDb();
            task.execute(myLocation);

            Log.d("LOCATIONTEST", latitude + " " + longitude + " " + userId);

        }
    }

    public Location getMylocation() {
        return mylocation;
    }

    private void getMyLocation(){
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(10000); // 60 sec
                    locationRequest.setFastestInterval(10000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied. You can initialize location requests here.
                                    int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // MyLocation settings are not satisfied. But could be fixed by showing the user a dialog.
                                    try {
                                        status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // MyLocation settings are not satisfied. And we can do nothing.
                                    finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        LogoutUser();
                        break;
                }
                break;
        }
    }

    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            getMyLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    //LOCATION METHODS OFF


    public void setActionBarTitle(String title) {
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
        } else if (id == R.id.nav_report_problem) {
            FragmentManager ft = getFragmentManager();
            FragmentTransaction transaction = ft.beginTransaction();
            transaction.replace(R.id.flMain, new ReportProblemFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_reset_password) {
            FragmentManager ft = getFragmentManager();
            FragmentTransaction transaction = ft.beginTransaction();
            transaction.replace(R.id.flMain, new ResetPasswordFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_navigator) {
            FragmentManager ft = getFragmentManager();
            FragmentTransaction transaction = ft.beginTransaction();
            transaction.replace(R.id.flMain, new PlacePickFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_chat) {
            FragmentManager ft = getFragmentManager();
            FragmentTransaction transaction = ft.beginTransaction();
            transaction.replace(R.id.flMain, new ChatUserListFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void LogoutUser() {
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

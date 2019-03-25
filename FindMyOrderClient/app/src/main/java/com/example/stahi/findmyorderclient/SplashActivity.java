package com.example.stahi.findmyorderclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.stahi.findmyorderclient.Drawer.MainActivity;
import com.example.stahi.findmyorderclient.LoginActivities.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_MS = 2000;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                // check if user is already logged in or not
                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                if (sharedPref != null) {
                    String email = sharedPref.getString("email", "email");
                    if (email.equals("email")) {
                        //go to login
                        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        SplashActivity.this.startActivity(loginIntent);
                    } else {
                        // otherwise redirect the user area
                        Intent userAreaIntent = new Intent(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.startActivity(userAreaIntent);
                    }
                    finish();
                }
            }
        };

        mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);
    }
}

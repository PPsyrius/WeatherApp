package com.androdocs.weatherapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    AlertDialog.Builder builder;
    boolean isConnected;
    int permissionRequestResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initMembers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initMembers();
    }

    //Starts the Next Activity
    private void startWeatherActivity() {
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 1000);
    }

    //Init the rest
    private void initMembers() {
        builder = new AlertDialog.Builder(SplashActivity.this);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        permissionRequestResult = this.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION");
        startWeatherActivity();
    }

}

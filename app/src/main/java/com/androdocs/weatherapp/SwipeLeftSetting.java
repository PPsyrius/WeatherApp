package com.androdocs.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.androdocs.weatherapp.Gestures.OnSwipeTouchListener;
import com.androdocs.weatherapp.utilities.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SwipeLeftSetting extends AppCompatActivity {
    double latitude, longitude;
    String LocName;

    RelativeLayout tvSwipeMe;
    ToggleButton ImpSwitch, ThaiSwitch;
    Button ButtonCurLoc, ButtonManLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_left_setting);
        initMembers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initMembers();
    }

    //Swipe Listener
    private class MyOnSwipeTouchListener extends OnSwipeTouchListener {
        public MyOnSwipeTouchListener(Context c) {
            super(c);
        }

        @Override
        public void onSwipeLeft() {
            Intent i = new Intent(SwipeLeftSetting.this, MainActivity.class);
            startActivity(i);
        }
    }

    private void searchByCityName() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this, R.style.AlertDialog);
        alert.setTitle(this.getString(R.string.search_title));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setMaxLines(1);
        input.setSingleLine(true);
        alert.setView(input);
        alert.setPositiveButton(R.string.ok, (dialog, whichButton) -> {
            String result = input.getText().toString();
            if (!result.isEmpty()) {
                System.out.println(result);
                Constants.CITY = result;
            }
        });
        alert.setNegativeButton(R.string.cancel, (dialog, whichButton) -> {
            // Cancelled
        });
        alert.show();
    }

    //Init the rest
    private void initMembers() {
        //SetUp View & Gesture
        tvSwipeMe = findViewById(R.id.tvSwipeMe);
        tvSwipeMe.setOnTouchListener(new MyOnSwipeTouchListener(this));

        //Setting Switch Stuff
        SharedPreferences sharedPrefs = getSharedPreferences("com.androdocs.weatherapp", MODE_PRIVATE);

        ImpSwitch =  findViewById(R.id.ImperialSwitch);
        ImpSwitch.setChecked(sharedPrefs.getBoolean("UnitPrefImp", false));
        ImpSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                Constants.UNIT = "imperial";
                SharedPreferences.Editor editor = getSharedPreferences("com.androdocs.weatherapp", MODE_PRIVATE).edit();
                editor.putBoolean("UnitPrefImp", true);
                editor.apply();
            }
            else {
                Constants.UNIT = "metric";
                SharedPreferences.Editor editor = getSharedPreferences("com.androdocs.weatherapp", MODE_PRIVATE).edit();
                editor.putBoolean("UnitPrefImp", false);
                editor.apply();
            }
        });
        ThaiSwitch =  findViewById(R.id.ThaiLanguageSwitch);
        ThaiSwitch.setChecked(sharedPrefs.getBoolean("LanguagePrefThai", false));
        ThaiSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                Constants.LANGUAGE = "THAI";
                SharedPreferences.Editor editor = getSharedPreferences("com.androdocs.weatherapp", MODE_PRIVATE).edit();
                editor.putBoolean("LanguagePrefThai", true);
                editor.apply();
            }
            else {
                Constants.LANGUAGE = "ENGLISH";
                SharedPreferences.Editor editor = getSharedPreferences("com.androdocs.weatherapp", MODE_PRIVATE).edit();
                editor.putBoolean("LanguagePrefThai", false);
                editor.apply();
            }
        });
        ButtonCurLoc = findViewById(R.id.ButtonCurrentLocation);
        ButtonCurLoc.setOnClickListener(v -> {
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert addresses != null;
            if (addresses.size() > 0) {
                LocName = addresses.get(0).getLocality(); //+","+addresses.get(0).getCountryCode();
                System.out.println(LocName);

                if (LocName != null) Constants.CITY = LocName;
            }
            else {
                //Placeholder Text until the System is fixed
                System.out.println("Placeholder Location, please remove this on release");
                Constants.CITY = "Lat Krabang,th";
            }
        });
        ButtonManLoc = findViewById(R.id.ButtonManualLocation);
        ButtonManLoc.setOnClickListener(v -> searchByCityName());
    }
}

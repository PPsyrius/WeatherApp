package com.androdocs.weatherapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androdocs.weatherapp.Adapters.CardAdapter;
import com.androdocs.weatherapp.models.DataObject;
import com.androdocs.weatherapp.models.Forecast;
import com.androdocs.weatherapp.utilities.Constants;
import com.androdocs.weatherapp.utilities.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.androdocs.weatherapp.Gestures.OnSwipeTouchListener;

public class SwipeRight5Days extends AppCompatActivity {
    private static final String TAG = SwipeRight5Days.class.getSimpleName();

    LocationManager locationManager;
    ProgressDialog progressDialog;
    StringBuilder addressStringBuilder;

    List<DataObject> weatherList;
    List<List<DataObject>> daysList;

    List<String> days;
    Set<String> distinctDays;
    CardAdapter cardAdapter;
    CardAdapter cardAdapter2;
    CardAdapter cardAdapter3;
    RecyclerView recyclerViewToday;
    RecyclerView recyclerViewTomorrow;
    RecyclerView recyclerViewLater;
    TabHost host;

    Toolbar toolbar;
    ConstraintLayout layout;

    RelativeLayout tvSwipeMe;
    String DegreeUnit, WindUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_right5_days);

        //SetUp View & Gesture
        /*
        tvSwipeMe = findViewById(R.id.tvSwipeMe);
        tvSwipeMe.setOnTouchListener(new MyOnSwipeTouchListener(this));
        */

        //Metric-Imperial
        Resources res = getResources();
        String[] tempUnitArray= res.getStringArray(R.array.temperature_degree);
        if(Objects.equals(Constants.UNIT, "metric")){ DegreeUnit = tempUnitArray[0]; }
        else{ DegreeUnit = tempUnitArray[1]; }

        String[] windUnitArray= res.getStringArray(R.array.wind_degree);
        if(Objects.equals(Constants.UNIT, "metric")){ WindUnit = windUnitArray[0]; }
        else{ WindUnit = windUnitArray[1]; }

        //Temp Unit
        Constants.TEMP_UNIT = " " + DegreeUnit;
        Constants.WIND_UNIT = " " + WindUnit;
        initMember();
        initUi();
        detectLocation();

        host = findViewById(R.id.tabHostT);
        host.setup();

        Date date = new Date();
        int day = date.getDay();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Today");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Today");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tomorrow");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tomorrow");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Later");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Later");
        host.addTab(spec);
        recyclerViewToday = findViewById(R.id.my_recycler_view);
        recyclerViewTomorrow = findViewById(R.id.my_recycler_view2);
        recyclerViewLater = findViewById(R.id.my_recycler_view3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        recyclerViewToday.setLayoutManager(layoutManager);
        recyclerViewToday.setItemAnimator(new DefaultItemAnimator());

        recyclerViewTomorrow.setLayoutManager(layoutManager2);
        recyclerViewTomorrow.setItemAnimator(new DefaultItemAnimator());

        recyclerViewLater.setLayoutManager(layoutManager3);
        recyclerViewLater.setItemAnimator(new DefaultItemAnimator());

        getWeather(addressStringBuilder);
    }

    /*** Swipe Listener ***/
    /*
    private class MyOnSwipeTouchListener extends OnSwipeTouchListener {
        public MyOnSwipeTouchListener(Context c) {
            super(c);
        }

        @Override
        public void onSwipeRight() {
            Intent i = new Intent(SwipeRight5Days.this, MainActivity.class);
            startActivity(i);
        }
    }
    */

    private void detectLocation() {
        addressStringBuilder = new StringBuilder();
        addressStringBuilder.append(Constants.CITY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menus, menu);
        return true;
    }

    private String getDate(Long milliTime) {
        Date currentDate = new Date(milliTime);
        SimpleDateFormat df = new SimpleDateFormat("dd");
        return df.format(currentDate);
    }

    private void getWeather(StringBuilder addressStringBuilder) {
        progressDialog.show();
        Call<Forecast> call = Utility.getApis().getWeatherForecastData(addressStringBuilder, Constants.API_KEY, Constants.UNIT);
        call.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(@NonNull Call<Forecast> call, @NonNull Response<Forecast> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.i(TAG, "onResponse: " + response.isSuccessful());
                    weatherList = response.body().getDataObjectList();
                    distinctDays = new LinkedHashSet<>();
                    for (DataObject obj : weatherList) {
                        distinctDays.add(getDate(obj.getDt() * 1000));
                    }
                    Log.i("DISTINCTSIZE", distinctDays.size() + "");

                    days = new ArrayList<>();
                    days.addAll(distinctDays);

                    for (String day : days) {
                        List<DataObject> temp = new ArrayList<>();
                        Log.i("DAY", day);
                        for (DataObject data : weatherList) {
                            Log.i("ELEMENT", getDate(data.getDt() * 1000));
                            if (getDate(data.getDt() * 1000).equals(day)) {
                                Log.i("ADDED", getDate(data.getDt() * 1000));
                                temp.add(data);
                            }
                        }
                        daysList.add(temp);
                    }

                    daysList.get(0).remove(0);

                    Log.i("DAYSLISTSIZE", daysList.size() + "");
                    cardAdapter = new CardAdapter(daysList.get(0));
                    recyclerViewToday.setAdapter(cardAdapter);

                    cardAdapter2 = new CardAdapter(daysList.get(1));
                    recyclerViewTomorrow.setAdapter(cardAdapter2);

                    cardAdapter3 = new CardAdapter(daysList.get(daysList.size() - 1));
                    recyclerViewLater.setAdapter(cardAdapter3);

                    toolbar.setTitle(response.body().getCity().getName() + ", " + response.body().getCity().getCountry());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Forecast> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(SwipeRight5Days.this, getString(R.string.msg_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUi() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.progress));
        toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        layout = findViewById(R.id.layoutWeather);
    }

    //Init the rest
    private void initMember() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        weatherList = new ArrayList<>();
        daysList = new ArrayList<>();
    }
}

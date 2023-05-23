package com.androdocs.weatherapp.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataObject {

    @SerializedName("dt")
    @Expose
    private int dt;
    @SerializedName("main")
    @Expose
    private com.androdocs.weatherapp.models.Main main;
    @SerializedName("weather")
    @Expose
    private final java.util.List<com.androdocs.weatherapp.models.Weather> weather = null;
    @SerializedName("wind")
    @Expose
    private com.androdocs.weatherapp.models.Wind wind;

    public long getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public com.androdocs.weatherapp.models.Main getMain() {
        return main;
    }

    public void setMain(com.androdocs.weatherapp.models.Main main) {
        this.main = main;
    }

    public java.util.List<com.androdocs.weatherapp.models.Weather> getWeather() {
        return weather;
    }

    public com.androdocs.weatherapp.models.Wind getWind() {
        return wind;
    }
}
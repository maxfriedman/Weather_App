package com.maxfriedman.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.maxfriedman.weather.activity.MainActivity;
import com.maxfriedman.weather.asynctask.WeatherAsyncTask;
import com.maxfriedman.weather.model.Weather;

import java.util.List;

/**
 * Created by Max on 12/12/15.
 */
public class PersistenceManager implements WeatherAsyncTask.QueryCompletionListener {

    private static final String TAG = "Persistence Manager";
    private Location mLocation;

    public interface WeatherFetchListener{
        void weatherObjectsFetched(List<Weather> scores);
        void errorFetchingWeatherObjects();
    }

    private Context mContext;
    private WeatherFetchListener mWeatherFetchListener;

    public PersistenceManager(Context context){
        mContext = context;
    }

    // Called from main activity - get weather objects based on settings parameters
    public void fetchWeatherObjects(final WeatherFetchListener listener, Boolean useCurrentLoc){

        mWeatherFetchListener = listener;

        SharedPreferences prefs = mContext.getSharedPreferences("prefs", mContext.MODE_PRIVATE);
        String zip = prefs.getString("zip", null);

        mLocation = ((MainActivity)mContext).location;

        // use current loc or zip code for forecast?
        if (useCurrentLoc == true && mLocation.getLatitude() != 0)
            refreshData(mLocation.getLatitude(), mLocation.getLongitude());
        else if (zip != null) {
            refreshData(zip);
        } else {
            dataNotFound();
        }
    }

    // refresh data using lat long
    public void refreshData(double lat, double lon) {

        WeatherAsyncTask weatherData = new WeatherAsyncTask(this);
        weatherData.execute(lat, lon);
    }

    // refresh data using zipcode
    public void refreshData(String zipcode) {

        double zip = Double.parseDouble(zipcode);
        WeatherAsyncTask weatherData = new WeatherAsyncTask(this);
        weatherData.execute(zip);
    }

    // data found/not found callbacks
    @Override
    public void dataFound(List<Weather> weatherObjects) {

        mWeatherFetchListener.weatherObjectsFetched(weatherObjects);
    }

    @Override
    public void dataNotFound() {

        mWeatherFetchListener.errorFetchingWeatherObjects();
    }

}

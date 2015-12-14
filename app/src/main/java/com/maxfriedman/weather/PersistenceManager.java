package com.maxfriedman.weather;

import android.content.Context;
import android.location.Location;

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

    public void fetchWeatherObjects(final WeatherFetchListener listener){

        mWeatherFetchListener = listener;
        refreshData("20052");

        /*
        scoresQuery.findInBackground(new FindCallback<Score>() {
            @Override
            public void done(List<Score> list, ParseException e) {
                if(e == null){
                    listener.scoresFetched(list);
                }
                else{
                    listener.errorFetchingScores();
                }
            }
        });*/
    }

    public void refreshData(double lat, double lon) {

        WeatherAsyncTask weatherData = new WeatherAsyncTask(this);
        weatherData.execute(lat, lon);
    }

    public void refreshData(String zipcode) {

        double zip = Double.parseDouble(zipcode);
        WeatherAsyncTask weatherData = new WeatherAsyncTask(this);
        weatherData.execute(zip);
    }

    /*
    //@Override
    public void locationFound(Location location) {

        Log.d(TAG, "location found");
        mLocation = location;
        refreshData(mLocation.getLatitude(), mLocation.getLongitude());
    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {

        Log.d(TAG, "location not found");

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.cant_find_location)
                .setPositiveButton(R.string.zipcode, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //openSettings();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //onCancel();
                    }
                });
    } */

    @Override
    public void dataFound(List<Weather> weatherObjects) {

        mWeatherFetchListener.weatherObjectsFetched(weatherObjects);

    }

    @Override
    public void dataNotFound() {

    }

}

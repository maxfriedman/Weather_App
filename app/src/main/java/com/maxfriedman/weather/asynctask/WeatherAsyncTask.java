package com.maxfriedman.weather.asynctask;

import android.os.AsyncTask;

import com.maxfriedman.weather.Utils;
import com.maxfriedman.weather.model.Weather;

import java.util.List;

/**
 * Created by Max on 12/12/15.
 */
public class WeatherAsyncTask extends AsyncTask<Double, Integer, List<Weather>> {
    private static final String TAG = "WeatherQuery";

    private QueryCompletionListener mCompletionListener;

    public interface QueryCompletionListener {
        public void dataFound(List<Weather> forecast);

        public void dataNotFound();
    }

    public WeatherAsyncTask(QueryCompletionListener completionListener) {

        mCompletionListener = completionListener;
    }

    // Parse JSON in bg
    @Override
    protected List<Weather> doInBackground(Double... query) {

        List<Weather> WeatherObject;
        String data = "";

        if (query.length == 1)
            data = Utils.getWeatherData(query[0].intValue());
        else if (query.length == 2)
            data = Utils.getWeatherData(query[0], query[1]);

        WeatherObject = Utils.parseJSON(data);

        return WeatherObject;
    }

    @Override
    protected void onPostExecute(List<Weather> weatherObjects) {
        if (weatherObjects != null) {
            mCompletionListener.dataFound(weatherObjects);
        } else {
            mCompletionListener.dataNotFound();
        }
    }

}

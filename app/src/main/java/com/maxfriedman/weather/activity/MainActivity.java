package com.maxfriedman.weather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maxfriedman.weather.PersistenceManager;
import com.maxfriedman.weather.R;
import com.maxfriedman.weather.adapter.WeatherArrayAdapter;
import com.maxfriedman.weather.model.Weather;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PersistenceManager.WeatherFetchListener {

    private LinearLayout mLinLayout;
    private TextView mTitle;
    private TextView mCityState;
    private ListView mListView;
    private ProgressBar mProgressBar;

    private Location mLocation;
    private PersistenceManager mPersistenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mTitle = (TextView) findViewById(R.id.title);
        mCityState = (TextView) findViewById(R.id.city);
        mListView = (ListView) findViewById(R.id.weatherList);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mPersistenceManager = new PersistenceManager(this);
        mPersistenceManager.fetchWeatherObjects(this);

        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void weatherObjectsFetched(List<Weather> weatherObjects) {

        mProgressBar.setVisibility(View.GONE);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        int days = prefs.getInt("days", 0);

        if (days == 0) {
            days = 3;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("days", days);
            editor.commit();
        }

        List<Weather> weatherObjectsCopy = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            weatherObjectsCopy.add(weatherObjects.get(i));
        }

        ArrayAdapter<Weather> adapter = new WeatherArrayAdapter(this,weatherObjectsCopy);
        mListView.setAdapter(adapter);

        Weather weatherObject = weatherObjects.get(0);
        mCityState.setText(weatherObject.getCityState());

        for (int i = 0; i < 7; i++) {

            Weather weatherOb = weatherObjects.get(i);

        }

    }

    @Override
    public void errorFetchingWeatherObjects() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, com.maxfriedman.weather.activity.SettingsActivity.class);
            this.startActivity(intent);
            return true;
        } else if (id == R.id.action_refresh) {
            mPersistenceManager.refreshData("20052");
        }

        return super.onOptionsItemSelected(item);
    }
}

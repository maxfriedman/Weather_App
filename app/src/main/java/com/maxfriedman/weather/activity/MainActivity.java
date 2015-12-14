package com.maxfriedman.weather.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maxfriedman.weather.PersistenceManager;
import com.maxfriedman.weather.R;
import com.maxfriedman.weather.adapter.WeatherArrayAdapter;
import com.maxfriedman.weather.model.Weather;
import com.maxfriedman.weather.sensor.LocationFinder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PersistenceManager.WeatherFetchListener, LocationFinder.LocationDetector {

    private TextView mCityState;
    private ListView mListView;
    private ProgressBar mProgressBar;

    public Location location;
    private PersistenceManager mPersistenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCityState = (TextView) findViewById(R.id.city);
        mListView = (ListView) findViewById(R.id.weatherList);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        // ask for permission to use loc

        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //show dialog
            new AlertDialog.Builder(this)
                    .setTitle(R.string.location_permission_title)
                    .setMessage(R.string.location_permission_message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //request user loc
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();
        }

        if(savedInstanceState == null) {

            // Retrieve loc
            LocationFinder locationFinder = new LocationFinder(this, this);
            locationFinder.detectLocation();

        } else {

            // save loc instance
            location = savedInstanceState.getParcelable("location");
        }

    }

    // listener callback method
    @Override
    public void weatherObjectsFetched(List<Weather> weatherObjects) {

        mProgressBar.setVisibility(View.GONE);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        int days = prefs.getInt("days", 0);

        // Set default if doesn't exist yet
        if (days == 0) {
            days = 3;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("days", days);
            editor.commit();
        }

        // only show days for the number specified by the user
        List<Weather> weatherObjectsCopy = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            weatherObjectsCopy.add(weatherObjects.get(i));
        }

        ArrayAdapter<Weather> adapter = new WeatherArrayAdapter(this,weatherObjectsCopy);
        mListView.setAdapter(adapter);

        Weather weatherObject = weatherObjects.get(0);
        mCityState.setText(weatherObject.getCityState());

    }

    @Override
    public void errorFetchingWeatherObjects() {

        Toast.makeText(this, R.string.data_not_found, Toast.LENGTH_SHORT);
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

    @Override
    public void onResume(){
        super.onResume();

        // Refresh UI when returning from settings

        if (mPersistenceManager != null) {

            SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
            String didChange = prefs.getString("didChange", null);

            if (didChange != null && didChange.equals("1")) {

                mProgressBar.setVisibility(View.VISIBLE);

                SharedPreferences.Editor editor = getSharedPreferences("prefs", 0).edit();
                editor.putString("changedUnit", "0");
                editor.commit();

                if (location != null)
                    mPersistenceManager.fetchWeatherObjects(this, true);
                else
                    mPersistenceManager.fetchWeatherObjects(this, false);

            }

        }

    }

    // Location delegate methods. Refresh forecast if data returned, fallback on zipcode

    @Override
    public void locationFound(Location location) {

        System.out.println("location found");

        mPersistenceManager = new PersistenceManager(this);
        mPersistenceManager.fetchWeatherObjects(this, true);
        mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void locationNotFound(LocationFinder.FailureReason failureReason) {

        Toast.makeText(this, R.string.cant_find_location, Toast.LENGTH_SHORT);

        mPersistenceManager = new PersistenceManager(this);
        mPersistenceManager.fetchWeatherObjects(this, false);
        mProgressBar.setVisibility(View.VISIBLE);

    }
}

package com.maxfriedman.weather;

import com.maxfriedman.weather.model.Weather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 12/12/15.
 */
public class Utils {

    private static final String TAG = "Utils";

    public static String parseQuery(double lat, double lon) {

        String url = Constants.WU_SEARCH_URL + Constants.API_KEY + Constants.API_FEATURES +
                Constants.API_SETTINGS + Constants.API_CONDITIONS + Constants.API_QUERY + lat + "," + lon + Constants.API_OUTPUT_FORMAT;
        return url;
    }

    public static String parseQuery(int zipcode) {

        String url = Constants.WU_SEARCH_URL + Constants.API_KEY + Constants.API_FEATURES +
                Constants.API_SETTINGS + Constants.API_CONDITIONS + Constants.API_QUERY + zipcode + "/data" + Constants.API_OUTPUT_FORMAT;
        return url;
    }

    public static String getWeatherData(double lat, double lon) {

        String queryUrl = parseQuery(lat, lon);
        return queryApi(queryUrl);
    }


    public static String getWeatherData(int zipcode) {

        String queryUrl = parseQuery(zipcode);
        return queryApi(queryUrl);
    }

    public static String queryApi(String queryUrl) {

        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(queryUrl);
        HttpResponse response;
        String data = "";

        System.out.println("LALA 1");

        try {
            System.out.println("LALA 2");

            response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            System.out.println("LALA 3");
            e.printStackTrace();
        }

        return data;
    }

    public static List<Weather> parseJSON(String data) {

        List<Weather> weatherObjects = new ArrayList<>();

        try {

            JSONObject reader = new JSONObject(data);

            String cityAndState = reader.getJSONObject("current_observation").getJSONObject("display_location").getString("full");

            JSONArray dailyWeatherData = reader.getJSONObject("forecast")
                    .getJSONObject("simpleforecast")
                    .getJSONArray("forecastday");

            for (int i = 0; i < dailyWeatherData.length(); i++) {

                // Parse JSON and pull out weather data

                JSONObject d = dailyWeatherData.getJSONObject(i);

                String highFahrenheit = d.getJSONObject("high").getString("fahrenheit");
                String highCelsius = d.getJSONObject("high").getString("celsius");
                String lowFahrenheit = d.getJSONObject("low").getString("fahrenheit");
                String lowCelsius = d.getJSONObject("low").getString("celsius");
                String conditions = d.getString("conditions");
                String iconUrl = d.getString("icon_url");
                String dayOfWeek = d.getJSONObject("date").getString("weekday_short");

                weatherObjects.add(new Weather(cityAndState, highFahrenheit, lowFahrenheit, highCelsius, lowCelsius, conditions, iconUrl, dayOfWeek));

                System.out.println(cityAndState + "   " + highFahrenheit + "   " + lowFahrenheit + "   " + conditions);
            }

        } catch (JSONException e) {
            return null;
        }

        if (weatherObjects.size() == 0)
            return null;

        return weatherObjects;
    }
}


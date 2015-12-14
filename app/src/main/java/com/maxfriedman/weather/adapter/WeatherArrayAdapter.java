package com.maxfriedman.weather.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.maxfriedman.weather.R;
import com.maxfriedman.weather.model.Weather;

import java.util.List;

/**
 * Created by Max on 12/12/15.
 */
public class WeatherArrayAdapter extends ArrayAdapter<Weather> {

    //view lookup cache
    private static class ViewHolder{

        ImageView icon;
        TextView dayOfWeek;
        TextView todayHighTemp;
        TextView todayLowTemp;
        TextView details;
        LinearLayout linLayout;

    }

    public WeatherArrayAdapter(Context context, List<Weather> scores){
        super(context, R.layout.weather_object,scores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //get the data for this index position
        Weather weatherObject = getItem(position);

        //check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        if(convertView == null){ //this is not a recycled view
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.weather_object,parent,false);

            viewHolder.dayOfWeek = (TextView) convertView.findViewById(R.id.dayOfWeek);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.details = (TextView) convertView.findViewById(R.id.details);
            viewHolder.todayHighTemp = (TextView) convertView.findViewById(R.id.todayHighTemp);
            viewHolder.todayLowTemp = (TextView) convertView.findViewById(R.id.todayLowTemp);
            viewHolder.linLayout = (LinearLayout) convertView.findViewById(R.id.linLayout);

            convertView.setTag(viewHolder);
        }
        else{ //this is a recycled view - this is providing us an optimization
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String degrees = sharedPreferences.getString("deg", null);
        if (degrees == null) {
            degrees = "F";
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("deg", degrees);
            editor.commit();
        }

        viewHolder.dayOfWeek.setText(weatherObject.getDayOfWeek());
        viewHolder.details.setText(weatherObject.getDetails());
        viewHolder.todayHighTemp.setText("Hi: " + weatherObject.getHighDegrees(degrees) + "˚" + degrees);
        viewHolder.todayLowTemp.setText("Lo: " + weatherObject.getLowDegrees(degrees) + "˚" + degrees);

        View view = viewHolder.linLayout.findViewWithTag("123");
        if (view == null) {

            ImageView icon = new ImageView(getContext());
            icon.setTag("123");
            ViewGroup.LayoutParams iconParams = new ViewGroup.LayoutParams(70, 70);
            Ion.with(icon).load(weatherObject.getIconUrl());
            icon.setLayoutParams(iconParams);
            viewHolder.linLayout.addView(icon, 1);
        }

        return convertView;

    }

}

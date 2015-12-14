package com.maxfriedman.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Max on 12/11/15.
 */
public class Weather implements Parcelable {

    private final String mCityState;

    private String mDayOfWeek;
    private String mHighFarenheit;
    private String mHighCelsius;
    private String mLowFarenheit;
    private String mLowCelsius;
    private String mIconUrl;
    private String mDetails;

    public Weather(String CityState, String highFarenheit, String lowFarenheit, String highCelsius, String lowCelsius, String description, String iconUrl, String weekday) {

        mCityState = CityState;
        mHighFarenheit = highFarenheit;
        mLowFarenheit = lowFarenheit;
        mHighCelsius = highCelsius;
        mLowCelsius = lowCelsius;
        mDetails = description;
        mIconUrl = iconUrl;
        mDayOfWeek = weekday;

    }

    public Weather(Parcel in) {

        mCityState = in.readString();
        mHighFarenheit = in.readString();
        mHighCelsius = in.readString();
        mLowFarenheit = in.readString();
        mLowCelsius = in.readString();
        mDetails = in.readString();
        mIconUrl = in.readString();
        mDayOfWeek = in.readString();
    }

    public String getHighDegrees(String unit) {

        switch (unit) {
            case "C":
                return mHighCelsius;
            case "F":
                return mHighFarenheit;
            default:
                break;
        }
        return "N/A";
    }

    public String getLowDegrees(String unit) {

        switch (unit) {
            case "C":
                return mLowCelsius;
            case "F":
                return mLowFarenheit;
            default:
                break;
        }
        return "...";
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public String getDayOfWeek() {
        return mDayOfWeek;
    }

    public String getCityState() {
        return mCityState;
    }

    public String getDetails() {
        return mDetails;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCityState);
        dest.writeString(mHighFarenheit);
        dest.writeString(mLowFarenheit);
        dest.writeString(mHighCelsius);
        dest.writeString(mLowCelsius);
        dest.writeString(mDetails);
        dest.writeString(mIconUrl);
        dest.writeString(mDayOfWeek);
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

}

package com.semeshky.kvg.kvgapi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StationLocation implements LatLngInterface {

    @Expose
    @SerializedName("category")
    private String mCategory;
    @Expose
    @SerializedName("id")
    private String mId;
    @Expose
    @SerializedName("latitude")
    private long mLatitude;
    @Expose
    @SerializedName("longitude")
    private long mLongitude;
    @Expose
    @SerializedName("name")
    private String mName;
    @Expose
    @SerializedName("shortName")
    private String mShortName;

    public String getCategory() {
        return mCategory;
    }

    public String getId() {
        return mId;
    }

    public long getLatitude() {
        return mLatitude;
    }

    public long getLongitude() {
        return mLongitude;
    }

    public String getName() {
        return mName;
    }

    public String getShortName() {
        return mShortName;
    }

    @Override
    public String toString() {
        return "StationLocation{" +
                "mCategory='" + mCategory + '\'' +
                ", mId='" + mId + '\'' +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mName='" + mName + '\'' +
                ", mShortName='" + mShortName + '\'' +
                '}';
    }
}

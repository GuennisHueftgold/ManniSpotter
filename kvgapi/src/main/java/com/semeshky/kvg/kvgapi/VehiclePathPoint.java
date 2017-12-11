package com.semeshky.kvg.kvgapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VehiclePathPoint implements LatLngInterface {
    @Expose
    @SerializedName("lat")
    private long mLatitude;
    @Expose
    @SerializedName("lon")
    private long mLongitude;
    @Expose
    @SerializedName("seq")
    private String mSequence;

    public long getLatitude() {
        return mLatitude;
    }

    public long getLongitude() {
        return mLongitude;
    }

    public String getSequence() {
        return mSequence;
    }
}

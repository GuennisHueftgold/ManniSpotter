package com.semeshky.kvg.kvgapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopPoint implements LatLngInterface{
    @Expose
    @SerializedName("category")
    private String mCategory;
    @Expose
    @SerializedName("id")
    private String mId;
    @Expose
    @SerializedName("label")
    private String mLabel;
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
    @Expose
    @SerializedName("stopPoint")
    private String mStopPoint;

    public String getCategory() {
        return mCategory;
    }

    public String getId() {
        return mId;
    }

    public String getLabel() {
        return mLabel;
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

    public String getStopPoint() {
        return mStopPoint;
    }
}

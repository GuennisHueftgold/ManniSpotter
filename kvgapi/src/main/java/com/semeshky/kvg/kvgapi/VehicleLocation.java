package com.semeshky.kvg.kvgapi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleLocation implements LatLngInterface {
    @Expose
    @SerializedName("id")
    private long mId;
    @Expose
    @SerializedName("isDeleted")
    private boolean mIsDeleted = false;
    @Expose
    @SerializedName("category")
    private String mCategory;
    @Expose
    @SerializedName("color")
    private String mColor;
    @Expose
    @SerializedName("tripId")
    private String mTripId;
    @Expose
    @SerializedName("name")
    private String mName;
    @Expose
    @SerializedName("path")
    private List<PathSegment> mPath;
    @Expose
    @SerializedName("longitude")
    private long mLongitude;
    @Expose
    @SerializedName("latitude")
    private long mLatitude;
    @Expose
    @SerializedName("heading")
    private int mHeading;

    public long getId() {
        return mId;
    }

    public boolean isDeleted() {
        return mIsDeleted;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getColor() {
        return mColor;
    }

    public String getTripId() {
        return mTripId;
    }

    public String getName() {
        return mName;
    }

    public List<PathSegment> getPath() {
        return mPath;
    }

    public long getLongitude() {
        return mLongitude;
    }

    public long getLatitude() {
        return mLatitude;
    }

    public int getHeading() {
        return mHeading;
    }
}

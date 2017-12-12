package com.semeshky.kvg.kvgapi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleLocationPath {
    @Expose
    @SerializedName("length")
    private float mLength;
    @Expose
    @SerializedName("x1")
    private long mFromLongitude;
    @Expose
    @SerializedName("x2")
    private long mToLongitude;
    @Expose
    @SerializedName("y1")
    private long mFromLatitude;
    @Expose
    @SerializedName("y2")
    private long mToLatitude;
    @Expose
    @SerializedName("angle")
    private int mAngle;

    public float getLength() {
        return mLength;
    }

    public long getFromLongitude() {
        return mFromLongitude;
    }

    public long getToLongitude() {
        return mToLongitude;
    }

    public long getFromLatitude() {
        return mFromLatitude;
    }

    public long getToLatitude() {
        return mToLatitude;
    }

    public int getAngle() {
        return mAngle;
    }
}

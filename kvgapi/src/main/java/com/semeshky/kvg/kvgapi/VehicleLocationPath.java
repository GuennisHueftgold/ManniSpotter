package com.semeshky.kvg.kvgapi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class VehicleLocationPath {
    @Expose
    @SerializedName("length")
    private float mLength;
    @Expose
    @SerializedName("x1")
    private long mX1;
    @Expose
    @SerializedName("x2")
    private long mX2;
    @Expose
    @SerializedName("y1")
    private long mY1;
    @Expose
    @SerializedName("y2")
    private long mY2;
    @Expose
    @SerializedName("angle")
    private int mAngle;

    public float getLength() {
        return mLength;
    }

    public long getX1() {
        return mX1;
    }

    public long getX2() {
        return mX2;
    }

    public long getY1() {
        return mY1;
    }

    public long getY2() {
        return mY2;
    }

    public int getAngle() {
        return mAngle;
    }
}

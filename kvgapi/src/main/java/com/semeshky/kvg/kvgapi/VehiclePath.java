package com.semeshky.kvg.kvgapi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehiclePath {
    @Expose
    @SerializedName("color")
    private String mColor;

    @Expose
    @SerializedName("wayPoints")
    private List<VehiclePathPoint> mPathPoints;

    public String getColor() {
        return mColor;
    }

    public List<VehiclePathPoint> getPathPoints() {
        return mPathPoints;
    }
}

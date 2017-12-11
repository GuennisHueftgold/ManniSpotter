package com.semeshky.kvg.kvgapi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StopPoints {

    @Expose
    @SerializedName("stopPoints")
    private List<StopPoint> mStopPoints;

    public List<StopPoint> getStopPoints() {
        return mStopPoints;
    }
}

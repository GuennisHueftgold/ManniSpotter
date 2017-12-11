package com.semeshky.kvg.kvgapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StationLocations {
    @Expose
    @SerializedName("stops")
    private List<StationLocation> mStops;

    public List<StationLocation> getStops() {
        return mStops;
    }
}

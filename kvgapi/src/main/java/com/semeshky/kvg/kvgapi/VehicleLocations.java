package com.semeshky.kvg.kvgapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleLocations {
    @Expose
    @SerializedName("lastUpdate")
    private long mLastUpdate;

    @Expose
    @SerializedName("vehicles")
    private List<VehicleLocation> mVehicles;

    public long getLastUpdate() {
        return mLastUpdate;
    }

    public List<VehicleLocation> getVehicles() {
        return mVehicles;
    }
}

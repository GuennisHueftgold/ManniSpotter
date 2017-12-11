package com.semeshky.kvg.kvgapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehiclePathInfo {
    @Expose
    @SerializedName("paths")
    private List<VehiclePath> mVehiclePaths;

    public List<VehiclePath> getVehiclePaths() {
        return mVehiclePaths;
    }
}

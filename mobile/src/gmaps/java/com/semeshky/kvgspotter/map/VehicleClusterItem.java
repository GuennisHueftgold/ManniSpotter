package com.semeshky.kvgspotter.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.semeshky.kvg.kvgapi.VehicleLocation;


public class VehicleClusterItem implements ClusterItem {

    private final VehicleLocation mVehicleLocation;
    private final LatLng mLatLng;

    public VehicleClusterItem(VehicleLocation vehicleLocation) {
        this.mVehicleLocation = vehicleLocation;
        this.mLatLng = CoordinateUtil.convert(this.mVehicleLocation);
    }

    public VehicleLocation getVehicleLocation() {
        return mVehicleLocation;
    }

    @Override
    public LatLng getPosition() {
        return this.mLatLng;
    }

    @Override
    public String getTitle() {
        return this.mVehicleLocation.getName();
    }

    @Override
    public String getSnippet() {
        return this.mVehicleLocation.getName();
    }

    public int getHeading() {
        return this.mVehicleLocation.getHeading();
    }
}

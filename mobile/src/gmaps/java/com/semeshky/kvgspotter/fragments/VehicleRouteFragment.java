package com.semeshky.kvgspotter.fragments;


import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.semeshky.kvg.kvgapi.VehicleLocation;
import com.semeshky.kvg.kvgapi.VehiclePath;
import com.semeshky.kvg.kvgapi.VehiclePathInfo;
import com.semeshky.kvg.kvgapi.VehiclePathPoint;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.map.MapStyleGenerator;
import com.semeshky.kvgspotter.map.CoordinateUtil;

import java.util.ArrayList;
import java.util.List;

public class VehicleRouteFragment extends BaseVehicleRouteFragment {

    private Polyline mRoutePolyline;
    private Marker mVehicleMarker;


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        super.onMapReady(googleMap);
        PolylineOptions line = new PolylineOptions();
        line.width(5).color(Color.RED);
        this.mRoutePolyline = this.mGoogleMap.addPolyline(line);
        MarkerOptions markerOptions = new MarkerOptions();
        MapStyleGenerator.vehicleMarker(this.getResources(), markerOptions);
        markerOptions.position(new LatLng(0, 0));
        markerOptions.visible(false);
        this.mVehicleMarker = this.mGoogleMap.addMarker(markerOptions);
    }

    @Override
    protected void updateVehicleMarker(VehicleLocation vehicleLocation) {
        if (vehicleLocation != null) {
            this.mVehicleMarker
                    .setPosition(CoordinateUtil.convert(vehicleLocation));
            this.mVehicleMarker.setTitle(vehicleLocation.getName());
            this.mVehicleMarker.setRotation(vehicleLocation.getHeading() - 90);
            this.mVehicleMarker.setVisible(true);
        } else {
            this.mVehicleMarker.setVisible(false);
        }
    }

    @Override
    protected void updatePolylist(VehiclePathInfo body) {
        if (body == null) {
            this.mRoutePolyline.setVisible(false);
            return;
        } else if (body.getVehiclePaths() == null) {
            this.mRoutePolyline.setVisible(false);
            return;
        } else if (body.getVehiclePaths().size() == 0) {
            this.mRoutePolyline.setVisible(false);
            return;
        }
        List<LatLng> mLatLngList = new ArrayList<>();
        final LatLngBounds.Builder builder = LatLngBounds.builder();
        for (VehiclePath path : body.getVehiclePaths()) {
            for (VehiclePathPoint point : path.getPathPoints()) {
                final LatLng latLng = CoordinateUtil.convert(point);
                mLatLngList.add(latLng);
                builder.include(latLng);
            }
        }
        this.mRoutePolyline.setPoints(mLatLngList);
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), getResources().getDimensionPixelSize(R.dimen.map_padding)));
        this.mRoutePolyline.setVisible(true);
    }
}

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
import com.semeshky.kvg.kvgapi.VehicleLocationPath;
import com.semeshky.kvg.kvgapi.VehiclePath;
import com.semeshky.kvg.kvgapi.VehiclePathInfo;
import com.semeshky.kvg.kvgapi.VehiclePathPoint;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.map.CoordinateUtil;
import com.semeshky.kvgspotter.map.MapStyleGenerator;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class VehicleRouteFragment extends BaseVehicleRouteFragment {

    private final static String[] POLYLINE_COLORS = {
            "#F44336",
            "#3F51B5",
            "#00BCD4",
            "#8BC34A",
            "#FFC107"
    };
    private Marker mVehicleMarker;
    private List<Polyline> mPolylines = new ArrayList<>();
    private Polyline mVehicleRoute;

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        super.onMapReady(googleMap);
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
            if (this.mVehicleRoute == null) {
                final float lineWidth = getResources().getDimension(R.dimen.map_route_path_width) / 2f;
                final PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions
                        .width(lineWidth)
                        .color(Color.BLACK);
                this.mVehicleRoute = this.getGoogleMap().addPolyline(polylineOptions);
            }
            final List<VehicleLocationPath> path = vehicleLocation.getPath();
            if (path == null || path.size() == 0) {
                this.mVehicleRoute.setVisible(false);
            } else {
                List<LatLng> latLngs = new ArrayList<>();
                for (VehicleLocationPath vehicleLocationPath : path) {
                    if (latLngs.size() == 0) {
                        latLngs.add(CoordinateUtil.convert(vehicleLocationPath.getFromLatitude(),
                                vehicleLocationPath.getFromLongitude()));
                    }
                    latLngs.add(CoordinateUtil.convert(vehicleLocationPath.getToLatitude(),
                            vehicleLocationPath.getToLongitude()));
                }
                Timber.d("Poitns set: %s", latLngs.size());
                this.mVehicleRoute.setPoints(latLngs);
                this.mVehicleRoute.setVisible(true);
            }
        } else {
            this.mVehicleMarker.setVisible(false);
        }
    }

    private void clearPolylineList() {
        while (this.mPolylines.size() > 0) {
            this.mPolylines.remove(0).remove();
        }
    }

    @Override
    protected void updatePolylist(VehiclePathInfo body) {
        this.clearPolylineList();
        final LatLngBounds.Builder builder = LatLngBounds.builder();
        final float lineWidth = getResources().getDimension(R.dimen.map_route_path_width);
        for (VehiclePath path : body.getVehiclePaths()) {
            final List<LatLng> latLngList = new ArrayList<>();
            for (VehiclePathPoint point : path.getPathPoints()) {
                final LatLng latLng = CoordinateUtil.convert(point);
                latLngList.add(latLng);
                builder.include(latLng);
            }
            if (latLngList.size() == 0) {
                //No items so skip
                continue;
            }
            PolylineOptions line = new PolylineOptions();
            line.color(Color.parseColor(POLYLINE_COLORS[this.mPolylines.size() % POLYLINE_COLORS.length]))
                    .width(lineWidth);
            line.addAll(latLngList);
            this.mPolylines.add(this.mGoogleMap.addPolyline(line));
        }
        if (this.mPolylines.size() == 0) {
            return;
        }
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), getResources().getDimensionPixelSize(R.dimen.map_padding)));
    }
}

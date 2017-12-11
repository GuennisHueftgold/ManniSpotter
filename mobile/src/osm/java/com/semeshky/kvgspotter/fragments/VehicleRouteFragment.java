package com.semeshky.kvgspotter.fragments;


import android.graphics.Color;
import android.view.ViewTreeObserver;

import com.semeshky.kvg.kvgapi.VehicleLocation;
import com.semeshky.kvg.kvgapi.VehiclePath;
import com.semeshky.kvg.kvgapi.VehiclePathInfo;
import com.semeshky.kvg.kvgapi.VehiclePathPoint;
import com.semeshky.kvgspotter.map.CoordinateUtil;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class VehicleRouteFragment extends BaseVehicleRouteFragment {

    private Polyline mRoutePolyline;
    private Marker mVehicleMarker;
    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener=new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            VehicleRouteFragment.this
                    .updateCamera();
        }
    };


    @Override
    public void onMapReady(final MapView mapView) {
        this.mRoutePolyline = new Polyline();
        this.mRoutePolyline.setWidth(5);
        this.mRoutePolyline.setColor(Color.RED);
        mapView.getOverlayManager()
                .add(this.mRoutePolyline);
        this.mVehicleMarker = new Marker(mapView);
        this.mVehicleMarker.setPosition(new GeoPoint(0d, 0d));
        this.mVehicleMarker.setFlat(true);

    }

    @Override
    protected void updateVehicleMarker(VehicleLocation vehicleLocation) {
        if (vehicleLocation != null) {
            this.mVehicleMarker
                    .setPosition(CoordinateUtil.convert(vehicleLocation));
            this.mVehicleMarker.setTitle(vehicleLocation.getName());
            this.mVehicleMarker.setRotation(vehicleLocation.getHeading() - 90);
        } else {
        }
    }

    @Override
    protected void updatePolylist(VehiclePathInfo body) {
        /*if (body == null) {
            this.mRoutePolyline.setVisible(false);
            return;
        } else if (body.getVehiclePaths() == null) {
            this.mRoutePolyline.setVisible(false);
            return;
        } else if (body.getVehiclePaths().size() == 0) {
            this.mRoutePolyline.setVisible(false);
            return;
        }*/
        List<GeoPoint> mLatLngList = new ArrayList<>();
        for (VehiclePath path : body.getVehiclePaths()) {
            for (VehiclePathPoint point : path.getPathPoints()) {
                final GeoPoint latLng = CoordinateUtil.convert(point);
                mLatLngList.add(latLng);
            }
        }
        this.mRoutePolyline.setPoints(mLatLngList);
        this.updateCamera();
    }

    private void updateCamera(){
        if(this.mRoutePolyline.getPoints()==null||this.mRoutePolyline.getPoints().size()==0)
            return;
        final BoundingBox boundingBox = BoundingBox
                .fromGeoPoints(this.mRoutePolyline.getPoints())
                .increaseByScale(1.1f);
        this.getGoogleMap().zoomToBoundingBox(boundingBox, true);
    }
    @Override
    public void onResume(){
        super.onResume();
        this.updateCamera();
        this.getView()
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(this.mGlobalLayoutListener);
    }

    @Override
    public void onPause(){
        super.onPause();
        this.getView()
                .getViewTreeObserver()
                .removeOnGlobalLayoutListener(this.mGlobalLayoutListener);
    }

}

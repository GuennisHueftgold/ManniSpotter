package com.semeshky.kvgspotter.fragments;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.semeshky.kvg.kvgapi.VehicleLocation;
import com.semeshky.kvg.kvgapi.VehicleLocations;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.activities.StationDetailActivity;
import com.semeshky.kvgspotter.activities.TripPassagesActivity;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.map.CoordinateUtil;
import com.semeshky.kvgspotter.map.DualOnMarkerClickListener;
import com.semeshky.kvgspotter.map.VehicleClusterItem;
import com.semeshky.kvgspotter.map.VehicleClusterRenderer;

import java.util.List;

public final class LiveMapFragment extends BaseLiveMapFragment {
    private ClusterManager<VehicleClusterItem> mClusterManager;
    private final GoogleMap.OnMarkerClickListener mOnMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (marker.getTag() instanceof Stop) {
                startActivity(StationDetailActivity
                        .createIntent(getContext(),
                                (Stop) marker.getTag()));
                return true;
            }
            return false;
        }
    };
    private final ClusterManager.OnClusterItemInfoWindowClickListener<VehicleClusterItem> mOnClusterItemClickListener
            = new ClusterManager.OnClusterItemInfoWindowClickListener<VehicleClusterItem>() {
        @Override
        public void onClusterItemInfoWindowClick(VehicleClusterItem clusterItem) {
            startActivity(TripPassagesActivity.createIntent(LiveMapFragment.this.getContext(),
                    clusterItem.getVehicleLocation().getTripId(),
                    clusterItem.getVehicleLocation().getName(),
                    null,
                    null,
                    "departure"));
        }
    };

    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);
        this.mClusterManager = new ClusterManager<>(this.getContext(), map);
        final VehicleClusterRenderer clusterRenderer = new VehicleClusterRenderer(this.getContext(), map, this.mClusterManager);
        this.mClusterManager.setRenderer(clusterRenderer);
        map.setOnCameraIdleListener(this.mClusterManager);
        final DualOnMarkerClickListener dualOnMarkerClickListener = new DualOnMarkerClickListener(this.mOnMarkerClickListener, this.mClusterManager);

        map.setOnMarkerClickListener(dualOnMarkerClickListener);
        map.setOnInfoWindowClickListener(this.mClusterManager);
        //this.mClusterManager.setOnClusterItemClickListener(this.mOnClusterItemClickListener);
        this.mClusterManager.setOnClusterItemInfoWindowClickListener(this.mOnClusterItemClickListener);
        //this.refreshData();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(54.3232941, 10.1381642), 12f));
    }

    @Override
    protected void updateVehicleLocations(VehicleLocations vehicleLocations) {
        /*int i = 0;
        final List<Overlay> overlays = this.mVehicleOverlay.getItems();
        final int markers = overlays.size();
        final List<VehicleLocation> vehicleLocationList = vehicleLocations.getVehicles();
        for (; i < vehicleLocationList.size(); i++) {
            final VehicleLocation vehicleLocation = vehicleLocationList.get(i);
            if (vehicleLocation.isDeleted())
                continue;
            Marker marker;
            if (i < markers) {
                marker = (Marker) overlays.get(i);
            } else {
                marker = new Marker(getGoogleMap());
                marker.setFlat(true);
                marker.setIcon(getResources().getDrawable(R.drawable.ic_label_black_24dp));
                marker.setAnchor(0.5f, 0.5f);
                marker.setOnMarkerClickListener(this.mMarkerClickListener);
                this.mVehicleOverlay.add(marker);
            }
            marker.setRelatedObject(vehicleLocation);
            marker.setSnippet(vehicleLocation.getName());
            marker.setRotation(vehicleLocation.getHeading() - 90);
            marker.setPosition(CoordinateUtil.convert(vehicleLocation));
        }
        for (; i < markers; i++) {
            this.mVehicleOverlay.remove(overlays.get(i));
        }
        this.getGoogleMap()
                .postInvalidate();*/
    }

    @Override
    protected void updateStops(List<Stop> stops) {
       /* int i = 0;
        final List<Overlay> overlays = this.mStopOverlay.getItems();
        final int markers = overlays.size();
        for (; i < stops.size(); i++) {
            final Stop stop = stops.get(i);
            Marker marker;
            if (i < markers) {
                marker = (Marker) overlays.get(i);
            } else {
                marker = new Marker(getGoogleMap());
                marker.setFlat(true);
                marker.setIcon(getResources().getDrawable(R.drawable.ic_stop_black_24dp));
                marker.setAnchor(0.5f, 0.9f);
                marker.setOnMarkerClickListener(this.mMarkerClickListener);
                this.mStopOverlay.add(marker);
            }
            marker.setRelatedObject(stop);
            marker.setSnippet(stop.getName());
            marker.setPosition(CoordinateUtil.convert(stop));
        }
        for (; i < markers; i++) {
            this.mStopOverlay.remove(overlays.get(i));
        }
        this.getGoogleMap()
                .postInvalidate();*/
    }

    /*
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        this.mClusterManager = new ClusterManager<>(this, map);
        this.mClusterManager.setRenderer(new VehicleClusterRenderer(this, this.map, this.mClusterManager));
        this.map.setOnCameraIdleListener(this.mClusterManager);
        final DualOnMarkerClickListener dualOnMarkerClickListener = new DualOnMarkerClickListener(this.mOnMarkerClickListener, this.mClusterManager);

        this.map.setOnMarkerClickListener(dualOnMarkerClickListener);
        this.map.setOnInfoWindowClickListener(this.mClusterManager);
        //this.mClusterManager.setOnClusterItemClickListener(this.mOnClusterItemClickListener);
        this.mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<VehicleCluster>() {
            @Override
            public void onClusterItemInfoWindowClick(VehicleCluster vehicleCluster) {
                startActivity(TripPassagesActivity.createIntent(LiveMapActivity.this,
                        vehicleCluster.getVehicleLocation().getTripId(),
                        vehicleCluster.getVehicleLocation().getName(),
                        null,
                        null,
                        "departure"));
            }
        });
        this.refreshData();
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(54.3232941, 10.1381642), 12f));
    }

    @Override
    protected void updateMarker() {
        map.clear();
        this.mClusterManager.clearItems();
        //LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (this.mStopsLiveData.getValue() != null) {
            for (Stop stop : this.mStopsLiveData.getValue()) {
                final Marker marker = map.addMarker(createStationMarker(stop));
                marker.setTag(stop);
            }
        }
        if (this.mVehicleLocations != null) {
            for (VehicleLocation vehicleLocation : this.mVehicleLocations.getVehicles()) {
                if (!vehicleLocation.isDeleted()) {
                    this.mClusterManager.addItem(new VehicleCluster(vehicleLocation));
                    //final Marker marker = map.addMarker(createBusMarker(vehicleLocation));
                    //marker.setTag(vehicleLocation);
                }
            }
        }
        this.mClusterManager.cluster();
    }

    private MarkerOptions createStationMarker(Stop stop) {
        return MapStyleGenerator
                .stationMarker(getResources())
                .position(CoordinateUtil.convert(stop));
    }*/
}

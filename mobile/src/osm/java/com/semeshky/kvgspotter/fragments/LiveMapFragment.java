package com.semeshky.kvgspotter.fragments;

import com.github.guennishueftgold.trapezeapi.VehicleLocation;
import com.github.guennishueftgold.trapezeapi.VehicleLocations;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.map.CoordinateUtil;
import com.semeshky.kvgspotter.map.ExtendedOverlayManager;
import com.semeshky.kvgspotter.viewmodel.ActivityLiveMapViewModel;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.List;

public final class LiveMapFragment extends BaseLiveMapFragment {
    private final FolderOverlay mVehicleOverlay=new FolderOverlay();
    private final FolderOverlay mStopOverlay=new FolderOverlay();
    private final Marker.OnMarkerClickListener mMarkerClickListener =new Marker.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker, MapView mapView) {
            if(marker.getRelatedObject()!=null){
                if(marker.getRelatedObject() instanceof VehicleLocation){
                    LiveMapFragment
                            .this
                            .mViewModel
                            .setSelectedVehicle((VehicleLocation) marker.getRelatedObject());
                    /*
                    LiveMapFragment
                            .this
                            .onVehicleLocationSelected((VehicleLocation) marker.getRelatedObject());*/
                }else if(marker.getRelatedObject() instanceof Stop){
                    LiveMapFragment
                            .this
                            .mViewModel
                            .setSelectedStop((Stop) marker.getRelatedObject());
                    /*
                    LiveMapFragment
                            .this
                            .onStopSelected((Stop)marker.getRelatedObject());*/
                }else{
                    return false;
                }
                return true;
            }else
                return false;
        }
    };

    @Override
    public void onMapReady(MapView map) {
        final ExtendedOverlayManager extendedOverlayManager = new ExtendedOverlayManager(map.getOverlayManager().getTilesOverlay());
        map.setOverlayManager(extendedOverlayManager);
        map.setMultiTouchControls(true);
        map.getController()
                .setZoom(10);
        map.getController()
                .setCenter(new GeoPoint(54d,10d));
        map.getOverlayManager()
                .add(0,this.mStopOverlay);
        map.getOverlayManager()
                .add(1,this.mVehicleOverlay);
        extendedOverlayManager.setOnTapMissListener(new ExtendedOverlayManager.OnTapMissListener() {
            @Override
            public void onTapMissed() {
                LiveMapFragment
                        .this
                        .mViewModel
                        .setDetailsStatus(ActivityLiveMapViewModel.DETAILS_STATUS_CLOSED);
            }
        });
    }

    @Override
    protected void updateVehicleLocations(VehicleLocations vehicleLocations) {
        int i=0;
        final List<Overlay> overlays=this.mVehicleOverlay.getItems();
        final int markers=overlays.size();
        final List<VehicleLocation> vehicleLocationList=vehicleLocations.getVehicles();
        for(;i<vehicleLocationList.size();i++){
            final VehicleLocation vehicleLocation=vehicleLocationList.get(i);
            if (vehicleLocation.isDeleted())
                continue;
            Marker marker;
            if(i<markers){
                marker= (Marker) overlays.get(i);
            }else{
                marker=new Marker(getGoogleMap());
                marker.setFlat(true);
                marker.setIcon(getResources().getDrawable(R.drawable.ic_label_red_24dp));
                marker.setAnchor(0.5f,0.5f);
                marker.setOnMarkerClickListener(this.mMarkerClickListener);
                this.mVehicleOverlay.add(marker);
            }
            marker.setRelatedObject(vehicleLocation);
            marker.setSnippet(vehicleLocation.getName());
            marker.setRotation(vehicleLocation.getHeading()-90);
            marker.setPosition(CoordinateUtil.convert(vehicleLocation));
        }
        for(;i<markers;i++){
            this.mVehicleOverlay.remove(overlays.get(i));
        }
        this.getGoogleMap()
                .postInvalidate();
    }

    @Override
    protected void updateStops(List<Stop> stops) {
        int i=0;
        final List<Overlay> overlays=this.mStopOverlay.getItems();
        final int markers=overlays.size();
        for(;i<stops.size();i++){
            final Stop stop=stops.get(i);
            Marker marker;
            if(i<markers){
                marker= (Marker) overlays.get(i);
            }else{
                marker=new Marker(getGoogleMap());
                marker.setFlat(true);
                marker.setIcon(getResources().getDrawable(R.drawable.ic_stop_black_24dp));
                marker.setAnchor(0.5f,0.9f);
                marker.setOnMarkerClickListener(this.mMarkerClickListener);
                this.mStopOverlay.add(marker);
            }
            marker.setRelatedObject(stop);
            marker.setSnippet(stop.getName());
            marker.setPosition(CoordinateUtil.convert(stop));
        }
        for(;i<markers;i++){
            this.mStopOverlay.remove(overlays.get(i));
        }
        this.getGoogleMap()
                .postInvalidate();
    }
}

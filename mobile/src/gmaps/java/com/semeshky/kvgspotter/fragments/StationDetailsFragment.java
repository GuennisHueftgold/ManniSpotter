package com.semeshky.kvgspotter.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.adapter.RoutesAdapter;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.database.StopPoint;
import com.semeshky.kvgspotter.databinding.FragmentStationDetailsBinding;
import com.semeshky.kvgspotter.map.GMapsUtil;
import com.semeshky.kvgspotter.map.MapStyleGenerator;
import com.semeshky.kvgspotter.map.CoordinateUtil;
import com.semeshky.kvgspotter.viewmodel.StationDetailActivityViewModel;

import java.util.List;

public final class StationDetailsFragment extends BaseStationDetailsFragment {

    @CallSuper
    @Override
    public void onMapReady(final GoogleMap map) {
        super.onMapReady(map);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    protected void updateMap(final Stop stop, final List<StopPoint> stopPoints) {
        this.getGoogleMap().clear();
        if (stop != null) {
            final LatLng latLng = CoordinateUtil.convert(stop);
            final MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng);
            this.getGoogleMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            this.getGoogleMap().addMarker(markerOptions);
        }
        if (stopPoints != null) {
            for (StopPoint stopPoint : stopPoints) {
                final MarkerOptions markerOptions1 =
                        MapStyleGenerator.stationMarker(getResources())
                                .position(CoordinateUtil.convert(stopPoint));
                this.getGoogleMap().addMarker(markerOptions1);
            }
        }
    }

}

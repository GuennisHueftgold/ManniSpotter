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

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.adapter.RoutesAdapter;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.database.StopPoint;
import com.semeshky.kvgspotter.databinding.FragmentStationDetailsBinding;
import com.semeshky.kvgspotter.map.CoordinateUtil;
import com.semeshky.kvgspotter.map.GMapsUtil;
import com.semeshky.kvgspotter.map.MapStyleGenerator;
import com.semeshky.kvgspotter.viewmodel.StationDetailActivityViewModel;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

import timber.log.Timber;

public final class StationDetailsFragment extends BaseStationDetailsFragment {

    @CallSuper
    @Override
    public void onMapReady(final MapView mapView) {
        updateMap(this.mViewModel.getStop().getValue(),this.mViewModel.getStopPoints().getValue());
    }

    @Override
    protected void updateMap(final Stop stop, final List<StopPoint> stopPoints) {
        this.getGoogleMap().getOverlayManager().clear();
        if (stop != null) {
            final GeoPoint latLng = CoordinateUtil.convert(stop);
            final Marker marker=new Marker(getGoogleMap());
            marker.setPosition(latLng);
            this.getGoogleMap().getOverlayManager()
                    .add(marker);
            this.getGoogleMap()
                    .getController()
                    .setZoom(17);
            this.getGoogleMap()
                    .getController()
                    .setCenter(latLng);
        }
        if (stopPoints != null) {
            for (StopPoint stopPoint : stopPoints) {
                final GeoPoint latLng = CoordinateUtil.convert(stopPoint);
                final Marker marker=new Marker(getGoogleMap());
                marker.setPosition(latLng);
                this.getGoogleMap().getOverlayManager()
                        .add(marker);
            }
        }
    }

}

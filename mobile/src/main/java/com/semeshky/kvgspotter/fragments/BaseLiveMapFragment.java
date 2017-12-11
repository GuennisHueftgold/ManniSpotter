package com.semeshky.kvgspotter.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.semeshky.kvg.kvgapi.VehicleLocation;
import com.semeshky.kvg.kvgapi.VehicleLocations;
import com.semeshky.kvgspotter.activities.StationDetailActivity;
import com.semeshky.kvgspotter.activities.TripPassagesActivity;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.viewmodel.ActivityLiveMapViewModel;

import java.util.List;

abstract class BaseLiveMapFragment extends MapFragment {
    private final Observer<VehicleLocations> mVehicleLocationsObserver = new Observer<VehicleLocations>() {
        @Override
        public void onChanged(@Nullable VehicleLocations vehicleLocations) {
            BaseLiveMapFragment.this.updateVehicleLocations(vehicleLocations);
        }
    };
    protected ActivityLiveMapViewModel mViewModel;
    private Observer<List<Stop>> mStopsObserver = new Observer<List<Stop>>() {
        @Override
        public void onChanged(@Nullable List<Stop> stops) {
            BaseLiveMapFragment.this.updateStops(stops);
        }
    };

    protected abstract void updateVehicleLocations(VehicleLocations vehicleLocations);

    protected abstract void updateStops(List<Stop> stops);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = ViewModelProviders.of(getActivity()).get(ActivityLiveMapViewModel.class);
        this.mViewModel.getVehicleLocations()
                .observe(this,
                        this.mVehicleLocationsObserver);
        this.mViewModel.getStops()
                .observe(this,
                        this.mStopsObserver);
    }

    public void onVehicleLocationSelected(VehicleLocation vehicleLocation) {
        final Intent intent = TripPassagesActivity.createIntent(this.getContext(),
                vehicleLocation.getTripId(),
                null,
                null,
                null,
                "departure");
        startActivity(intent);
    }

    public void onStopSelected(Stop stop) {
        final Intent intent = StationDetailActivity.createIntent(this.getContext(), stop);
        startActivity(intent);
    }
}

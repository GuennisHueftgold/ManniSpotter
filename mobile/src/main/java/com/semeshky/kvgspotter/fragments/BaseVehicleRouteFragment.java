package com.semeshky.kvgspotter.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.guennishueftgold.trapezeapi.VehicleLocation;
import com.github.guennishueftgold.trapezeapi.VehiclePathInfo;
import com.semeshky.kvgspotter.viewmodel.TripPassagesViewModel;

abstract class BaseVehicleRouteFragment extends MapFragment {

    protected TripPassagesViewModel mViewModel;
    private Observer<VehiclePathInfo> mPathInfoObserver = new Observer<VehiclePathInfo>() {
        @Override
        public void onChanged(@Nullable VehiclePathInfo vehiclePathInfo) {
            BaseVehicleRouteFragment.this.updatePolylist(vehiclePathInfo);
        }
    };
    private Observer<VehicleLocation> mVehicleLocationObserver = new Observer<VehicleLocation>() {
        @Override
        public void onChanged(@Nullable VehicleLocation vehicleLocation) {
            updateVehicleMarker(vehicleLocation);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = ViewModelProviders.of(getActivity()).get(TripPassagesViewModel.class);
        this.mViewModel.getVehiclePathInfo()
                .observe(this, this.mPathInfoObserver);
        this.mViewModel.getVehicleLocation()
                .observe(this, this.mVehicleLocationObserver);
    }

    protected abstract void updateVehicleMarker(VehicleLocation vehicleLocation);

    protected abstract void updatePolylist(VehiclePathInfo body);
}

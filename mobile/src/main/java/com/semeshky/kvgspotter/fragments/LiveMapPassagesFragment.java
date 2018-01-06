package com.semeshky.kvgspotter.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.guennishueftgold.trapezeapi.TripPassageStop;
import com.github.guennishueftgold.trapezeapi.TripPassages;
import com.github.guennishueftgold.trapezeapi.VehicleLocation;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.activities.StationDetailActivity;
import com.semeshky.kvgspotter.activities.TripPassagesActivity;
import com.semeshky.kvgspotter.adapter.TripPassagesAdapter;
import com.semeshky.kvgspotter.databinding.FragmentLiveMapDeparturesBinding;
import com.semeshky.kvgspotter.viewmodel.ActivityLiveMapViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;
import timber.log.Timber;

public class LiveMapPassagesFragment extends Fragment {

    private ActivityLiveMapViewModel mViewModel;
    private final Toolbar.OnMenuItemClickListener mToolbarMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_details:
                    final VehicleLocation vehicleLocation = LiveMapPassagesFragment.this
                            .mViewModel
                            .getSelectedVehicle()
                            .getValue();
                    final Intent intent = TripPassagesActivity
                            .createIntent(LiveMapPassagesFragment.this.getContext(), vehicleLocation);
                    LiveMapPassagesFragment
                            .this
                            .startActivity(intent);
                    return true;
                default:
                    return false;
            }
        }
    };
    private FragmentLiveMapDeparturesBinding mBinding;
    private TripPassagesAdapter mAdapter;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = ViewModelProviders.of(getActivity())
                .get(ActivityLiveMapViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_map_departures, container, false);
        return this.mBinding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mAdapter = new TripPassagesAdapter(new TripPassagesAdapter.OnStationClickListener() {
            @Override
            public void onStationSelected(View titleView, TripPassageStop station) {
                startActivity(StationDetailActivity.createIntent(getContext(), station));
            }
        });
        this.mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.mBinding.recyclerView.setAdapter(this.mAdapter);
        this.mToolbar = this.mBinding.toolbar.toolbar;
        this.mToolbar.inflateMenu(R.menu.live_map_departures);
        this.mToolbar.setOnMenuItemClickListener(this.mToolbarMenuItemClickListener);
        this.mViewModel.getSelectedVehicle().observe(this,
                new Observer<VehicleLocation>() {
                    @Override
                    public void onChanged(@Nullable VehicleLocation vehicleLocation) {
                        if (vehicleLocation == null)
                            return;
                        getVehicleInformation(vehicleLocation);
                        mToolbar.setTitle(vehicleLocation.getName());
                        mBinding.executePendingBindings();
                    }
                });
    }

    private void getVehicleInformation(VehicleLocation vehicleLocation) {
        this.mViewModel.loadTripPassages(vehicleLocation.getTripId())
                .subscribe(new DisposableSingleObserver<TripPassages>() {
                    @Override
                    public void onSuccess(TripPassages tripPassages) {
                        List<TripPassageStop> stops = new ArrayList<>();
                        stops.addAll(tripPassages.getActual());
                        stops.addAll(tripPassages.getOld());
                        Collections.sort(stops, new Comparator<TripPassageStop>() {
                            @Override
                            public int compare(TripPassageStop tripPassageStop, TripPassageStop t1) {
                                return tripPassageStop.getStopSeqNum() - t1.getStopSeqNum();
                            }
                        });
                        mAdapter.setItems(stops);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }
}

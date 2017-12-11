package com.semeshky.kvgspotter.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.semeshky.kvg.kvgapi.Station;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.adapter.RoutesAdapter;
import com.semeshky.kvgspotter.database.Stop;
import com.semeshky.kvgspotter.database.StopPoint;
import com.semeshky.kvgspotter.databinding.FragmentStationDetailsBinding;
import com.semeshky.kvgspotter.map.GMapsUtil;
import com.semeshky.kvgspotter.viewmodel.StationDetailActivityViewModel;

import java.util.List;

abstract class BaseStationDetailsFragment extends MapFragment {
    protected StationDetailActivityViewModel mViewModel;
    protected FragmentStationDetailsBinding mBinding;
    protected RoutesAdapter mRoutesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = ViewModelProviders.of(getActivity()).get(StationDetailActivityViewModel.class);
        this.mRoutesAdapter = new RoutesAdapter();
        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_station_details, container, false);
        return this.mBinding.getRoot();
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.mBinding.recyclerView.setAdapter(this.mRoutesAdapter);
        this.mViewModel
                .getStation()
                .observe(this, new Observer<Station>() {
                    @Override
                    public void onChanged(@Nullable Station station) {
                        BaseStationDetailsFragment
                                .this
                                .updateStation(station);
                    }
                });
        this.mViewModel
                .getStop()
                .observe(this, new Observer<Stop>() {
                    @Override
                    public void onChanged(@Nullable Stop stop) {
                        BaseStationDetailsFragment.this
                                .updateMap(stop);
                    }
                });
        this.mViewModel
                .getStopPoints()
                .observe(this, new Observer<List<StopPoint>>() {
                    @Override
                    public void onChanged(@Nullable List<StopPoint> stopPoints) {
                        BaseStationDetailsFragment.this
                                .updateMap(stopPoints);
                    }
                });
    }

    private void updateStation(Station station) {
        this.mRoutesAdapter.setStation(station);
        this.mBinding.setStation(station);
        this.mBinding.executePendingBindings();
    }

    private void updateMap(final Stop stop) {
        this.updateMap(stop, this.mViewModel.getStopPoints().getValue());
    }

    private void updateMap(final List<StopPoint> stopPoints) {
        this.updateMap(this.mViewModel.getStop().getValue(), stopPoints);
    }

    protected abstract void updateMap(final Stop stop, final List<StopPoint> stopPoints);


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.stop_map_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_navigate_to:
                GMapsUtil.openNavigationTo(this.getActivity(), this.mViewModel.getStop().getValue());
                return true;

            default:
                return false;
        }
    }
}

package com.semeshky.kvgspotter.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semeshky.kvg.kvgapi.Departure;
import com.semeshky.kvg.kvgapi.Station;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.activities.TripPassagesActivity;
import com.semeshky.kvgspotter.adapter.DepartureAdapter;
import com.semeshky.kvgspotter.databinding.FragmentStationDeparturesBinding;
import com.semeshky.kvgspotter.viewmodel.StationDetailActivityViewModel;

public class StationDeparturesFragment extends Fragment {

    private StationDetailActivityViewModel mViewModel;
    private FragmentStationDeparturesBinding mBinding;
    private DepartureAdapter mDepartureAdapter;
    private SwipeRefreshLayout.OnRefreshListener mSwipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = ViewModelProviders.of(getActivity())
                .get(StationDetailActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_station_departures, container, false);
        return this.mBinding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mDepartureAdapter = new DepartureAdapter(new DepartureAdapter.Presenter() {
            @Override
            public void onOpenClick(Departure departure) {
                startActivity(TripPassagesActivity.createIntent(view.getContext(), departure));
            }
        });
        this.mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.mBinding.recyclerView.setAdapter(this.mDepartureAdapter);
        this.mBinding.swipeRefreshLayout.setOnRefreshListener(this.mSwipeRefreshListener);
        this.mViewModel.getStation().observe(this,
                new Observer<Station>() {
                    @Override
                    public void onChanged(@Nullable Station station) {
                        if (station == null)
                            return;
                        StationDeparturesFragment
                                .this
                                .mDepartureAdapter
                                .setItems(station.getActual());
                    }
                });
    }
}

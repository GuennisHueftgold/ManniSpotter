package com.semeshky.kvgspotter.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.guennishueftgold.trapezeapi.Departure;
import com.github.guennishueftgold.trapezeapi.Station;
import com.semeshky.kvgspotter.activities.TripPassagesActivity;
import com.semeshky.kvgspotter.adapter.DepartureAdapter;
import com.semeshky.kvgspotter.databinding.FragmentStationDeparturesBinding;
import com.semeshky.kvgspotter.viewmodel.StationDetailActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public final class StationDeparturesFragment extends Fragment {

    protected StationDetailActivityViewModel mViewModel;
    protected FragmentStationDeparturesBinding mBinding;
    protected DepartureAdapter mDepartureAdapter;
    protected SwipeRefreshLayout.OnRefreshListener mSwipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            StationDeparturesFragment
                    .this
                    .mViewModel
                    .refresh();
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
        this.mBinding = FragmentStationDeparturesBinding
                .inflate(inflater,
                        container,
                        false);
        this.mBinding.setViewModel(this.mViewModel);
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
        this.mViewModel
                .getStation()
                .observe(this,
                        new Observer<Station>() {
                            @Override
                            public void onChanged(@Nullable Station station) {
                                StationDeparturesFragment
                                        .this
                                        .updateViews(station);
                            }
                        });
    }

    protected void updateViews(Station station) {
        if (station == null)
            return;
        List<Departure> departureList = new ArrayList<>();
        departureList.addAll(station.getActual());
        departureList.addAll(station.getOld());
        this.mDepartureAdapter
                .setItems(departureList);
        this.mBinding
                .setDepartureCount(departureList.size());
        this.mBinding
                .executePendingBindings();
    }
}

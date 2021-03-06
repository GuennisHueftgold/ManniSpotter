package com.semeshky.kvgspotter.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.guennishueftgold.trapezeapi.TripPassageStop;
import com.github.guennishueftgold.trapezeapi.TripPassages;
import com.semeshky.kvgspotter.activities.StationDetailActivity;
import com.semeshky.kvgspotter.adapter.TripPassagesAdapter;
import com.semeshky.kvgspotter.databinding.FragmentTripPassagesBinding;
import com.semeshky.kvgspotter.viewmodel.TripPassagesViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TripPassagesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private TripPassagesAdapter mDepartureAdapter;
    private TripPassagesViewModel mViewModel;
    private FragmentTripPassagesBinding mBinding;
    private Observer<TripPassages> mTripPassagesObserver = new Observer<TripPassages>() {
        @Override
        public void onChanged(@Nullable TripPassages tripPassages) {
            List<TripPassageStop> mStops = new ArrayList<>();
            mStops.addAll(tripPassages.getActual());
            mStops.addAll(tripPassages.getOld());
            Collections.sort(mStops, new Comparator<TripPassageStop>() {
                @Override
                public int compare(TripPassageStop tripPassageStop, TripPassageStop t1) {
                    return tripPassageStop.getStopSeqNum() - t1.getStopSeqNum();
                }
            });
            TripPassagesFragment.this.mDepartureAdapter.setItems(mStops);
        }
    };
    private Observer<Integer> mRefreshObserver = new Observer<Integer>() {
        @Override
        public void onChanged(@Nullable Integer status) {
            if (status == null)
                return;
            TripPassagesFragment
                    .this
                    .mBinding
                    .swipeRefreshLayout
                    .setRefreshing(status == TripPassagesViewModel.STATUS_REFRESHING);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mViewModel = ViewModelProviders.of(getActivity()).get(TripPassagesViewModel.class);
        this.mViewModel.getTripPassages()
                .observe(this, this.mTripPassagesObserver);
        this.mViewModel
                .getTripPassagesRefreshStatus()
                .observe(this, this.mRefreshObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mBinding = FragmentTripPassagesBinding.inflate(inflater, container, false);
        return this.mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mDepartureAdapter = new TripPassagesAdapter(new TripPassagesAdapter.OnStationClickListener() {
            @Override
            public void onStationSelected(View titleView, TripPassageStop station) {
                final Activity activity = TripPassagesFragment.this.getActivity();
                final ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                                titleView,
                                StationDetailActivity.SCENE_TRANSITION_TITLE);
                final Intent intent = StationDetailActivity.createIntent(activity,
                        station);
                startActivity(intent, options.toBundle());
            }
        });
        this.mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        this.mBinding.recyclerView.setAdapter(this.mDepartureAdapter);
        this.mBinding.swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onRefresh() {
        this.mViewModel.refreshData();
    }
}

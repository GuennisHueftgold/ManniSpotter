package com.semeshky.kvgspotter.activities;


import android.location.Location;

import com.semeshky.kvgspotter.adapter.HomeAdapter;
import com.semeshky.kvgspotter.viewmodel.MainActivityViewModel;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

@Implements(MainActivityViewModel.class)
public class ShadowMainActivityViewModel {

    private PublishProcessor<List<HomeAdapter.DistanceStop>> mNearbyFlowable = PublishProcessor.create();
    private PublishProcessor<List<HomeAdapter.DistanceStop>> mFavoriteFlowable = PublishProcessor.create();


    public void publishNearby(List<HomeAdapter.DistanceStop> list) {
        this.mNearbyFlowable.onNext(list);
    }

    public void publishFavorite(List<HomeAdapter.DistanceStop> list) {
        this.mFavoriteFlowable.onNext(list);
    }


    @Implementation
    public Flowable<List<HomeAdapter.DistanceStop>> createNearbyFlowable(Flowable<Location> locationFlowable) {
        return this.mNearbyFlowable;
    }


    @Implementation
    public Flowable<List<HomeAdapter.DistanceStop>> getFavoriteFlowable(Flowable<Location> locationFlowable) {
        return this.mFavoriteFlowable;
    }
}

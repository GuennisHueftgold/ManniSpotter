package com.semeshky.kvgspotter.activities;

import com.github.guennishueftgold.trapezeapi.Station;
import com.semeshky.kvgspotter.viewmodel.StationDetailActivityViewModel;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import io.reactivex.Flowable;
import io.reactivex.processors.PublishProcessor;

@Implements(StationDetailActivityViewModel.class)
public class ShadowStationDetailActivityViewModel {

    private PublishProcessor<Station> mFlowStationProcessor = PublishProcessor.create();
    private int mStopSyncServiceCallCount = 0;
    private int mStartSyncServiceCallCount = 0;

    public static ShadowStationDetailActivityViewModel shadowOf(StationDetailActivityViewModel viewModel) {
        return Shadow.extract(viewModel);
    }

    public int getStopSyncServiceCallCount() {
        return mStopSyncServiceCallCount;
    }

    public int getStartSyncServiceCallCount() {
        return mStartSyncServiceCallCount;
    }

    public void reset() {
        this.mStartSyncServiceCallCount = 0;
        this.mStopSyncServiceCallCount = 0;
    }

    public void publishFlowStation(Station station) {
        this.mFlowStationProcessor.onNext(station);
    }

    @Implementation
    public Flowable<Station> getFlowSation() {
        return this.mFlowStationProcessor;
    }

    @Implementation
    public void startSyncService() {
        this.mStartSyncServiceCallCount++;
    }

    @Implementation
    public void stopSyncService() {
        this.mStopSyncServiceCallCount++;
    }
}

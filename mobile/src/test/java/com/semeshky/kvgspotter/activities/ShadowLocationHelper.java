package com.semeshky.kvgspotter.activities;

import android.location.Location;

import com.semeshky.kvgspotter.location.LocationHelper;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.processors.PublishProcessor;

@Implements(LocationHelper.class)
public class ShadowLocationHelper {
    private PublishProcessor<Location> mLocationPublishProcessor = PublishProcessor.create();

    @Implementation
    public Single<Location> getLastLocationSingle() {
        return this.mLocationPublishProcessor.singleOrError();
    }


    @Implementation
    public Flowable<Location> getLocationFlowable() {
        return this.mLocationPublishProcessor;
    }
}

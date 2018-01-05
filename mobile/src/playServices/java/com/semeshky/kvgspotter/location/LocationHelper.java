package com.semeshky.kvgspotter.location;


import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class LocationHelper extends AbstractLocationHelper {
    private final FusedLocationProviderClient mFusedLocationProvider;
    private final Object mLock = new Object();
    private Flowable<Location> mLocationFlowable;

    public LocationHelper(@NonNull Context context) {
        super(context);
        this.mFusedLocationProvider = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public Single<Location> getLastLocationSingle() {
        return Single.create(new LocationSingleOnSubscribe(this.mFusedLocationProvider))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread());
    }


    @Override
    public Flowable<Location> getLocationFlowable() {
        synchronized (this.mLock) {
            if (this.mLocationFlowable == null) {
                LocationFlowableOnSubscribe locationFlowableOnSubscribe = new LocationFlowableOnSubscribe(this.mFusedLocationProvider);
                this.mLocationFlowable = Flowable.create(locationFlowableOnSubscribe, BackpressureStrategy.LATEST)
                        .share()
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.newThread());
            }
        }
        return this.mLocationFlowable;
    }
}

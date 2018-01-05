package com.semeshky.kvgspotter.location;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class LocationHelper extends AbstractLocationHelper {
    private final Object mLock = new Object();
    private final LocationManager mLocationManager;
    private Flowable<Location> mLocationFlowable;

    public LocationHelper(@NonNull Context context) {
        super(context);
        this.mLocationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public Single<Location> getLastLocationSingle() {
        return Single.create(new LocationSingleOnSubscribe(this.mLocationManager))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread());
    }


    @Override
    public Flowable<Location> getLocationFlowable() {
        synchronized (this.mLock) {
            if (this.mLocationFlowable == null) {
                LocationFlowableOnSubscribe locationFlowableOnSubscribe = new LocationFlowableOnSubscribe(this.mLocationManager);
                this.mLocationFlowable = Flowable.create(locationFlowableOnSubscribe, BackpressureStrategy.LATEST)
                        .share()
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.newThread());
            }
        }
        return this.mLocationFlowable;
    }
}

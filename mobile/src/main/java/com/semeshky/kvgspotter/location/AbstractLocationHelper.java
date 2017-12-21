package com.semeshky.kvgspotter.location;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import io.reactivex.Flowable;
import io.reactivex.Single;

abstract class AbstractLocationHelper {

    public AbstractLocationHelper(@NonNull Context context) {

    }

    public abstract Single<Location> getLastLocationSingle();

    public abstract Flowable<Location> getLocationFlowable();
}

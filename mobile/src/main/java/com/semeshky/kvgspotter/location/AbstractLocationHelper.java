package com.semeshky.kvgspotter.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import io.reactivex.Flowable;
import io.reactivex.Single;

abstract class AbstractLocationHelper {

    public AbstractLocationHelper(@NonNull Context context) {

    }

    /**
     * Checks if the location permissions are granted
     *
     * @param context
     * @return
     */
    public static boolean hasLocationPermission(@NonNull Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public abstract Single<Location> getLastLocationSingle();

    public abstract Flowable<Location> getLocationFlowable();
}

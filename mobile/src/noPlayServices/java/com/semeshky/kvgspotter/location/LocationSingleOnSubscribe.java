package com.semeshky.kvgspotter.location;


import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

class LocationSingleOnSubscribe implements SingleOnSubscribe<Location> {

    private final LocationManager mClient;

    LocationSingleOnSubscribe(@NonNull LocationManager client) {
        this.mClient = client;
    }

    @Override
    public void subscribe(final SingleEmitter<Location> emitter) throws Exception {
        try {
            Location location = this.mClient
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null)
                location = this.mClient
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location == null)
                throw new Exception("No location could be acquired!");
            emitter.onSuccess(location);
        } catch (SecurityException exception) {
            emitter.onError(exception);
        }
    }
}

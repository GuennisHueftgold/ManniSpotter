package com.semeshky.kvgspotter.location;


import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

class LocationSingleOnSubscribe implements SingleOnSubscribe<Location> {

    private final FusedLocationProviderClient mClient;

    public LocationSingleOnSubscribe(@NonNull FusedLocationProviderClient client) {
        this.mClient = client;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void subscribe(final SingleEmitter<Location> emitter) throws Exception {
        if (LocationHelper.hasLocationPermission(this.mClient.getApplicationContext())) {
            emitter.onError(new Exception("Location Permission is required"));
            return;
        }
        this.mClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        emitter.onSuccess(location);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        emitter.onError(e);
                    }
                });
    }
}

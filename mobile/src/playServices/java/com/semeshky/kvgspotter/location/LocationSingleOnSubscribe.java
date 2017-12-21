package com.semeshky.kvgspotter.location;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

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

    @Override
    public void subscribe(final SingleEmitter<Location> emitter) throws Exception {
        if (ActivityCompat.checkSelfPermission(this.mClient.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.mClient.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

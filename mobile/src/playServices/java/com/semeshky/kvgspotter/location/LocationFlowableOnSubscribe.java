package com.semeshky.kvgspotter.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import io.reactivex.Emitter;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;

class LocationFlowableOnSubscribe extends LocationCallback implements FlowableOnSubscribe<Location> {

    private final FusedLocationProviderClient mFusedLocationProvider;

    LocationFlowableOnSubscribe(@NonNull FusedLocationProviderClient fusedLocationProvider) {
        this.mFusedLocationProvider = fusedLocationProvider;
    }

    @Override
    public void subscribe(FlowableEmitter<Location> emitter) throws Exception {
        if (ActivityCompat.checkSelfPermission(this.mFusedLocationProvider.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.mFusedLocationProvider.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            emitter.onError(new SecurityException("No location permission requested"));
            emitter.onComplete();
            return;
        }
        final InternalLocationCallback callback = new InternalLocationCallback(emitter);
        emitter.setCancellable(new Cancellable() {
            @Override
            public void cancel() throws Exception {
                LocationFlowableOnSubscribe
                        .this
                        .mFusedLocationProvider
                        .removeLocationUpdates(callback);
            }
        });

        final LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        this.mFusedLocationProvider
                .requestLocationUpdates(locationRequest,
                        callback,
                        null);
    }

    private final class InternalLocationCallback extends LocationCallback {
        private final Emitter<Location> mEmitter;

        public InternalLocationCallback(Emitter<Location> emitter) {
            this.mEmitter = emitter;
        }

        @Override
        public void onLocationResult(LocationResult var1) {
            this.mEmitter.onNext(var1.getLastLocation());
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            if (locationAvailability.isLocationAvailable())
                return;
            this.mEmitter.onError(new Exception("No location availeable"));
        }
    }
}

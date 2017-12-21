package com.semeshky.kvgspotter.location;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;

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

    @SuppressLint("MissingPermission")
    @Override
    public void subscribe(FlowableEmitter<Location> emitter) throws Exception {
        if (!LocationHelper.hasLocationPermission(this.mFusedLocationProvider.getApplicationContext())) {
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

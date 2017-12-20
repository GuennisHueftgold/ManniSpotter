package com.semeshky.kvgspotter.rx;

import android.location.Location;

import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;

public class LocationFlowableOnSubscribe extends LocationCallback implements FlowableOnSubscribe<Location> {

    private List<FlowableEmitter<Location>> mEmitterList = new ArrayList<>();
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult var1) {
            for (FlowableEmitter<Location> emitter : mEmitterList) {
                emitter.onNext(var1.getLastLocation());
            }
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            if (locationAvailability.isLocationAvailable())
                return;
            for (FlowableEmitter<Location> emitter : mEmitterList) {
                emitter.onError(new Exception("No location availeable"));
            }
        }
    };
    private final OnSuccessListener<? super Location> mOnLocationSuccessListener = new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            for (FlowableEmitter<Location> emitter : mEmitterList) {
                emitter.onNext(location);
            }
        }
    };

    public void finish() {
        for (FlowableEmitter<Location> emitter : this.mEmitterList) {
            emitter.onComplete();
        }
        this.mEmitterList.clear();
    }

    public LocationCallback getLocationCallback() {
        return this.mLocationCallback;
    }

    @Override
    public void subscribe(FlowableEmitter<Location> emitter) throws Exception {
        emitter.setCancellable(new Cancellable() {
            @Override
            public void cancel() throws Exception {

            }
        });
        this.mEmitterList.add(emitter);
    }

    public OnSuccessListener<? super Location> getOnSuccessListener() {
        return this.mOnLocationSuccessListener;
    }
}

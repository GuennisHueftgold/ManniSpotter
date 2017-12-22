package com.semeshky.kvgspotter.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import io.reactivex.Emitter;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;

class LocationFlowableOnSubscribe implements FlowableOnSubscribe<Location> {

    private final LocationManager mLocationManager;

    LocationFlowableOnSubscribe(@NonNull LocationManager locationManager) {
        this.mLocationManager = locationManager;
    }

    @Override
    public void subscribe(FlowableEmitter<Location> emitter) throws Exception {
        try {
            final InternalLocationCallback callback = new InternalLocationCallback(emitter);
            this.mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, callback);
            emitter.setCancellable(new Cancellable() {
                @Override
                public void cancel() throws Exception {
                    LocationFlowableOnSubscribe
                            .this
                            .mLocationManager
                            .removeUpdates(callback);
                }
            });
        } catch (SecurityException e) {
            emitter.onError(e);
        }
    }

    private final class InternalLocationCallback implements LocationListener {
        private final Emitter<Location> mEmitter;

        public InternalLocationCallback(Emitter<Location> emitter) {
            this.mEmitter = emitter;
        }

        @Override
        public void onLocationChanged(Location location) {
            this.mEmitter.onNext(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}

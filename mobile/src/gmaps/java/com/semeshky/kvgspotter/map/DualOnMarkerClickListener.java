package com.semeshky.kvgspotter.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


public class DualOnMarkerClickListener implements GoogleMap.OnMarkerClickListener {

    private final GoogleMap.OnMarkerClickListener mMarkerClickListener1, mMarkerClickListener2;

    public DualOnMarkerClickListener(GoogleMap.OnMarkerClickListener listener1,
                                     GoogleMap.OnMarkerClickListener listener2) {
        this.mMarkerClickListener1 = listener1;
        this.mMarkerClickListener2 = listener2;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (this.mMarkerClickListener1.onMarkerClick(marker)) {
            return true;
        }
        return this.mMarkerClickListener2.onMarkerClick(marker);
    }
}

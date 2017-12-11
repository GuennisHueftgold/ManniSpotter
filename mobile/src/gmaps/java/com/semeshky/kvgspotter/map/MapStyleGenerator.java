package com.semeshky.kvgspotter.map;

import android.content.res.Resources;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.util.BitmapUtil;

public final class MapStyleGenerator {
    public static MarkerOptions vehicleMarker(Resources resources) {
        final MarkerOptions markerOptions=new MarkerOptions();
        MapStyleGenerator
                .vehicleMarker(resources,markerOptions);
        return markerOptions;
    }
    public static void vehicleMarker(Resources resources, MarkerOptions markerOptions) {
        markerOptions
                .anchor(0.5f, 0.5f)
                .infoWindowAnchor(0.5f,0.5f)
                .zIndex(2)
                .flat(true)
                //.icon(BitmapDescriptorFactory.fromBitmap(bm));
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapUtil.drawableToBitmap(resources.getDrawable(R.drawable.ic_label_black_24dp))));
    }

    public static MarkerOptions stationMarker(Resources resources) {
        return new MarkerOptions()
                .flat(true)
                .anchor(0.5f, 0.5f)
                .infoWindowAnchor(0.5f,0.5f)
                .zIndex(1)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapUtil
                                .drawableToBitmap(resources.getDrawable(R.drawable.ic_station_icon))));
    }
}

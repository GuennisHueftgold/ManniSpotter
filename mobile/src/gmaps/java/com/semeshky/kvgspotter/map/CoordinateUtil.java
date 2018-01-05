package com.semeshky.kvgspotter.map;

import com.google.android.gms.maps.model.LatLng;
import com.github.guennishueftgold.trapezeapi.TrapezeApiClient;
import com.github.guennishueftgold.trapezeapi.LatLngInterface;

public final class CoordinateUtil {

    public static LatLng convert(LatLngInterface latLngInterface) {
        return CoordinateUtil.convert(latLngInterface.getLatitude(),
                latLngInterface.getLongitude());
    }

    public static LatLng convert(long latitude, long longitude) {
        return new LatLng(latitude / TrapezeApiClient.COORDINATES_CONVERTION_CONSTANT,
                longitude / TrapezeApiClient.COORDINATES_CONVERTION_CONSTANT);
    }
}

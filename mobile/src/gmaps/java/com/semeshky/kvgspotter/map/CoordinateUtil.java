package com.semeshky.kvgspotter.map;

import com.google.android.gms.maps.model.LatLng;
import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvg.kvgapi.LatLngInterface;

public final class CoordinateUtil {

    public static LatLng convert(LatLngInterface latLngInterface) {
        return new LatLng(latLngInterface.getLatitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT,
                latLngInterface.getLongitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT);
    }
}

package com.semeshky.kvgspotter.map;

import com.google.android.gms.maps.model.LatLng;
import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvg.kvgapi.LatLngInterface;

public final class CoordinateUtil {

    public static LatLng convert(LatLngInterface latLngInterface) {
        return CoordinateUtil.convert(latLngInterface.getLatitude(),
                latLngInterface.getLongitude());
    }

    public static LatLng convert(long latitude, long longitude) {
        return new LatLng(latitude / KvgApiClient.COORDINATES_CONVERTION_CONSTANT,
                longitude / KvgApiClient.COORDINATES_CONVERTION_CONSTANT);
    }
}

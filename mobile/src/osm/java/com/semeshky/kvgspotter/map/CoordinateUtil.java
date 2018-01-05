package com.semeshky.kvgspotter.map;

import com.github.guennishueftgold.trapezeapi.LatLngInterface;
import com.github.guennishueftgold.trapezeapi.TrapezeApiClient;

import org.osmdroid.util.GeoPoint;

public final class CoordinateUtil {

    public static GeoPoint convert(LatLngInterface latLngInterface) {
        return new GeoPoint(latLngInterface.getLatitude() / TrapezeApiClient.COORDINATES_CONVERTION_CONSTANT,
                latLngInterface.getLongitude() / TrapezeApiClient.COORDINATES_CONVERTION_CONSTANT);
    }
}

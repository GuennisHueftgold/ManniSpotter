package com.semeshky.kvgspotter.map;

import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvg.kvgapi.LatLngInterface;

import org.osmdroid.util.GeoPoint;

public final class CoordinateUtil {

    public static GeoPoint convert(LatLngInterface latLngInterface) {
        return new GeoPoint(latLngInterface.getLatitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT,
                latLngInterface.getLongitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT);
    }
}

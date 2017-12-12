package com.semeshky.kvg.kvgapi;

import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalTime;

import java.util.List;

@SuppressWarnings("unchecked")
final class GeneralTypes {

    public static final TypeToken<Route> ROUTE_TYPE_TOKEN =
            (TypeToken<Route>) TypeToken.get(Route.class);
    static final TypeToken<List<Route>> ROUTE_LIST_TYPE_TOKEN =
            (TypeToken<List<Route>>) TypeToken.getParameterized(List.class, Route.class);
    static final TypeToken<List<String>> STRING_LIST_TYPE_TOKEN =
            (TypeToken<List<String>>) TypeToken.getParameterized(List.class, String.class);
    static final TypeToken<List<Departure>> DEPARTURE_LIST_TYPE_TOKEN =
            (TypeToken<List<Departure>>) TypeToken.getParameterized(List.class, Departure.class);
    static final TypeToken<List<ShortStationInfo>> SHORT_STATION_INFO_LIST_TYPE_TOKEN =
            (TypeToken<List<ShortStationInfo>>) TypeToken.getParameterized(List.class, ShortStationInfo.class);
    static final TypeToken<LocalTime> LOCAL_TIME_TYPE_TOKEN =
            (TypeToken<LocalTime>) TypeToken.get(LocalTime.class);
    static final TypeToken<List<VehiclePathPoint>> VEHICLE_PATH_POINT_LIST_TYPE_TOKEN =
            (TypeToken<List<VehiclePathPoint>>) TypeToken.getParameterized(List.class, VehiclePathPoint.class);
    static final TypeToken<List<VehiclePath>> VEHICLE_PATH_LIST_TYPE_TOKEN =
            (TypeToken<List<VehiclePath>>) TypeToken.getParameterized(List.class, VehiclePath.class);
}

package com.semeshky.kvg.kvgapi;

import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalTime;

import java.util.List;

@SuppressWarnings("unchecked")
final class GeneralTypes {

    public static final TypeToken<List<Route>> ROUTE_LIST_TYPE_TOKEN =
            (TypeToken<List<Route>>) TypeToken.getParameterized(List.class, Route.class);
    public static final TypeToken<List<String>> STRING_LIST_TYPE_TOKEN =
            (TypeToken<List<String>>) TypeToken.getParameterized(List.class, String.class);
    public static final TypeToken<List<Departure>> DEPARTURE_LIST_TYPE_TOKEN =
            (TypeToken<List<Departure>>) TypeToken.getParameterized(List.class, Departure.class);
    public static final TypeToken<Route> ROUTE_TYPE_TOKEN =
            (TypeToken<Route>) TypeToken.get(Route.class);
    public static final TypeToken<List<ShortStationInfo>> SHORT_STATION_INFO_LIST_TYPE_TOKEN =
            (TypeToken<List<ShortStationInfo>>) TypeToken.getParameterized(List.class, ShortStationInfo.class);
    public static final TypeToken<LocalTime> LOCAL_TIME_TYPE_TOKEN =
            (TypeToken<LocalTime>) TypeToken.get(LocalTime.class);
}

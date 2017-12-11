package com.semeshky.kvg.kvgapi;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalTime;

class KvgApiTypeAdapterFactory implements TypeAdapterFactory {


    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() == Route.class) {
            return (TypeAdapter<T>) new Route.Converter(gson);
        } else if (type.getRawType() == Departure.class) {
            return (TypeAdapter<T>) new Departure.Converter(gson);
        } else if (type.getRawType() == Station.class) {
            return (TypeAdapter<T>) new Station.Converter(gson);
        } else if (type.getRawType() == TripPassages.class) {
            return (TypeAdapter<T>) new TripPassages.Converter(gson);
        } else if (type.getRawType() == LocalTime.class) {
            return (TypeAdapter<T>) new LocalTimeTypeAdapter();
        } else if (type.getRawType() == TripPassageStop.class) {
            return (TypeAdapter<T>) new TripPassageStop.Converter();
        } else if (type.getRawType() == ShortStationInfo.class) {
            return (TypeAdapter<T>) new ShortStationInfo.Converter();
        } else if (type.getRawType() == StopsByCharacterResult.class) {
            return (TypeAdapter<T>) new StopsByCharacterResult.Converter(gson);
        } else {
            return null;
        }
    }
}

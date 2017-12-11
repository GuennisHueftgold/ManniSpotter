package com.semeshky.kvg.kvgapi;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

public final class Station {
    private final String mStopName;
    private final String mStopShortName;
    private final List<String> mDirections;
    private final long mFirstPassageTime;
    private final List<String> mGeneralAlerts;
    private final long mLastPassageTime;
    private final List<Departure> mActual;
    private final List<Departure> mOld;
    private final List<Route> mRoutes;

    public Station(Builder builder) {
        this.mStopName = builder.mStopName;
        this.mStopShortName = builder.mStopShortName;
        this.mDirections = builder.mDirections;
        this.mFirstPassageTime = builder.mFirstPassageTime;
        this.mGeneralAlerts = builder.mGeneralAlerts;
        this.mLastPassageTime = builder.mLastPassageTime;
        this.mActual = builder.mActual;
        this.mOld = builder.mOld;
        this.mRoutes = builder.mRoutes;

    }

    public List<Route> getRoutes() {
        return mRoutes;
    }

    public String getStopName() {
        return mStopName;
    }

    public String getStopShortName() {
        return mStopShortName;
    }

    public List<String> getDirections() {
        return mDirections;
    }

    public long getFirstPassageTime() {
        return mFirstPassageTime;
    }

    public List<String> getGeneralAlerts() {
        return mGeneralAlerts;
    }

    public long getLastPassageTime() {
        return mLastPassageTime;
    }

    public List<Departure> getActual() {
        return mActual;
    }

    public List<Departure> getOld() {
        return mOld;
    }

    @Override
    public String toString() {
        return "Station{" +
                "stopName='" + mStopName + '\'' +
                ", stopShortName='" + mStopShortName + '\'' +
                ", directions=" + mDirections +
                ", firstPassageTime=" + mFirstPassageTime +
                ", generalAlerts=" + mGeneralAlerts +
                ", lastPassageTime=" + mLastPassageTime +
                ", actual=" + mActual +
                ", old=" + mOld +
                ", routes=" + mRoutes +
                '}';
    }

    public final static class Builder {

        private String mStopName;
        private String mStopShortName;
        private List<String> mDirections;
        private long mFirstPassageTime;
        private List<String> mGeneralAlerts;
        private long mLastPassageTime;
        private List<Departure> mActual;
        private List<Departure> mOld;
        private List<Route> mRoutes;

        public String getStopName() {
            return mStopName;
        }

        public void setStopName(String stopName) {
            mStopName = stopName;
        }

        public List<Route> getRoutes() {
            return mRoutes;
        }

        public void setRoutes(List<Route> routes) {
            mRoutes = routes;
        }

        public String getStopShortName() {
            return mStopShortName;
        }

        public void setStopShortName(String stopShortName) {
            mStopShortName = stopShortName;
        }

        public List<String> getDirections() {
            return mDirections;
        }

        public void setDirections(List<String> directions) {
            this.mDirections = directions;
        }

        public long getFirstPassageTime() {
            return mFirstPassageTime;
        }

        public void setFirstPassageTime(long firstPassageTime) {
            mFirstPassageTime = firstPassageTime;
        }

        public List<String> getGeneralAlerts() {
            return mGeneralAlerts;
        }

        public void setGeneralAlerts(List<String> generalAlerts) {
            mGeneralAlerts = generalAlerts;
        }

        public long getLastPassageTime() {
            return mLastPassageTime;
        }

        public void setLastPassageTime(long lastPassageTime) {
            mLastPassageTime = lastPassageTime;
        }

        public List<Departure> getActual() {
            return mActual;
        }

        public void setActual(List<Departure> actual) {
            mActual = actual;
        }

        public List<Departure> getOld() {
            return mOld;
        }

        public void setOld(List<Departure> old) {
            mOld = old;
        }

        public Station build() {
            return new Station(this);
        }
    }

    public final static class Converter extends TypeAdapter<Station> {

        private final static String NAME_ACTUAL = "actual";
        private final static String NAME_OLD = "old";
        private final static String NAME_DIRECTIONS = "directions";
        private final static String NAME_FIRST_PASSAGE_TIME = "firstPassageTime";
        private final static String NAME_GENERAL_ALERTS = "generalAlerts";
        private final static String NAME_LAST_PASSAGE_TIME = "lastPassageTime";
        private final static String NAME_ROUTES = "routes";
        private final static String NAME_STOP_NAME = "stopName";
        private final static String NAME_STOP_SHORT_NAME = "stopShortName";
        private final TypeAdapter<List<Departure>> mDepartueListTypeAdapter;
        private final TypeAdapter<List<String>> mStringListTypeAdapter;
        private final TypeAdapter<List<Route>> mRouteListTypeAdapter;

        public Converter(@NonNull Gson gson) {
            this.mDepartueListTypeAdapter = gson.getAdapter(GeneralTypes.DEPARTURE_LIST_TYPE_TOKEN);
            this.mStringListTypeAdapter = gson.getAdapter(GeneralTypes.STRING_LIST_TYPE_TOKEN);
            this.mRouteListTypeAdapter = gson.getAdapter(GeneralTypes.ROUTE_LIST_TYPE_TOKEN);
        }

        @Override
        public void write(JsonWriter out, Station value) throws IOException {

        }

        @Override
        public Station read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            }
            in.beginObject();
            Builder builder = new Builder();
            String name;
            while (in.hasNext()) {
                name = in.nextName();
                if (name.equals(NAME_ACTUAL) && in.peek() == JsonToken.BEGIN_ARRAY) {
                    builder.setActual(this.mDepartueListTypeAdapter.read(in));
                } else if (name.equals(NAME_OLD) && in.peek() == JsonToken.BEGIN_ARRAY) {
                    builder.setOld(this.mDepartueListTypeAdapter.read(in));
                } else if (name.equals(NAME_DIRECTIONS) && in.peek() == JsonToken.BEGIN_ARRAY) {
                    builder.setDirections(this.mStringListTypeAdapter.read(in));
                } else if (name.equals(NAME_FIRST_PASSAGE_TIME) && in.peek() == JsonToken.NUMBER) {
                    builder.setFirstPassageTime(in.nextLong());
                } else if (name.equals(NAME_LAST_PASSAGE_TIME) && in.peek() == JsonToken.NUMBER) {
                    builder.setLastPassageTime(in.nextLong());
                } else if (name.equals(NAME_GENERAL_ALERTS) && in.peek() == JsonToken.BEGIN_ARRAY) {
                    builder.setGeneralAlerts(this.mStringListTypeAdapter.read(in));
                } else if (name.equals(NAME_ROUTES) && in.peek() == JsonToken.BEGIN_ARRAY) {
                    builder.setRoutes(this.mRouteListTypeAdapter.read(in));
                } else if (name.equals(NAME_STOP_SHORT_NAME) && in.peek() == JsonToken.STRING) {
                    builder.setStopShortName(in.nextString());
                } else if (name.equals(NAME_STOP_NAME) && in.peek() == JsonToken.STRING) {
                    builder.setStopName(in.nextString());
                } else {
                    Timber.d("Not handled Name: " + name);
                    in.skipValue();
                }
            }
            in.endObject();
            return builder.build();
        }
    }
}

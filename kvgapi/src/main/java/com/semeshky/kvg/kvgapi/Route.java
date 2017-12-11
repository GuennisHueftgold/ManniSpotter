package com.semeshky.kvg.kvgapi;

import android.support.annotation.IntDef;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.util.List;

import timber.log.Timber;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Route {
    public final static int ROUTE_TYPE_BUS = 1;

    /*"alerts": [],
        "authority": "KVG",
        "directions": [
        "Pillauer Straße",
        "Wik, Kanal"
        ],
        "id": "60835712076873989",
        "name": "11",
        "routeType": "bus",
        "shortName": "11"*/
    public final static int ROUTE_TYPE_UNKNOWN = 0;
    private final List<String> mAlerts;
    private final String mAuthority;
    private final List<String> mDirections;
    private final String mId;
    private final String mName;
    @RouteType
    private final int mRouteType;
    private final String mShortName;

    public Route(Builder builder) {
        this.mAlerts = builder.mAlerts;
        this.mAuthority = builder.mAuthority;
        this.mDirections = builder.mDirections;
        this.mId = builder.mId;
        this.mName = builder.mName;
        this.mRouteType = builder.mRouteType;
        this.mShortName = builder.mShortName;
    }

    public static int getRouteTypeBus() {
        return ROUTE_TYPE_BUS;
    }

    public static int getRouteTypeUnknown() {
        return ROUTE_TYPE_UNKNOWN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (mRouteType != route.mRouteType) return false;
        if (mAlerts != null ? !mAlerts.equals(route.mAlerts) : route.mAlerts != null) return false;
        if (mAuthority != null ? !mAuthority.equals(route.mAuthority) : route.mAuthority != null)
            return false;
        if (mDirections != null ? !mDirections.equals(route.mDirections) : route.mDirections != null)
            return false;
        if (mId != null ? !mId.equals(route.mId) : route.mId != null) return false;
        if (mName != null ? !mName.equals(route.mName) : route.mName != null) return false;
        return mShortName != null ? mShortName.equals(route.mShortName) : route.mShortName == null;
    }

    @Override
    public int hashCode() {
        int result = mAlerts != null ? mAlerts.hashCode() : 0;
        result = 31 * result + (mAuthority != null ? mAuthority.hashCode() : 0);
        result = 31 * result + (mDirections != null ? mDirections.hashCode() : 0);
        result = 31 * result + (mId != null ? mId.hashCode() : 0);
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + mRouteType;
        result = 31 * result + (mShortName != null ? mShortName.hashCode() : 0);
        return result;
    }

    public List<String> getAlerts() {
        return mAlerts;
    }

    public String getAuthority() {
        return mAuthority;
    }

    public List<String> getDirections() {
        return mDirections;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getRouteType() {
        return mRouteType;
    }

    public String getShortName() {
        return mShortName;
    }

    @Retention(SOURCE)
    @IntDef({ROUTE_TYPE_BUS, ROUTE_TYPE_UNKNOWN})
    public @interface RouteType {
    }

    public final static class Builder {

        private List<String> mAlerts;
        private String mAuthority;
        private List<String> mDirections;
        private String mId;
        private String mName;

        @RouteType
        private int mRouteType;
        private String mShortName;

        public List<String> getAlerts() {
            return mAlerts;
        }

        public void setAlerts(List<String> alerts) {
            mAlerts = alerts;
        }

        public String getAuthority() {
            return mAuthority;
        }

        public void setAuthority(String authority) {
            mAuthority = authority;
        }

        public List<String> getDirections() {
            return mDirections;
        }

        public void setDirections(List<String> directions) {
            mDirections = directions;
        }

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        @RouteType
        public int getRouteType() {
            return mRouteType;
        }

        public void setRouteType(
                @RouteType int routeType) {
            mRouteType = routeType;
        }

        public String getShortName() {
            return mShortName;
        }

        public void setShortName(String shortName) {
            mShortName = shortName;
        }

        public Route build() {
            return new Route(this);
        }
    }

    public static final class Converter extends TypeAdapter<Route> {
        private final static String NAME_ALERTS = "alerts",
                NAME_AUTHORITY = "authority",
                NAME_DIRECTIONS = "directions",
                NAME_ID = "id",
                NAME_NAME = "name",
                NAME_ROUTE_TYPE = "routeType",
                NAME_SHORT_NAME = "shortName";
        private final TypeToken<List<String>> STRING_LIST_TYPE_TOKEN = new TypeToken<List<String>>() {
        };
        private final TypeAdapter<List<String>> mStringListTypeAdapter;

        public Converter(Gson gson) {
            this.mStringListTypeAdapter = gson.getAdapter(STRING_LIST_TYPE_TOKEN);
        }

        @Override
        public void write(JsonWriter out, Route value) throws IOException {

        }

        @Override
        public Route read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.skipValue();
                return null;
            }
            Route.Builder builder = new Builder();
            in.beginObject();
            String name;
            while (in.hasNext()) {
                name = in.nextName();
                if (name.equals(NAME_ALERTS) && in.peek() == JsonToken.BEGIN_ARRAY) {
                    builder.setAlerts(this.mStringListTypeAdapter.read(in));
                } else if (name.equals(NAME_AUTHORITY) && in.peek() == JsonToken.STRING) {
                    builder.setAuthority(in.nextString());
                } else if (name.equals(NAME_DIRECTIONS) && in.peek() == JsonToken.BEGIN_ARRAY) {
                    builder.setDirections(this.mStringListTypeAdapter.read(in));
                } else if (name.equals(NAME_ID) && in.peek() == JsonToken.STRING) {
                    builder.setId(in.nextString());
                } else if (name.equals(NAME_NAME) && in.peek() == JsonToken.STRING) {
                    builder.setName(in.nextString());
                } else if (name.equals(NAME_ROUTE_TYPE) && in.peek() == JsonToken.STRING) {
                    final String routeType = in.nextString().toLowerCase();
                    switch (routeType) {
                        case "bus":
                            builder.setRouteType(ROUTE_TYPE_BUS);
                            break;
                        default:
                            in.skipValue();
                            Timber.d("Unknown Route Type: " + routeType);
                            break;
                    }
                } else if (name.equals(NAME_SHORT_NAME) && in.peek() == JsonToken.STRING) {
                    builder.setShortName(in.nextString());
                } else {
                    in.skipValue();
                    Timber.e("Didnt parse name: " + name);
                }
            }
            in.endObject();
            return builder.build();
        }

    }
}

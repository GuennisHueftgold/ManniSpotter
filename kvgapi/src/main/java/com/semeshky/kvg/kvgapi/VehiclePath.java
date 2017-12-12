package com.semeshky.kvg.kvgapi;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

public final class VehiclePath {
    private final String mColor;
    private final List<VehiclePathPoint> mPathPoints;

    private VehiclePath(Builder builder) {
        this.mColor = builder.mColor;
        this.mPathPoints = builder.mPathPoints;
    }

    public String getColor() {
        return mColor;
    }

    public List<VehiclePathPoint> getPathPoints() {
        return mPathPoints;
    }

    public static final class Builder {
        private String mColor;
        private List<VehiclePathPoint> mPathPoints;

        public String getColor() {
            return mColor;
        }

        public void setColor(String color) {
            mColor = color;
        }

        public List<VehiclePathPoint> getPathPoints() {
            return mPathPoints;
        }

        public void setPathPoints(List<VehiclePathPoint> pathPoints) {
            mPathPoints = pathPoints;
        }

        public VehiclePath build() {
            return new VehiclePath(this);
        }
    }

    static final class Converter extends TypeAdapter<VehiclePath> {
        private final static String NAME_COLOR = "color", NAME_WAYPOINTS = "wayPoints";
        private final TypeAdapter<List<VehiclePathPoint>> mTypeAdapter;

        Converter(Gson gson) {
            this.mTypeAdapter = gson.getAdapter(GeneralTypes.VEHICLE_PATH_POINT_LIST_TYPE_TOKEN);
        }

        @Override
        public void write(JsonWriter out, VehiclePath value) throws IOException {
            out.nullValue();
            //TODO:
        }

        @Override
        public VehiclePath read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.skipValue();
                return null;
            }
            in.beginObject();
            String name;
            Builder builder = new Builder();
            while (in.hasNext()) {
                name = in.nextName();
                switch (name) {
                    case NAME_COLOR:
                        builder.setColor(in.nextString());
                        break;
                    case NAME_WAYPOINTS:
                        builder.setPathPoints(this.mTypeAdapter.read(in));
                        break;
                    default:
                        Timber.d("Unkown name: %s", name);
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
            return builder.build();
        }
    }
}

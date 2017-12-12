package com.semeshky.kvg.kvgapi;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

public final class VehiclePathInfo {
    private final List<VehiclePath> mVehiclePaths;

    private VehiclePathInfo(Builder builder) {
        this.mVehiclePaths = builder.mVehiclePaths;
    }

    public List<VehiclePath> getVehiclePaths() {
        return mVehiclePaths;
    }

    public static final class Builder {

        private List<VehiclePath> mVehiclePaths;

        public List<VehiclePath> getVehiclePaths() {
            return mVehiclePaths;
        }

        public void setVehiclePaths(List<VehiclePath> vehiclePaths) {
            mVehiclePaths = vehiclePaths;
        }

        public VehiclePathInfo build() {
            return new VehiclePathInfo(this);
        }
    }

    static final class Converter extends TypeAdapter<VehiclePathInfo> {
        private final static String NAME_PATHS = "paths";
        private final TypeAdapter<List<VehiclePath>> mTypeAdapter;

        public Converter(Gson gson) {
            this.mTypeAdapter = gson.getAdapter(GeneralTypes.VEHICLE_PATH_LIST_TYPE_TOKEN);
        }

        @Override
        public void write(JsonWriter out, VehiclePathInfo value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            out.name(NAME_PATHS);
            this.mTypeAdapter.write(out, value.getVehiclePaths());
            out.endObject();
        }

        @Override
        public VehiclePathInfo read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.skipValue();
                return null;
            }
            Builder builder = new Builder();
            in.beginObject();
            String name;
            while (in.hasNext()) {
                name = in.nextName();
                switch (name) {
                    case NAME_PATHS:
                        builder.setVehiclePaths(this.mTypeAdapter.read(in));
                        break;
                    default:
                        Timber.d("Unknown name: %s", name);
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
            return builder.build();
        }
    }
}

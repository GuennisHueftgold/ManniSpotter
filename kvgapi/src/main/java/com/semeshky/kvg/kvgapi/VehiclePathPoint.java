package com.semeshky.kvg.kvgapi;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import timber.log.Timber;


public final class VehiclePathPoint implements LatLngInterface {
    private final long mLatitude;
    private final long mLongitude;
    private final int mSequence;

    private VehiclePathPoint(Builder builder) {
        this.mLatitude = builder.mLatitude;
        this.mLongitude = builder.mLongitude;
        this.mSequence = builder.mSequence;
    }

    public long getLatitude() {
        return mLatitude;
    }

    public long getLongitude() {
        return mLongitude;
    }

    public int getSequence() {
        return mSequence;
    }

    public static final class Builder {
        private long mLatitude;
        private long mLongitude;
        private int mSequence;

        public long getLatitude() {
            return mLatitude;
        }

        public void setLatitude(long latitude) {
            mLatitude = latitude;
        }

        public long getLongitude() {
            return mLongitude;
        }

        public void setLongitude(long longitude) {
            mLongitude = longitude;
        }

        public int getSequence() {
            return mSequence;
        }

        public void setSequence(int sequence) {
            mSequence = sequence;
        }

        public VehiclePathPoint build() {
            return new VehiclePathPoint(this);
        }
    }

    final static class Converter extends TypeAdapter<VehiclePathPoint> {
        private final static String NAME_LAT = "lat", NAME_LON = "lon", NAME_SEQ = "seq";

        @Override
        public void write(JsonWriter out, VehiclePathPoint value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            out.name(NAME_LAT)
                    .value(value.getLatitude());
            out.name(NAME_LON)
                    .value(value.getLongitude());
            out.name(NAME_SEQ)
                    .value(value.getSequence());
            out.endObject();
        }

        @Override
        public VehiclePathPoint read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.skipValue();
                return null;
            }
            Builder builder = new Builder();
            in.beginObject();
            String name;
            while (in.hasNext()) {
                name = in.nextName().toLowerCase();
                switch (name) {
                    case NAME_LAT:
                        builder.setLatitude(in.nextLong());
                        break;
                    case NAME_LON:
                        builder.setLongitude(in.nextLong());
                        break;
                    case NAME_SEQ:
                        builder.setSequence(Integer.parseInt(in.nextString()));
                        break;
                    default:
                        Timber.d("Unknown tag %s", name);
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
            return builder.build();
        }
    }
}

package com.semeshky.kvg.kvgapi;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import timber.log.Timber;

public class PathSegment {
    private final double mLength;
    private final long mFromLongitude;
    private final long mToLongitude;
    private final long mFromLatitude;
    private final long mToLatitude;
    private final int mAngle;

    public PathSegment(Builder builder) {
        this.mFromLatitude = builder.mFromLatitude;
        this.mToLatitude = builder.mToLatitude;
        this.mToLongitude = builder.mToLongitude;
        this.mAngle = builder.mAngle;
        this.mFromLongitude = builder.mFromLongitude;
        this.mLength = builder.mLength;
    }

    public double getLength() {
        return mLength;
    }

    public long getFromLongitude() {
        return mFromLongitude;
    }

    public long getToLongitude() {
        return mToLongitude;
    }

    public long getFromLatitude() {
        return mFromLatitude;
    }

    public long getToLatitude() {
        return mToLatitude;
    }

    public int getAngle() {
        return mAngle;
    }

    public static final class Builder {
        private double mLength;
        private long mFromLongitude;
        private long mToLongitude;
        private long mFromLatitude;
        private long mToLatitude;
        private int mAngle;

        public double getLength() {
            return mLength;
        }

        public void setLength(double length) {
            mLength = length;
        }

        public long getFromLongitude() {
            return mFromLongitude;
        }

        public void setFromLongitude(long fromLongitude) {
            mFromLongitude = fromLongitude;
        }

        public long getToLongitude() {
            return mToLongitude;
        }

        public void setToLongitude(long toLongitude) {
            mToLongitude = toLongitude;
        }

        public long getFromLatitude() {
            return mFromLatitude;
        }

        public void setFromLatitude(long fromLatitude) {
            mFromLatitude = fromLatitude;
        }

        public long getToLatitude() {
            return mToLatitude;
        }

        public void setToLatitude(long toLatitude) {
            mToLatitude = toLatitude;
        }

        public int getAngle() {
            return mAngle;
        }

        public void setAngle(int angle) {
            mAngle = angle;
        }

        public PathSegment build() {
            return new PathSegment(this);
        }
    }

    static final class Converter extends TypeAdapter<PathSegment> {
        private final static String NAME_ANGLE = "angle",
                NAME_X1 = "x1",
                NAME_X2 = "x2",
                NAME_Y1 = "y1",
                NAME_Y2 = "y2",
                NAME_LENGTH = "length";

        @Override
        public void write(JsonWriter out, PathSegment value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            out.name(NAME_ANGLE)
                    .value(value.getAngle());
            out.name(NAME_LENGTH)
                    .value(value.getLength());
            out.name(NAME_X1)
                    .value(value.getFromLongitude());
            out.name(NAME_X2)
                    .value(value.getToLongitude());
            out.name(NAME_Y1)
                    .value(value.getFromLatitude());
            out.name(NAME_Y2)
                    .value(value.getToLatitude());
            out.endObject();
        }

        @Override
        public PathSegment read(JsonReader in) throws IOException {
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
                    case NAME_ANGLE:
                        builder.setAngle(in.nextInt());
                        break;
                    case NAME_LENGTH:
                        builder.setLength(in.nextDouble());
                        break;
                    case NAME_X1:
                        builder.setFromLongitude(in.nextLong());
                        break;
                    case NAME_X2:
                        builder.setToLongitude(in.nextLong());
                        break;
                    case NAME_Y1:
                        builder.setFromLatitude(in.nextLong());
                        break;
                    case NAME_Y2:
                        builder.setToLatitude(in.nextLong());
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

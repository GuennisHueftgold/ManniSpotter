package com.semeshky.kvg.kvgapi;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import timber.log.Timber;

public final class ShortStationInfo {
    private final String mId;
    private final String mStopName;
    private final String mStopShortName;

    private ShortStationInfo(Builder builder) {
        this.mId = builder.mId;
        this.mStopName = builder.mStopName;
        this.mStopShortName = builder.mStopShortName;
    }

    public String getId() {
        return mId;
    }

    public String getStopName() {
        return mStopName;
    }

    public String getStopShortName() {
        return mStopShortName;
    }

    public static class Builder {
        private String mId;
        private String mStopName;
        private String mStopShortName;

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getStopName() {
            return mStopName;
        }

        public void setStopName(String stopName) {
            mStopName = stopName;
        }

        public String getStopShortName() {
            return mStopShortName;
        }

        public void setStopShortName(String stopShortName) {
            mStopShortName = stopShortName;
        }

        public ShortStationInfo build() {
            return new ShortStationInfo(this);
        }
    }

    public static class Converter extends TypeAdapter<ShortStationInfo> {
        private final static String NAME_ID = "id", NAME_NAME = "name", NAME_NUMBER = "number";

        @Override
        public void write(JsonWriter out, ShortStationInfo value) throws IOException {
            //TODO:
            throw new JsonSyntaxException("Not yet implemented");
        }

        @Override
        public ShortStationInfo read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.skipValue();
                return null;
            }
            in.beginObject();
            String name;
            Builder builder = new Builder();
            while (in.hasNext()) {
                name = in.nextName();
                if (NAME_NAME.equalsIgnoreCase(name) && in.peek() == JsonToken.STRING) {
                    builder.setStopName(in.nextString());
                } else if (NAME_ID.equalsIgnoreCase(name) && in.peek() == JsonToken.STRING) {
                    builder.setId(in.nextString());
                } else if (NAME_NUMBER.equalsIgnoreCase(name) && in.peek() == JsonToken.STRING) {
                    builder.setStopShortName(in.nextString());
                } else {
                    Timber.d("Unknown Name: %s", name);
                    in.skipValue();
                }
            }
            in.endObject();
            return builder.build();
        }
    }
}

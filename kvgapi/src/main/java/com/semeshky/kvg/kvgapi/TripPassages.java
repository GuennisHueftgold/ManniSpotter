package com.semeshky.kvg.kvgapi;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class TripPassages {

    private final List<TripPassageStop> mActual;
    private final String mDirectionText;
    private final List<TripPassageStop> mOld;
    private final String mRouteName;

    private TripPassages(TripPassages.Builder builder) {
        this.mActual = builder.mActual;
        this.mDirectionText = builder.mDirectionText;
        this.mOld = builder.mOld;
        this.mRouteName = builder.mRouteName;
    }

    public List<TripPassageStop> getActual() {
        return mActual;
    }

    public String getDirectionText() {
        return mDirectionText;
    }

    public List<TripPassageStop> getOld() {
        return mOld;
    }

    public String getRouteName() {
        return mRouteName;
    }

    @Override
    public String toString() {
        return "TripPassages{" +
                "mActual=" + mActual +
                ", mDirectionText='" + mDirectionText + '\'' +
                ", mOld=" + mOld +
                ", mRouteName='" + mRouteName + '\'' +
                '}';
    }

    public static class Builder {
        private List<TripPassageStop> mActual;
        private String mDirectionText;
        private List<TripPassageStop> mOld;
        private String mRouteName;

        public List<TripPassageStop> getActual() {
            return mActual;
        }

        public void setActual(List<TripPassageStop> actual) {
            mActual = actual;
        }

        public void addActual(TripPassageStop actual) {
            if (this.mActual == null) {
                this.mActual = new ArrayList<>();
            }
            this.mActual.add(actual);
        }

        public String getDirectionText() {
            return mDirectionText;
        }

        public void setDirectionText(String directionText) {
            mDirectionText = directionText;
        }

        public List<TripPassageStop> getOld() {
            return mOld;
        }

        public void setOld(List<TripPassageStop> old) {
            mOld = old;
        }

        public void addOld(TripPassageStop old) {
            if (this.mOld == null) {
                this.mOld = new ArrayList<>();
            }
            this.mOld.add(old);
        }

        public String getRouteName() {
            return mRouteName;
        }

        public void setRouteName(String routeName) {
            mRouteName = routeName;
        }

        public TripPassages build() {
            return new TripPassages(this);
        }
    }

    public final static class Converter extends TypeAdapter<TripPassages> {

        private final static String
                ROUTE_NAME = "routeName",
                DIRECTION_TEXT = "directionText",
                OLD = "old",
                ACTUAL = "actual";
        private final TypeAdapter<List<TripPassageStop>> mTripPassageStopConverter;

        private final TypeToken<List<TripPassageStop>> PASSAGE_STOP_LIST_TYPE_TOKEN = new TypeToken<List<TripPassageStop>>() {
        };

        public Converter(Gson gson) {
            this.mTripPassageStopConverter = gson.getAdapter(PASSAGE_STOP_LIST_TYPE_TOKEN);
        }

        @Override
        public void write(JsonWriter out, TripPassages value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            out.name(DIRECTION_TEXT).value(value.mDirectionText);
            out.name(ROUTE_NAME).value(value.mRouteName);
            out.name(OLD);
            this.mTripPassageStopConverter.write(out, value.mOld);
            out.name(ACTUAL);
            this.mTripPassageStopConverter.write(out, value.mActual);
            out.endObject();
        }

        @Override
        public TripPassages read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            } else if (in.peek() != JsonToken.BEGIN_OBJECT) {
                throw new JsonParseException("Expected begin object");
            }
            in.beginObject();
            TripPassages.Builder tripPassages = new TripPassages.Builder();
            String name;
            while (in.hasNext()) {
                name = in.nextName();
                if (name.equals(ROUTE_NAME) && in.peek() == JsonToken.STRING) {
                    tripPassages.setRouteName(in.nextString());
                } else if (name.equals(ACTUAL) && in.peek() == JsonToken.BEGIN_ARRAY) {
                    tripPassages.setActual(this.mTripPassageStopConverter.read(in));
                } else if (name.equals(OLD) && in.peek() == JsonToken.BEGIN_ARRAY) {
                    tripPassages.setOld(this.mTripPassageStopConverter.read(in));
                } else if (name.equals(DIRECTION_TEXT) && in.peek() == JsonToken.STRING) {
                    tripPassages.setDirectionText(in.nextString());
                } else {
                    Timber.d("Not handled Name: " + name);
                    in.skipValue();
                }
            }
            in.endObject();
            return tripPassages.build();
        }


    }
}

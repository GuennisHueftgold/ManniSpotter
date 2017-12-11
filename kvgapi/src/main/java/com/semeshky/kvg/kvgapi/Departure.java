package com.semeshky.kvg.kvgapi;


import android.support.annotation.IntDef;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.joda.time.LocalTime;

import java.io.IOException;
import java.lang.annotation.Retention;

import timber.log.Timber;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Departure {
    public final static int STATUS_DEPARTED = 1;
    public final static int STATUS_PREDICTED = 2;
    public final static int STATUS_UNKNOWN = 0;
    private final int mActualRelativeTime;
    private final String mDirection;
    private final String mMixedTime;
    private final String mPassageId;
    private final String mPatternText;
    private final LocalTime mPlannedTime;
    private final LocalTime mActualTime;
    private final String mRouteId;
    @Status
    private final int mStatus;
    private final String mTripId;
    private final String mVehicleId;

    public Departure(Builder builder) {
        this.mActualRelativeTime = builder.mActualRelativeTime;
        this.mDirection = builder.mDirection;
        this.mMixedTime = builder.mMixedTime;
        this.mPassageId = builder.mPassageId;
        this.mPatternText = builder.mPatternText;
        this.mPlannedTime = builder.mPlannedTime;
        this.mActualTime = builder.mActualTime;
        this.mRouteId = builder.mRouteId;
        this.mStatus = builder.mStatus;
        this.mTripId = builder.mTripId;
        this.mVehicleId = builder.mVehicleId;
    }

    public LocalTime getActualTime() {
        return mActualTime;
    }

    @Override
    public String toString() {
        return "Departure{" +
                "actualRelativeTime=" + mActualRelativeTime +
                ", direction='" + mDirection + '\'' +
                ", mixedTime='" + mMixedTime + '\'' +
                ", passageId='" + mPassageId + '\'' +
                ", patternText='" + mPatternText + '\'' +
                ", plannedTime=" + mPlannedTime +
                ", actualTime=" + mActualTime +
                ", routeId='" + mRouteId + '\'' +
                ", status=" + mStatus +
                ", tripId='" + mTripId + '\'' +
                ", vehicleId='" + mVehicleId + '\'' +
                '}';
    }

    public int getActualRelativeTime() {
        return mActualRelativeTime;
    }

    public String getDirection() {
        return mDirection;
    }

    public String getMixedTime() {
        return mMixedTime;
    }

    public String getPassageId() {
        return mPassageId;
    }

    public String getPatternText() {
        return mPatternText;
    }

    public LocalTime getPlannedTime() {
        return mPlannedTime;
    }

    public String getRouteId() {
        return mRouteId;
    }

    @Status
    public int getStatus() {
        return mStatus;
    }

    public String getTripId() {
        return mTripId;
    }

    public String getVehicleId() {
        return mVehicleId;
    }

    @Retention(SOURCE)
    @IntDef({STATUS_DEPARTED, STATUS_PREDICTED, STATUS_UNKNOWN})
    public @interface Status {
    }

    public final static class Builder {
        private int mActualRelativeTime;
        private String mDirection;
        private String mMixedTime;
        private String mPassageId;
        private String mPatternText;
        private LocalTime mPlannedTime;
        private String mRouteId;
        private LocalTime mActualTime;
        @Status
        private int mStatus = STATUS_UNKNOWN;
        private String mTripId;
        private String mVehicleId;

        public int getActualRelativeTime() {
            return mActualRelativeTime;
        }

        public void setActualRelativeTime(int actualRelativeTime) {
            mActualRelativeTime = actualRelativeTime;
        }

        public LocalTime getActualTime() {
            return mActualTime;
        }

        public void setActualTime(LocalTime actualTime) {
            mActualTime = actualTime;
        }

        public String getDirection() {
            return mDirection;
        }

        public void setDirection(String direction) {
            mDirection = direction;
        }

        public String getMixedTime() {
            return mMixedTime;
        }

        public void setMixedTime(String mixedTime) {
            mMixedTime = mixedTime;
        }

        public String getPassageId() {
            return mPassageId;
        }

        public void setPassageId(String passageId) {
            mPassageId = passageId;
        }

        public String getPatternText() {
            return mPatternText;
        }

        public void setPatternText(String patternText) {
            mPatternText = patternText;
        }

        public LocalTime getPlannedTime() {
            return mPlannedTime;
        }

        public void setPlannedTime(LocalTime plannedTime) {
            mPlannedTime = plannedTime;
        }

        public String getRouteId() {
            return mRouteId;
        }

        public void setRouteId(String routeId) {
            mRouteId = routeId;
        }

        @Status
        public int getStatus() {
            return mStatus;
        }

        public void setStatus(@Status int status) {
            mStatus = status;
        }

        public String getTripId() {
            return mTripId;
        }

        public void setTripId(String tripId) {
            mTripId = tripId;
        }

        public String getVehicleId() {
            return mVehicleId;
        }

        public void setVehicleId(String vehicleId) {
            mVehicleId = vehicleId;
        }

        public Departure build() {
            return new Departure(this);
        }
    }

    public final static class Converter extends TypeAdapter<Departure> {
        private final static String ACTUAL_RELATIVE_TIME = "actualRelativeTime";
        private final static String PLANNED_TIME = "plannedTime";
        private final static String DIRECTION = "direction";
        private final static String MIXED_TIME = "mixedTime";
        private final static String STATUS = "status";
        private final static String PASSAGE_ID = "passageid";
        private final static String PATTERN_TEXT = "patternText";
        private final static String ROUTE_ID = "routeId";
        private final static String TRIP_ID = "tripId";
        private final static String VEHICLE_ID = "vehicleId";
        private final static String ACTUAL_TIME = "actualTime";
        private final TypeAdapter<LocalTime> mLocalTimeTypeAdapter;

        public Converter(Gson gson) {
            this.mLocalTimeTypeAdapter = gson.getAdapter(GeneralTypes.LOCAL_TIME_TYPE_TOKEN);
        }

        @Override
        public void write(JsonWriter out, Departure value) throws IOException {

        }

        @Override
        public Departure read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.skipValue();
                return null;
            }
            Departure.Builder builder = new Builder();
            in.beginObject();
            String name;
            while (in.hasNext()) {
                name = in.nextName();
                if (name.equals(ACTUAL_RELATIVE_TIME) && in.peek() == JsonToken.NUMBER) {
                    builder.setActualRelativeTime(in.nextInt());
                } else if (name.equals(PLANNED_TIME) && in.peek() == JsonToken.STRING) {
                    builder.setPlannedTime(this.mLocalTimeTypeAdapter.read(in));
                } else if (name.equals(ACTUAL_TIME) && in.peek() == JsonToken.STRING) {
                    builder.setActualTime(this.mLocalTimeTypeAdapter.read(in));
                } else if (name.equals(MIXED_TIME) && in.peek() == JsonToken.STRING) {
                    builder.setMixedTime(in.nextString());
                } else if (name.equals(DIRECTION) && in.peek() == JsonToken.STRING) {
                    builder.setDirection(in.nextString());
                } else if (name.equals(PASSAGE_ID) && in.peek() == JsonToken.STRING) {
                    builder.setPassageId(in.nextString());
                } else if (name.equals(PATTERN_TEXT) && in.peek() == JsonToken.STRING) {
                    builder.setPatternText(in.nextString());
                } else if (name.equals(ROUTE_ID) && in.peek() == JsonToken.STRING) {
                    builder.setRouteId(in.nextString());
                } else if (name.equals(TRIP_ID) && in.peek() == JsonToken.STRING) {
                    builder.setTripId(in.nextString());
                } else if (name.equals(VEHICLE_ID) && in.peek() == JsonToken.STRING) {
                    builder.setVehicleId(in.nextString());
                } else if (name.equals(STATUS) && in.peek() == JsonToken.STRING) {
                    final String status = in.nextString();
                    switch (status.toLowerCase()) {
                        case "departed":
                            builder.setStatus(STATUS_DEPARTED);
                            break;
                        case "predicted":
                            builder.setStatus(STATUS_DEPARTED);
                            break;
                    }
                } else {
                    in.skipValue();
                    Timber.d("Skipped value for: " + name);
                }
            }
            in.endObject();
            return builder.build();
        }
    }
    /*{
        "actualRelativeTime": -298,
            "direction": "Wik Kanal",
            "mixedTime": "0 %UNIT_MIN%",
            "passageid": "-9187343239776208027",
            "patternText": "11",
            "plannedTime": "11:06",
            "routeId": "60835712076873989",
            "status": "DEPARTED",
            "tripId": "60839568961970697",
            "vehicleId": "60842017088864987"
    }*/
}

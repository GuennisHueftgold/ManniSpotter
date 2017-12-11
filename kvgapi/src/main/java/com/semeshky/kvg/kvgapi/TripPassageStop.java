package com.semeshky.kvg.kvgapi;


import android.support.annotation.IntDef;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.joda.time.LocalTime;

import java.io.IOException;
import java.lang.annotation.Retention;

import timber.log.Timber;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class TripPassageStop {

    public final static int STATUS_PREDICTED = 1,
            STATUS_DEPARTED = 2,
            STATUS_STOPPING = 3,
            STATUS_PLANNED = 4,
            STATUS_UNKNOWN = -1;
    private final LocalTime mPlannedTime;
    private final LocalTime mActualTime;
    @Status
    private final int mStatus;
    private final String mId;
    private final String mShortName;
    private final String mName;
    private final int mStopSeqNum;
    private TripPassageStop(Builder builder) {
        this.mActualTime = builder.mActualTime;
        this.mStopSeqNum = builder.mStopSeqNum;
        this.mStatus = builder.mStatus;
        this.mName = builder.mName;
        this.mShortName = builder.mShortName;
        this.mPlannedTime = builder.mPlannedTime;
        this.mId = builder.mId;
    }

    public LocalTime getPlannedTime() {
        return mPlannedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TripPassageStop that = (TripPassageStop) o;

        if (mStatus != that.mStatus) return false;
        if (mStopSeqNum != that.mStopSeqNum) return false;
        if (mPlannedTime != null ? !mPlannedTime.equals(that.mPlannedTime) : that.mPlannedTime != null)
            return false;
        if (mActualTime != null ? !mActualTime.equals(that.mActualTime) : that.mActualTime != null)
            return false;
        if (mId != null ? !mId.equals(that.mId) : that.mId != null) return false;
        if (mShortName != null ? !mShortName.equals(that.mShortName) : that.mShortName != null)
            return false;
        return mName != null ? mName.equals(that.mName) : that.mName == null;
    }

    @Override
    public int hashCode() {
        int result = mPlannedTime != null ? mPlannedTime.hashCode() : 0;
        result = 31 * result + (mActualTime != null ? mActualTime.hashCode() : 0);
        result = 31 * result + mStatus;
        result = 31 * result + (mId != null ? mId.hashCode() : 0);
        result = 31 * result + (mShortName != null ? mShortName.hashCode() : 0);
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + mStopSeqNum;
        return result;
    }

    public LocalTime getActualTime() {
        return mActualTime;
    }

    @Status
    public int getStatus() {
        return mStatus;
    }

    public String getId() {
        return mId;
    }

    public String getShortName() {
        return mShortName;
    }

    public String getName() {
        return mName;
    }

    public int getStopSeqNum() {
        return mStopSeqNum;
    }

    @Override
    public String toString() {
        return "TripPassageStop{" +
                "mPlannedTime=" + mPlannedTime +
                ", mActualTime=" + mActualTime +
                ", mStatus=" + mStatus +
                ", mId='" + mId + '\'' +
                ", mShortName='" + mShortName + '\'' +
                ", mName='" + mName + '\'' +
                ", mStopSeqNum=" + mStopSeqNum +
                '}';
    }

    @Retention(SOURCE)
    @IntDef({STATUS_PREDICTED, STATUS_DEPARTED, STATUS_UNKNOWN, STATUS_STOPPING, STATUS_PLANNED})
    public @interface Status {
    }

    public static class Builder {
        private LocalTime mActualTime;
        private LocalTime mPlannedTime;
        private int mStatus;
        private String mId;
        private String mShortName;
        private String mName;
        private int mStopSeqNum;

        public Builder() {

        }

        public LocalTime getPlannedTime() {
            return mPlannedTime;
        }

        public void setPlannedTime(LocalTime plannedTime) {
            mPlannedTime = plannedTime;
        }

        public LocalTime getActualTime() {
            return mActualTime;
        }

        public void setActualTime(LocalTime actualTime) {
            mActualTime = actualTime;
        }

        @Status
        public int getStatus() {
            return mStatus;
        }

        public void setStatus(@Status int status) {
            mStatus = status;
        }

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getShortName() {
            return mShortName;
        }

        public void setShortName(String shortName) {
            mShortName = shortName;
        }

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public int getStopSeqNum() {
            return mStopSeqNum;
        }

        public void setStopSeqNum(int stopSeqNum) {
            mStopSeqNum = stopSeqNum;
        }

        public TripPassageStop build() {
            return new TripPassageStop(this);
        }
    }

    public final static class Converter extends TypeAdapter<TripPassageStop> {

        private final static String STOP_SEQ_NUM = "stop_seq_num",
                SHORT_NAME = "shortName",
                NAME = "name",
                ID = "id",
                STOP = "stop",
                STATUS = "status",
                ACTUAL_TIME = "actualTime",
                PLANNED_TIME = "plannedTime";

        @Override
        public void write(JsonWriter out, TripPassageStop value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            out.name(STOP_SEQ_NUM).value(Long.toString(value.getStopSeqNum()));
            out.name(ACTUAL_TIME).value(value.mActualTime.toString());
            out.name(PLANNED_TIME).value(value.mPlannedTime.toString());
            out.name(STATUS).value(value.mStatus);
            out.name(STOP).beginObject();
            out.name(ID).value(value.mId);
            out.name(NAME).value(value.mName);
            out.name(SHORT_NAME).value(value.mShortName);
            out.endObject();
            out.endObject();
        }

        @Override
        public TripPassageStop read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            } else if (in.peek() != JsonToken.BEGIN_OBJECT) {
                throw new JsonParseException("Expected begin object");
            }
            in.beginObject();
            TripPassageStop.Builder tripPassageStop = new TripPassageStop.Builder();
            String name;
            while (in.hasNext()) {
                name = in.nextName();
                if (name.equals(STOP_SEQ_NUM) && in.peek() == JsonToken.STRING) {
                    tripPassageStop.setStopSeqNum(Integer.parseInt(in.nextString()));
                } else if (name.equals(STOP_SEQ_NUM) && in.peek() == JsonToken.NUMBER) {
                    tripPassageStop.setStopSeqNum(in.nextInt());
                } else if (name.equals(ACTUAL_TIME) && in.peek() == JsonToken.STRING) {
                    tripPassageStop.setActualTime(LocalTime.parse(in.nextString()));
                } else if (name.equals(PLANNED_TIME) && in.peek() == JsonToken.STRING) {
                    tripPassageStop.setPlannedTime(LocalTime.parse(in.nextString()));
                } else if (name.equals(STATUS) && in.peek() == JsonToken.STRING) {
                    final String status = in.nextString().toLowerCase();
                    switch (status) {
                        case "departed":
                            tripPassageStop.setStatus(STATUS_DEPARTED);
                            break;
                        case "predicted":
                            tripPassageStop.setStatus(STATUS_PREDICTED);
                            break;
                        case "stopping":
                            tripPassageStop.setStatus(STATUS_STOPPING);
                            break;
                        case "planned":
                            tripPassageStop.setStatus(STATUS_PLANNED);
                            break;
                        default:
                            Timber.d("Parsed Unknown status: " + status);
                            tripPassageStop.setStatus(STATUS_UNKNOWN);
                            break;
                    }
                } else if (name.equals(STOP) && in.peek() == JsonToken.BEGIN_OBJECT) {
                    this.readStop(tripPassageStop, in);
                } else {
                    Timber.d("Not handled Name: " + name);
                    in.skipValue();
                }
            }
            in.endObject();
            return tripPassageStop.build();
        }

        private void readStop(TripPassageStop.Builder tripPassageStop, JsonReader in) throws IOException {
            in.beginObject();
            String name;
            while (in.hasNext()) {
                name = in.nextName();
                if (name.equals(ID) && in.peek() == JsonToken.STRING) {
                    tripPassageStop.setId(in.nextString());
                } else if (name.equals(SHORT_NAME) && in.peek() == JsonToken.STRING) {
                    tripPassageStop.setShortName(in.nextString());
                } else if (name.equals(NAME) && in.peek() == JsonToken.STRING) {
                    tripPassageStop.setName(in.nextString());
                } else {
                    in.skipValue();
                    Timber.d("Not handled Name :" + name);
                }
            }
            in.endObject();
        }

    }
}

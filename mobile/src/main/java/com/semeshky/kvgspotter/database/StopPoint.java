package com.semeshky.kvgspotter.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.github.guennishueftgold.trapezeapi.LatLngInterface;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "stopPoints",
        indices = {@Index(value = "id", unique = true)})
public class StopPoint implements LatLngInterface {
    @PrimaryKey(autoGenerate = true)
    private int mUid;
    @ColumnInfo(name = "category",
            typeAffinity = ColumnInfo.TEXT)
    private String mCategory;
    @ColumnInfo(name = "id",
            typeAffinity = ColumnInfo.TEXT)
    private String mId;
    @ColumnInfo(name = "latitude",
            typeAffinity = ColumnInfo.INTEGER)
    private long mLatitude;
    @ColumnInfo(name = "longitude",
            typeAffinity = ColumnInfo.INTEGER)
    private long mLongitude;
    @ColumnInfo(name = "name",
            typeAffinity = ColumnInfo.TEXT)
    private String mName;
    @ColumnInfo(name = "shortName",
            typeAffinity = ColumnInfo.TEXT)
    private String mShortName;
    @ColumnInfo(name = "label",
            typeAffinity = ColumnInfo.TEXT)
    private String mLabel;
    @ColumnInfo(name = "stopPoint",
            typeAffinity = ColumnInfo.TEXT)
    private String mStopPoint;

    public static StopPoint create(com.github.guennishueftgold.trapezeapi.StopPoint stopPoint) {
        final StopPoint stop = new StopPoint();
        stop.mId = stopPoint.getId();
        stop.mCategory = stopPoint.getCategory();
        stop.mLatitude = stopPoint.getLatitude();
        stop.mLongitude = stopPoint.getLongitude();
        stop.mName = stopPoint.getName();
        stop.mShortName = stopPoint.getShortName();
        stop.mLabel = stopPoint.getLabel();
        stop.mStopPoint = stopPoint.getStopPoint();
        return stop;
    }

    public static List<StopPoint> create(List<com.github.guennishueftgold.trapezeapi.StopPoint> stopPoints) {
        final List<StopPoint> stops = new ArrayList<>();
        for (com.github.guennishueftgold.trapezeapi.StopPoint stopPoint : stopPoints) {
            stops.add(StopPoint.create(stopPoint));
        }
        return stops;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getStopPoint() {
        return mStopPoint;
    }

    public void setStopPoint(String stopPoint) {
        mStopPoint = stopPoint;
    }

    public int getUid() {
        return mUid;
    }

    public void setUid(int uid) {
        mUid = uid;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getShortName() {
        return mShortName;
    }

    public void setShortName(String shortName) {
        mShortName = shortName;
    }
}

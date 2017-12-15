package com.semeshky.kvgspotter.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.semeshky.kvg.kvgapi.LatLngInterface;
import com.semeshky.kvg.kvgapi.StationLocation;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "stops",
        indices = {@Index(value = "id", unique = true),
                @Index(value = "shortName", unique = true)})
public class Stop implements LatLngInterface {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid",
            typeAffinity = ColumnInfo.INTEGER)
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

    public static Stop create(StationLocation stationLocation) {
        final Stop stop = new Stop();
        stop.mId = stationLocation.getId();
        stop.mCategory = stationLocation.getCategory();
        stop.mLatitude = stationLocation.getLatitude();
        stop.mLongitude = stationLocation.getLongitude();
        stop.mName = stationLocation.getName();
        stop.mShortName = stationLocation.getShortName();
        return stop;
    }

    public static List<Stop> create(List<StationLocation> stationLocations) {
        final List<Stop> stops = new ArrayList<>();
        for (StationLocation stationLocation : stationLocations) {
            stops.add(Stop.create(stationLocation));
        }
        return stops;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "mUid=" + mUid +
                ", mCategory='" + mCategory + '\'' +
                ", mId='" + mId + '\'' +
                ", mName='" + mName + '\'' +
                ", mShortName='" + mShortName + '\'' +
                '}';
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

package com.semeshky.kvgspotter.database;

import android.arch.persistence.room.ColumnInfo;

public class FavoriteStationWithName extends FavoriteStation {
    @ColumnInfo(name = "name",
            typeAffinity = ColumnInfo.TEXT)
    private String mName;
    @ColumnInfo(name = "longitude",
            typeAffinity = ColumnInfo.INTEGER)
    private long mLongitude;
    @ColumnInfo(name = "latitude",
            typeAffinity = ColumnInfo.INTEGER)
    private long mLatitude;

    public FavoriteStationWithName() {
    }

    public long getLongitude() {
        return mLongitude;
    }

    public void setLongitude(long longitude) {
        mLongitude = longitude;
    }

    public long getLatitude() {
        return mLatitude;
    }

    public void setLatitude(long latitude) {
        mLatitude = latitude;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}

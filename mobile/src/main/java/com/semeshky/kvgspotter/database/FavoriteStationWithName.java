package com.semeshky.kvgspotter.database;

import android.arch.persistence.room.ColumnInfo;

public class FavoriteStationWithName extends FavoriteStation {
    @ColumnInfo(name = "name",
            typeAffinity = ColumnInfo.TEXT)
    private String mName;

    public FavoriteStationWithName() {
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}

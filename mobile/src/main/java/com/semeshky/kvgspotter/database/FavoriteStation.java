package com.semeshky.kvgspotter.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import org.joda.time.DateTime;

@Entity(tableName = "favoriteStations",
        indices = {@Index(value = "shortName", unique = true)},
        foreignKeys = @ForeignKey(entity = Stop.class,
                parentColumns = "shortName",
                childColumns = "shortName"))
public class FavoriteStation {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id",
            typeAffinity = ColumnInfo.INTEGER)
    private long mId;
    @ColumnInfo(name = "shortName",
            typeAffinity = ColumnInfo.TEXT)
    private String mShortName;
    @TypeConverters({DateTimeConverter.class})
    @ColumnInfo(name = "created",
            typeAffinity = ColumnInfo.TEXT)
    private DateTime mCreated = DateTime.now();

    public FavoriteStation() {
    }

    public String getShortName() {
        return mShortName;
    }

    public void setShortName(String shortName) {
        mShortName = shortName;
    }


    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public DateTime getCreated() {
        return mCreated;
    }

    public void setCreated(DateTime created) {
        mCreated = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FavoriteStation that = (FavoriteStation) o;

        if (mId != that.mId) return false;
        if (mShortName != null ? !mShortName.equals(that.mShortName) : that.mShortName != null)
            return false;
        return mCreated != null ? mCreated.equals(that.mCreated) : that.mCreated == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + (mShortName != null ? mShortName.hashCode() : 0);
        result = 31 * result + (mCreated != null ? mCreated.hashCode() : 0);
        return result;
    }
}

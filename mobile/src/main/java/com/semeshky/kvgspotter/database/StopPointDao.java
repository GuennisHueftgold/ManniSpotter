package com.semeshky.kvgspotter.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface StopPointDao {
    String QUERY_GET_ALL = "SELECT * FROM stopPoints";

    @Query(QUERY_GET_ALL)
    List<StopPoint> getAll();

    @Query(QUERY_GET_ALL)
    LiveData<List<StopPoint>> getAllSync();

    @Query("SELECT * FROM stopPoints WHERE shortName == :shortName")
    StopPoint getFavoriteByShortName(final String shortName);

    @Query("SELECT * FROM stopPoints WHERE name LIKE '%' || :searchTerm || '%' LIMIT :searchLimit")
    List<StopPoint> searchStation(final String searchTerm, final int searchLimit);

    @Query("SELECT COUNT(*) from stopPoints")
    int countStops();

    @Query("SELECT COUNT(*) from stopPoints")
    LiveData<Integer> countStopsSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(StopPoint... stopPoints);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StopPoint> stopPoints);

    @Delete
    void delete(StopPoint user);

    @Query("SELECT * FROM stopPoints WHERE shortName == :shortName")
    LiveData<List<StopPoint>> getAllWithShortName(String shortName);
}
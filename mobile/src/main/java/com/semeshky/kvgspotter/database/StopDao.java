package com.semeshky.kvgspotter.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface StopDao {
    String QUERY_GET_ALL = "SELECT * FROM stops";

    @Query(QUERY_GET_ALL)
    List<Stop> getAll();

    @Query(QUERY_GET_ALL)
    LiveData<List<Stop>> getAllSync();

    @Query(QUERY_GET_ALL)
    Flowable<List<Stop>> getAllFlow();

    @Query("SELECT * FROM stops WHERE shortName == :shortName")
    Stop getStopByShortName(final String shortName);

    @Query("SELECT * FROM stops WHERE shortName == :shortName")
    LiveData<Stop> getStopByShortNameSync(final String shortName);

    @Query("SELECT * FROM stops WHERE name LIKE '%' || :searchTerm || '%' LIMIT :searchLimit")
    List<Stop> searchStation(final String searchTerm, final int searchLimit);

    @Query("SELECT COUNT(*) from stops")
    int countStops();

    @Query("SELECT COUNT(*) from stops")
    LiveData<Integer> countStopsSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Stop... users);

    @Delete
    void delete(Stop user);
}
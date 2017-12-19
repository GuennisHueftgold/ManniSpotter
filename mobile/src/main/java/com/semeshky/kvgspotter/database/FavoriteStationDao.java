package com.semeshky.kvgspotter.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoriteStationDao {
    String QUERY_GET_ALL = "SELECT * FROM favoriteStations";

    @Query(QUERY_GET_ALL)
    List<FavoriteStation> getAll();

    @Query(QUERY_GET_ALL)
    LiveData<List<FavoriteStation>> getAllSync();

    @Query("SELECT * FROM favoriteStations WHERE id == :id")
    FavoriteStation getFavoriteById(final int id);

    @Query("SELECT EXISTS(SELECT 1 FROM favoriteStations WHERE shortName == :id LIMIT 1)")
    boolean isFavoriteStation(final String id);

    @Query("SELECT EXISTS(SELECT 1 FROM favoriteStations WHERE shortName == :id LIMIT 1)")
    LiveData<Boolean> isFavoriteStationSync(final String id);

    @Insert
    long insert(FavoriteStation favoriteStation);

    @Insert
    long[] insertAll(FavoriteStation... favoriteStations);

    @Delete()
    int delete(FavoriteStation user);

    @Query("DELETE FROM favoriteStations WHERE shortName == :shortName")
    int delete(String shortName);

    @Query("SELECT favoriteStations.*,stops.name, stops.longitude, stops.latitude FROM favoriteStations LEFT JOIN stops ON favoriteStations.shortName = stops.shortName")
    LiveData<List<FavoriteStationWithName>> getAllWithNameSync();

    @Query("SELECT favoriteStations.*,stops.name, stops.longitude, stops.latitude FROM favoriteStations LEFT JOIN stops ON favoriteStations.shortName = stops.shortName")
    List<FavoriteStationWithName> getAllWithName();
}
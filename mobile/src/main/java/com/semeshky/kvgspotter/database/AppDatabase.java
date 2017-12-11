package com.semeshky.kvgspotter.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Database(entities = {FavoriteStation.class,
        Stop.class,
        StopPoint.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private final static String DATABASE_NAME = "kvgspotter.db";
    private static AppDatabase sAppDatabase;

    public static void init(@NonNull Context context) {
        if (sAppDatabase == null) {
            sAppDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME)
                    .build();
        }
    }

    public static AppDatabase getInstance() {
        return sAppDatabase;
    }

    public static Single<Long> insertFavoriteStation(final FavoriteStation favoriteStation) {
        return Single.create(new SingleOnSubscribe<Long>() {
            @Override
            public void subscribe(SingleEmitter<Long> emitter) throws Exception {
                try {
                    emitter
                            .onSuccess(
                                    sAppDatabase
                                            .favoriteSationDao()
                                            .insert(favoriteStation));
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Boolean> deleteFavoriteStation(final FavoriteStation favoriteStation) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
                try {
                    emitter
                            .onSuccess(
                                    sAppDatabase
                                            .favoriteSationDao()
                                            .delete(favoriteStation) > 0);
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public abstract FavoriteStationDao favoriteSationDao();

    public abstract StopDao stopDao();

    public abstract StopPointDao stopPointDao();
}

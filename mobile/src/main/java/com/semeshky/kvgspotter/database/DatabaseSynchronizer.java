package com.semeshky.kvgspotter.database;

import android.content.Context;

import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvg.kvgapi.StationLocation;
import com.semeshky.kvg.kvgapi.StationLocations;
import com.semeshky.kvg.kvgapi.StopPoints;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class DatabaseSynchronizer {

    public static Single<Integer> synchronizeStops(Context context) {
        KvgApiClient.init(context);
        AppDatabase.init(context);
        return Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> emitter) throws Exception {
                try {
                    final Response<StationLocations> response = KvgApiClient
                            .getInstance()
                            .getService()
                            .getStationLocations()
                            .execute();
                    if (response.code() != 200) {
                        emitter.onError(new Exception("Couldnt reach server with error:" + response.code()));
                        return;
                    }
                    for (StationLocation stop : response.body().getStops()) {
                        AppDatabase
                                .getInstance()
                                .stopDao()
                                .insertAll(Stop.create(stop));
                    }
                    emitter.onSuccess(response.body().getStops().size());
                } catch (RuntimeException | IOException ex) {
                    emitter.onError(ex);
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Boolean> isDataSynchronized() {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
                try {
                    final StopDao stopDao = AppDatabase.getInstance()
                            .stopDao();
                    if (stopDao.countStops() == 0) {
                        emitter.onSuccess(false);
                    } else {
                        emitter.onSuccess(true);
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    public static Single<Integer> synchronizeStopPoints(Context context) {
        KvgApiClient.init(context);
        AppDatabase.init(context);
        return Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> emitter) throws Exception {
                try {
                    final Response<StopPoints> response = KvgApiClient
                            .getInstance()
                            .getService()
                            .getStopPoints()
                            .execute();
                    if (response.code() != 200) {
                        emitter.onError(new Exception("Couldnt reach server with error:" + response.code()));
                        return;
                    }
                    for (com.semeshky.kvg.kvgapi.StopPoint stop : response.body().getStopPoints()) {
                        AppDatabase
                                .getInstance()
                                .stopPointDao()
                                .insertAll(StopPoint.create(stop));
                    }
                    emitter.onSuccess(response.body().getStopPoints().size());
                } catch (RuntimeException | IOException ex) {
                    emitter.onError(ex);
                }
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

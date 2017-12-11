package com.semeshky.kvgspotter;

import android.app.Application;

import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.jobs.SynchronizeJobManager;

import io.reactivex.observers.DisposableSingleObserver;
import timber.log.Timber;


public abstract class AbstractKvgSpotterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KvgApiClient.init(this);
        AppDatabase.init(this);
        SynchronizeJobManager.synchronize(this)
                .subscribe(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        Timber.d("All jobs created");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }
}

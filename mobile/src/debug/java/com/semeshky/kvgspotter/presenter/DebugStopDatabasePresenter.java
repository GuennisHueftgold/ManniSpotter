package com.semeshky.kvgspotter.presenter;


import android.databinding.ObservableInt;

import com.semeshky.kvgspotter.database.DatabaseSynchronizer;

import io.reactivex.observers.DisposableSingleObserver;
import timber.log.Timber;

public class DebugStopDatabasePresenter {

    public final ObservableInt numberOfEntries = new ObservableInt();


    public void refresh() {
        DatabaseSynchronizer.synchronizeStops(null)
                .subscribe(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        Timber.d("data syncronized: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }
}

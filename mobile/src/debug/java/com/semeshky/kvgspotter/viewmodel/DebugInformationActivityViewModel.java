package com.semeshky.kvgspotter.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.semeshky.kvgspotter.database.DatabaseSynchronizer;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public final class DebugInformationActivityViewModel extends ViewModel {
    public final ObservableBoolean refreshingStops = new ObservableBoolean(false);
    public final ObservableInt stops = new ObservableInt(0);
    public final ObservableInt stopPoints = new ObservableInt(0);

    public void updateStops() {
        if (refreshingStops.get()) {
            return;
        }
        refreshingStops.set(true);
        Single.zip(DatabaseSynchronizer.synchronizeStopPoints(),
                DatabaseSynchronizer.synchronizeStops(),
                new BiFunction<Integer, Integer, Boolean>() {
                    @Override
                    public Boolean apply(Integer integer, Integer integer2) throws Exception {
                        return integer > 0 && integer2 > 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        refreshingStops.set(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshingStops.set(false);
                    }
                });
    }

}

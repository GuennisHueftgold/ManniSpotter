package com.semeshky.kvgspotter.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.IntDef;

import com.semeshky.kvgspotter.database.DatabaseSynchronizer;

import java.lang.annotation.Retention;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * ViewModel for {@link com.semeshky.kvgspotter.activities.SplashActivity}
 */
public class SplashActivityViewModel extends ViewModel {
    public final static int SYNC_STATUS_SYNCED = 1,
            SYNC_STATUS_ERROR = 2,
            SYNC_STATUS_NOT_SYNCED = 3,
            SYNC_STATUS_SYNCHRONIZING = 4;

    private final MutableLiveData<Integer> mSyncStatus = new MutableLiveData<>();

    private final AtomicBoolean mSynchronizing = new AtomicBoolean(false);
    private final AtomicBoolean mSynchronized = new AtomicBoolean(false);

    public SplashActivityViewModel() {
        this.mSyncStatus.setValue(SYNC_STATUS_NOT_SYNCED);
    }

    public boolean isSynchronizing() {
        return this.mSynchronizing.get();
    }

    public boolean getSynchronized() {
        return this.mSynchronized.get();
    }

    public void synchronize() {
        if (this.mSynchronized.get()) {
            return;
        }
        if (!this.mSynchronizing.compareAndSet(false, true)) {
            return;
        }
        this.setSyncStatus(SYNC_STATUS_SYNCHRONIZING);
        Single.zip(DatabaseSynchronizer.synchronizeStops(),
                DatabaseSynchronizer.synchronizeStopPoints(),
                new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        SplashActivityViewModel
                                .this
                                .setSyncStatus(SYNC_STATUS_SYNCED);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SplashActivityViewModel
                                .this
                                .setSyncStatus(SYNC_STATUS_ERROR);
                    }
                });
    }

    public LiveData<Integer> getSyncStatus() {
        return mSyncStatus;
    }

    protected void setSyncStatus(@SyncStatus int status) {
        if (status == SYNC_STATUS_SYNCED) {
            this.mSynchronized.set(true);
        } else {
            this.mSynchronized.set(false);
        }
        this.mSynchronizing.set(false);
        this.mSyncStatus
                .postValue(status);
    }

    public boolean isSynchronized() {
        return this.mSynchronized.get();
    }

    @Retention(SOURCE)
    @IntDef({SYNC_STATUS_SYNCED, SYNC_STATUS_ERROR, SYNC_STATUS_NOT_SYNCED, SYNC_STATUS_SYNCHRONIZING})
    public @interface SyncStatus {
    }
}

package com.semeshky.kvgspotter.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.semeshky.kvgspotter.database.DatabaseSynchronizer;

import io.reactivex.Single;

/**
 * ViewModel for {@link com.semeshky.kvgspotter.activities.SplashActivity}
 */
public class SplashActivityViewModel extends ViewModel {
    /**
     * Checks if the data is synchronized for the first start up
     *
     * @return
     */
    public Single<Boolean> isSynchronized() {
        return DatabaseSynchronizer
                .isDataSynchronized();
    }
}

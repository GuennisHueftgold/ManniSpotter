package com.semeshky.kvgspotter.jobs;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.semeshky.kvgspotter.api.KvgApiClient;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.DatabaseSynchronizer;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class SynchronizeStopJobService extends JobService {

    public static final int JOB_ID = 120;
    private Disposable mDisposable;

    @Override
    public boolean onStartJob(final JobParameters params) {
        KvgApiClient.init(this);
        AppDatabase.init(this);
        DatabaseSynchronizer
                .synchronizeStops(this)
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        SynchronizeStopJobService.this
                                .mDisposable = d;
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Timber.d("updated " + integer + " entries");
                        SynchronizeStopJobService.this.jobFinished(params, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SynchronizeStopJobService.this.jobFinished(params, true);
                    }
                });
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
            this.mDisposable.dispose();
        }
        return true;
    }

}
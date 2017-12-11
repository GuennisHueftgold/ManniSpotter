package com.semeshky.kvgspotter.jobs;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvgspotter.database.AppDatabase;
import com.semeshky.kvgspotter.database.DatabaseSynchronizer;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class SynchronizeStopPointJobService extends JobService {

    public static final int JOB_ID = 121;
    private Disposable mDisposable;

    @Override
    public boolean onStartJob(final JobParameters params) {
        KvgApiClient.init(this);
        AppDatabase.init(this);
        DatabaseSynchronizer
                .synchronizeStopPoints(this)
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        SynchronizeStopPointJobService.this
                                .mDisposable = d;
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        SynchronizeStopPointJobService.this.jobFinished(params, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SynchronizeStopPointJobService.this.jobFinished(params, true);
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
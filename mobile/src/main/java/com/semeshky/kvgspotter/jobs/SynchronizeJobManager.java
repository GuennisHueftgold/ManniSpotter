package com.semeshky.kvgspotter.jobs;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public final class SynchronizeJobManager {

    public static Single<Boolean> synchronize(final Context context) {
        return Single.create(new SingleOnSubscribe<JobScheduler>() {
            @Override
            public void subscribe(SingleEmitter<JobScheduler> emitter) throws Exception {
                final JobScheduler jobScheduler = (JobScheduler)
                        context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                emitter.onSuccess(jobScheduler);
            }
        })
                .flatMap(new Function<JobScheduler, SingleSource<Boolean>>() {
                    @Override
                    public SingleSource<Boolean> apply(JobScheduler jobScheduler) throws Exception {
                        boolean jobsReady = true;
                        if (!isJobRunning(jobScheduler, SynchronizeStopJobService.JOB_ID)) {
                            JobInfo.Builder builder = createJobBuilder(context,
                                    SynchronizeStopJobService.class,
                                    SynchronizeStopJobService.JOB_ID);
                            jobsReady &= SynchronizeJobManager.scheduleJob(jobScheduler, builder);
                        }
                        if (!isJobRunning(jobScheduler, SynchronizeStopPointJobService.JOB_ID)) {
                            JobInfo.Builder builder = createJobBuilder(context,
                                    SynchronizeStopPointJobService.class,
                                    SynchronizeStopPointJobService.JOB_ID);
                            jobsReady &= SynchronizeJobManager.scheduleJob(jobScheduler, builder);
                        }
                        return Single.just(jobsReady);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private static boolean isJobRunning(final JobScheduler jobScheduler, final int jobId) {
        boolean jobExists = false;
        if (Build.VERSION.SDK_INT >= 24) {
            jobExists = jobScheduler.getPendingJob(jobId) != null;
        } else {
            List<JobInfo> jobList = jobScheduler.getAllPendingJobs();
            for (JobInfo jobInfo : jobList) {
                if (jobInfo.getId() == jobId) {
                    jobExists = true;
                    break;
                }
            }
        }
        return jobExists;
    }


    private static boolean scheduleJob(@NonNull JobScheduler jobScheduler,
                                       @NonNull JobInfo.Builder builder) {
        final JobInfo jobInfo = builder.build();
        final boolean creationResult = jobScheduler.schedule(jobInfo) != JobScheduler.RESULT_SUCCESS;
        if (creationResult) {
            Timber.d("Created Job %s with id: %d", jobInfo.getService().getShortClassName(), jobInfo.getId());
        } else {
            Timber.e("Failed creating Job %s with id: %d", jobInfo.getService().getShortClassName(), jobInfo.getId());
        }
        return creationResult;
    }

    private static <T extends JobService> JobInfo.Builder createJobBuilder(Context context,
                                                                           Class<T> clazz,
                                                                           int jobId) {
        final JobInfo.Builder builder = new JobInfo.Builder(jobId,
                new ComponentName(context.getPackageName(),
                        clazz.getName()))
                .setPersisted(true)
                .setPeriodic(24 * 3600 * 1000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        if (Build.VERSION.SDK_INT >= 26) {
            builder.setRequiresBatteryNotLow(true);
        }
        return builder;
    }

}

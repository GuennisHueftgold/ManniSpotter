package com.semeshky.kvgspotter.location;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import java.util.concurrent.Executor;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;

@RunWith(AndroidJUnit4.class)
public class LocationHelperTest {
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule
            .grant(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
    @Rule
    public TestRule mScheduleTestRule = new TestRule() {
        private final Scheduler immediate = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(new Executor() {
                    @Override
                    public void execute(@NonNull Runnable runnable) {
                        runnable.run();
                    }
                });
            }
        };
        private final TestScheduler testScheduler = new TestScheduler();

        public TestScheduler getTestScheduler() {
            return testScheduler;
        }

        @Override
        public Statement apply(final Statement base, Description d) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    final Function<Scheduler, Scheduler> tt = new Function<Scheduler, Scheduler>() {
                        @Override
                        public Scheduler apply(Scheduler scheduler) throws Exception {
                            return testScheduler;
                        }
                    };
                    RxJavaPlugins.setIoSchedulerHandler(tt);
                    RxJavaPlugins.setComputationSchedulerHandler(tt);
                    RxJavaPlugins.setNewThreadSchedulerHandler(tt);
                    RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
                        @Override
                        public Scheduler apply(Scheduler scheduler) throws Exception {
                            return immediate;
                        }
                    });

                    try {
                        base.evaluate();
                    } finally {
                        RxJavaPlugins.reset();
                        RxAndroidPlugins.reset();
                    }
                }
            };
        }
    };

    @Test()
    public void getLastLocationSingle_should_fail() {

    }

    @Test()
    public void getLastLocationSingle_should_succeed_for_gps_provider() throws InterruptedException {/*
        final MockContext mockContext = mock(MockContext.class);
        final LocationManager mockLocationManager = mock(LocationManager.class);
        final Context context = InstrumentationRegistry.getTargetContext();
        //final MockLocationHelper mockLocationHelper=new MockLocationHelper(context);
        final Location testLocation = MockLocationHelper.createLocation(LocationManager.GPS_PROVIDER, 10, 10);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(testLocation);
        when(mockContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(mockLocationManager);
        final LocationHelper locationHelper = new LocationHelper(context);
        final TestObserver<Location> testObserver = new TestObserver<>();
        locationHelper.getLastLocationSingle()
                .subscribe(testObserver);
        testObserver.assertSubscribed();
        testObserver.awaitCount(1);
        testObserver.assertNoErrors();
        assertEquals(1, testObserver.valueCount());
        assertEquals(testLocation, testObserver.values().get(0));*/
        //mockLocationHelper.stop();
    }
}

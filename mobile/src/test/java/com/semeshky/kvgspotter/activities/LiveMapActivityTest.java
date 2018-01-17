package com.semeshky.kvgspotter.activities;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.viewmodel.ActivityLiveMapViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {ShadowLiveMapFragment.class})
public class LiveMapActivityTest {
    @Test
    public void LiveMapActivity_should_construct_correclty() {
        ActivityController activityController = Robolectric.buildActivity(LiveMapActivity.class);
        activityController.create();
        LiveMapActivity liveMapActivity = (LiveMapActivity) activityController.get();
        assertNotNull(liveMapActivity.mViewModel);
        activityController.start();
    }

    @Test
    public void LiveMapActivity_should_start_and_stop_location_update() {
        ActivityController activityController = Robolectric.buildActivity(LiveMapActivity.class);
        activityController.create();
        LiveMapActivity liveMapActivity = (LiveMapActivity) activityController.get();
        ActivityLiveMapViewModel mockViewModel = mock(ActivityLiveMapViewModel.class);
        liveMapActivity.mViewModel = mockViewModel;
        activityController.resume();
        verify(mockViewModel, times(1)).startVehicleLocationUpdater();
        activityController.pause();
        verify(mockViewModel, times(1)).stopVehicleLocationUpdater();
    }
}

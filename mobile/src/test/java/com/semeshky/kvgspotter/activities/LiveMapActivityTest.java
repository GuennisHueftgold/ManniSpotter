package com.semeshky.kvgspotter.activities;

import com.semeshky.kvgspotter.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;

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
}

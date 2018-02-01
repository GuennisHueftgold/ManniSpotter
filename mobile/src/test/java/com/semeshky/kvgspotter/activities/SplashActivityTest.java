package com.semeshky.kvgspotter.activities;

import com.semeshky.kvgspotter.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        shadows = {ShadowSplashActivityViewModel.class,
                ShadowClientSettings.class})
public class SplashActivityTest {
    @Test
    public void SplashActivity_should_construct_correclty() {
        ActivityController activityController = Robolectric.buildActivity(SplashActivity.class);
        activityController.create();
        SplashActivity splashActivity = (SplashActivity) activityController.get();
        assertNotNull(splashActivity.mViewModel);
        activityController.start();
    }

}

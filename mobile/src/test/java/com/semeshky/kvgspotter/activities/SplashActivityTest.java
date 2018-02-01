package com.semeshky.kvgspotter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.settings.ClientSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowIntent;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        shadows = {ShadowSplashActivityViewModel.class,
                ShadowClientSettings.class,
                ShadowSplashFragmentAdapter.class})
public class SplashActivityTest {

    private Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void SplashActivity_should_directly_close_and_to_to_MainActivity() {
        ShadowClientSettings clientSettings = ShadowClientSettings.shadowOf(ClientSettings.getInstance(context));
        clientSettings.setFirstSetup(true);
        ActivityController activityController = Robolectric.buildActivity(SplashActivity.class);
        activityController.create();
        SplashActivity splashActivity = (SplashActivity) activityController.get();
        Intent startedIntent = shadowOf(splashActivity).getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertEquals(MainActivity.class, shadowIntent.getIntentClass());
        assertTrue(splashActivity.isFinishing());
    }

    @Test
    public void SplashActivity_onSaveInstanceState_should_work_properly() {
        Bundle bundle = mock(Bundle.class);
        ActivityController activityController = Robolectric.buildActivity(SplashActivity.class);
        activityController.create();
        SplashActivity splashActivity = (SplashActivity) activityController.get();
        splashActivity.mEntryAnimationPlayed = false;
        activityController.saveInstanceState(bundle);
        verify(bundle, times(1)).putBoolean(SplashActivity.KEY_ENTRY_ANIMATION_PLAYED, false);
        reset(bundle);
        splashActivity.mEntryAnimationPlayed = true;
        activityController.saveInstanceState(bundle);
        verify(bundle, times(1)).putBoolean(SplashActivity.KEY_ENTRY_ANIMATION_PLAYED, true);
    }

    @Test
    public void SplashActivity_onRestoreInstanceState_should_work_properly() {
        Bundle bundle = new Bundle();
        Bundle bundleSpy = spy(bundle);
        ActivityController activityController = Robolectric.buildActivity(SplashActivity.class);
        activityController.create();
        SplashActivity splashActivity = (SplashActivity) activityController.get();
        activityController.restoreInstanceState(bundleSpy);
        verify(bundleSpy, times(1)).getBoolean(SplashActivity.KEY_ENTRY_ANIMATION_PLAYED, false);
        assertFalse(splashActivity.mEntryAnimationPlayed);
        reset(bundleSpy);
        bundle.putBoolean(SplashActivity.KEY_ENTRY_ANIMATION_PLAYED, true);
        activityController.restoreInstanceState(bundleSpy);
        verify(bundleSpy, times(1)).getBoolean(SplashActivity.KEY_ENTRY_ANIMATION_PLAYED, false);
        assertTrue(splashActivity.mEntryAnimationPlayed);
        reset(bundleSpy);
        bundle.putBoolean(SplashActivity.KEY_ENTRY_ANIMATION_PLAYED, false);
        activityController.restoreInstanceState(bundleSpy);
        verify(bundleSpy, times(1)).getBoolean(SplashActivity.KEY_ENTRY_ANIMATION_PLAYED, false);
        assertFalse(splashActivity.mEntryAnimationPlayed);
        reset(bundleSpy);
    }

    @Test
    public void SplashActivity_should_construct_correclty() {
        ActivityController activityController = Robolectric.buildActivity(SplashActivity.class);
        activityController.create();
        SplashActivity splashActivity = (SplashActivity) activityController.get();
        assertNotNull(splashActivity.mViewModel);
        activityController.start();
    }

}

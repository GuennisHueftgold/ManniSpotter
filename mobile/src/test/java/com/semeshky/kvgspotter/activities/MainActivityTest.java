package com.semeshky.kvgspotter.activities;

import android.content.Intent;
import android.location.Location;
import android.view.MenuItem;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.viewmodel.MainActivityViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowIntent;

import java.util.List;

import io.reactivex.Flowable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {
    @Test
    public void MainActivity_should_construct_correclty() {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.create();
        MainActivity mainActivity = (MainActivity) activityController.get();
        assertNotNull(mainActivity.mViewModel);
    }

    @Test
    public void MainActivity_should_start_location_updates() {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.create();
        MainActivity mainActivity = (MainActivity) activityController.get();
        MainActivityViewModel mockViewModel = mock(MainActivityViewModel.class);
        when(mockViewModel.getFavoriteFlowable(nullable(Flowable.class))).thenReturn(Flowable.<List<Location>>empty());
        mainActivity.mViewModel = mockViewModel;
        activityController.start();
        activityController.resume();
        assertNotNull(mainActivity.mFavoriteDisposable);
        activityController.pause();
        assertNotNull(mainActivity.mFavoriteDisposable);
        assertTrue(mainActivity.mFavoriteDisposable.isDisposed());
    }

    @Test
    public void onOptionsItemSelected_should_start_LiveMapActivity() {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.create();
        MainActivity mainActivity = (MainActivity) activityController.get();
        MenuItem liveMapMenuItem = mock(MenuItem.class);
        when(liveMapMenuItem.getItemId()).thenReturn(R.id.action_live_map);
        assertTrue(mainActivity.onOptionsItemSelected(liveMapMenuItem));
        Intent startedIntent = shadowOf(mainActivity).getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertEquals(LiveMapActivity.class, shadowIntent.getIntentClass());
    }

    @Test
    public void onOptionsItemSelected_should_start_PreferencesActivity() {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.create();
        MainActivity mainActivity = (MainActivity) activityController.get();
        MenuItem liveMapMenuItem = mock(MenuItem.class);
        when(liveMapMenuItem.getItemId()).thenReturn(R.id.action_settings);
        assertTrue(mainActivity.onOptionsItemSelected(liveMapMenuItem));
        Intent startedIntent = shadowOf(mainActivity).getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertEquals(PreferencesActivity.class, shadowIntent.getIntentClass());
    }

    @Test
    public void onOptionsItemSelected_should_not_start_any_activity() {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.create();
        MainActivity mainActivity = (MainActivity) activityController.get();
        MenuItem liveMapMenuItem = mock(MenuItem.class);
        when(liveMapMenuItem.getItemId()).thenReturn(R.id.action_done);
        assertFalse(mainActivity.onOptionsItemSelected(liveMapMenuItem));
        assertNull(shadowOf(mainActivity).getNextStartedActivity());
    }
}

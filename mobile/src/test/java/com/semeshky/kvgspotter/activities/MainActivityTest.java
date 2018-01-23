package com.semeshky.kvgspotter.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.ShadowTimber;
import com.semeshky.kvgspotter.api.Release;
import com.semeshky.kvgspotter.fragments.RequestLocationPermissionDialogFragment;
import com.semeshky.kvgspotter.util.SemVer;
import com.semeshky.kvgspotter.viewmodel.MainActivityViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowIntent;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        shadows = {ShadowSnackbar.class,
                ShadowRequestLocationPermissionDialogFragment.class})
public class MainActivityTest {
    private Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void MainActivity_should_construct_correclty() {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.create();
        MainActivity mainActivity = (MainActivity) activityController.get();
        assertNotNull(mainActivity.mViewModel);
    }

    @Test
    @Config(shadows = {ShadowTimber.class})
    public void MainActivity_check_silent_error_consumer() throws Exception {
        assertEquals(0, ShadowTimber.getExceptionCount());
        final Throwable testThrowable = new Throwable("test");
        MainActivity.SILENT_ERROR_CONSUMER.accept(testThrowable);
        assertEquals(1, ShadowTimber.getExceptionCount());
        assertEquals(testThrowable, ShadowTimber.getLastException());
    }

    @Test
    @Config(shadows = {ShadowMainActivity.class})
    public void MainActivity_release_consumer_should_work_correctly() throws Exception {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.create();
        MainActivity mainActivity = (MainActivity) activityController.get();
        ShadowMainActivity shadowMainActivity = Shadow.extract(mainActivity);
        mainActivity.mReleaseConsumer.accept(null);
        assertEquals(0, shadowMainActivity.getShowUpdateNoticeCallCount());
        final SemVer semVer = SemVer.parse(BuildConfig.VERSION_NAME);
        Release release = new Release.Builder()
                .setTagName(semVer.getMajor() + "." + semVer.getMinor() + "." + (semVer.getPatch() - 1))
                .build();
        mainActivity.mReleaseConsumer.accept(release);
        assertEquals(0, shadowMainActivity.getShowUpdateNoticeCallCount());
        release = new Release.Builder()
                .setTagName(semVer.getMajor() + "." + semVer.getMinor() + "." + semVer.getPatch())
                .build();
        mainActivity.mReleaseConsumer.accept(release);
        assertEquals(0, shadowMainActivity.getShowUpdateNoticeCallCount());
        release = new Release.Builder()
                .setTagName(semVer.getMajor() + "." + semVer.getMinor() + "." + (semVer.getPatch() + 1))
                .build();
        mainActivity.mReleaseConsumer.accept(release);
        assertEquals(1, shadowMainActivity.getShowUpdateNoticeCallCount());
        assertEquals(release, shadowMainActivity.getLastShowUpdateNoticeArg());
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
    public void MainActivity_should_dispose_all_consumer_on_pause() {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.create();
        MainActivity mainActivity = (MainActivity) activityController.get();
        mainActivity.mFavoriteDisposable = null;
        mainActivity.mNearbyDisposable = null;
        mainActivity.mUpdateCheckDisposable = null;
        activityController.pause();

        final Disposable mockFavoriteDisposable = mock(Disposable.class);
        final Disposable mockNearbyDisposable = mock(Disposable.class);
        final Disposable mockUpdateCheckDisposable = mock(Disposable.class);
        mainActivity.mFavoriteDisposable = mockFavoriteDisposable;
        mainActivity.mNearbyDisposable = mockNearbyDisposable;
        mainActivity.mUpdateCheckDisposable = mockUpdateCheckDisposable;
        when(mockFavoriteDisposable.isDisposed()).thenReturn(true);
        when(mockNearbyDisposable.isDisposed()).thenReturn(true);
        when(mockUpdateCheckDisposable.isDisposed()).thenReturn(true);
        activityController.pause();
        verify(mockFavoriteDisposable, never()).dispose();
        verify(mockNearbyDisposable, never()).dispose();
        verify(mockUpdateCheckDisposable, never()).dispose();
        reset(mockFavoriteDisposable);
        reset(mockNearbyDisposable);
        reset(mockUpdateCheckDisposable);
        when(mockFavoriteDisposable.isDisposed()).thenReturn(false);
        when(mockNearbyDisposable.isDisposed()).thenReturn(false);
        when(mockUpdateCheckDisposable.isDisposed()).thenReturn(false);
        activityController.pause();
        verify(mockFavoriteDisposable, times(1)).dispose();
        verify(mockNearbyDisposable, times(1)).dispose();
        verify(mockUpdateCheckDisposable, times(1)).dispose();
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

    @Test
    public void coordinatorLayout_always_available() {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.create();
        MainActivity mainActivity = (MainActivity) activityController.get();
        activityController.resume();
        activityController.visible();
        assertNotNull(mainActivity.findViewById(R.id.coordinatorLayout));
    }

    @Test
    public void showUpdateNotice_should_show_the_snackbar() {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.create();
        MainActivity mainActivity = (MainActivity) activityController.get();
        //activityController.resume();
        activityController.visible();
        final String testUrl = "http://url.com";
        final Release release = new Release.Builder()
                .setTagName("v1.2.3")
                .setHtmlUrl(testUrl)
                .build();
        mainActivity.showUpdateNotice(release);
        assertEquals(1, ShadowSnackbar.shownSnackbarCount());
        final ShadowSnackbar latestSnackbarShadow = ShadowSnackbar.shadowOf(ShadowSnackbar.getLatestSnackbar());
        assertEquals(context.getString(R.string.update_available), latestSnackbarShadow.text);
        assertEquals(Snackbar.LENGTH_LONG, latestSnackbarShadow.duration);
        assertEquals(R.string.update, latestSnackbarShadow.actionResId);
        assertNotNull(latestSnackbarShadow.actionClickListener);
        latestSnackbarShadow.actionClickListener.onClick(latestSnackbarShadow.view);
        Intent startedIntent = shadowOf(mainActivity).getNextStartedActivity();
        assertEquals(Intent.ACTION_VIEW, startedIntent.getAction());
        assertEquals(Uri.parse(testUrl), startedIntent.getData());
    }

    @Test
    @Config(shadows = {ShadowActivityCompat.class})
    public void showRequestPermissionDialog_should_work_properly() {
        ActivityController activityController = Robolectric.buildActivity(MainActivity.class);
        activityController.create();
        MainActivity mainActivity = (MainActivity) activityController.get();
        activityController.resume();
        activityController.visible();
        mainActivity.showRequestPermissionDialog();
        assertEquals(1, ShadowRequestLocationPermissionDialogFragment.getCreatedInstancesCount());
        RequestLocationPermissionDialogFragment fragment =
                ShadowRequestLocationPermissionDialogFragment.getLatestFragment();
        ShadowRequestLocationPermissionDialogFragment shadowFragment =
                ShadowRequestLocationPermissionDialogFragment.shadowOf(fragment);
        assertEquals(mainActivity.getSupportFragmentManager(),
                fragment.getFragmentManager());
        assertEquals(MainActivity.TAG_ASK_FOR_LOCATION,
                fragment.getTag());
        assertNotNull(shadowFragment.mListener);
        assertEquals(0, ShadowActivityCompat.getRequestPermissionCallCount());
        shadowFragment.mListener.onApproveRequest(false);
        assertEquals(0, ShadowActivityCompat.getRequestPermissionCallCount());
        shadowFragment.mListener.onApproveRequest(true);
        assertEquals(1, ShadowActivityCompat.getRequestPermissionCallCount());
    }
}


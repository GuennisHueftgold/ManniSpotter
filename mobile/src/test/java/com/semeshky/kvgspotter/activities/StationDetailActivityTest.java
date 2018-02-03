package com.semeshky.kvgspotter.activities;

import android.content.Context;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {ShadowStationDetailAdapter.class,
        ShadowStationDetailActivityViewModel.class,
        ShadowSnackbar.class})
public class StationDetailActivityTest {

    private StationDetailActivity mStationDetailActivity;
    private ShadowStationDetailActivityViewModel mShadowViewModel;
    private ActivityController<StationDetailActivity> mActivityController;
    private Context mContext;

    @Before
    public void setup() {
        this.mContext = RuntimeEnvironment.application;
        this.mActivityController = Robolectric.buildActivity(StationDetailActivity.class);
        this.mActivityController.create();
        this.mStationDetailActivity = this.mActivityController.get();
        this.mShadowViewModel = ShadowStationDetailActivityViewModel.shadowOf(this.mStationDetailActivity.mViewModel);
        ShadowSnackbar.reset();
    }

    @Test
    public void should_construct_correclty() {
        assertNotNull(mStationDetailActivity.mViewModel);
    }

    @Test
    public void should_start_and_stop_location_update() {
        assertEquals(0, this.mShadowViewModel.getStartSyncServiceCallCount());
        assertEquals(0, this.mShadowViewModel.getStopSyncServiceCallCount());
        this.mActivityController.resume();
        assertEquals(1, this.mShadowViewModel.getStartSyncServiceCallCount());
        this.mActivityController.pause();
        assertEquals(1, this.mShadowViewModel.getStopSyncServiceCallCount());
    }

    @Test
    public void should_create_and_clear_error_disposable_correctly() {
        assertNull(this.mStationDetailActivity.mErrorDisposable);
        this.mActivityController.resume();
        assertNotNull(this.mStationDetailActivity.mErrorDisposable);
        assertFalse(this.mStationDetailActivity.mErrorDisposable.isDisposed());
        this.mActivityController.pause();
        assertTrue(this.mStationDetailActivity.mErrorDisposable.isDisposed());
    }

    @Test
    public void showFavoriteStatusSnackar_correctly_creates_snackbar() {
        this.mStationDetailActivity.showFavoriteStatusSnackar(true);
        assertEquals(1, ShadowSnackbar.shownSnackbarCount());
        assertEquals(mContext.getString(R.string.stop_favorited), ShadowSnackbar.getTextOfLatestSnackbar());
        ShadowSnackbar.reset();
        this.mStationDetailActivity.showFavoriteStatusSnackar(false);
        assertEquals(1, ShadowSnackbar.shownSnackbarCount());
        assertEquals(mContext.getString(R.string.stop_removed_from_favorites), ShadowSnackbar.getTextOfLatestSnackbar());
    }
}

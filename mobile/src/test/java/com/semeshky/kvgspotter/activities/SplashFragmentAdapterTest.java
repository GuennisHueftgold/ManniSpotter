package com.semeshky.kvgspotter.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.semeshky.kvgspotter.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,
        shadows = {ShadowPagerAdapter.class})
public class SplashFragmentAdapterTest {

    private FragmentManager mFragmentManager;

    @Before
    public void setup() {
        FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class)
                .create()
                .start()
                .resume()
                .get();
        mFragmentManager = activity.getSupportFragmentManager();
    }

    @Test
    public void setAllowAdvance_should_work() {
        SplashFragmentAdapter splashFragmentAdapter = new SplashFragmentAdapter(this.mFragmentManager);
        ShadowPagerAdapter shadowPagerAdapter = ShadowPagerAdapter.shadowOf(splashFragmentAdapter);
        shadowPagerAdapter.reset();
        splashFragmentAdapter.mAllowAdvance = false;
        splashFragmentAdapter.setAllowAdvance(true);
        assertTrue(splashFragmentAdapter.mAllowAdvance);
        assertEquals(1, shadowPagerAdapter.notifyDataSetChangedCallCount());
        shadowPagerAdapter.reset();
        assertTrue(splashFragmentAdapter.mAllowAdvance);
        assertEquals(0, shadowPagerAdapter.notifyDataSetChangedCallCount());
        shadowPagerAdapter.reset();
        splashFragmentAdapter.setAllowAdvance(false);
        assertFalse(splashFragmentAdapter.mAllowAdvance);
        assertEquals(1, shadowPagerAdapter.notifyDataSetChangedCallCount());
    }

    @Test
    @Config(maxSdk = 22)
    public void mRequiresAskingForLocation_should_be_false() {
        SplashFragmentAdapter splashFragmentAdapter = new SplashFragmentAdapter(this.mFragmentManager);
        assertFalse(splashFragmentAdapter.mRequiresAskingForLocation);
    }

    @Test
    @Config(minSdk = 23)
    public void mRequiresAskingForLocation_should_be_true() {
        SplashFragmentAdapter splashFragmentAdapter = new SplashFragmentAdapter(this.mFragmentManager);
        assertTrue(splashFragmentAdapter.mRequiresAskingForLocation);
    }

    @Test
    @Config(minSdk = 23)
    public void getCount_should_return_proper_values_for_sdk_23_and_up() {
        SplashFragmentAdapter splashFragmentAdapter = new SplashFragmentAdapter(this.mFragmentManager);
        splashFragmentAdapter.setAllowAdvance(false);
        assertEquals(2, splashFragmentAdapter.getCount());
        splashFragmentAdapter.setAllowAdvance(true);
        assertEquals(4, splashFragmentAdapter.getCount());
    }

    @Test
    @Config(maxSdk = 22)
    public void getCount_should_return_proper_values_for_sdk_below_23() {
        SplashFragmentAdapter splashFragmentAdapter = new SplashFragmentAdapter(this.mFragmentManager);
        splashFragmentAdapter.setAllowAdvance(false);
        assertEquals(2, splashFragmentAdapter.getCount());
        splashFragmentAdapter.setAllowAdvance(true);
        assertEquals(3, splashFragmentAdapter.getCount());
    }
}

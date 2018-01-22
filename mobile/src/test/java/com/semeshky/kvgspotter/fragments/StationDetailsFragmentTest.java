package com.semeshky.kvgspotter.fragments;

import com.semeshky.kvgspotter.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class StationDetailsFragmentTest {

    @Before
    public void setup() {

    }

    @Test
    public void shouldNotBeNull() throws Exception {
        StationDetailsFragment fragment = new StationDetailsFragment();
        startFragment(fragment);
    }
}
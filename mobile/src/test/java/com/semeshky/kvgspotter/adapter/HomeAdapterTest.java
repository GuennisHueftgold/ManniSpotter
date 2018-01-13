package com.semeshky.kvgspotter.adapter;

import com.semeshky.kvgspotter.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class HomeAdapterTest {

    @Test
    public void HomeAdapter_initial_getItemCount_should_be_four() {
        HomeAdapter.HomeAdapterEventListener mockListener = mock(HomeAdapter.HomeAdapterEventListener.class);
        HomeAdapter homeAdapter = new HomeAdapter(mockListener);
        assertEquals(4, homeAdapter.getItemCount());
        assertEquals(HomeAdapter.TYPE_TITLE, homeAdapter.getItemViewType(0));
        assertEquals(HomeAdapter.TYPE_FAVORITE_INFO, homeAdapter.getItemViewType(1));
        assertEquals(HomeAdapter.TYPE_TITLE, homeAdapter.getItemViewType(2));
        assertEquals(HomeAdapter.TYPE_NEARBY_STOP_INFO, homeAdapter.getItemViewType(3));
    }
}

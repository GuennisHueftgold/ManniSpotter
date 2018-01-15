package com.semeshky.kvgspotter.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.viewholder.HomeRequestPermissionViewHolder;
import com.semeshky.kvgspotter.viewholder.ListSectionTitleViewHolder;
import com.semeshky.kvgspotter.viewholder.NoFavoriteViewHolder;
import com.semeshky.kvgspotter.viewholder.StopDistanceViewHolder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class HomeAdapterTest {

    private Context context;

    public static List<HomeAdapter.DistanceStop> createStopList(int items) {
        final List<HomeAdapter.DistanceStop> stopList = new ArrayList<>();
        for (int i = 0; i < items; i++) {
            stopList.add(new HomeAdapter.DistanceStop(i, "shortName" + i, "name" + i, i));
        }
        return stopList;
    }

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
    }

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

    @Test
    public void onCreateViewHolder_should_construct_the_correct_types() {
        HomeAdapter.HomeAdapterEventListener mockListener = mock(HomeAdapter.HomeAdapterEventListener.class);
        HomeAdapter homeAdapter = new HomeAdapter(mockListener);
        LinearLayout vg = new LinearLayout(context);
        vg.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
        vg.layout(0, 0, 500, 500);
        assertEquals(StopDistanceViewHolder.class, homeAdapter.onCreateViewHolder(vg, HomeAdapter.TYPE_STOP).getClass());
        assertEquals(ListSectionTitleViewHolder.class, homeAdapter.onCreateViewHolder(vg, HomeAdapter.TYPE_TITLE).getClass());
        assertEquals(HomeRequestPermissionViewHolder.class, homeAdapter.onCreateViewHolder(vg, HomeAdapter.TYPE_NEARBY_STOP_INFO).getClass());
        assertEquals(NoFavoriteViewHolder.class, homeAdapter.onCreateViewHolder(vg, HomeAdapter.TYPE_FAVORITE_INFO).getClass());
    }

    @Test(expected = RuntimeException.class)
    public void onCreateViewHolder_should_throw_exception_for_unknown_types() {
        HomeAdapter.HomeAdapterEventListener mockListener = mock(HomeAdapter.HomeAdapterEventListener.class);
        HomeAdapter homeAdapter = new HomeAdapter(mockListener);
        ViewGroup vg = mock(ViewGroup.class);
        when(vg.getContext()).thenReturn(context);
        homeAdapter.onCreateViewHolder(vg, -1249);
    }

    @Test
    public void setFavorites_should_add_items_correctly() {
        final HomeAdapter.HomeAdapterEventListener mockListener = mock(HomeAdapter.HomeAdapterEventListener.class);
        final HomeAdapter homeAdapter = new HomeAdapter(mockListener);
        final HomeAdapter homeAdapterSpy = spy(homeAdapter);
        final int testItems = 10;
        List<HomeAdapter.DistanceStop> stops = createStopList(testItems);
        homeAdapterSpy.setFavorites(stops, true);
        assertEquals(stops, homeAdapter.mFavoriteStationList);
        verify(homeAdapterSpy, times(1)).setFavorites(stops, true);
        verify(homeAdapterSpy, times(1)).updateIndex();
        reset(homeAdapterSpy);
        homeAdapter.mFavoriteStationList.clear();
        homeAdapterSpy.setFavorites(stops);
        assertEquals(stops, homeAdapterSpy.mFavoriteStationList);
        verify(homeAdapterSpy, times(1)).setFavorites(stops, true);
        verify(homeAdapterSpy, times(1)).updateIndex();
        reset(homeAdapterSpy);
        homeAdapter.mFavoriteStationList.clear();
        homeAdapterSpy.setFavorites(stops, false);
        assertEquals(stops, homeAdapterSpy.mFavoriteStationList);
        verify(homeAdapterSpy, times(1)).setFavorites(stops, false);
        verify(homeAdapterSpy, never()).updateIndex();
    }
}

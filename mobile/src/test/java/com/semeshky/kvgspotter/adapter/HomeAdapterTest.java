package com.semeshky.kvgspotter.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
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
            stopList.add(createStop(i));
        }
        return stopList;
    }

    public static HomeAdapter.DistanceStop createStop(int idx) {
        return new HomeAdapter.DistanceStop(idx, "shortName" + idx, "name" + idx, idx);
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
        doNothing().when(homeAdapterSpy).updateIndex();
        List<HomeAdapter.DistanceStop> stops = createStopList(testItems);
        homeAdapterSpy.setFavorites(stops, true);
        assertEquals(stops, homeAdapter.mFavoriteStationList);
        verify(homeAdapterSpy, times(1)).setFavorites(stops, true);
        verify(homeAdapterSpy, times(1)).updateIndex();
        reset(homeAdapterSpy);
        doNothing().when(homeAdapterSpy).updateIndex();
        homeAdapter.mFavoriteStationList.clear();
        homeAdapterSpy.setFavorites(stops);
        assertEquals(stops, homeAdapterSpy.mFavoriteStationList);
        verify(homeAdapterSpy, times(1)).setFavorites(stops, true);
        verify(homeAdapterSpy, times(1)).updateIndex();
        reset(homeAdapterSpy);
        doNothing().when(homeAdapterSpy).updateIndex();
        homeAdapter.mFavoriteStationList.clear();
        homeAdapterSpy.setFavorites(stops, false);
        assertEquals(stops, homeAdapterSpy.mFavoriteStationList);
        verify(homeAdapterSpy, times(1)).setFavorites(stops, false);
        verify(homeAdapterSpy, never()).updateIndex();
    }

    @Test
    public void setNearby_should_add_items_correctly() {
        final HomeAdapter.HomeAdapterEventListener mockListener = mock(HomeAdapter.HomeAdapterEventListener.class);
        final HomeAdapter homeAdapter = new HomeAdapter(mockListener);
        final HomeAdapter homeAdapterSpy = spy(homeAdapter);
        final int testItems = 10;
        doNothing().when(homeAdapterSpy).updateIndex();
        List<HomeAdapter.DistanceStop> stops = createStopList(testItems);
        homeAdapterSpy.setNearby(stops, true);
        assertEquals(stops, homeAdapter.mNearbyStopList);
        verify(homeAdapterSpy, times(1)).setNearby(stops, true);
        verify(homeAdapterSpy, times(1)).updateIndex();
        reset(homeAdapterSpy);
        doNothing().when(homeAdapterSpy).updateIndex();
        homeAdapter.mNearbyStopList.clear();
        homeAdapterSpy.setNearby(stops);
        assertEquals(stops, homeAdapterSpy.mNearbyStopList);
        verify(homeAdapterSpy, times(1)).setNearby(stops, true);
        verify(homeAdapterSpy, times(1)).updateIndex();
        reset(homeAdapterSpy);
        doNothing().when(homeAdapterSpy).updateIndex();
        homeAdapter.mNearbyStopList.clear();
        homeAdapterSpy.setNearby(stops, false);
        assertEquals(stops, homeAdapterSpy.mNearbyStopList);
        verify(homeAdapterSpy, times(1)).setNearby(stops, false);
        verify(homeAdapterSpy, never()).updateIndex();
    }

    @Test
    public void addNearby_should_add_items_correctly() {
        final HomeAdapter.HomeAdapterEventListener mockListener = mock(HomeAdapter.HomeAdapterEventListener.class);
        final HomeAdapter homeAdapter = new HomeAdapter(mockListener);
        final HomeAdapter homeAdapterSpy = spy(homeAdapter);
        final HomeAdapter.DistanceStop distanceStop = createStop(29);
        //
        doNothing().when(homeAdapterSpy).updateIndex();
        assertEquals(0, homeAdapter.mNearbyStopList.size());
        homeAdapterSpy.addNearby(distanceStop, true);
        assertEquals(distanceStop, homeAdapter.mNearbyStopList.get(0));
        assertEquals(1, homeAdapter.mNearbyStopList.size());
        verify(homeAdapterSpy, times(1)).updateIndex();
        //
        reset(homeAdapterSpy);
        doNothing().when(homeAdapterSpy).updateIndex();
        homeAdapter.mNearbyStopList.clear();
        assertEquals(0, homeAdapter.mNearbyStopList.size());
        homeAdapterSpy.addNearby(distanceStop, false);
        assertEquals(distanceStop, homeAdapter.mNearbyStopList.get(0));
        assertEquals(1, homeAdapter.mNearbyStopList.size());
        verify(homeAdapterSpy, never()).updateIndex();
        //
        reset(homeAdapterSpy);
        doNothing().when(homeAdapterSpy).updateIndex();
        homeAdapter.mNearbyStopList.clear();
        assertEquals(0, homeAdapter.mNearbyStopList.size());
        homeAdapterSpy.addNearby(distanceStop);
        assertEquals(distanceStop, homeAdapter.mNearbyStopList.get(0));
        assertEquals(1, homeAdapter.mNearbyStopList.size());
        verify(homeAdapterSpy, times(1)).addNearby(distanceStop, true);
        verify(homeAdapterSpy, times(1)).updateIndex();
    }

    @Test
    public void addFavorite_should_add_items_correctly() {
        final HomeAdapter.HomeAdapterEventListener mockListener = mock(HomeAdapter.HomeAdapterEventListener.class);
        final HomeAdapter homeAdapter = new HomeAdapter(mockListener);
        final HomeAdapter homeAdapterSpy = spy(homeAdapter);
        final HomeAdapter.DistanceStop distanceStop = createStop(29);
        //
        doNothing().when(homeAdapterSpy).updateIndex();
        assertEquals(0, homeAdapterSpy.mFavoriteStationList.size());
        homeAdapterSpy.addFavorite(distanceStop, true);
        assertEquals(distanceStop, homeAdapterSpy.mFavoriteStationList.get(0));
        assertEquals(1, homeAdapterSpy.mFavoriteStationList.size());
        verify(homeAdapterSpy, times(1)).updateIndex();
        //
        reset(homeAdapterSpy);
        doNothing().when(homeAdapterSpy).updateIndex();
        homeAdapterSpy.mFavoriteStationList.clear();
        assertEquals(0, homeAdapterSpy.mFavoriteStationList.size());
        homeAdapterSpy.addFavorite(distanceStop, false);
        assertEquals(distanceStop, homeAdapterSpy.mFavoriteStationList.get(0));
        assertEquals(1, homeAdapterSpy.mFavoriteStationList.size());
        verify(homeAdapterSpy, never()).updateIndex();
        //
        reset(homeAdapterSpy);
        doNothing().when(homeAdapterSpy).updateIndex();
        homeAdapterSpy.mFavoriteStationList.clear();
        assertEquals(0, homeAdapterSpy.mFavoriteStationList.size());
        homeAdapterSpy.addFavorite(distanceStop);
        assertEquals(distanceStop, homeAdapterSpy.mFavoriteStationList.get(0));
        assertEquals(1, homeAdapterSpy.mFavoriteStationList.size());
        verify(homeAdapterSpy, times(1)).addFavorite(distanceStop, true);
        verify(homeAdapterSpy, times(1)).updateIndex();
    }

    @Test
    public void setHasLocationPermission_should_work() {
        final HomeAdapter.HomeAdapterEventListener mockListener = mock(HomeAdapter.HomeAdapterEventListener.class);
        final HomeAdapter homeAdapter = new HomeAdapter(mockListener);
        final HomeAdapter homeAdapterSpy = spy(homeAdapter);
        doNothing().when(homeAdapterSpy).updateIndex();
        homeAdapterSpy.setHasLocationPermission(true);
        assertTrue(homeAdapterSpy.mHasLocationpermission);
        homeAdapterSpy.setHasLocationPermission(false);
        assertFalse(homeAdapterSpy.mHasLocationpermission);
        verify(homeAdapterSpy, times(2)).updateIndex();
    }

    @Test
    public void updateIndex_should_work_with_location_permission_false() {
        final HomeAdapter.HomeAdapterEventListener mockListener = mock(HomeAdapter.HomeAdapterEventListener.class);
        final HomeAdapter homeAdapter = new HomeAdapter(mockListener);
        final HomeAdapter homeAdapterSpy = spy(homeAdapter);
        homeAdapter.setHasLocationPermission(false);
        homeAdapter.updateIndex();
        assertEquals(4, homeAdapter.mListItems.size());
        assertEquals(HomeAdapter.TYPE_TITLE, homeAdapter.mListItems.get(0).type);
        assertEquals(R.string.favorites, homeAdapter.mListItems.get(0).tag);
        assertEquals(HomeAdapter.TYPE_FAVORITE_INFO, homeAdapter.mListItems.get(1).type);
        assertEquals(HomeAdapter.TYPE_TITLE, homeAdapter.mListItems.get(2).type);
        assertEquals(R.string.nearby, homeAdapter.mListItems.get(2).tag);
        assertEquals(HomeAdapter.TYPE_NEARBY_STOP_INFO, homeAdapter.mListItems.get(3).type);
    }

    @Test
    public void updateIndex_should_work_with_location_permission_true() {
        final HomeAdapter.HomeAdapterEventListener mockListener = mock(HomeAdapter.HomeAdapterEventListener.class);
        final HomeAdapter homeAdapter = new HomeAdapter(mockListener);
        final HomeAdapter homeAdapterSpy = spy(homeAdapter);
        homeAdapter.setHasLocationPermission(true);
        homeAdapter.updateIndex();
        assertEquals(4, homeAdapter.mListItems.size());
        assertEquals(HomeAdapter.TYPE_TITLE, homeAdapter.mListItems.get(0).type);
        assertEquals(R.string.favorites, homeAdapter.mListItems.get(0).tag);
        assertEquals(HomeAdapter.TYPE_FAVORITE_INFO, homeAdapter.mListItems.get(1).type);
        assertEquals(HomeAdapter.TYPE_TITLE, homeAdapter.mListItems.get(2).type);
        assertEquals(R.string.nearby, homeAdapter.mListItems.get(2).tag);
        assertEquals(HomeAdapter.TYPE_TEXT_SINGLE_LINE, homeAdapter.mListItems.get(3).type);
        assertEquals(R.string.no_nearby_stops_found_yet, homeAdapter.mListItems.get(3).tag);
        //
        final List<HomeAdapter.DistanceStop> itemList1 = createStopList(10);
        final List<HomeAdapter.DistanceStop> itemList2 = createStopList(5);
        homeAdapter.mFavoriteStationList.addAll(itemList1);
        homeAdapter.mNearbyStopList.addAll(itemList2);
        homeAdapter.updateIndex();
        assertEquals(17, homeAdapter.mListItems.size());
        assertEquals(HomeAdapter.TYPE_TITLE, homeAdapter.mListItems.get(0).type);
        assertEquals(R.string.favorites, homeAdapter.mListItems.get(0).tag);
        //check favorites
        for (int i = 0; i < 10; i++) {
            assertEquals("expected " + i + " to be of type STOP", HomeAdapter.TYPE_STOP, homeAdapter.mListItems.get(i + 1).type);
            assertEquals(itemList1.get(i), homeAdapter.mListItems.get(i + 1).tag);
        }
        assertEquals(HomeAdapter.TYPE_TITLE, homeAdapter.mListItems.get(11).type);
        assertEquals(R.string.nearby, homeAdapter.mListItems.get(11).tag);
        //check favorites
        for (int i = 0; i < 5; i++) {
            assertEquals("expected " + i + " to be of type STOP", HomeAdapter.TYPE_STOP, homeAdapter.mListItems.get(i + 12).type);
            assertEquals(itemList2.get(i), homeAdapter.mListItems.get(i + 12).tag);
        }
    }
}

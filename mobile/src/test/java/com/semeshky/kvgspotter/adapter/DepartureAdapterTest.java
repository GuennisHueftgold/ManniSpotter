package com.semeshky.kvgspotter.adapter;

import android.content.Context;

import com.github.guennishueftgold.trapezeapi.Departure;
import com.github.guennishueftgold.trapezeapi.DepartureStatus;
import com.semeshky.kvgspotter.BuildConfig;

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
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DepartureAdapterTest {

    private Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void setItems_should_sort_the_items_in_advance() {
        DepartureAdapter.Presenter presenter = mock(DepartureAdapter.Presenter.class);
        DepartureAdapter adapter = new DepartureAdapter(presenter);

/*        RecyclerView rvParent = new RecyclerView(context);
        rvParent.setLayoutManager(new LinearLayoutManager(context));
        rvParent.setAdapter(adapter);
  */
        final List<Departure> departureList = new ArrayList<>();
        Departure dep1 = DepartureComparatorTest
                .createDeparture(DepartureStatus.STATUS_PLANNED, null, null);
        Departure dep2 = DepartureComparatorTest
                .createDeparture(DepartureStatus.STATUS_STOPPING, null, null);
        Departure dep3 = DepartureComparatorTest
                .createDeparture(DepartureStatus.STATUS_DEPARTED, null, null);
        departureList.add(dep1);
        departureList.add(dep2);
        departureList.add(dep3);
        adapter.setItems(departureList);
        assertEquals(3, departureList.size());
        assertEquals(dep3, adapter.getItem(0));
        assertEquals(dep2, adapter.getItem(1));
        assertEquals(dep1, adapter.getItem(2));
    }

    @Test
    public void getItemId_should_return_the_correct_id() {
        DepartureAdapter.Presenter presenter = mock(DepartureAdapter.Presenter.class);
        DepartureAdapter adapter = new DepartureAdapter(presenter);
        final List<Departure> departureList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            departureList.add(new Departure.Builder()
                    .setTripId("" + i)
                    .setActualRelativeTime(i)
                    .build());
        }
        adapter.setItems(departureList);
        for (int i = 0; i < departureList.size(); i++) {
            assertEquals(Long.parseLong(adapter.getItem(i).getTripId()), adapter.getItemId(i));
        }
    }

    @Test
    public void areItemsTheSame_should_return_correct_values() {
        DepartureAdapter.Presenter presenter = mock(DepartureAdapter.Presenter.class);
        DepartureAdapter adapter = new DepartureAdapter(presenter);
        Departure dep1 = new Departure.Builder()
                .setTripId("trip1")
                .setRouteId("route1")
                .build();
        Departure dep2 = new Departure.Builder()
                .setTripId("trip2")
                .setRouteId("route1")
                .build();
        Departure dep3 = new Departure.Builder()
                .setTripId("trip1")
                .setRouteId("route2")
                .build();
        Departure dep4 = new Departure.Builder()
                .setTripId("trip2")
                .setRouteId("route2")
                .build();
        assertTrue(adapter.areItemsTheSame(dep1, dep1));
        assertFalse(adapter.areItemsTheSame(dep1, dep2));
        assertFalse(adapter.areItemsTheSame(dep1, dep3));
        assertFalse(adapter.areItemsTheSame(dep1, dep4));
    }
}

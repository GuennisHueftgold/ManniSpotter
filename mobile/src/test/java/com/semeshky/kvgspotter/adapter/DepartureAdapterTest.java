package com.semeshky.kvgspotter.adapter;

import android.content.Context;
import android.view.View;

import com.github.guennishueftgold.trapezeapi.Departure;
import com.github.guennishueftgold.trapezeapi.DepartureStatus;
import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.databinding.VhStationDepartureBinding;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    public void bind_should_set_departed_correctly() {
        DepartureAdapter.Presenter presenter = mock(DepartureAdapter.Presenter.class);
        VhStationDepartureBinding mockBinding = mock(VhStationDepartureBinding.class);
        View mockView = mock(View.class);
        when(mockView.getContext()).thenReturn(context);
        when(mockBinding.getRoot()).thenReturn(mockView);
        DepartureAdapter adapter = new DepartureAdapter(presenter);
        Departure departure = new Departure.Builder()
                .setTripId("trip1")
                .setRouteId("route1")
                .setStatus(DepartureStatus.STATUS_DEPARTED)
                .build();
        adapter.bind(mockBinding, departure, Collections.emptyList());
        verify(mockBinding, times(1))
                .setSecondaryTextVisible(true);
        verify(mockBinding, times(1))
                .setSecondaryTextAlert(false);
        verify(mockBinding, times(1))
                .setActive(false);
        verify(mockBinding, times(1))
                .setSecondaryText(context.getResources().getString(R.string.departed));
        //Check callcount
        verify(mockBinding, times(1))
                .setActive(any(Boolean.class));
        verify(mockBinding, times(1))
                .setSecondaryTextVisible(any(Boolean.class));
        verify(mockBinding, times(1))
                .setSecondaryTextAlert(any(Boolean.class));
        verify(mockBinding, times(1))
                .setSecondaryText(any(String.class));
    }

    @Test
    public void bind_should_set_stopping_correctly() {
        DepartureAdapter.Presenter presenter = mock(DepartureAdapter.Presenter.class);
        VhStationDepartureBinding mockBinding = mock(VhStationDepartureBinding.class);
        View mockView = mock(View.class);
        when(mockView.getContext()).thenReturn(context);
        when(mockBinding.getRoot()).thenReturn(mockView);
        DepartureAdapter adapter = new DepartureAdapter(presenter);
        Departure departure = new Departure.Builder()
                .setTripId("trip1")
                .setRouteId("route1")
                .setStatus(DepartureStatus.STATUS_STOPPING)
                .build();
        adapter.bind(mockBinding, departure, Collections.emptyList());
        verify(mockBinding, times(1))
                .setSecondaryTextVisible(true);
        verify(mockBinding, times(1))
                .setSecondaryTextAlert(false);
        verify(mockBinding, times(1))
                .setActive(true);
        verify(mockBinding, times(1))
                .setSecondaryText(context.getResources().getString(R.string.stopping));
        //Check callcount
        verify(mockBinding, times(1))
                .setActive(any(Boolean.class));
        verify(mockBinding, times(1))
                .setSecondaryTextVisible(any(Boolean.class));
        verify(mockBinding, times(1))
                .setSecondaryTextAlert(any(Boolean.class));
        verify(mockBinding, times(1))
                .setSecondaryText(any(String.class));
    }
}

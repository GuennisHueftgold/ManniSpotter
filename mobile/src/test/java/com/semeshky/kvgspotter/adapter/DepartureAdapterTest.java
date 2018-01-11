package com.semeshky.kvgspotter.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

        RecyclerView rvParent = new RecyclerView(context);
        rvParent.setLayoutManager(new LinearLayoutManager(context));
        rvParent.setAdapter(adapter);
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
}

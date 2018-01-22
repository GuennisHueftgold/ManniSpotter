package com.semeshky.kvgspotter.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.guennishueftgold.trapezeapi.Departure;
import com.github.guennishueftgold.trapezeapi.Station;
import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.adapter.DepartureAdapter;
import com.semeshky.kvgspotter.databinding.FragmentStationDeparturesBinding;
import com.semeshky.kvgspotter.viewmodel.StationDetailActivityViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startVisibleFragment;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class StationDeparturesFragmentTest {
    @Before
    public void setup() {

    }

    @Test
    public void shouldNotBeNull() throws Exception {
        StationDeparturesFragment fragment = new StationDeparturesFragment();
        startVisibleFragment(fragment, StationDeparturesTestActivity.class, 1);
    }

    @Test
    public void updateViews_with_null_should_not_execute_update() throws Exception {
        StationDeparturesFragment fragment = new StationDeparturesFragment();
        startVisibleFragment(fragment, StationDeparturesTestActivity.class, 1);
        final FragmentStationDeparturesBinding departuresBinding = fragment.mBinding;
        final FragmentStationDeparturesBinding departuresBindingSpy = spy(departuresBinding);
        fragment.mBinding = departuresBindingSpy;
        final DepartureAdapter departureAdapter = mock(DepartureAdapter.class);
        fragment.mDepartureAdapter = departureAdapter;
        fragment.updateViews(null);
        verify(departureAdapter, never()).setItems(ArgumentMatchers.<Departure>anyList());
        verify(departuresBindingSpy, never()).setDepartureCount(anyInt());
        verify(departuresBindingSpy, never()).executePendingBindings();
    }

    @Test
    public void updateViews_with_null_should_execute_update() throws Exception {
        StationDeparturesFragment fragment = new StationDeparturesFragment();
        startVisibleFragment(fragment, StationDeparturesTestActivity.class, 1);
        final FragmentStationDeparturesBinding departuresBinding = fragment.mBinding;
        final FragmentStationDeparturesBinding departuresBindingSpy = spy(departuresBinding);
        fragment.mBinding = departuresBindingSpy;
        final DepartureAdapter departureAdapter = mock(DepartureAdapter.class);
        fragment.mDepartureAdapter = departureAdapter;
        List<Departure> departuresOld = new ArrayList<>();
        List<Departure> departuresNew = new ArrayList<>();
        departuresOld.add(new Departure.Builder().setTripId("id_1").build());
        departuresOld.add(new Departure.Builder().setTripId("id_2").build());
        departuresOld.add(new Departure.Builder().setTripId("id_3").build());
        departuresOld.add(new Departure.Builder().setTripId("id_4").build());
        departuresNew.add(new Departure.Builder().setTripId("id_5").build());
        departuresNew.add(new Departure.Builder().setTripId("id_6").build());
        departuresNew.add(new Departure.Builder().setTripId("id_7").build());
        List<Departure> combinedDepartures = new ArrayList<>();
        combinedDepartures.addAll(departuresNew);
        combinedDepartures.addAll(departuresOld);
        Station station = new Station.Builder()
                .setActual(departuresNew)
                .setOld(departuresOld)
                .build();
        fragment.updateViews(station);
        verify(departureAdapter, times(1)).setItems(combinedDepartures);
        verify(departuresBindingSpy, times(1)).setDepartureCount(combinedDepartures.size());
        verify(departuresBindingSpy, times(1)).executePendingBindings();
    }

    @Test
    public void binding_in_layout_progress_bar_should_show_correctly() throws Exception {
        StationDeparturesFragment fragment = new StationDeparturesFragment();
        startVisibleFragment(fragment, StationDeparturesTestActivity.class, 1);
        ProgressBar progressBar = fragment.getView().findViewById(R.id.progressBar);
        assertNotNull(progressBar);
        fragment.mViewModel.isRefreshing.set(true);
        fragment.mBinding.setDepartureCount(0);
        fragment.mBinding.executePendingBindings();
        assertEquals("should be visible", View.VISIBLE, progressBar.getVisibility());
        fragment.mViewModel.isRefreshing.set(true);
        fragment.mBinding.setDepartureCount(10);
        fragment.mBinding.executePendingBindings();
        assertEquals("should be not visible", View.GONE, progressBar.getVisibility());
        fragment.mViewModel.isRefreshing.set(false);
        fragment.mBinding.setDepartureCount(0);
        fragment.mBinding.executePendingBindings();
        assertEquals("should be not visible", View.GONE, progressBar.getVisibility());
        fragment.mViewModel.isRefreshing.set(false);
        fragment.mBinding.setDepartureCount(10);
        fragment.mBinding.executePendingBindings();
        assertEquals("should be not visible", View.GONE, progressBar.getVisibility());
    }
}


class StationDeparturesTestActivity extends AppCompatActivity {
    StationDetailActivityViewModel mViewModel;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setTheme(R.style.AppTheme);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(1);
        setContentView(frameLayout);
        this.mViewModel = ViewModelProviders.of(this)
                .get(StationDetailActivityViewModel.class);
    }
}
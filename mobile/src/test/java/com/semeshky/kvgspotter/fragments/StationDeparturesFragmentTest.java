package com.semeshky.kvgspotter.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.github.guennishueftgold.trapezeapi.Departure;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
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
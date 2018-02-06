package com.semeshky.kvgspotter.activities;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.fragments.StationDeparturesFragment;
import com.semeshky.kvgspotter.fragments.StationDetailsFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class StationDetailPagerAdapterTest {
    private Context mContext;
    private StationDetailPagerAdapter mAdapter;

    @Before
    public void setup() {
        this.mContext = RuntimeEnvironment.application;

        FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class)
                .create()
                .start()
                .resume()
                .get();

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        this.mAdapter = new StationDetailPagerAdapter(fragmentManager, this.mContext);
    }

    @Test
    public void getCount_should_return_correct_number() {
        assertEquals(2, this.mAdapter.getCount());
    }

    @Test
    public void getPageTitle_should_return_correct_name() {
        assertEquals(this.mContext.getString(R.string.departures), this.mAdapter.getPageTitle(0));
        assertEquals(this.mContext.getString(R.string.map), this.mAdapter.getPageTitle(1));
    }

    @Test
    public void getItem_should_return_correct_fragment() {
        assertThat(this.mAdapter.getItem(0), instanceOf(StationDeparturesFragment.class));
        assertThat(this.mAdapter.getItem(1), instanceOf(StationDetailsFragment.class));
    }
}

package com.semeshky.kvgspotter.activities;

import android.support.v4.app.Fragment;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

@Implements(StationDetailPagerAdapter.class)
public class ShadowStationDetailAdapter {
    @Implementation
    public Fragment getItem(int position) {
        return new Fragment();
    }

    @Implementation
    public int getCount() {
        return 2;
    }

    @Implementation
    public CharSequence getPageTitle(int position) {
        return "Title_" + position;
    }
}

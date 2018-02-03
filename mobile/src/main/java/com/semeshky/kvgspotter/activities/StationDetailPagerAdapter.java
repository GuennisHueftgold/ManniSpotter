package com.semeshky.kvgspotter.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.fragments.StationDeparturesFragment;
import com.semeshky.kvgspotter.fragments.StationDetailsFragment;


/**
 * Title pager adapter for StationDetailActivity if no dual pane layout is used
 */
class StationDetailPagerAdapter extends FragmentPagerAdapter {

    private final String mDepartureTitle;
    private final String mMapTitle;

    StationDetailPagerAdapter(FragmentManager fm, @NonNull Context context) {
        super(fm);
        this.mDepartureTitle = context.getString(R.string.departures);
        this.mMapTitle = context.getString(R.string.map);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new StationDeparturesFragment();
            case 1:
                return new StationDetailsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return this.mDepartureTitle;
            case 1:
                return this.mMapTitle;
            default:
                return null;
        }
    }
}
package com.semeshky.kvgspotter.activities;

import android.support.v4.view.PagerAdapter;

import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadow.api.Shadow;

@Implements(PagerAdapter.class)
public class ShadowPagerAdapter {

    @RealObject
    PagerAdapter mPagerAdapter;
    private int mNotifyDataSetChangedCalls = 0;

    public static ShadowPagerAdapter shadowOf(PagerAdapter pagerAdapter) {
        return Shadow.extract(pagerAdapter);
    }

    public void reset() {
        this.mNotifyDataSetChangedCalls = 0;
    }

    public void notifyDataSetChanged() {
        this.mNotifyDataSetChangedCalls++;
        Shadow.directlyOn(this.mPagerAdapter, PagerAdapter.class).notifyDataSetChanged();
    }

    public int notifyDataSetChangedCallCount() {
        return this.mNotifyDataSetChangedCalls;
    }
}

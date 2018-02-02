package com.semeshky.kvgspotter.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadow.api.Shadow;

import java.util.ArrayList;
import java.util.List;

@Implements(SplashFragmentAdapter.class)
public class ShadowSplashFragmentAdapter {

    @RealObject
    private SplashFragmentAdapter realAdapter;
    private List<Integer> mGetItemArgs = new ArrayList<>();
    private int mCount = -1;

    public static ShadowSplashFragmentAdapter shadowOf(SplashFragmentAdapter pagerAdapter) {
        return Shadow.extract(pagerAdapter);
    }

    @Implementation
    public Fragment getItem(int position) {
        this.mGetItemArgs.add(position);
        return new Fragment();
    }

    @Implementation
    public int getCount() {
        if (this.mCount < 0) {
            return Shadow.directlyOn(realAdapter, SplashFragmentAdapter.class).getCount();
        } else {
            return this.mCount;
        }
    }

    /**
     * Set the count to be returned by the adapter. Set it to a value lower than 0 to call the original implementation
     *
     * @param count
     */
    public void setCount(int count) {
        this.mCount = count;
        Shadow.directlyOn(realAdapter, PagerAdapter.class).notifyDataSetChanged();
    }

    public int getGetItemArg(int callNum) {
        return this.mGetItemArgs.get(callNum);
    }

    public int getItemCallCount() {
        return this.mGetItemArgs.size();
    }
}

package com.semeshky.kvgspotter.activities;

import android.support.v4.app.Fragment;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import java.util.ArrayList;
import java.util.List;

@Implements(SplashFragmentAdapter.class)
public class ShadowSplashFragmentAdapter {

    private List<Integer> mGetItemArgs = new ArrayList<>();

    @Implementation
    public Fragment getItem(int position) {
        this.mGetItemArgs.add(position);
        return null;
    }

    public int getGetItemArg(int callNum) {
        return this.mGetItemArgs.get(callNum);
    }

    public int getItemCallCount() {
        return this.mGetItemArgs.size();
    }
}

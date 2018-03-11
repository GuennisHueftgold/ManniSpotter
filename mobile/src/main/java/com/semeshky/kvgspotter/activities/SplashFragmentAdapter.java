package com.semeshky.kvgspotter.activities;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.semeshky.kvgspotter.fragments.SetupStep1Fragment;
import com.semeshky.kvgspotter.fragments.SetupStep2Fragment;
import com.semeshky.kvgspotter.fragments.SetupStep3Fragment;
import com.semeshky.kvgspotter.fragments.SetupStep4Fragment;

class SplashFragmentAdapter extends FragmentPagerAdapter {

    protected final boolean mRequiresAskingForLocation = Build.VERSION.SDK_INT >= 23;
    protected boolean mAllowAdvance = false;

    public SplashFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setAllowAdvance(boolean allowAdvance) {
        if (this.mAllowAdvance == allowAdvance)
            return;
        this.mAllowAdvance = allowAdvance;
        this.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SetupStep1Fragment();
            case 1:
                return new SetupStep2Fragment();
            case 2:
                if (this.mRequiresAskingForLocation) {
                    return new SetupStep3Fragment();
                } else {
                    return new SetupStep4Fragment();
                }
            case 3:
                if (this.mRequiresAskingForLocation) {
                    return new SetupStep4Fragment();
                }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.mAllowAdvance ? (this.mRequiresAskingForLocation ? 4 : 3) : 2;
    }
}
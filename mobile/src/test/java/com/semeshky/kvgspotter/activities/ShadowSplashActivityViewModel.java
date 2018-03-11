package com.semeshky.kvgspotter.activities;

import com.semeshky.kvgspotter.viewmodel.SplashActivityViewModel;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

@Implements(SplashActivityViewModel.class)
public class ShadowSplashActivityViewModel {

    private boolean mSynchronized = false;

    public static ShadowSplashActivityViewModel shadowOf(SplashActivityViewModel model) {
        return Shadow.extract(model);
    }

    @Implementation
    public boolean isSynchronized() {
        return this.mSynchronized;
    }

    public void setSynchronized(boolean aSynchronized) {
        this.mSynchronized = aSynchronized;
    }
}

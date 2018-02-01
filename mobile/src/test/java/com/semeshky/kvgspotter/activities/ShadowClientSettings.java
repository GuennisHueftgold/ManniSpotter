package com.semeshky.kvgspotter.activities;

import com.semeshky.kvgspotter.settings.ClientSettings;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

@Implements(ClientSettings.class)
public class ShadowClientSettings {

    private boolean mSetupIsDone = false;

    @Implementation()
    public boolean isFirstSetupDone() {
        return this.mSetupIsDone;
    }

    public void setFirstSetup(boolean state) {
        this.mSetupIsDone = state;
    }
}

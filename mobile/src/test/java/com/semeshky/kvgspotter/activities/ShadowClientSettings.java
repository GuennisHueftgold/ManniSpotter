package com.semeshky.kvgspotter.activities;

import com.semeshky.kvgspotter.settings.ClientSettings;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

@Implements(ClientSettings.class)
public class ShadowClientSettings {

    private boolean mSetupIsDone = false;

    public static ShadowClientSettings shadowOf(ClientSettings clientSettings) {
        return Shadow.extract(clientSettings);
    }

    @Implementation()
    public boolean isFirstSetupDone() {
        return this.mSetupIsDone;
    }

    public void setFirstSetup(boolean state) {
        this.mSetupIsDone = state;
    }
}

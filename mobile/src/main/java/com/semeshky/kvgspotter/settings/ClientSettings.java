package com.semeshky.kvgspotter.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public final class ClientSettings {
    final static String NAME = "client_settings";
    static final String KEY_LAST_UPDATE_NOTICE_TIMESTAMP = "last_update_notice_timestamp";
    static final String KEY_FIRST_SETUP_DONE = "first_setup_done";
    protected static ClientSettings sClientSettings;
    private final SharedPreferences mSharedPreferences;

    ClientSettings(Context context) {
        this.mSharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static ClientSettings getInstance(@NonNull Context context) {
        if (sClientSettings == null) {
            sClientSettings = new ClientSettings(context.getApplicationContext());
        }
        return sClientSettings;
    }

    public long getLastUpdateNoticeTimestamp() {
        return this.mSharedPreferences.getLong(KEY_LAST_UPDATE_NOTICE_TIMESTAMP, 0);
    }

    public void setLastUpdateNoticeTimestamp(long timestamp) {
        SharedPreferences.Editor editor = this.mSharedPreferences.edit();
        editor.putLong(KEY_LAST_UPDATE_NOTICE_TIMESTAMP, timestamp);
        editor.commit();
    }

    public boolean isFirstSetupDone() {
        return this.mSharedPreferences.getBoolean(KEY_FIRST_SETUP_DONE, true);
    }

    public void setFirstSetup(boolean firstSetupDone) {
        SharedPreferences.Editor editor = this.mSharedPreferences.edit();
        editor.putBoolean(KEY_FIRST_SETUP_DONE, firstSetupDone);
        editor.commit();
    }
}

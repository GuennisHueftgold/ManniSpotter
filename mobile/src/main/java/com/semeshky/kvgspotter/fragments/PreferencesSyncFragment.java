package com.semeshky.kvgspotter.fragments;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.database.AppDatabase;

public final class PreferencesSyncFragment extends BasePreferenceFragment {
    private static final CharSequence KEY_STOP_PREFERENCE = "key_stop_preference";
    private static final CharSequence KEY_STOP_POINT_PREFERENCE = "key_stop_point_preference";
    private Preference mStopPointPreference;
    private Preference mStopPreference;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.preferences_sync);
        this.mStopPreference = this.findPreference(KEY_STOP_PREFERENCE);
        this.mStopPointPreference = this.findPreference(KEY_STOP_POINT_PREFERENCE);
        AppDatabase
                .getInstance()
                .stopPointDao()
                .countStopsSync()
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer stopsCount) {
                        if (stopsCount == null) {
                            return;
                        }
                        mStopPointPreference.setSummary(getResources().getString(R.string.x_stop_points_synchronized, stopsCount));
                    }
                });
        AppDatabase
                .getInstance()
                .stopDao()
                .countStopsSync()
                .observe(this, new Observer<Integer>() {
                    @Override
                    public void onChanged(@Nullable Integer stopsCount) {
                        if (stopsCount == null) {
                            return;
                        }
                        mStopPreference.setSummary(getResources().getString(R.string.x_stops_synchronized, stopsCount));
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
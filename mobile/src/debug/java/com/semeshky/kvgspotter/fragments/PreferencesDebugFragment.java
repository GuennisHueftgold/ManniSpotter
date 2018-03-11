package com.semeshky.kvgspotter.fragments;

import android.os.Bundle;

import com.semeshky.kvgspotter.R;

public class PreferencesDebugFragment extends BasePreferenceFragment {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.preferences_debug_sync);
    }
}
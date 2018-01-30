package com.semeshky.kvgspotter.fragments;

import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.location.LocationHelper;

public final class PreferencesMainFragment extends BasePreferenceFragment {
    private final static String KEY_LOCATION_PERMISSION = "key_location_permission";
    private static final CharSequence KEY_APP_VERSION = "key_app_version";
    private Preference mPreferenceAppVersion;
    private CheckBoxPreference mPreferenceLocationPermission;
    private Preference.OnPreferenceChangeListener mOnPreferenceChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            switch (preference.getKey()) {
                case KEY_LOCATION_PERMISSION:
                    final boolean newState = (boolean) newValue;
                    return newState == isLocationPermissionGranted();
                default:
                    return false;
            }
        }
    };

    private boolean isLocationPermissionGranted() {
        return this.getContext() != null && LocationHelper.hasLocationPermission(getContext());
    }

    private void updateLocationPreference(CheckBoxPreference preference) {
        if (this.getContext() != null) {
            final boolean locationEnabled = LocationHelper.hasLocationPermission(getContext());
            if (preference.isChecked() != locationEnabled) {
                preference.setChecked(locationEnabled);
            }
        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.preferences_main);
        if (BuildConfig.DEBUG) {
            addPreferencesFromResource(R.xml.preferences_main_debug);
        }
        this.mPreferenceLocationPermission = (CheckBoxPreference) this.findPreference(KEY_LOCATION_PERMISSION);
        this.mPreferenceAppVersion = this.findPreference(KEY_APP_VERSION);
        this.mPreferenceLocationPermission.setOnPreferenceChangeListener(this.mOnPreferenceChangeListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mPreferenceAppVersion.setSummary(BuildConfig.VERSION_NAME);
        this.updateLocationPreference(this.mPreferenceLocationPermission);
    }
}
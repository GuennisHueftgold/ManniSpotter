package com.semeshky.kvgspotter.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.fragments.BasePreferenceFragment;
import com.semeshky.kvgspotter.fragments.PreferencesMainFragment;

import java.lang.reflect.Constructor;

import timber.log.Timber;

public class PreferencesActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {
    final static String TAG_MAIN_FRAGMENT = "mainFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_preferences);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);
        if (fragment == null) {
            fragment = new PreferencesMainFragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFragment, fragment, TAG_MAIN_FRAGMENT);
        ft.commit();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        if (pref.getFragment() == null) {
            return false;
        }
        try {
            Class<?> clazz = Class.<PreferenceFragmentCompat>forName(pref.getFragment());
            Constructor<?> ctor = clazz.getConstructor();
            BasePreferenceFragment fragmentCompat = (BasePreferenceFragment) ctor.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFragment, fragmentCompat, fragmentCompat.getTagName());
            ft.addToBackStack(fragmentCompat.getTagName());
            ft.commit();
            Timber.d("onPreferenceStartFragment - %s - %s", fragmentCompat.getTag(), fragmentCompat.getTagName());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

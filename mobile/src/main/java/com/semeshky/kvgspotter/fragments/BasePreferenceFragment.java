package com.semeshky.kvgspotter.fragments;

import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceFragmentCompat;

public abstract class BasePreferenceFragment extends PreferenceFragmentCompat {

    @NonNull
    public final String getTagName() {
        return this.getClass().getName();
    }
}

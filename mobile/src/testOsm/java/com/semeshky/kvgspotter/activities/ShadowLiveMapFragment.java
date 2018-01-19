package com.semeshky.kvgspotter.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.fragments.LiveMapFragment;

import org.osmdroid.views.MapView;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

/**
 * Stub for tests of the activity without the fragment specific quirks like NullPointer
 * on ZoomController for the osm map view
 */
@Implements(LiveMapFragment.class)
public class ShadowLiveMapFragment  {
    @Implementation
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    @Implementation
    public void onViewCreated(View view, Bundle savedInstanceState) {
        throw new RuntimeException("JJJ");
    }
    @Implementation
    public void onMapReady(MapView map) {
    }
}

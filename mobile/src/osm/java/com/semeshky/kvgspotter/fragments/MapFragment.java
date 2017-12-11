package com.semeshky.kvgspotter.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

import timber.log.Timber;

public abstract class MapFragment extends Fragment {
    protected final static String KEY_MAPVIEW_BUNDLE = "mapview_bundle";
    private MapView mMapView;

    protected MapView getGoogleMap() {
        return mMapView;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(KEY_MAPVIEW_BUNDLE);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(KEY_MAPVIEW_BUNDLE, mapViewBundle);
        }
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mMapView = view.findViewById(R.id.mapView);
        this.onMapReady(this.mMapView);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public abstract void onMapReady(MapView map);


}

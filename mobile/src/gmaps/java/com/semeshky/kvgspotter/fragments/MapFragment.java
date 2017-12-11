package com.semeshky.kvgspotter.fragments;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.semeshky.kvgspotter.R;

public abstract class MapFragment extends Fragment implements OnMapReadyCallback {
    protected final static String KEY_MAPVIEW_BUNDLE = "mapview_bundle";
    protected MapView mMapView;
    protected GoogleMap mGoogleMap;

    protected GoogleMap getGoogleMap() {
        return mGoogleMap;
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
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mMapView = view.findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(KEY_MAPVIEW_BUNDLE);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @CallSuper
    @Override
    public void onMapReady(GoogleMap map) {
        this.mGoogleMap = map;
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}

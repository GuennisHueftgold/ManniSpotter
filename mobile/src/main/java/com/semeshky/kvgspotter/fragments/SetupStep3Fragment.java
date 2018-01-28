package com.semeshky.kvgspotter.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semeshky.kvgspotter.R;
import com.semeshky.kvgspotter.location.LocationHelper;

/**
 * Select Favorite
 */
public class SetupStep3Fragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 3892;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnGrantAccess:
                    askForPermission();
                    break;
                default:
                    break;
            }
        }
    };
    private ConstraintLayout mConstraintLayout;

    private void askForPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        }
    }

    protected void updateStatusViews() {
        updateStatusViews(LocationHelper.hasLocationPermission(getContext()));
    }

    protected void updateStatusViews(boolean accessGranted) {
        TransitionManager.beginDelayedTransition(this.mConstraintLayout);
        getView()
                .findViewById(R.id.btnGrantAccess)
                .setVisibility(accessGranted ? View.GONE : View.VISIBLE);
        getView()
                .findViewById(R.id.txtStatus)
                .setVisibility(accessGranted ? View.VISIBLE : View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup_step_3, container, false);
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.mConstraintLayout = view.findViewById(R.id.constraintLayout);
        view.findViewById(R.id.btnGrantAccess)
                .setOnClickListener(this.mOnClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.updateStatusViews();
    }
}

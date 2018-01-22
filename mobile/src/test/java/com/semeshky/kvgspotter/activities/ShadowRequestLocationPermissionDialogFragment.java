package com.semeshky.kvgspotter.activities;

import com.semeshky.kvgspotter.fragments.RequestLocationPermissionDialogFragment;

import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadow.api.Shadow;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"UnusedDeclaration", "Unchecked"})
@Implements(RequestLocationPermissionDialogFragment.class)
public class ShadowRequestLocationPermissionDialogFragment {

    private static List<RequestLocationPermissionDialogFragment> sInstances = new ArrayList<>();
    @RealObject
    protected RequestLocationPermissionDialogFragment fragment;
    protected RequestLocationPermissionDialogFragment.OnLocationRequestDialogListener mListener;

    public static int getCreatedInstancesCount() {
        return sInstances.size();
    }

    public static RequestLocationPermissionDialogFragment getLatestFragment() {
        if (sInstances.size() == 0) {
            return null;
        }
        return sInstances.get(sInstances.size() - 1);
    }

    public static ShadowRequestLocationPermissionDialogFragment shadowOf(RequestLocationPermissionDialogFragment fragment) {
        return Shadow.extract(fragment);
    }

    public void __constructor__() {
        sInstances.add(fragment);
    }

    public void setOnLocationRequestDialogListener(RequestLocationPermissionDialogFragment.OnLocationRequestDialogListener listener) {
        this.mListener = listener;
    }
}

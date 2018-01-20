package com.semeshky.kvgspotter.activities;

import android.app.Activity;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import org.robolectric.annotation.Implements;

@Implements(ActivityCompat.class)
public class ShadowActivityCompat {

    private static Activity sActivity;
    private static String[] sPermissions;
    private static int sRequestCode;
    private static int sRequestPermissionCallCount = 0;

    public static void requestPermissions(final @NonNull Activity activity,
                                          final @NonNull String[] permissions,
                                          final @IntRange(from = 0) int requestCode) {
        sActivity = activity;
        sPermissions = permissions;
        sRequestCode = requestCode;
        sRequestPermissionCallCount++;
    }

    public static Activity getActivity() {
        return sActivity;
    }

    public static String[] getPermissions() {
        return sPermissions;
    }

    public static int getRequestCode() {
        return sRequestCode;
    }

    public static int getRequestPermissionCallCount() {
        return sRequestPermissionCallCount;
    }
}

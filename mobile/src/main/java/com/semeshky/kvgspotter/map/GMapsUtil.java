package com.semeshky.kvgspotter.map;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.semeshky.kvg.kvgapi.KvgApiClient;
import com.semeshky.kvg.kvgapi.LatLngInterface;


public class GMapsUtil {

    private final static String MAPS_PACKAGE_NAME = "com.google.android.apps.maps";

    public static void openNavigationTo(Context context, double latitude, double longitude) {
        if (isPackageInstalled(MAPS_PACKAGE_NAME, context.getPackageManager())) {
            StringBuilder builder = new StringBuilder();
            builder.append("google.navigation:q=")
                    .append(latitude)
                    .append(",")
                    .append(longitude);
            Uri gmmIntentUri = Uri.parse(builder.toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("https://www.google.com/maps/dir/?api=1&destination")
                    .append(latitude)
                    .append(",")
                    .append(longitude);
            Uri gmmIntentUri = Uri.parse(builder.toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            context.startActivity(mapIntent);
        }
    }

    private static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void openNavigationTo(Context context, LatLngInterface latLngInterface) {
        GMapsUtil.openNavigationTo(context,
                latLngInterface.getLatitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT,
                latLngInterface.getLongitude() / KvgApiClient.COORDINATES_CONVERTION_CONSTANT);
    }
}

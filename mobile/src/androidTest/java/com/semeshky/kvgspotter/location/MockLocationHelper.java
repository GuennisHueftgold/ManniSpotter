package com.semeshky.kvgspotter.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class MockLocationHelper {
    public final static String PROVIDER_NAME = "mock_provider_name";
    private final LocationManager mLocationManager;
    private final String mProviderName;

    public MockLocationHelper(Context ctx) {
        this(ctx, PROVIDER_NAME);
    }

    public MockLocationHelper(Context ctx, String providerName) {
        this.mLocationManager = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);
        this.mProviderName = providerName;

        this.mLocationManager.addTestProvider(providerName, false, false, false, false, false,
                true, true, 0, 5);
        this.mLocationManager.setTestProviderEnabled(providerName, true);
    }

    public static Location createLocation(String providerName, double lat, double lon) {
        Location mockLocation = new Location(providerName);
        mockLocation.setLatitude(lat);
        mockLocation.setLongitude(lon);
        mockLocation.setAltitude(0);
        mockLocation.setTime(System.currentTimeMillis());
        return mockLocation;
    }

    /**
     * Creates a mock location that will be pushed
     *
     * @param lat latitude
     * @param lon longitude
     * @return the mock location being pushed
     */
    public Location pushLocation(double lat, double lon) {
        final Location mockLocation = MockLocationHelper.createLocation(this.mProviderName, lat, lon);
        this.mLocationManager.setTestProviderLocation(this.mProviderName, mockLocation);
        return mockLocation;
    }

    public void stop() {
        this.mLocationManager.removeTestProvider(this.mProviderName);
    }
}

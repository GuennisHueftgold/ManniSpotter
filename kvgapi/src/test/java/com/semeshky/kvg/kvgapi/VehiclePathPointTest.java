package com.semeshky.kvg.kvgapi;


import com.google.gson.TypeAdapter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VehiclePathPointTest {

    private final static String FULL_INFORMATION = "{\"lat\":29,\"lon\":42,\"seq\":\"2\"}";
    private final static String MISSING_INFORMATION = "{\"lon\":42,\"seq\":\"2\"}";
    private final static String WRONG_TYPE = "{\"lat\":\"29a\",\"lon\":42,\"seq\":\"2\"}";

    private final static TypeAdapter<VehiclePathPoint> adapter = new VehiclePathPoint.Converter();

    @Test
    public void typeadapter_read_full_information() throws Exception {
        VehiclePathPoint vehiclePathInfo = adapter.fromJson(FULL_INFORMATION);
        assertEquals(vehiclePathInfo.getLatitude(), 29);
        assertEquals(vehiclePathInfo.getLongitude(), 42);
        assertEquals(vehiclePathInfo.getSequence(), 2);
    }

    @Test(expected = NumberFormatException.class)
    public void typeadapter_read_wrong_type() throws Exception {
        VehiclePathPoint vehiclePathInfo = adapter.fromJson(WRONG_TYPE);
        assertEquals(vehiclePathInfo.getLatitude(), 29);
        assertEquals(vehiclePathInfo.getLongitude(), 42);
        assertEquals(vehiclePathInfo.getSequence(), 2);
    }

    @Test()
    public void typeadapter_read_missing_information() throws Exception {
        VehiclePathPoint vehiclePathInfo = adapter.fromJson(MISSING_INFORMATION);
        assertEquals(vehiclePathInfo.getLatitude(), 0);
        assertEquals(vehiclePathInfo.getLongitude(), 42);
        assertEquals(vehiclePathInfo.getSequence(), 2);
    }

    @Test()
    public void typeadapter_read_null_provided() throws Exception {
        VehiclePathPoint vehiclePathInfo = adapter.fromJson("null");
        assertNull(vehiclePathInfo);
    }

    @Test(expected = IllegalStateException.class)
    public void typeadapter_read_no_object() throws Exception {
        VehiclePathPoint vehiclePathInfo = adapter.fromJson("1234");
        assertNull(vehiclePathInfo);
    }

    @Test
    public void builder_setter_test() throws Exception {
        final int seqValue = 29, lonValue = 83, latValue = 392;
        VehiclePathPoint.Builder builder = new VehiclePathPoint.Builder();
        builder.setSequence(seqValue);
        builder.setLongitude(lonValue);
        builder.setLatitude(latValue);
        final VehiclePathPoint vehiclePathPoint = builder.build();
        assertEquals(vehiclePathPoint.getSequence(), seqValue);
        assertEquals(vehiclePathPoint.getLongitude(), lonValue);
        assertEquals(vehiclePathPoint.getLatitude(), latValue);
    }
}

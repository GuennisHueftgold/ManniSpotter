package com.semeshky.kvgspotter.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import com.semeshky.kvgspotter.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AbstractLocationHelperTest {
    private Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
    }

    @Test
    @Config(sdk = {22})
    public void hasLocationPermission_should_return_true_for_sub_23sdk_int() {
        final Context contextSpy = spy(context);
        when(contextSpy.checkPermission(anyString(), anyInt(), anyInt())).thenReturn(PackageManager.PERMISSION_DENIED);
        assertTrue(AbstractLocationHelper.hasLocationPermission(contextSpy));
        verify(contextSpy, never()).checkPermission(anyString(), anyInt(), anyInt());
    }

    @Test
    @Config(sdk = {23})
    public void hasLocationPermission_should_return_true_for_23sdk_int_and_above() {
        final Context contextSpy = spy(context);
        when(contextSpy.checkPermission(anyString(), anyInt(), anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED);
        assertTrue(AbstractLocationHelper.hasLocationPermission(contextSpy));
        verify(contextSpy, times(1)).checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(), anyInt());
        verify(contextSpy, times(1)).checkPermission(eq(Manifest.permission.ACCESS_COARSE_LOCATION), anyInt(), anyInt());
    }

    @Test
    @Config(sdk = {23})
    public void hasLocationPermission_should_return_false_for_23sdk_int_and_above() {
        final Context contextSpy = spy(context);
        when(contextSpy.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(), anyInt())).thenReturn(PackageManager.PERMISSION_DENIED);
        when(contextSpy.checkPermission(eq(Manifest.permission.ACCESS_COARSE_LOCATION), anyInt(), anyInt())).thenReturn(PackageManager.PERMISSION_DENIED);
        assertFalse(AbstractLocationHelper.hasLocationPermission(contextSpy));
        verify(contextSpy, times(1)).checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(), anyInt());
        verify(contextSpy, never()).checkPermission(eq(Manifest.permission.ACCESS_COARSE_LOCATION), anyInt(), anyInt());
        //
        reset(contextSpy);
        when(contextSpy.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(), anyInt())).thenReturn(PackageManager.PERMISSION_DENIED);
        when(contextSpy.checkPermission(eq(Manifest.permission.ACCESS_COARSE_LOCATION), anyInt(), anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED);
        assertFalse(AbstractLocationHelper.hasLocationPermission(contextSpy));
        verify(contextSpy, times(1)).checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(), anyInt());
        verify(contextSpy, never()).checkPermission(eq(Manifest.permission.ACCESS_COARSE_LOCATION), anyInt(), anyInt());
        //
        reset(contextSpy);
        when(contextSpy.checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(), anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED);
        when(contextSpy.checkPermission(eq(Manifest.permission.ACCESS_COARSE_LOCATION), anyInt(), anyInt())).thenReturn(PackageManager.PERMISSION_DENIED);
        assertFalse(AbstractLocationHelper.hasLocationPermission(contextSpy));
        verify(contextSpy, times(1)).checkPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION), anyInt(), anyInt());
        verify(contextSpy, times(1)).checkPermission(eq(Manifest.permission.ACCESS_COARSE_LOCATION), anyInt(), anyInt());
    }
}

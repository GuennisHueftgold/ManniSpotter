package com.semeshky.kvgspotter.map;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.test.mock.MockContext;

import com.github.guennishueftgold.trapezeapi.LatLngInterface;
import com.github.guennishueftgold.trapezeapi.TrapezeApiClient;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GmapsUtilTest {

    @Test()
    public void isPackageInstalled_should_return_true() throws PackageManager.NameNotFoundException {
        final String testPackageName = "test.package.name";
        PackageManager packageManager = mock(PackageManager.class);
        when(packageManager.getPackageInfo(testPackageName, 0)).thenReturn(null);
        assertTrue(GMapsUtil.isPackageInstalled(testPackageName, packageManager));
    }


    @Test()
    public void isPackageInstalled_should_return_false() throws PackageManager.NameNotFoundException {
        final String testPackageName = "test.package.name";
        PackageManager packageManager = mock(PackageManager.class);
        when(packageManager.getPackageInfo(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
                .thenThrow(new PackageManager.NameNotFoundException());
        assertFalse(GMapsUtil.isPackageInstalled(testPackageName, packageManager));
    }

    @Test
    public void openNavigationTo_should_call_correctly() throws PackageManager.NameNotFoundException {
        final MockContext mockContext = mock(MockContext.class);
        final PackageManager mockPackageManager = mock(PackageManager.class);
        when(mockPackageManager.getPackageInfo(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
                .thenThrow(new PackageManager.NameNotFoundException());
        when(mockContext.getPackageManager()).thenReturn(mockPackageManager);
        final LatLngInterface latLngInterface = new LatLngInterface() {
            @Override
            public long getLongitude() {
                return 10;
            }

            @Override
            public long getLatitude() {
                return 20;
            }
        };
        GMapsUtil.openNavigationTo(mockContext,
                latLngInterface);
        ArgumentCaptor<Intent> argument = ArgumentCaptor.forClass(Intent.class);
        verify(mockContext, times(1)).startActivity(argument.capture());
        final Intent intent = argument.getValue();
        assertEquals(intent.getAction(), Intent.ACTION_VIEW);
        StringBuilder stringBuilder = new StringBuilder()
                .append("https://www.google.com/maps/dir/?api=1&destination=")
                .append(latLngInterface.getLatitude() / TrapezeApiClient.COORDINATES_CONVERTION_CONSTANT)
                .append(",")
                .append(latLngInterface.getLongitude() / TrapezeApiClient.COORDINATES_CONVERTION_CONSTANT);
        assertEquals(Uri.parse(stringBuilder.toString()), intent.getData());
    }

    @Test
    public void openNavigationTo_should_call_correctly_to_open_gmaps() throws PackageManager.NameNotFoundException {
        final MockContext mockContext = mock(MockContext.class);
        final PackageManager mockPackageManager = mock(PackageManager.class);
        when(mockPackageManager.getPackageInfo(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
                .thenReturn(null);
        when(mockContext.getPackageManager()).thenReturn(mockPackageManager);
        final LatLngInterface latLngInterface = new LatLngInterface() {
            @Override
            public long getLongitude() {
                return 10;
            }

            @Override
            public long getLatitude() {
                return 20;
            }
        };
        GMapsUtil.openNavigationTo(mockContext,
                latLngInterface);
        ArgumentCaptor<Intent> argument = ArgumentCaptor.forClass(Intent.class);
        verify(mockContext, times(1)).startActivity(argument.capture());
        final Intent intent = argument.getValue();
        assertEquals(intent.getAction(), Intent.ACTION_VIEW);
        StringBuilder stringBuilder = new StringBuilder()
                .append("google.navigation:q=")
                .append(latLngInterface.getLatitude() / TrapezeApiClient.COORDINATES_CONVERTION_CONSTANT)
                .append(",")
                .append(latLngInterface.getLongitude() / TrapezeApiClient.COORDINATES_CONVERTION_CONSTANT);
        assertEquals("com.google.android.apps.maps", intent.getPackage());
        assertEquals(Uri.parse(stringBuilder.toString()), intent.getData());
    }
}

package com.semeshky.kvgspotter.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import io.reactivex.SingleEmitter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LocationSingleOnSubscribeTest {

    MockContext mockContext = mock(MockContext.class);
    LocationManager mockLocationManager = mock(LocationManager.class);
    Context context = InstrumentationRegistry.getTargetContext();

    //final MockLocationHelper mockLocationHelper=new MockLocationHelper(context);
    @Before
    public void createInstance() {
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSystemService(Context.LOCATION_SERVICE)).thenReturn(mockLocationManager);
    }

    @Test()
    public void getLastLocationSingle_should_succeed_for_gps_provider() throws Exception {
        LocationSingleOnSubscribe singleOnSubscribe = new LocationSingleOnSubscribe(mockLocationManager);
        final Location testLocation = MockLocationHelper.createLocation(LocationManager.GPS_PROVIDER, 10, 10);
        when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(testLocation);
        SingleEmitter<Location> emitter = mock(SingleEmitter.class);
        singleOnSubscribe.subscribe(emitter);
        ArgumentCaptor<Location> argument = ArgumentCaptor.forClass(Location.class);
        verify(emitter, times(1)).onSuccess(argument.capture());
        verify(emitter, never()).onError(ArgumentMatchers.<Throwable>any());
        assertEquals(testLocation, argument.getValue());
    }

    @Test()
    public void getLastLocationSingle_should_succeed_for_network_provider() throws Exception {
        LocationSingleOnSubscribe singleOnSubscribe = new LocationSingleOnSubscribe(mockLocationManager);
        final Location testLocation = MockLocationHelper.createLocation(LocationManager.GPS_PROVIDER, 10, 10);
        when(mockLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)).thenReturn(testLocation);
        SingleEmitter<Location> emitter = mock(SingleEmitter.class);
        singleOnSubscribe.subscribe(emitter);
        ArgumentCaptor<Location> argument = ArgumentCaptor.forClass(Location.class);
        verify(emitter, times(1)).onSuccess(argument.capture());
        verify(emitter, never()).onError(ArgumentMatchers.<Throwable>any());
        assertEquals(testLocation, argument.getValue());
    }

    @Test()
    public void getLastLocationSingle_should_report_error() throws Exception {
        LocationSingleOnSubscribe singleOnSubscribe = new LocationSingleOnSubscribe(mockLocationManager);
        when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenReturn(null);
        SingleEmitter<Location> emitter = mock(SingleEmitter.class);
        singleOnSubscribe.subscribe(emitter);
        verify(emitter, never()).onSuccess(ArgumentMatchers.<Location>any());
        verify(emitter, times(1)).onError(ArgumentMatchers.<Exception>any());
    }

    @Test()
    public void getLastLocationSingle_should_catch_security_exception() throws Exception {
        LocationSingleOnSubscribe singleOnSubscribe = new LocationSingleOnSubscribe(mockLocationManager);
        when(mockLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).thenThrow(new SecurityException());
        SingleEmitter<Location> emitter = mock(SingleEmitter.class);
        singleOnSubscribe.subscribe(emitter);
        verify(emitter, never()).onSuccess(ArgumentMatchers.<Location>any());
        verify(emitter, times(1)).onError(ArgumentMatchers.<SecurityException>any());
    }
}

package com.semeshky.kvgspotter.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.semeshky.kvgspotter.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ClientSettingsTest {
    private Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
    }

    @Test
    public void getLastUpdateNoticeTimestamp_should_return_correct_values() {
        Context mockContext = mock(Context.class);
        SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
        when(mockSharedPreferences.getLong(anyString(), anyLong())).thenReturn(-20L);
        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPreferences);
        ClientSettings clientSettings = new ClientSettings(mockContext);
        assertEquals(-20L, clientSettings.getLastUpdateNoticeTimestamp());
        verify(mockSharedPreferences, times(1)).getLong(ClientSettings.KEY_LAST_UPDATE_NOTICE_TIMESTAMP, 0);
    }

    @Test
    public void setLastUpdateNoticeTimestamp_should_set_correct_values() {
        Context mockContext = mock(Context.class);
        SharedPreferences mockSharedPreferences = mock(SharedPreferences.class);
        SharedPreferences.Editor mockEditor = mock(SharedPreferences.Editor.class);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        when(mockContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockSharedPreferences);
        ClientSettings clientSettings = new ClientSettings(mockContext);
        final long testTimestamp = 39032L;
        clientSettings.setLastUpdateNoticeTimestamp(testTimestamp);
        verify(mockSharedPreferences, times(1)).edit();
        verify(mockEditor, times(1)).putLong(ClientSettings.KEY_LAST_UPDATE_NOTICE_TIMESTAMP, testTimestamp);
        verify(mockEditor, times(1)).commit();
    }
}

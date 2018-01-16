package com.semeshky.kvgspotter.adapter;

import com.github.guennishueftgold.trapezeapi.Departure;
import com.semeshky.kvgspotter.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AbstractDataboundAdapterDiffUtilCallbackTest {
    @Test
    public void areItemsTheSame_should_work_properly() {
        final AbstractDataboundAdapter mockAdapter = mock(AbstractDataboundAdapter.class);
        final List<Departure> itemList = new ArrayList<>();
        final List<Departure> itemListSpy = mock(List.class);
        final Departure mockDeparture = mock(Departure.class);
        when(itemListSpy.get(anyInt())).thenReturn(mockDeparture);
        when(mockAdapter.getItem(anyInt())).thenReturn(mockDeparture);
        when(mockAdapter.areItemsTheSame(any(), any())).thenReturn(true);
        AbstractDataboundAdapterDiffUtilCallback callback = new AbstractDataboundAdapterDiffUtilCallback(mockAdapter, itemListSpy);
        callback.areItemsTheSame(10, 10);
        verify(mockAdapter, times(1)).areItemsTheSame(mockDeparture, mockDeparture);
        verify(itemListSpy, times(1)).get(10);
        verify(mockAdapter, times(1)).getItem(10);
    }
}

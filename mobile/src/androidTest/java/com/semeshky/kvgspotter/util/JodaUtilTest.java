package com.semeshky.kvgspotter.util;


import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.semeshky.kvgspotter.R;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JodaUtilTest {
    @Test()
    public void convertLocalTimeOrDelta_should_provide_time_delta() {
        final Context context = InstrumentationRegistry.getTargetContext();
        final LocalTime localTime = LocalTime.now().plusMinutes(10);
        final String result = JodaUtil.convertLocalTimeOrDelta(context, localTime);
        assertEquals(context.getResources().getQuantityText(R.plurals.minutes, 10).toString(), result);
    }

    @Test()
    public void convertLocalTimeOrDelta_should_provide_placeholder() {
        final Context context = InstrumentationRegistry.getTargetContext();
        final LocalTime localTime = null;
        final String result = JodaUtil.convertLocalTimeOrDelta(context, localTime);
        assertEquals("--:--", result);
    }

    @Test()
    public void convertLocalTimeOrDelta_should_provide_full_time() {
        final Context context = InstrumentationRegistry.getTargetContext();
        final LocalTime localTime = LocalTime.now().plusMinutes(50);
        final String result = JodaUtil.convertLocalTimeOrDelta(context, localTime);
        assertEquals(localTime.toString(DateTimeFormat.shortDateTime()), result);
    }
}

package com.semeshky.kvgspotter.util;

import android.content.Context;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class JodaUtilTest {

    private Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
    }

    @Test()
    public void convertLocalTime_should_provide_the_correct_value() {
        final LocalTime localTime = new LocalTime(10, 20, 30);
        final String result = JodaUtil.convertLocalTime(localTime);
        assertEquals("10:20", result);
    }

    @Test()
    public void convertLocalTime_should_provide_the_placeholder_value() {
        final String result = JodaUtil.convertLocalTime(null);
        assertEquals("--:--", result);
    }

    @Test()
    public void convertLocalTimeOrDelta_should_provide_time_delta() {
        final LocalTime localTime = LocalTime.now().plusMinutes(10);
        final String result = JodaUtil.convertLocalTimeOrDelta(context, localTime);
        assertEquals(context.getResources().getQuantityString(R.plurals.minutes, 10), result);
    }

    @Test()
    public void convertLocalTimeOrDelta_should_provide_placeholder() {
        final LocalTime localTime = null;
        final String result = JodaUtil.convertLocalTimeOrDelta(context, localTime);
        assertEquals("--:--", result);
    }

    @Test()
    public void convertLocalTimeOrDelta_should_provide_full_time() {
        final LocalTime localTime = LocalTime.now().plusMinutes(50);
        final String result = JodaUtil.convertLocalTimeOrDelta(context, localTime);
        assertEquals(localTime.toString(DateTimeFormat.shortDateTime()), result);
    }
}

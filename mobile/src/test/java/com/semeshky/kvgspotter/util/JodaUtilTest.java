package com.semeshky.kvgspotter.util;

import android.content.Context;

import com.semeshky.kvgspotter.BuildConfig;
import com.semeshky.kvgspotter.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowSystemClock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {ShadowSystemClock.class})
public class JodaUtilTest {

    private final DateTime mSystemDateTime = new DateTime(2010, 6, 6, 23, 50, 0, 0);
    private Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
        DateTimeUtils.setCurrentMillisFixed(this.mSystemDateTime.getMillis());
    }

    @After
    public void dismantle() {
        DateTimeUtils.setCurrentMillisSystem();
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

    @Test()
    public void convertLocalTimeOrDelta_should_provide_correct_time_for_day_skip() {
        final DateTime currentTime = new DateTime(2010, 6, 6, 23, 50, 0, 0);
        assertTrue(ShadowSystemClock.setCurrentTimeMillis(currentTime.getMillis()));

        final LocalTime localTime = new LocalTime(0, 6, 0, 0);
        final String result = JodaUtil.convertLocalTimeOrDelta(context, localTime);
        assertEquals(context.getResources().getQuantityString(R.plurals.minutes, 16), result);

        final LocalTime localTime2 = new LocalTime(23, 56, 0, 0);
        final String result2 = JodaUtil.convertLocalTimeOrDelta(context, localTime2);
        assertEquals(context.getResources().getQuantityString(R.plurals.minutes, 6), result2);
    }
}

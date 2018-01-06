package com.semeshky.kvgspotter.util;

import org.joda.time.LocalTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JodaUtilTest {
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

}

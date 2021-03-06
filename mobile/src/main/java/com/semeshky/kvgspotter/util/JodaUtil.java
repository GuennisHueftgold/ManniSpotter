package com.semeshky.kvgspotter.util;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.semeshky.kvgspotter.R;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class JodaUtil {
    private final static DateTimeFormatter SHORT_DATE_TIME_FORMATTER = DateTimeFormat.shortDateTime();

    public static String convertLocalTime(@Nullable LocalTime localTime) {
        if (localTime == null) {
            return "--:--";
        }
        return localTime.toString("HH:mm");
    }

    public static String convertLocalTimeOrDelta(@NonNull Context context, @Nullable LocalTime localTime) {
        if (localTime == null) {
            return "--:--";
        }
        final int delta = Minutes.minutesBetween(LocalTime.now(), localTime).getMinutes();
        if (delta < 20) {
            return context.getResources().getQuantityString(R.plurals.minutes, delta);
        } else {
            return localTime.toString(DateTimeFormat.shortDateTime());
        }
    }

    public static String timestampToLocalTime(long timestamp) {
        return new DateTime(timestamp).toLocalDateTime().toString(SHORT_DATE_TIME_FORMATTER);
    }
}

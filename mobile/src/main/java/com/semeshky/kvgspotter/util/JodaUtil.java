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
    public final static int DELTA_MINUTE_BORDER = 20;
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
        final LocalTime currentTime = LocalTime.now();
        final LocalTime futureBorder = currentTime.plusMinutes(DELTA_MINUTE_BORDER);

        int minuteDelta= Minutes.minutesBetween(currentTime,localTime).getMinutes();
        if( minuteDelta< 0)
            minuteDelta+=24*60;
        if (minuteDelta<DELTA_MINUTE_BORDER) {
            return context.getResources().getQuantityString(R.plurals.minutes, minuteDelta);
        } else {
            return localTime.toString(DateTimeFormat.shortDateTime());
        }
    }

    public static String timestampToLocalTime(long timestamp) {
        return new DateTime(timestamp).toLocalDateTime().toString(SHORT_DATE_TIME_FORMATTER);
    }
}

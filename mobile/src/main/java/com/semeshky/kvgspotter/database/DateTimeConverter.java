package com.semeshky.kvgspotter.database;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

class DateTimeConverter {

    private static final DateTimeFormatter FORMATTER = ISODateTimeFormat.dateTime();

    @TypeConverter
    public static DateTime toDate(String timestamp) {
        return timestamp == null ? null : DateTime.parse(timestamp, FORMATTER);
    }

    @TypeConverter
    public static String toTimestamp(DateTime dateTime) {
        return dateTime == null ? null : dateTime.withZone(DateTimeZone.UTC).toString(FORMATTER);
    }
}
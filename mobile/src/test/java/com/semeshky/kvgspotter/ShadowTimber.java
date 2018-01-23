package com.semeshky.kvgspotter;

import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

@Implements(Timber.class)
public class ShadowTimber {
    private static List<Throwable> sExceptionList = new ArrayList<>();
    @RealObject
    Timber realObject;

    public static void reset() {
        sExceptionList.clear();
    }

    public static void e(Throwable throwable) {
        sExceptionList.add(throwable);
    }

    public static Throwable getLastException() {
        if (sExceptionList.size() == 0)
            return null;
        return sExceptionList.get(sExceptionList.size() - 1);
    }

    public static int getExceptionCount() {
        return sExceptionList.size();
    }
}

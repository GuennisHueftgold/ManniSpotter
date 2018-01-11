package com.semeshky.kvgspotter.adapter;


import com.github.guennishueftgold.trapezeapi.Departure;
import com.github.guennishueftgold.trapezeapi.DepartureStatus;

import org.joda.time.LocalTime;
import org.joda.time.Seconds;

import java.util.Comparator;

final class DepartureComparator implements Comparator<Departure> {
    int getStatusImportance(int status) {
        switch (status) {
            case DepartureStatus.STATUS_DEPARTED:
                return 1;
            case DepartureStatus.STATUS_STOPPING:
                return 2;
            default:
                return 3;
        }
    }

    @Override
    public int compare(Departure d1, Departure d2) {
        if (d1.getStatus() != d2.getStatus()) {
            final int diff = getStatusImportance(d1.getStatus()) - getStatusImportance(d2.getStatus());
            if (diff != 0)
                return diff;
        }
        LocalTime t1 = d1.getActualTime();
        LocalTime t2 = d2.getActualTime();
        if (t1 == null)
            t1 = d1.getPlannedTime();
        if (t2 == null)
            t2 = d2.getPlannedTime();
        if (t1 == null || t2 == null)
            return d1.getActualRelativeTime() - d2.getActualRelativeTime();
        return Seconds.secondsBetween(t2, t1).getSeconds();
    }
}
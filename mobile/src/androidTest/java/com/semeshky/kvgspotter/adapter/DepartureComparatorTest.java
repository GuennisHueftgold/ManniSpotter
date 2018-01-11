package com.semeshky.kvgspotter.adapter;

import android.support.test.runner.AndroidJUnit4;

import com.github.guennishueftgold.trapezeapi.Departure;
import com.github.guennishueftgold.trapezeapi.DepartureStatus;

import org.joda.time.LocalTime;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DepartureComparatorTest {

    final static DepartureComparator DEPARTURE_COMPARATOR = new DepartureComparator();

    static Departure createDeparture(int status, LocalTime planned, LocalTime actual) {
        return createDeparture(status, planned, actual, 0);
    }

    static Departure createDeparture(int status, LocalTime planned, LocalTime actual, int actualRelativeTime) {
        return new Departure.Builder()
                .setStatus(status)
                .setActualTime(actual)
                .setPlannedTime(planned)
                .setActualRelativeTime(actualRelativeTime)
                .build();
    }

    @Test()
    public void should_sort_if_two_actual_times_are_given_correctly() {
        final LocalTime actualTime = LocalTime.fromMillisOfDay(2000);
        final int secondDifference = 2;
        final Departure departure1 = createDeparture(DepartureStatus.STATUS_DEPARTED,
                LocalTime.fromMillisOfDay(1000),
                actualTime);
        final Departure departure2 = createDeparture(DepartureStatus.STATUS_DEPARTED,
                LocalTime.fromMillisOfDay(1000),
                actualTime.plusSeconds(secondDifference));
        assertEquals(-secondDifference, DEPARTURE_COMPARATOR.compare(departure1, departure2));
        assertEquals(secondDifference, DEPARTURE_COMPARATOR.compare(departure2, departure1));
    }

    @Test()
    public void should_sort_if_one_actual_times_is_null_correctly() {
        final int secondDifference = 2;
        final LocalTime actualTime = LocalTime.fromMillisOfDay(2000);
        final LocalTime plannedTime = actualTime.plusSeconds(secondDifference);
        final Departure departure1 = createDeparture(DepartureStatus.STATUS_DEPARTED,
                plannedTime,
                actualTime);
        final Departure departure2 = createDeparture(DepartureStatus.STATUS_DEPARTED,
                plannedTime,
                null);
        assertEquals(-secondDifference, DEPARTURE_COMPARATOR.compare(departure1, departure2));
        assertEquals(secondDifference, DEPARTURE_COMPARATOR.compare(departure2, departure1));
    }

    @Test()
    public void should_sort_if_one_item_has_no_planned_and_no_actual_time_correctly() {
        final int secondDifference = 2;
        final LocalTime actualTime = LocalTime.fromMillisOfDay(2000);
        final LocalTime plannedTime = actualTime.plusSeconds(secondDifference);
        final Departure departure1 = createDeparture(DepartureStatus.STATUS_DEPARTED,
                plannedTime,
                actualTime,
                200);
        final Departure departure2 = createDeparture(DepartureStatus.STATUS_DEPARTED,
                null,
                null,
                400);
        assertEquals(-200, DEPARTURE_COMPARATOR.compare(departure1, departure2));
        assertEquals(200, DEPARTURE_COMPARATOR.compare(departure2, departure1));
    }

    @Test()
    public void should_sort_by_status_correctly() {
        final Departure departure1 = createDeparture(DepartureStatus.STATUS_DEPARTED,
                LocalTime.fromMillisOfDay(1000),
                LocalTime.fromMillisOfDay(2000));
        final Departure departure2 = createDeparture(DepartureStatus.STATUS_STOPPING,
                LocalTime.fromMillisOfDay(1000),
                LocalTime.fromMillisOfDay(2000));
        final Departure departure3 = createDeparture(DepartureStatus.STATUS_PLANNED,
                LocalTime.fromMillisOfDay(1000),
                LocalTime.fromMillisOfDay(2000));
        final Departure departure4 = createDeparture(DepartureStatus.STATUS_PREDICTED,
                LocalTime.fromMillisOfDay(1000),
                LocalTime.fromMillisOfDay(2000));
        final Departure departure5 = createDeparture(DepartureStatus.STATUS_UNKNOWN,
                LocalTime.fromMillisOfDay(1000),
                LocalTime.fromMillisOfDay(2000));
        // TEST DEPARTED
        assertEquals(0, DEPARTURE_COMPARATOR.compare(departure1, departure1));
        assertThat(0, greaterThan(DEPARTURE_COMPARATOR.compare(departure1, departure2)));
        assertThat(0, greaterThan(DEPARTURE_COMPARATOR.compare(departure1, departure3)));
        assertThat(0, greaterThan(DEPARTURE_COMPARATOR.compare(departure1, departure4)));
        assertThat(0, greaterThan(DEPARTURE_COMPARATOR.compare(departure1, departure5)));
        // TEST STOPPING
        assertThat(0, lessThan(DEPARTURE_COMPARATOR.compare(departure2, departure1)));
        assertEquals(0, DEPARTURE_COMPARATOR.compare(departure2, departure2));
        assertThat(0, greaterThan(DEPARTURE_COMPARATOR.compare(departure2, departure3)));
        assertThat(0, greaterThan(DEPARTURE_COMPARATOR.compare(departure2, departure4)));
        assertThat(0, greaterThan(DEPARTURE_COMPARATOR.compare(departure2, departure5)));
        // TEST PLANNED
        assertThat(0, lessThan(DEPARTURE_COMPARATOR.compare(departure3, departure1)));
        assertThat(0, lessThan(DEPARTURE_COMPARATOR.compare(departure3, departure2)));
        assertEquals(0, DEPARTURE_COMPARATOR.compare(departure3, departure3));
        assertEquals(0, DEPARTURE_COMPARATOR.compare(departure3, departure4));
        assertEquals(0, DEPARTURE_COMPARATOR.compare(departure3, departure5));
        // TEST PREDICTED
        assertThat(0, lessThan(DEPARTURE_COMPARATOR.compare(departure4, departure1)));
        assertThat(0, lessThan(DEPARTURE_COMPARATOR.compare(departure4, departure2)));
        assertEquals(0, DEPARTURE_COMPARATOR.compare(departure4, departure3));
        assertEquals(0, DEPARTURE_COMPARATOR.compare(departure4, departure4));
        assertEquals(0, DEPARTURE_COMPARATOR.compare(departure4, departure5));
        // TEST UNKNOWN
        assertThat(0, lessThan(DEPARTURE_COMPARATOR.compare(departure5, departure1)));
        assertThat(0, lessThan(DEPARTURE_COMPARATOR.compare(departure5, departure2)));
        assertEquals(0, DEPARTURE_COMPARATOR.compare(departure5, departure3));
        assertEquals(0, DEPARTURE_COMPARATOR.compare(departure5, departure4));
        assertEquals(0, DEPARTURE_COMPARATOR.compare(departure5, departure5));
    }
}

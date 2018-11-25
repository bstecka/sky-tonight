package com.example.barbara.skytonight;

import com.example.barbara.skytonight.entity.MeteorShowerEvent;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertTrue;

public class MeteorShowerEventUnitTest {

    @Test
    public void northernShowerForSouthernLocation_hasLowVisibility() {
        //TEST DATA FOR QUADRANTIDS 2019 VISIBILITY IN SYDNEY, AUSTRALIA
        final double LATITUDE = -33.865143;
        final double LONGITUDE = 151.209900;
        final double RA = 229.5;
        final double DECL = 49.5;
        Calendar startDate = Calendar.getInstance();
        startDate.set(2018, Calendar.DECEMBER, 27);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2018, Calendar.JANUARY, 10);
        Calendar peakDate = Calendar.getInstance();
        peakDate.set(2019, Calendar.JANUARY, 3);
        MeteorShowerEvent meteorShowerEvent = new MeteorShowerEvent(1, "test", startDate, endDate, peakDate, 0, RA, DECL);
        boolean lowVisibility = meteorShowerEvent.isVisibilityLow(LATITUDE, LONGITUDE);
        assertTrue(lowVisibility);
    }

}

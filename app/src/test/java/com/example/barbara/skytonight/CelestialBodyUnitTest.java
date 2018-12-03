package com.example.barbara.skytonight;

import com.example.barbara.skytonight.entity.CelestialBody;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class CelestialBodyUnitTest {

    @Test
    public void altitude_isCorrect() {
        final double RA = 245.4713;
        final double DECL = -21.2128;
        final double ALT = 4.8;
        final double LATITUDE = 51.1125;
        final double LONGITUDE = 17.1216;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.NOVEMBER, 25, 15, 21, 0);
        CelestialBody celestialBody = new CelestialBody(1, "test", RA, DECL, 0, false, calendar);
        double alt = celestialBody.getAltitude(LATITUDE, LONGITUDE);
        assertEquals(ALT, alt, 1);
    }

    @Test
    public void azimuth_isCorrect() {
        final double RA = 245.4713;
        final double DECL = -21.2128;
        final double AZ = 227.0;
        final double LATITUDE = 51.1125;
        final double LONGITUDE = 17.1216;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.NOVEMBER, 25, 15, 21, 0);
        CelestialBody celestialBody = new CelestialBody(1, "test", RA, DECL, 0, false, calendar);
        double az = celestialBody.getAzimuth(LATITUDE, LONGITUDE);
        assertEquals(AZ, az, 1);
    }

    @Test
    public void approxDirection_isCorrect() {
        final double RA = 245.4713;
        final double DECL = -21.2128;
        final double LATITUDE = 51.1125;
        final double LONGITUDE = 17.1216;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.NOVEMBER, 25, 15, 21, 0);
        CelestialBody celestialBody = new CelestialBody(1, "test", RA, DECL, 0, false, calendar);
        celestialBody.getAzimuth(LATITUDE, LONGITUDE);
        assertEquals("sw", celestialBody.getApproximateDirectionString());
    }

    @Test
    public void approxDirectionForInvalidData_isEmpty() {
        final double RA = 245.4713;
        final double DECL = -21.2128;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.NOVEMBER, 25, 15, 21, 0);
        CelestialBody celestialBody = new CelestialBody(1, "test", RA, DECL, 0, false, calendar);
        assertEquals("", celestialBody.getApproximateDirectionString());
    }

    @Test
    public void phaseIdWaxing_isCorrect() {
        CelestialBody celestialBody = new CelestialBody(1, "test", 0, 0, 25, true, null);
        assertEquals(3, celestialBody.getPhaseId());
    }

    @Test
    public void phaseIdWaning_isCorrect() {
        CelestialBody celestialBody = new CelestialBody(1, "test", 0, 0, 25, false, null);
        assertEquals(17, celestialBody.getPhaseId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void azAltForNull_throwsIllegalArgumentException() {
        final double RA = 245.4713;
        final double DECL = -21.2128;
        final double LATITUDE = 51.1125;
        final double LONGITUDE = 17.1216;
        CelestialBody celestialBody = new CelestialBody(1, "test", RA, DECL, 0, false, null);
        celestialBody.getAzimuth(LATITUDE, LONGITUDE);
    }

}

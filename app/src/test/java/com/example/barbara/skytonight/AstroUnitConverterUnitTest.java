package com.example.barbara.skytonight;

import android.support.v4.util.Pair;

import com.example.barbara.skytonight.entity.AstroUnitConverter;

import org.junit.Test;

import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AstroUnitConverterUnitTest {

    @Test
    public void julianDateForFullDay_isCorrect() {
        final double JULIAN_DATE = 2458429.5;
        AstroUnitConverter astroUnitConverter = new AstroUnitConverter();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UT1"));
        calendar.set(2018, Calendar.NOVEMBER, 7, 0, 0, 0);
        double julianDate = astroUnitConverter.getJulianDate(calendar);
        assertEquals(JULIAN_DATE, julianDate, 0.00001);
    }

    @Test
    public void julianDateForLaterDate_isLater() {
        AstroUnitConverter astroUnitConverter = new AstroUnitConverter();
        Calendar firstDate = Calendar.getInstance();
        Calendar laterDate = Calendar.getInstance();
        Random random = new Random();
        long firstDateMilis = random.nextLong()/2;
        long laterDateMilis = 0;
        while (laterDateMilis < firstDateMilis) {
            laterDateMilis = random.nextLong();
        }
        firstDate.setTimeInMillis(firstDateMilis);
        laterDate.setTimeInMillis(laterDateMilis);
        assertTrue(astroUnitConverter.getJulianDate(laterDate) > astroUnitConverter.getJulianDate(firstDate));
    }

    @Test
    public void julianDateForFractionsOfDay_isCorrect() {
        final double JULIAN_DATE = 2458402.234028;
        AstroUnitConverter astroUnitConverter = new AstroUnitConverter();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UT1"));
        calendar.set(2018, Calendar.OCTOBER, 10, 17, 37, 0);
        double julianDate = astroUnitConverter.getJulianDate(calendar);
        assertEquals(JULIAN_DATE, julianDate, 0.00001);
    }

    @Test
    public void GMSTForDate_isCorrect() {
        final double GMST = 3.879589 * 15;
        AstroUnitConverter astroUnitConverter = new AstroUnitConverter();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UT1"));
        calendar.set(2018, Calendar.APRIL, 15, 14, 18, 0);
        double calculatedGMST = astroUnitConverter.getGMST(calendar);
        assertEquals(GMST, calculatedGMST, 0.0001);
    }

    @Test
    public void localSideralTimeForDate_westernLongitude_isCorrect() {
        final double LONGITUDE = -12.1694444;
        final double LST = 17.70411 * 15;
        AstroUnitConverter astroUnitConverter = new AstroUnitConverter();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UT1"));
        calendar.set(2018, Calendar.NOVEMBER, 10, 15, 12, 0);
        double calculatedLST = astroUnitConverter.getLocalSiderealTime(calendar, LONGITUDE);
        assertEquals(LST, calculatedLST, 0.001);
    }

    @Test
    public void localSideralTimeForDate_easternLongitude_isCorrect() {
        final double LONGITUDE = 52.225;
        final double LST = 21.99707336 * 15;
        AstroUnitConverter astroUnitConverter = new AstroUnitConverter();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UT1"));
        calendar.set(2018, Calendar.NOVEMBER, 10, 15, 12, 0);
        double calculatedLST = astroUnitConverter.getLocalSiderealTime(calendar, LONGITUDE);
        assertEquals(LST, calculatedLST, 0.001);
    }

    @Test
    public void azAlt_isInRange() {
        final double RA = 245.4713;
        final double DECL = -21.2128;
        final double LATITUDE = 51.1125;
        final double LONGITUDE = 17.1216;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, Calendar.NOVEMBER, 25, 15, 21, 0);
        AstroUnitConverter astroUnitConverter = new AstroUnitConverter();
        Pair<Double, Double> azAlt = astroUnitConverter.getAzAlt(RA, DECL, LATITUDE, LONGITUDE, calendar);
        double az = azAlt.first;
        double alt = azAlt.second;
        assertTrue(az >= 0.0 && az <= 360.0 && alt >= -90.0 && alt <= 90.0);
    }

}

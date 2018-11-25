package com.example.barbara.skytonight;

import com.example.barbara.skytonight.entity.ISSObject;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class ISSObjectUnitTest {

    @Test
    public void nextFlyBy_isCorrect() {
        ArrayList<Calendar> flybytimes = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendarArg = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();
        Calendar calendar4 = Calendar.getInstance();
        calendar2.add(Calendar.HOUR, 1);
        calendarArg.add(Calendar.HOUR, 3);
        calendar3.add(Calendar.DAY_OF_YEAR, 2);
        calendar4.add(Calendar.DAY_OF_YEAR, 5);
        flybytimes.add(calendar2);
        flybytimes.add(calendar4);
        flybytimes.add(calendar3);
        flybytimes.add(calendar1);
        ArrayList<Integer> durations = new ArrayList<>();
        durations.add(2);
        durations.add(4);
        durations.add(3);
        durations.add(1);
        ISSObject issObject = new ISSObject(flybytimes, durations, calendarArg);
        assertEquals(issObject.getNextFlyby(), calendar3);
    }

    @Test
    public void nextDuration_isCorrect() {
        ArrayList<Calendar> flybytimes = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendarArg = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();
        Calendar calendar4 = Calendar.getInstance();
        calendar2.add(Calendar.HOUR, 1);
        calendarArg.add(Calendar.HOUR, 3);
        calendar3.add(Calendar.DAY_OF_YEAR, 2);
        calendar4.add(Calendar.DAY_OF_YEAR, 5);
        flybytimes.add(calendar2);
        flybytimes.add(calendar4);
        flybytimes.add(calendar3);
        flybytimes.add(calendar1);
        ArrayList<Integer> durations = new ArrayList<>();
        durations.add(2);
        durations.add(4);
        durations.add(3);
        durations.add(1);
        ISSObject issObject = new ISSObject(flybytimes, durations, calendarArg);
        assertEquals(issObject.getNextDuration(), 3);
    }

}

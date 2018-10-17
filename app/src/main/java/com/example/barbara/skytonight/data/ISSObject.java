package com.example.barbara.skytonight.data;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ISSObject extends AstroObject {
    private ArrayList<Calendar> flybyTimes;
    private ArrayList<Integer> durations;

    public ISSObject(ArrayList<Calendar> flybyTimes, ArrayList<Integer> durations){
        super(1000, "ISS");
        this.flybyTimes = flybyTimes;
        this.durations = durations;
    }

    public Calendar getNextFlyby() {
        return getNextFlyby(Calendar.getInstance());
    }

    public int getNextDuration() {
        return getNextDuration(Calendar.getInstance());
    }

    public int getNextDuration(Calendar cal) {
        int duration = 0;
        for (int i = 0; i < durations.size() && i < flybyTimes.size() && duration == 0; i++){
            Calendar flyby = flybyTimes.get(i);
            if (flyby.getTimeInMillis() + duration * 1000 > cal.getTimeInMillis())
                duration = durations.get(i);
        }
        return duration;
    }

    public void displayLists() {
        for (int i = 0; i < durations.size() && i < flybyTimes.size(); i++){
            Calendar flyby = flybyTimes.get(i);
            int duration = durations.get(i);
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Log.e("ISSObject", sdf.format(flyby.getTime()) + " " + duration);
        }
    }

    public Calendar getNextFlyby(Calendar cal) {
        Calendar nextFlyby = null;
        for (int i = 0;  i < durations.size() && i < flybyTimes.size() && nextFlyby == null; i++){
            int duration = durations.get(i);
            Calendar flyby = flybyTimes.get(i);
            if (flyby.getTimeInMillis() + duration * 1000 > cal.getTimeInMillis())
                nextFlyby = flyby;
        }
        if (nextFlyby == null)
            nextFlyby = Calendar.getInstance();
        return nextFlyby;
    }

    @Override
    public String toString() {
        return getNextFlyby().getTime().toString() + " " + getNextDuration();
    }

}

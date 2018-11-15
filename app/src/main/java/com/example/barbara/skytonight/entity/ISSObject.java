package com.example.barbara.skytonight.entity;

import java.util.ArrayList;
import java.util.Calendar;

public class ISSObject extends AstroObject {
    private ArrayList<Calendar> flybyTimes;
    private ArrayList<Integer> durations;
    private Calendar time;

    public ISSObject(ArrayList<Calendar> flybyTimes, ArrayList<Integer> durations, Calendar time){
        super(AstroConstants.ID_ISS, "ISS");
        this.flybyTimes = flybyTimes;
        this.durations = durations;
        this.time = time;
    }

    public Calendar getNextFlyby() {
        if (time != null)
            return getNextFlyby(time);
        else
            return getNextFlyby(Calendar.getInstance());
    }

    public int getNextDuration() {
        if (time != null)
            return getNextDuration(time);
        else
            return getNextDuration(Calendar.getInstance());
    }

    private int getNextDuration(Calendar cal) {
        int duration = 0;
        for (int i = 0; i < durations.size() && i < flybyTimes.size() && duration == 0; i++){
            Calendar flyby = flybyTimes.get(i);
            if (flyby.getTimeInMillis() + duration * 1000 > cal.getTimeInMillis())
                duration = durations.get(i);
        }
        return duration;
    }

    private Calendar getNextFlyby(Calendar cal) {
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
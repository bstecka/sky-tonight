package com.example.barbara.skytonight.entity;

import java.util.ArrayList;
import java.util.Calendar;

public class ISSObject extends AstroObject {
    private ArrayList<Calendar> flybyTimes;
    private ArrayList<Integer> durations;

    public ISSObject(ArrayList<Calendar> flybyTimes, ArrayList<Integer> durations, Calendar time){
        super(AstroConstants.ID_ISS, "ISS", time);
        this.flybyTimes = flybyTimes;
        this.durations = durations;
    }

    public Calendar getNextFlyby() {
        if (super.getTime() != null)
            return getNextFlyby(super.getTime());
        else
            return getNextFlyby(Calendar.getInstance());
    }

    public int getNextDuration() {
        if (super.getTime() != null)
            return getNextDuration(super.getTime());
        else
            return getNextDuration(Calendar.getInstance());
    }

    private int getNextDuration(Calendar cal) {
        Calendar nextFlyby = null;
        int flybyIndex = 0, duration = 0;
        for (int i = 0;  i < flybyTimes.size() && i < flybyTimes.size(); i++){
            Calendar currentFlyby = flybyTimes.get(i);
            if (currentFlyby.getTimeInMillis() + duration * 1000 > cal.getTimeInMillis()) {
                if (nextFlyby == null) {
                    nextFlyby = currentFlyby;
                    flybyIndex = i;
                }
                else if (currentFlyby.getTimeInMillis() < nextFlyby.getTimeInMillis()) {
                    nextFlyby = currentFlyby;
                    flybyIndex = i;
                }
            }
        }
        duration = durations.get(flybyIndex);
        return duration;
    }

    private Calendar getNextFlyby(Calendar cal) {
        Calendar nextFlyby = null;
        for (int i = 0;  i < durations.size() && i < flybyTimes.size(); i++){
            int duration = durations.get(i);
            Calendar currentFlyby = flybyTimes.get(i);
            if (currentFlyby.getTimeInMillis() + duration * 1000 > cal.getTimeInMillis()) {
                if (nextFlyby == null)
                    nextFlyby = currentFlyby;
                else if (currentFlyby.getTimeInMillis() < nextFlyby.getTimeInMillis())
                    nextFlyby = currentFlyby;
            }
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

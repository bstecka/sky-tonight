package com.example.barbara.skytonight.data;

import java.util.Calendar;
import java.util.Date;

public class MeteorShowerEvent extends AstroEvent {

    private Calendar peakDate;

    public MeteorShowerEvent(int id, String name, Calendar startDate, Calendar endDate, Calendar peakDate) {
        super(id, "ms_" + name.toLowerCase().replace(' ', '_'), startDate, endDate);
        this.peakDate = peakDate;
    }

    @Override
    public Date getPeakDate() {
        return peakDate.getTime();
    }
}

package com.example.barbara.skytonight.presentation.core;

import java.util.Calendar;

public class CalendarPresenter implements CalendarContract.Presenter {

    private final CalendarContract.View mCalendarView;

    public CalendarPresenter(CalendarContract.View mCalendarView) {
        this.mCalendarView = mCalendarView;
    }

    @Override
    public void start() {
        mCalendarView.updateDayInfoText(Calendar.getInstance());
    }

}

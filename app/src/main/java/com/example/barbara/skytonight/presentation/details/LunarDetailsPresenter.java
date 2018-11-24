package com.example.barbara.skytonight.presentation.details;
import com.example.barbara.skytonight.data.MoonSunDataSource;
import com.example.barbara.skytonight.data.RepositoryFactory;
import com.example.barbara.skytonight.data.repository.MoonSunDataRepository;
import com.example.barbara.skytonight.entity.LunarEclipseEvent;
import com.example.barbara.skytonight.entity.MoonSunData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LunarDetailsPresenter implements LunarDetailsContract.Presenter {

    private final LunarDetailsContract.View view;
    private final MoonSunDataRepository moonSunDataRepository;
    private LunarEclipseEvent event;

    public LunarDetailsPresenter(LunarDetailsContract.View view, LunarEclipseEvent event) {
        this.view = view;
        this.moonSunDataRepository = RepositoryFactory.getMoonSunDataRepository(view.getContext());
        this.event = event;
    }

    @Override
    public void start() {
        if (event != null) {
            Calendar time = event.getPeak();
            double latitude = event.getLatitude();
            double longitude = event.getLongitude();
            moonSunDataRepository.getMoonSunData(time, latitude, longitude, new MoonSunDataSource.GetMoonSunDataCallback(){
                @Override
                public void onDataLoaded(MoonSunData moonSunData) {
                    setDataInView(moonSunData, event);
                }

                @Override
                public void onDataNotAvailable() {
                    setDataInView(event);
                }
            });
        }
    }

    private void setDataInView(LunarEclipseEvent event) {
        view.setDateTextViews(getStringForCalendar(event.getPartialBegins()),
                getStringForCalendar(event.getPartialEnds()),
                getStringForCalendar(event.getTotalBegins()),
                getStringForCalendar(event.getTotalEnds()),
                getStringForCalendar(event.getPeak()),
                getStringForCalendar(event.getPenunmbralBegins()),
                getStringForCalendar(event.getPenunmbralEnds()));
        view.setTitle(event.getName(), new SimpleDateFormat("MMM dd yyyy", Locale.getDefault()).format(event.getPeakDate()));
    }

    private void setDataInView(MoonSunData moonSunData, LunarEclipseEvent event) {
        setDataInView(event);
        view.setMoonTimesTextView(getStringForCalendar(moonSunData.getMoonrise()), getStringForCalendar(moonSunData.getMoonset()));
        view.setSunTimesTextView(getStringForCalendar(moonSunData.getSunrise()), getStringForCalendar(moonSunData.getSunset()));
    }

    private String getStringForCalendar(Calendar calendar) {
        if (calendar == null)
            return "";
        else
            return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
    }
}

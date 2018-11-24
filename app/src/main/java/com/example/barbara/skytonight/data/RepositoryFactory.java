package com.example.barbara.skytonight.data;

import android.content.Context;

import com.example.barbara.skytonight.data.local.AudioLocalDataSource;
import com.example.barbara.skytonight.data.local.NoteLocalDataSource;
import com.example.barbara.skytonight.data.local.PhotoLocalDataSource;
import com.example.barbara.skytonight.data.local.VideoLocalDataSource;
import com.example.barbara.skytonight.data.remote.AstroObjectsRemoteDataSource;
import com.example.barbara.skytonight.data.remote.ISSRemoteDataSource;
import com.example.barbara.skytonight.data.remote.LunarEclipseRemoteDataSource;
import com.example.barbara.skytonight.data.remote.MeteorShowerRemoteDataSource;
import com.example.barbara.skytonight.data.remote.MoonSunRemoteDataSource;
import com.example.barbara.skytonight.data.remote.NewsRemoteDataSource;
import com.example.barbara.skytonight.data.remote.SolarEclipseRemoteDataSource;
import com.example.barbara.skytonight.data.remote.WeatherRemoteDataSource;
import com.example.barbara.skytonight.data.repository.AstroObjectRepository;
import com.example.barbara.skytonight.data.repository.AudioRepository;
import com.example.barbara.skytonight.data.repository.EventsRepository;
import com.example.barbara.skytonight.data.repository.ISSRepository;
import com.example.barbara.skytonight.data.repository.LocationRepository;
import com.example.barbara.skytonight.data.repository.MoonSunDataRepository;
import com.example.barbara.skytonight.data.repository.NewsRepository;
import com.example.barbara.skytonight.data.repository.NoteRepository;
import com.example.barbara.skytonight.data.repository.PhotoRepository;
import com.example.barbara.skytonight.data.repository.VideoRepository;
import com.example.barbara.skytonight.data.repository.WeatherRepository;

public class RepositoryFactory {

    private static AstroObjectRepository astroObjectRepository;
    private static AudioRepository audioRepository;
    private static LocationRepository locationRepository;
    private static EventsRepository eventsRepository;
    private static ISSRepository issRepository;
    private static MoonSunDataRepository moonSunDataRepository;
    private static NewsRepository newsRepository;
    private static NoteRepository noteRepository;
    private static PhotoRepository photoRepository;
    private static VideoRepository videoRepository;
    private static WeatherRepository weatherRepository;

    public static AstroObjectRepository getAstroObjectRepository(Context context) {
        if (astroObjectRepository == null) {
            astroObjectRepository = AstroObjectRepository.getInstance(AstroObjectsRemoteDataSource.getInstance(context.getApplicationContext()));
        }
        return astroObjectRepository;
    }

    public static AudioRepository getAudioRepository(Context context) {
        if (audioRepository == null) {
            audioRepository = AudioRepository.getInstance(AudioLocalDataSource.getInstance(context.getApplicationContext()));
        }
        return audioRepository;
    }

    public static LocationRepository getLocationRepository() {
        if (locationRepository == null) {
            locationRepository = LocationRepository.getInstance();
        }
        return locationRepository;
    }

    public static EventsRepository getEventsRepository(Context context) {
        if (eventsRepository == null) {
            eventsRepository = EventsRepository.getInstance(SolarEclipseRemoteDataSource.getInstance(context.getApplicationContext()),
                    LunarEclipseRemoteDataSource.getInstance(context.getApplicationContext()),
                    MeteorShowerRemoteDataSource.getInstance(context.getApplicationContext()));
        }
        return eventsRepository;
    }

    public static ISSRepository getISSRepository(Context context) {
        if (issRepository == null) {
            issRepository = ISSRepository.getInstance(ISSRemoteDataSource.getInstance(context.getApplicationContext()));
        }
        return issRepository;
    }

    public static MoonSunDataRepository getMoonSunDataRepository(Context context) {
        if (moonSunDataRepository == null) {
            moonSunDataRepository = MoonSunDataRepository.getInstance(MoonSunRemoteDataSource.getInstance(context.getApplicationContext()));
        }
        return moonSunDataRepository;
    }

    public static NewsRepository getNewsRepository(Context context) {
        if (newsRepository == null) {
            newsRepository = NewsRepository.getInstance(NewsRemoteDataSource.getInstance(context.getApplicationContext()), context.getApplicationContext());
        }
        return newsRepository;
    }

    public static NoteRepository getNoteRepository(Context context) {
        if (noteRepository == null) {
            noteRepository = NoteRepository.getInstance(NoteLocalDataSource.getInstance(context));
        }
        return noteRepository;
    }

    public static PhotoRepository getPhotoRepository(Context context) {
        if (photoRepository == null) {
            photoRepository = PhotoRepository.getInstance(PhotoLocalDataSource.getInstance(context));
        }
        return photoRepository;
    }

    public static VideoRepository getVideoRepository(Context context) {
        if (videoRepository == null) {
            videoRepository = VideoRepository.getInstance(VideoLocalDataSource.getInstance(context));
        }
        return videoRepository;
    }

    public static WeatherRepository getWeatherRepository(Context context) {
        if (weatherRepository == null){
            weatherRepository = WeatherRepository.getInstance(WeatherRemoteDataSource.getInstance(context));
        }
        return weatherRepository;
    }
}

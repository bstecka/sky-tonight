package com.example.barbara.skytonight;

import com.example.barbara.skytonight.data.AstroObjectsDataSource;
import com.example.barbara.skytonight.data.ISSDataSource;
import com.example.barbara.skytonight.data.WeatherDataSource;
import com.example.barbara.skytonight.data.repository.AstroObjectRepository;
import com.example.barbara.skytonight.data.repository.ISSRepository;
import com.example.barbara.skytonight.data.repository.LocationRepository;
import com.example.barbara.skytonight.data.repository.WeatherRepository;
import com.example.barbara.skytonight.entity.AstroConstants;
import com.example.barbara.skytonight.entity.AstroObject;
import com.example.barbara.skytonight.entity.CelestialBody;
import com.example.barbara.skytonight.entity.ISSObject;
import com.example.barbara.skytonight.entity.WeatherObject;
import com.example.barbara.skytonight.presentation.core.TodayContract;
import com.example.barbara.skytonight.presentation.core.TodayPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.mockito.Mockito.*;

public class TodayPresenterUnitTest {

    @Mock
    private AstroObjectRepository astroObjectRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private WeatherRepository weatherRepository;
    @Mock
    private ISSRepository issRepository;
    @Mock
    private TodayContract.View view;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void mockRepositoryAnswers() {
        final Calendar time = Calendar.getInstance();
        time.add(Calendar.HOUR, 3);
        final ISSObject issObject = createMockISSObject(time);
        final List<WeatherObject> weatherObjects = createMockWeatherObjects(time);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ISSDataSource.GetISSObjectCallback)invocation.getArguments()[3]).onDataLoaded(issObject);
                return null;
            }
        }).when(issRepository).getISSObject(
                any(Calendar.class),
                anyDouble(),
                anyDouble(),
                any(ISSDataSource.GetISSObjectCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((WeatherDataSource.GetWeatherObjectsCallback)invocation.getArguments()[2]).onDataLoaded(weatherObjects);
                return null;
            }
        }).when(weatherRepository).getWeatherObjects(
                anyDouble(),
                anyDouble(),
                any(WeatherDataSource.GetWeatherObjectsCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Integer id = (Integer) invocation.getArguments()[1];
                ((AstroObjectsDataSource.GetAstroObjectsCallback)invocation.getArguments()[2]).onDataLoaded(createMockAstroObject(id, time));
                return null;
            }
        }).when(astroObjectRepository).getAstroObject(
                any(Calendar.class),
                anyInt(),
                any(AstroObjectsDataSource.GetAstroObjectsCallback.class));
    }

    private void mockRepositoryFailures() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((ISSDataSource.GetISSObjectCallback)invocation.getArguments()[3]).onDataNotAvailable();
                return null;
            }
        }).when(issRepository).getISSObject(
                any(Calendar.class),
                anyDouble(),
                anyDouble(),
                any(ISSDataSource.GetISSObjectCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((WeatherDataSource.GetWeatherObjectsCallback)invocation.getArguments()[2]).onDataNotAvailable();
                return null;
            }
        }).when(weatherRepository).getWeatherObjects(
                anyDouble(),
                anyDouble(),
                any(WeatherDataSource.GetWeatherObjectsCallback.class));
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((AstroObjectsDataSource.GetAstroObjectsCallback)invocation.getArguments()[2]).onDataNotAvailable();
                return null;
            }
        }).when(astroObjectRepository).getAstroObject(
                any(Calendar.class),
                anyInt(),
                any(AstroObjectsDataSource.GetAstroObjectsCallback.class));
    }

    @Test
    public void fetchInvalidISSDataShouldNotLoadIntoView() {
        mockRepositoryFailures();
        TodayPresenter todayPresenter = new TodayPresenter(
                this.view,
                this.astroObjectRepository,
                this.locationRepository,
                this.weatherRepository,
                this.issRepository
        );
        todayPresenter.loadISS(20.3, 52.6);
        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(0)).updateList(any(ISSObject.class));
    }

    @Test
    public void fetchValidISSDataShouldLoadIntoView() {
        TodayPresenter todayPresenter = new TodayPresenter(
                this.view,
                this.astroObjectRepository,
                this.locationRepository,
                this.weatherRepository,
                this.issRepository
        );
        todayPresenter.loadISS(20.3, 52.6);
        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).updateList(any(ISSObject.class));
    }

    @Test
    public void fetchValidWeatherDataShouldLoadIntoView() {
        TodayPresenter todayPresenter = new TodayPresenter(
                this.view,
                this.astroObjectRepository,
                this.locationRepository,
                this.weatherRepository,
                this.issRepository
        );
        todayPresenter.loadWeather(20.3, 52.6);
        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).updateWeatherView(any(WeatherObject.class));
    }

    @Test
    public void fetchValidAstroObjectDataShouldLoadIntoView() {
        TodayPresenter todayPresenter = new TodayPresenter(
                this.view,
                this.astroObjectRepository,
                this.locationRepository,
                this.weatherRepository,
                this.issRepository
        );
        todayPresenter.showObjects();
        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).clearList();
        inOrder.verify(view, times(AstroConstants.ASTRO_OBJECT_IDS.length)).updateList(any(CelestialBody.class));
    }

    private ISSObject createMockISSObject(Calendar time) {
        ArrayList<Calendar> flybytimes = new ArrayList<>();
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();
        Calendar calendar4 = Calendar.getInstance();
        calendar2.add(Calendar.HOUR, 1);
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
        return new ISSObject(flybytimes, durations, time);
    }

    private List<WeatherObject> createMockWeatherObjects(Calendar time) {
        List<WeatherObject> list = new ArrayList<>();
        list.add(new WeatherObject(504, 88, time));
        Calendar time2 = Calendar.getInstance();
        time2.setTime(time.getTime());
        time2.add(Calendar.HOUR, 1);
        list.add(new WeatherObject(503, 12, time2));
        return list;
    }

    private AstroObject createMockAstroObject(int id, Calendar time) {
        return new CelestialBody(id, "test", 30.0, 30.0, 15, false, time);
    }
}

package com.example.barbara.skytonight.presentation.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.AstroObject;
import com.example.barbara.skytonight.entity.WeatherObject;
import com.example.barbara.skytonight.AppConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TodayFragment extends Fragment implements TodayContract.View {

    private TodayContract.Presenter mPresenter;

    private OnListFragmentInteractionListener mListener;
    private TodayRecyclerViewAdapter mAdapter;
    ArrayList<AstroObject> list;

    public TodayFragment() {
        list = new ArrayList<>();
    }

    @Override
    public void setPresenter(TodayContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mAdapter = new TodayRecyclerViewAdapter(list, mListener, AppConstants.DEFAULT_LATITUDE, AppConstants.DEFAULT_LONGITUDE);
        mPresenter.start();
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_list, container, false);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { updateTimeTextView(); }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { mPresenter.start(); }
        });
        View rView = view.findViewById(R.id.astroObjectRecyclerView);
        if (rView instanceof RecyclerView) {
            Context context = rView.getContext();
            RecyclerView recyclerView = (RecyclerView) rView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        return view;
    }

    @Override
    public void showErrorText() {
        if (getView() != null && getContext() != null) {
            View rView = getView().findViewById(R.id.astroObjectRecyclerView);
            rView.setVisibility(View.INVISIBLE);
            TextView textView = getView().findViewById(R.id.errorTextView);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideErrorText() {
        if (getView() != null && getContext() != null) {
            View rView = getView().findViewById(R.id.astroObjectRecyclerView);
            rView.setVisibility(View.VISIBLE);
            TextView textView = getView().findViewById(R.id.errorTextView);
            textView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getTimeOverhead() {
        int overhead = 0;
        if (getView() != null) {
            SeekBar seekBar = getView().findViewById(R.id.seekBar);
            overhead = seekBar.getProgress();
        }
        return overhead;
    }

    @Override
    public void refreshLocationInAdapter(Location location) {
        mAdapter.setLatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void updateWeatherView(WeatherObject currentWeather) {
        if (getView() != null && getContext() != null) {
            ConstraintLayout layout = getView().findViewById(R.id.weatherLayout);
            layout.setVisibility(View.VISIBLE);
            ImageView imageView = getView().findViewById(R.id.weatherImageView);
            imageView.setVisibility(View.VISIBLE);
            TextView cloudCoverage = getView().findViewById(R.id.cloudCoverageTextView);
            cloudCoverage.setText(getContext().getString(R.string.weather_cloud_coverage, currentWeather.getCloudCoverage()));
            TextView conditions = getView().findViewById(R.id.conditionsTextView);
            try {
                int weatherStringId = getContext().getResources().getIdentifier(currentWeather.getWeatherIdString(), "string", getContext().getPackageName());
                conditions.setText(getContext().getResources().getString(weatherStringId));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                conditions.setText(R.string.weather_condition_unknown);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void clearList() {
        this.list.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteFromList(int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id)
                list.remove(i);
        }
    }

    @Override
    public void updateList(AstroObject object) {
        deleteFromList(object.getId());
        boolean found = false;
        int index = 0;
        if (!list.isEmpty()) {
           while (index < list.size() && !found) {
                if (object.getId() < list.get(index).getId())
                    found = true;
                else
                    index++;
            }
        }
        this.list.add(index, object);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public Activity getCurrentActivity(){
        return getActivity();
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(String item);
    }

    private void updateTimeTextView() {
        if (getView() != null) {
            TextView textView = getView().findViewById(R.id.timeTextView);
            if (getTimeOverhead() == 0)
                textView.setText(R.string.time_now);
            else if (getTimeOverhead() == 1)
                textView.setText(R.string.time_now_one);
            else {
                Calendar time = Calendar.getInstance();
                time.add(Calendar.HOUR, getTimeOverhead());
                DateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                textView.setText(getString(R.string.time_from_now, getTimeOverhead(), sdf.format(time.getTime())));
            }
        }
    }
}

package com.example.barbara.skytonight.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.photos.PhotoGalleryActivity;

import java.util.Calendar;

public class CalendarFragment extends Fragment implements CalendarContract.View {

    private CalendarContract.Presenter mPresenter;
    private OnFragmentInteractionListener mListener;
    private Calendar currentlySelectedDate = Calendar.getInstance();
    private View view;
    private Context context;

    public CalendarFragment() { }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(CalendarContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Activity getViewActivity() {
        return getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        context = view.getContext();
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                updateDayInfoText(selectedDate);
                currentlySelectedDate = selectedDate;
            }
        });
        AppCompatButton photosButton = view.findViewById(R.id.buttonPhotos);
        photosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhotosButtonClick();
            }
        });
        return view;
    }

    @Override
    public void updateDayInfoText(Calendar selectedDate) {
        int numberOfPhotos = mPresenter.getNumberOfPhotos(selectedDate);
        TextView textView = view.findViewById(R.id.dayInfoTextView);
        textView.setText(context.getString(R.string.day_info_text, 0, numberOfPhotos));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void onPhotosButtonClick() {
        Intent intent = new Intent(getActivity(), PhotoGalleryActivity.class);
        intent.putExtra("year", currentlySelectedDate.get(Calendar.YEAR));
        intent.putExtra("dayOfYear", currentlySelectedDate.get(Calendar.DAY_OF_YEAR));
        startActivity(intent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

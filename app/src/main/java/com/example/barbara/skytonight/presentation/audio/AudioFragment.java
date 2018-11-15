package com.example.barbara.skytonight.presentation.audio;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.barbara.skytonight.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class AudioFragment extends Fragment implements AudioContract.View {

    private AudioContract.Presenter mPresenter;
    private AudioRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<File> fileList;
    private Calendar selectedDate;
    private View view;
    private Integer selectedYear = null;
    private Integer selectedMonth = null;
    private boolean recordingPermitted = true;
    private boolean isRecording = false;
    private boolean weekMode = false;

    @Override
    public void setPresenter(AudioContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fileList.isEmpty())
            mPresenter.start();
    }

    public void releaseMediaPlayer() {
        if (mAdapter != null) {
            mAdapter.releaseMediaPlayer();
        }
    }

    public void setRecordingPermitted(boolean permitted) {
        recordingPermitted = permitted;
    }

    private void onFloatingActionButtonClick() {
        if (recordingPermitted) {
            FloatingActionButton button = view.findViewById(R.id.floatingActionButton);
            if (!isRecording) {
                isRecording = true;
                mPresenter.startRecording();
                button.setImageResource(R.drawable.ic_baseline_stop_24px);
            } else {
                isRecording = false;
                mPresenter.stopRecording();
                button.setImageResource(R.drawable.ic_voice);
                mPresenter.start();
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_audio, container, false);
        fileList = new ArrayList<>();
        final FloatingActionButton button = view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onFloatingActionButtonClick();
            }
        });
        mAdapter = new AudioRecyclerViewAdapter(fileList);
        recyclerView = view.findViewById(R.id.audioRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    public void setWeekMode(boolean value) {
        weekMode = value;
    }

    @Override
    public boolean isWeekModeEnabled() {
        return weekMode;
    }


    @Override
    public Context getContext() { return view.getContext(); }

    @Override
    public ArrayList<File> getFileList() { return fileList; }

    @Override
    public Calendar getSelectedDate() { return selectedDate; }

    @Override
    public Integer getSelectedMonth() {
        return selectedMonth;
    }

    @Override
    public Integer getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedDate(Calendar selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void setSelectedMonthYear(int month, int year) {
        selectedDate = null;
        selectedMonth = month;
        selectedYear = year;
    }

    @Override
    public Activity getViewActivity() { return getActivity(); }

    @Override
    public void clearListInView() {
        fileList.clear();
    }

    @Override
    public void refreshListInView() {
        mAdapter.notifyDataSetChanged();
    }

}

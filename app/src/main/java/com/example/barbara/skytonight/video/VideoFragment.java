package com.example.barbara.skytonight.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.barbara.skytonight.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class VideoFragment extends Fragment implements VideoContract.View {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private VideoContract.Presenter mPresenter;
    private MyVideoRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<File> fileList;
    private Calendar selectedDate;
    private View view;
    private Integer selectedYear = null;
    private Integer selectedMonth = null;

    @Override
    public void setPresenter(VideoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fileList.isEmpty())
            mPresenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_video, container, false);
        fileList = new ArrayList<>();
        final FloatingActionButton button = view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPresenter.dispatchTakeVideoIntent();
            }
        });
        mAdapter = new MyVideoRecyclerViewAdapter(fileList);
        recyclerView = view.findViewById(R.id.videoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void startVideoActivity(Intent intent) {
        startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(view.getContext(), R.string.video_saved, Toast.LENGTH_SHORT).show();
            fileList.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

}

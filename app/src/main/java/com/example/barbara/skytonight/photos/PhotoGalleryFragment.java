package com.example.barbara.skytonight.photos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.barbara.skytonight.R;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class PhotoGalleryFragment extends Fragment implements PhotoGalleryContract.View {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private PhotoGalleryContract.Presenter mPresenter;
    private MyPhotoRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<ImageFile> photoList;
    private Calendar selectedDate;
    private View view;
    private Integer selectedYear = null;
    private Integer selectedMonth = null;
    private boolean weekMode = false;

    @Override
    public void setPresenter(PhotoGalleryContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (photoList.isEmpty())
            mPresenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        photoList = new ArrayList<>();
        android.support.design.widget.FloatingActionButton button = view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPresenter.dispatchTakePhotoIntent();
            }
        });
        mAdapter = new MyPhotoRecyclerViewAdapter(photoList);
        recyclerView = view.findViewById(R.id.photoRecyclerView);
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
    public ArrayList<ImageFile> getPhotoList() { return photoList; }

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
        photoList.clear();
    }

    @Override
    public void refreshListInView() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void startPhotoActivity(Intent intent) { startActivityForResult(intent, REQUEST_TAKE_PHOTO); }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(view.getContext(), R.string.photo_saved, Toast.LENGTH_SHORT).show();
            photoList.clear();
            mAdapter.notifyDataSetChanged();
        }
    }
}

package com.example.barbara.skytonight.presentation.photos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.ImageFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PhotoGalleryFragment extends Fragment implements PhotoGalleryContract.View {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private PhotoGalleryContract.Presenter mPresenter;
    private boolean inDeleteMode = false;
    private SimplePhotoRecyclerViewAdapter mAdapter;
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
        final Toolbar toolbar = view.findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.title_activity_photo_gallery);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);
        photoList = new ArrayList<>();
        android.support.design.widget.FloatingActionButton button = view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPresenter.dispatchTakePhotoIntent();
            }
        });
        mAdapter = new SimplePhotoRecyclerViewAdapter(photoList);
        RecyclerView recyclerView = view.findViewById(R.id.photoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.topbar_list_menu, menu);
        MenuItem deleteSelectedAction = menu.findItem(R.id.action_delete_selected);
        deleteSelectedAction.setVisible(false);
        MenuItem cancel = menu.findItem(R.id.action_cancel);
        cancel.setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu){
        MenuItem deleteSelectedAction = menu.findItem(R.id.action_delete_selected);
        MenuItem cancelAction = menu.findItem(R.id.action_cancel);
        MenuItem enterDeleteMode = menu.findItem(R.id.action_delete);
        if (inDeleteMode) {
            deleteSelectedAction.setVisible(true);
            cancelAction.setVisible(true);
            enterDeleteMode.setVisible(false);
        }
        else {
            deleteSelectedAction.setVisible(false);
            cancelAction.setVisible(false);
            enterDeleteMode.setVisible(true);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getActivity() != null)
            getActivity().invalidateOptionsMenu();
        switch (item.getItemId()) {
            case R.id.action_delete:
                inDeleteMode = true;
                mAdapter.clearSelectedFiles();
                mAdapter.setDeleteMode(true);
                mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
                return true;
            case R.id.action_delete_selected:
                inDeleteMode = false;
                mAdapter.setDeleteMode(false);
                mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
                List<File> selectedFiles = mAdapter.getSelectedFiles();
                mPresenter.deleteFiles(selectedFiles);
                return true;
            case R.id.action_cancel:
                inDeleteMode = false;
                mAdapter.setDeleteMode(false);
                mAdapter.clearSelectedFiles();
                mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
                return true;
        }
        return super.onOptionsItemSelected(item);
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

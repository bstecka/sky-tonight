package com.example.barbara.skytonight.presentation.audio;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.barbara.skytonight.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AudioFragment extends Fragment implements AudioContract.View {

    private AudioContract.Presenter mPresenter;
    private AudioRecyclerViewAdapter mAdapter;
    private ArrayList<File> fileList;
    private Calendar selectedDate;
    private View view;
    private Integer selectedYear = null;
    private Integer selectedMonth = null;
    private boolean recordingPermitted = true;
    private boolean isRecording = false;
    private boolean weekMode = false;
    private boolean inDeleteMode = false;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_audio, container, false);
        final Toolbar toolbar = view.findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.title_activity_audio);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);
        final FloatingActionButton button = view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onFloatingActionButtonClick();
            }
        });
        fileList = new ArrayList<>();
        mAdapter = new AudioRecyclerViewAdapter(fileList);
        RecyclerView recyclerView = view.findViewById(R.id.audioRecyclerView);
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

    private void onFloatingActionButtonClick() {
        if (recordingPermitted && !(mAdapter != null && mAdapter.isPlaying())) {
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

}

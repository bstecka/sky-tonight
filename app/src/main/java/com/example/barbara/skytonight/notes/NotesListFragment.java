package com.example.barbara.skytonight.notes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.photos.ImageFile;
import com.example.barbara.skytonight.photos.MyPhotoRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class NotesListFragment extends Fragment implements NotesListContract.View {

    private NotesListContract.Presenter mPresenter;
    private MyNotesRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<NoteFile> notesList;
    private Calendar selectedDate;
    private View view;
    private Integer selectedYear = null;
    private Integer selectedMonth = null;

    @Override
    public void setPresenter(NotesListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        notesList = new ArrayList<>();
        mAdapter = new MyNotesRecyclerViewAdapter(notesList);
        recyclerView = view.findViewById(R.id.notesListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public ArrayList<NoteFile> getNotesList() { return notesList; }

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
        notesList.clear();
    }

    @Override
    public void refreshListInView() {
        mAdapter.notifyDataSetChanged();
    }

}

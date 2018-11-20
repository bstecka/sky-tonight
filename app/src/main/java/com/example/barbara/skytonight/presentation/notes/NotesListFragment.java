package com.example.barbara.skytonight.presentation.notes;

import android.app.Activity;
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

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.NoteFile;
import com.example.barbara.skytonight.presentation.core.CalendarContract;

import java.util.ArrayList;
import java.util.Calendar;

public class NotesListFragment extends Fragment implements NotesListContract.View {

    private NotesListContract.Presenter mPresenter;
    private SimpleNotesRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<NoteFile> notesList;
    private Calendar selectedDate;
    private View view;
    private Integer selectedYear = null;
    private Integer selectedMonth = null;
    private boolean weekMode = false;

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
        mAdapter = new SimpleNotesRecyclerViewAdapter(notesList);
        recyclerView = view.findViewById(R.id.notesListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mAdapter);
        final FloatingActionButton button = view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onFloatingButtonClick();
            }
        });
        return view;
    }

    private void onFloatingButtonClick() {
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        intent.putExtra("type", CalendarContract.TAB_TYPE_DAY);
        intent.putExtra("year", Calendar.getInstance().get(Calendar.YEAR));
        intent.putExtra("dayOfYear", Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
        startActivity(intent);
    }

    public void setWeekMode(boolean value) {
        weekMode = value;
    }

    @Override
    public boolean isWeekModeEnabled() {
        return weekMode;
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

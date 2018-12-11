package com.example.barbara.skytonight.presentation.notes;

import android.app.Activity;
import android.content.Intent;
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
import com.example.barbara.skytonight.entity.NoteFile;
import com.example.barbara.skytonight.presentation.core.CalendarContract;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotesListFragment extends Fragment implements NotesListContract.View {

    private NotesListContract.Presenter mPresenter;
    private SimpleNotesRecyclerViewAdapter mAdapter;
    private ArrayList<NoteFile> notesList;
    private Calendar selectedDate;
    private Integer selectedYear = null;
    private Integer selectedMonth = null;
    private boolean weekMode = false;
    private boolean inDeleteMode = false;

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
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        final Toolbar toolbar = view.findViewById(R.id.toolBar);
        toolbar.setTitle(R.string.title_activity_notes);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);
        notesList = new ArrayList<>();
        mAdapter = new SimpleNotesRecyclerViewAdapter(notesList);
        RecyclerView recyclerView = view.findViewById(R.id.notesListRecyclerView);
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
        switch (item.getItemId()) {
            case R.id.action_delete:
                inDeleteMode = true;
                if (getActivity() != null)
                    getActivity().invalidateOptionsMenu();
                mAdapter.clearSelectedFiles();
                mAdapter.setDeleteMode(true);
                mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
                return true;
            case R.id.action_delete_selected:
                inDeleteMode = false;
                if (getActivity() != null)
                    getActivity().invalidateOptionsMenu();
                mAdapter.setDeleteMode(false);
                mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
                List<File> selectedFiles = mAdapter.getSelectedFiles();
                mPresenter.deleteFiles(selectedFiles);
                return true;
            case R.id.action_cancel:
                inDeleteMode = false;
                if (getActivity() != null)
                    getActivity().invalidateOptionsMenu();
                mAdapter.setDeleteMode(false);
                mAdapter.clearSelectedFiles();
                mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
                return true;
        }
        return super.onOptionsItemSelected(item);
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

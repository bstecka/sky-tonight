package com.example.barbara.skytonight.presentation.notes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.NoteFile;
import com.example.barbara.skytonight.presentation.notes.NoteContract;

import java.util.Calendar;

public class NoteFragment extends Fragment implements NoteContract.View {

    private NoteContract.Presenter mPresenter;
    private Calendar selectedDate;
    private View view;
    private boolean editModeEnabled = false;
    private boolean weekMode = false;
    private boolean createMode = false;
    String filePath;

    @Override
    public void setPresenter(NoteContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_note, container, false);
        final android.support.design.widget.FloatingActionButton button = view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!editModeEnabled) {
                    TextView textView = view.findViewById(R.id.textView);
                    EditText editText = view.findViewById(R.id.editText);
                    textView.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.VISIBLE);
                    button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_done));
                    editModeEnabled = true;
                }
                else {
                    TextView textView = view.findViewById(R.id.textView);
                    EditText editText = view.findViewById(R.id.editText);
                    textView.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                    mPresenter.saveFile(editText.getText().toString());
                    button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit));
                    editModeEnabled = false;
                }
            }
        });
        return view;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    public void setWeekMode(boolean value) {
        weekMode = value;
    }

    public void setCreateMode(boolean value) { createMode = value; }

    @Override
    public void exitCreateMode() { createMode = false; }

    @Override
    public boolean isCreateModeEnabled() {
        return createMode;
    }

    @Override
    public boolean isWeekModeEnabled() {
        return weekMode;
    }

    public boolean isEditModeEnabled() { return editModeEnabled; }

    @Override
    public Context getContext() { return view.getContext(); }

    @Override
    public Calendar getSelectedDate() { return selectedDate; }

    public void setSelectedDate(Calendar selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void setText(String text) {
        TextView textView = view.findViewById(R.id.textView);
        EditText editText = view.findViewById(R.id.editText);
        textView.setText(text);
        editText.setText(text);
        textView.refreshDrawableState();
        editText.refreshDrawableState();
    }

    @Override
    public Activity getViewActivity() { return getActivity(); }

}

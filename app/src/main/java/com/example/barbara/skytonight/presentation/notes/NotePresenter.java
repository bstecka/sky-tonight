package com.example.barbara.skytonight.presentation.notes;

import android.os.Environment;
import com.example.barbara.skytonight.data.NoteDataSource;
import com.example.barbara.skytonight.data.NoteRepository;
import com.example.barbara.skytonight.data.local.NoteLocalDataSource;

import java.io.File;

public class NotePresenter implements NoteContract.Presenter {

    private final NoteContract.View mNotesView;
    private NoteRepository noteRepository;

    public NotePresenter(NoteContract.View mNotesView) {
        this.mNotesView = mNotesView;
        File storageDir = mNotesView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        this.noteRepository = NoteRepository.getInstance(NoteLocalDataSource.getInstance(storageDir));
    }

    @Override
    public void start() {
        readFiles();
    }

    private void readFiles() {
        noteRepository.readNotesForDay(mNotesView.getSelectedDate(), new NoteDataSource.GetNoteFilesCallback() {
            @Override
            public void onDataLoaded(NoteFile file) {
                mNotesView.setText(file.getContent());
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    public void saveFile(String text) {
        noteRepository.saveFile(mNotesView.getSelectedDate(), text);
        mNotesView.setText(text);
    }
}

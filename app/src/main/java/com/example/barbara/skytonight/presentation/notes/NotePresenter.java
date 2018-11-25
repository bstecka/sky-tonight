package com.example.barbara.skytonight.presentation.notes;

import com.example.barbara.skytonight.data.NoteDataSource;
import com.example.barbara.skytonight.data.RepositoryFactory;
import com.example.barbara.skytonight.data.repository.NoteRepository;
import com.example.barbara.skytonight.entity.NoteFile;

public class NotePresenter implements NoteContract.Presenter {

    private final NoteContract.View mNotesView;
    private NoteRepository noteRepository;

    public NotePresenter(NoteContract.View mNotesView) {
        this.mNotesView = mNotesView;
        this.noteRepository = RepositoryFactory.getNoteRepository(mNotesView.getContext());
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

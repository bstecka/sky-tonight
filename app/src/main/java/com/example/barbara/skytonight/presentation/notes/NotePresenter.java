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
        readFile();
    }

    private void readFile() {
        if (!mNotesView.isCreateModeEnabled()) {
            noteRepository.readSingleNote(mNotesView.getFilePath(), new NoteDataSource.GetNoteFilesCallback() {
                @Override
                public void onDataLoaded(NoteFile file) {
                    mNotesView.setText(file.getContent());
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }
    }

    public void saveFile(String text) {
        if (mNotesView.isCreateModeEnabled()) {
            noteRepository.saveFile(mNotesView.getSelectedDate(), text);
            mNotesView.exitCreateMode();
        }
        else
            noteRepository.replaceFile(mNotesView.getFilePath(), text);
        mNotesView.setText(text);
    }
}

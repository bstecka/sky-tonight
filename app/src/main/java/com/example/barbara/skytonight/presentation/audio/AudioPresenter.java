package com.example.barbara.skytonight.presentation.audio;

import android.media.MediaRecorder;
import android.util.Log;

import com.example.barbara.skytonight.data.AudioDataSource;
import com.example.barbara.skytonight.data.RepositoryFactory;
import com.example.barbara.skytonight.data.repository.AudioRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AudioPresenter implements AudioContract.Presenter {

    private final AudioContract.View mAudioView;
    private MediaRecorder mRecorder = null;
    private AudioRepository audioRepository;

    public AudioPresenter(AudioContract.View mAudioView) {
        this.mAudioView = mAudioView;
        this.audioRepository = RepositoryFactory.getAudioRepository(mAudioView.getContext());
    }

    @Override
    public void start() {
        mAudioView.clearListInView();
        readFiles();
    }

    @Override
    public void startRecording() {
        File file = createFile();
        String fileName = file.getAbsolutePath();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("AudioPresenter", "prepare() failed");
        }
        mRecorder.start();
    }

    @Override
    public void deleteFiles(List<File> fileList){
        audioRepository.deleteFiles(fileList);
        mAudioView.clearListInView();
        readFiles();
    }

    @Override
    public void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void readFilesForDay(Calendar selectedDate) {
        final ArrayList<File> list = mAudioView.getFileList();
        audioRepository.readFilesForDay(selectedDate, new AudioDataSource.GetAudioFilesCallback() {
            @Override
            public void onDataLoaded(List<File> files) {
                list.clear();
                list.addAll(files);
                mAudioView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readFilesForWeek(final Calendar selectedDate) {
        final ArrayList<File> list = mAudioView.getFileList();
        audioRepository.readFilesForWeek(selectedDate, new AudioDataSource.GetAudioFilesCallback() {
            @Override
            public void onDataLoaded(List<File> files) {
                list.clear();
                list.addAll(files);
                mAudioView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readFilesForMonth(int month, int year) {
        final ArrayList<File> list = mAudioView.getFileList();
        audioRepository.readFilesForMonth(month, year, new AudioDataSource.GetAudioFilesCallback() {
            @Override
            public void onDataLoaded(List<File> files) {
                list.clear();
                list.addAll(files);
                mAudioView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readFiles() {
        Calendar selectedDate = mAudioView.getSelectedDate();
        if (mAudioView.isWeekModeEnabled()) {
            readFilesForWeek(selectedDate);
        } else if (selectedDate != null) {
            readFilesForDay(selectedDate);
        } else if (mAudioView.getSelectedMonth() != null) {
            readFilesForMonth(mAudioView.getSelectedMonth(), mAudioView.getSelectedYear());
        }
    }

    private File createFile() {
        return audioRepository.createFile(mAudioView.getSelectedDate());
    }
}

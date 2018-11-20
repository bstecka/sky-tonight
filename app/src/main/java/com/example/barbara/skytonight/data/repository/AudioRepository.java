package com.example.barbara.skytonight.data.repository;

import com.example.barbara.skytonight.data.AudioDataSource;

import java.io.File;
import java.util.Calendar;

public class AudioRepository implements AudioDataSource {

    private static AudioRepository INSTANCE = null;
    private AudioDataSource mAudioDataSource;

    private AudioRepository(AudioDataSource audioDataSource) {
        mAudioDataSource = audioDataSource;
    }

    public static AudioRepository getInstance(AudioDataSource audioDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new AudioRepository(audioDataSource);
        }
        return INSTANCE;
    }

    @Override
    public File createFile(Calendar date) {
        return mAudioDataSource.createFile(date);
    }

    @Override
    public void readFilesForDay(Calendar date, GetAudioFilesCallback callback) {
        mAudioDataSource.readFilesForDay(date, callback);
    }

    @Override
    public void readFilesForWeek(Calendar date, GetAudioFilesCallback callback) {
        mAudioDataSource.readFilesForWeek(date, callback);
    }

    @Override
    public void readFilesForMonth(int month, int year, GetAudioFilesCallback callback) {
        mAudioDataSource.readFilesForMonth(month, year, callback);
    }
}

package com.example.barbara.skytonight.data.repository;

import com.example.barbara.skytonight.data.VideoDataSource;

import java.io.File;
import java.util.Calendar;

public class VideoRepository implements VideoDataSource {

    private static VideoRepository INSTANCE = null;
    private VideoDataSource mVideoDataSource;

    private VideoRepository(VideoDataSource videoDataSource) {
        mVideoDataSource = videoDataSource;
    }

    public static VideoRepository getInstance(VideoDataSource videoDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new VideoRepository(videoDataSource);
        }
        return INSTANCE;
    }

    @Override
    public File createFile(Calendar date) {
        return mVideoDataSource.createFile(date);
    }

    @Override
    public void readFilesForDay(Calendar date, GetVideoFilesCallback callback) {
        mVideoDataSource.readFilesForDay(date, callback);
    }

    @Override
    public void readFilesForWeek(Calendar date, GetVideoFilesCallback callback) {
        mVideoDataSource.readFilesForWeek(date, callback);
    }

    @Override
    public void readFilesForMonth(int month, int year, GetVideoFilesCallback callback) {
        mVideoDataSource.readFilesForMonth(month, year, callback);
    }
}

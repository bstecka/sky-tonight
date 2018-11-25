package com.example.barbara.skytonight.presentation.video;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.example.barbara.skytonight.data.RepositoryFactory;
import com.example.barbara.skytonight.data.VideoDataSource;
import com.example.barbara.skytonight.data.repository.VideoRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VideoPresenter implements VideoContract.Presenter {

    private final VideoContract.View mVideoView;
    private VideoRepository videoRepository;

    public VideoPresenter(VideoContract.View mVideoView) {
        this.mVideoView = mVideoView;
        this.videoRepository = RepositoryFactory.getVideoRepository(mVideoView.getContext());
    }

    @Override
    public void start() {
        mVideoView.clearListInView();
        readFiles();
    }

    public void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(mVideoView.getViewActivity().getPackageManager()) != null) {
            File videoFile = createFile();
            if (videoFile != null) {
                Uri videoURI = FileProvider.getUriForFile(mVideoView.getContext(), "com.example.barbara.skytonight.fileprovider", videoFile);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
            }
            mVideoView.startVideoActivity(takeVideoIntent);
        }
    }

    private void readFilesForDay(Calendar selectedDate) {
        final ArrayList<File> list = mVideoView.getFileList();
        videoRepository.readFilesForDay(selectedDate, new VideoDataSource.GetVideoFilesCallback() {
            @Override
            public void onDataLoaded(List<File> files) {
                list.clear();
                list.addAll(files);
                mVideoView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readFilesForWeek(final Calendar selectedDate) {
        final ArrayList<File> list = mVideoView.getFileList();
        videoRepository.readFilesForWeek(selectedDate, new VideoDataSource.GetVideoFilesCallback() {
            @Override
            public void onDataLoaded(List<File> files) {
                list.clear();
                list.addAll(files);
                mVideoView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readFilesForMonth(int month, int year) {
        final ArrayList<File> list = mVideoView.getFileList();
        videoRepository.readFilesForMonth(month, year, new VideoDataSource.GetVideoFilesCallback() {
            @Override
            public void onDataLoaded(List<File> files) {
                list.clear();
                list.addAll(files);
                mVideoView.refreshListInView();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void readFiles() {
        Calendar selectedDate = mVideoView.getSelectedDate();
        if (mVideoView.isWeekModeEnabled()) {
            readFilesForWeek(selectedDate);
        } else if (selectedDate != null) {
            readFilesForDay(selectedDate);
        } else if (mVideoView.getSelectedMonth() != null) {
            readFilesForMonth(mVideoView.getSelectedMonth(), mVideoView.getSelectedYear());
        }
    }

    private File createFile() {
        return videoRepository.createFile(mVideoView.getSelectedDate());
    }

}

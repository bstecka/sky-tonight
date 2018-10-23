package com.example.barbara.skytonight.video;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.barbara.skytonight.video.VideoFragment.REQUEST_VIDEO_CAPTURE;

public class VideoPresenter implements VideoContract.Presenter {

    private final VideoContract.View mVideoView;
    private MediaRecorder mRecorder = null;

    public VideoPresenter(VideoContract.View mVideoView) {
        this.mVideoView = mVideoView;
    }

    @Override
    public void start() {
        mVideoView.clearListInView();
        readFiles();
    }

    public void startRecording() {
        File file = null;
        try {
            file = createFile(mVideoView.getViewActivity());
            String fileName = file.getAbsolutePath();
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setOutputFile(fileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
            mRecorder.setMaxFileSize(10000);
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("AudioPresenter", "prepare() failed");
            }
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private void readFilesForDay(Calendar selectedDate) {
        ArrayList<File> list = mVideoView.getFileList();
        File storageDir = mVideoView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        final String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(selectedDate.getTime());
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(timeStamp);
                }
            });
            for (File file : filteredFiles)
                list.add(file);
            mVideoView.refreshListInView();
        }
    }

    private void readFilesForWeek() {
        ArrayList<File> list = mVideoView.getFileList();
        File storageDir = mVideoView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    Calendar now = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    try {
                        Date date = sdf.parse(name.substring(4, 13));
                        calendar.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                    return now.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR) && now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                }
            });
            for (File file : filteredFiles)
                list.add(file);
            mVideoView.refreshListInView();
        }
    }

    public void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(mVideoView.getViewActivity().getPackageManager()) != null) {
            File videoFile = null;
            try {
                videoFile = createFile(mVideoView.getViewActivity());
            } catch (IOException ex) {
                Log.e("VideoPresenter", "IOException");
            }
            if (videoFile != null) {
                Uri videoURI = FileProvider.getUriForFile(mVideoView.getContext(), "com.example.barbara.skytonight.fileprovider", videoFile);
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
            }
            mVideoView.startVideoActivity(takeVideoIntent);
        }
    }

    private void readFilesForMonth(int month, int year) {
        ArrayList<File> list = mVideoView.getFileList();
        File storageDir = mVideoView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(Calendar.MONTH, month);
        selectedDate.set(Calendar.YEAR, year);
        final String timeStamp = new SimpleDateFormat("yyyyMM", Locale.getDefault()).format(selectedDate.getTime());
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(timeStamp);
                }
            });
            for (File file : filteredFiles)
                list.add(file);
            mVideoView.refreshListInView();
        }
    }

    private void readFiles() {
        Calendar selectedDate = mVideoView.getSelectedDate();
        if (selectedDate != null) {
            readFilesForDay(selectedDate);
        } else if (mVideoView.getSelectedMonth() != null) {
            readFilesForMonth(mVideoView.getSelectedMonth(), mVideoView.getSelectedYear());
        } else {
            readFilesForWeek();
        }
    }

    private File createFile(Activity activity) throws IOException {
        Calendar selectedDate = mVideoView.getSelectedDate();
        String timeStamp;
        if (selectedDate != null)
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(selectedDate.getTime());
        else
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String videoFileName = "MP4_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        return File.createTempFile(videoFileName, ".mp4", storageDir);
    }

}

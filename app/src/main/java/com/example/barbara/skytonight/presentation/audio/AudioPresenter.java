package com.example.barbara.skytonight.presentation.audio;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.example.barbara.skytonight.presentation.audio.AudioContract;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AudioPresenter implements AudioContract.Presenter {

    private final AudioContract.View mAudioView;
    private MediaRecorder mRecorder = null;

    public AudioPresenter(AudioContract.View mAudioView) {
        this.mAudioView = mAudioView;
    }

    @Override
    public void start() {
        mAudioView.clearListInView();
        readFiles();
    }

    public void startRecording() {
        File file = null;
        try {
            file = createFile(mAudioView.getViewActivity());
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
        ArrayList<File> list = mAudioView.getFileList();
        File storageDir = mAudioView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM);
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
            mAudioView.refreshListInView();
        }
    }

    private void readFilesForWeek(final Calendar selectedDate) {
        ArrayList<File> list = mAudioView.getFileList();
        File storageDir = mAudioView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    try {
                        Date date = sdf.parse(name.substring(4, 13));
                        calendar.setTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                    return selectedDate.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR) && selectedDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
                }
            });
            for (File file : filteredFiles)
                list.add(file);
            mAudioView.refreshListInView();
        }
    }

    private void readFilesForMonth(int month, int year) {
        ArrayList<File> list = mAudioView.getFileList();
        File storageDir = mAudioView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DCIM);
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
            mAudioView.refreshListInView();
        }
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

    private File createFile(Activity activity) throws IOException {
        Calendar selectedDate = mAudioView.getSelectedDate();
        String timeStamp;
        if (selectedDate != null)
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(selectedDate.getTime());
        else
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String imageFileName = "3GP_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        return File.createTempFile(imageFileName, ".3gp", storageDir);
    }

}

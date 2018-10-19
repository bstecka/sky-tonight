package com.example.barbara.skytonight.notes;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class NotesListPresenter implements NotesListContract.Presenter {

    private final NotesListContract.View mNotesListView;

    public NotesListPresenter(NotesListContract.View mNotesListView) {
        this.mNotesListView = mNotesListView;
    }

    @Override
    public void start() {
        mNotesListView.clearListInView();
        readNotesAsync();
    }

    private void readNotesAsyncForDay(Calendar selectedDate) {
        File storageDir = mNotesListView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        final String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(selectedDate.getTime());
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(timeStamp);
                }
            });
            for (File file : filteredFiles)
                new ReadTextFileTask(file, mNotesListView.getNotesList(), mNotesListView).execute(file);
        }
    }

    private void readNotesAsyncForWeek() {
        File storageDir = mNotesListView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
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
            for (File file : filteredFiles) {
                new ReadTextFileTask(file, mNotesListView.getNotesList(), mNotesListView).execute(file);
            }
        }
    }

    private void readNotesAsyncForMonth(int month, int year) {
        File storageDir = mNotesListView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
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
                new ReadTextFileTask(file, mNotesListView.getNotesList(), mNotesListView).execute(file);
        }
    }

    private void readNotesAsync() {
        Calendar selectedDate = mNotesListView.getSelectedDate();
        if (selectedDate != null) {
            readNotesAsyncForDay(selectedDate);
        } else if (mNotesListView.getSelectedMonth() != null) {
            readNotesAsyncForMonth(mNotesListView.getSelectedMonth(), mNotesListView.getSelectedYear());
        } else {
            readNotesAsyncForWeek();
        }
    }

    private static class ReadTextFileTask extends AsyncTask<File, Void, NoteFile> {
        NotesListContract.View view;
        ArrayList<NoteFile> list;
        File file;

        ReadTextFileTask(File file, ArrayList<NoteFile> list, NotesListContract.View view){
            this.file = file;
            this.list = list;
            this.view = view;
        }

        @Override
        protected NoteFile doInBackground(File... params) {
            return readFile(params[0]);
        }

        private NoteFile readFile(File file){
            StringBuilder stringBuilder = new StringBuilder((int)file.length());
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
                String lineSeparator = System.getProperty("line.separator");
                try {
                    while(scanner.hasNextLine())
                        stringBuilder.append(scanner.nextLine() + lineSeparator);
                    return new NoteFile(stringBuilder.toString(), file);
                } finally {
                    scanner.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NoteFile result) {
            if (result != null && result.getContent().trim().length() > 1) {
                list.add(result);
                view.refreshListInView();
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}

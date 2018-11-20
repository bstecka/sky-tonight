package com.example.barbara.skytonight.data.local;
import android.os.AsyncTask;
import android.util.Log;

import com.example.barbara.skytonight.data.NoteDataSource;
import com.example.barbara.skytonight.presentation.notes.NoteFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class NoteLocalDataSource implements NoteDataSource {

    private static NoteLocalDataSource INSTANCE;
    private File storageDir;

    private NoteLocalDataSource() {}

    private NoteLocalDataSource(File storageDir) {
        this.storageDir = storageDir;
    }

    public static NoteLocalDataSource getInstance(File storageDir) {
        if (INSTANCE == null) {
            INSTANCE = new NoteLocalDataSource(storageDir);
        }
        return INSTANCE;
    }

    public void saveFile(Calendar date, String content) {
        final String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date.getTime());
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) { return name.contains(timeStamp); }
            });
            for (File file : filteredFiles)
                file.delete();
        }
        try {
            File file = createTextFile(date);
            writeStringAsFile(file, content);
        } catch (IOException e) {
            Log.e("NoteLocalDataSource", "IOException");
        }
    }

    private File createTextFile(Calendar date) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date.getTime());
        String imageFileName = "TXT_" + timeStamp + "_";
        return File.createTempFile(imageFileName, ".txt", storageDir);
    }

    private void writeStringAsFile(File file, String fileContents) {
        try {
            FileWriter out = new FileWriter(file);
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            Log.e("NoteLocalDataSource", "IOException");
        }
    }

    public void readNotesForDay(Calendar selectedDate, GetNoteFilesCallback callback) {
        final String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(selectedDate.getTime());
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(timeStamp);
                }
            });
            for (File file : filteredFiles)
                new NoteLocalDataSource.ReadTextFileTask(file, callback).execute(file);
        }
    }

    public void readNotesForWeek(final Calendar selectedDate, GetNoteFilesCallback callback) {
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
            for (File file : filteredFiles) {
                new NoteLocalDataSource.ReadTextFileTask(file, callback).execute(file);
            }
        }
    }

    public void readNotesForMonth(int month, int year, GetNoteFilesCallback callback) {
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
                new NoteLocalDataSource.ReadTextFileTask(file, callback).execute(file);
        }
    }

    private static class ReadTextFileTask extends AsyncTask<File, Void, NoteFile> {
        File file;
        GetNoteFilesCallback callback;

        ReadTextFileTask(File file, GetNoteFilesCallback callback){
            this.file = file;
            this.callback = callback;
        }

        @Override
        protected NoteFile doInBackground(File... params) {
            return readFile(params[0]);
        }

        private NoteFile readFile(File file){
            StringBuilder stringBuilder = new StringBuilder((int)file.length());
            Scanner scanner;
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
                callback.onDataLoaded(result);
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}

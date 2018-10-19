package com.example.barbara.skytonight.notes;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

public class NotePresenter implements NoteContract.Presenter {

    private final NoteContract.View mNotesView;

    public NotePresenter(NoteContract.View mNotesView) {
        this.mNotesView = mNotesView;
    }

    @Override
    public void start() {
        readFilesAsync();
    }

    private void readFilesAsync() {
        File storageDir = mNotesView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        Calendar date = mNotesView.getSelectedDate();
        final String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date.getTime());
        if (storageDir != null) {
            File[] filteredFiles = storageDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) { return name.contains(timeStamp); }
            });
            for (File file : filteredFiles)
                new ReadTextFileTask(file, mNotesView).execute(file);
        }
    }

    private void writeStringAsFile(File file, String fileContents) {
        try {
            FileWriter out = new FileWriter(file);
            out.write(fileContents);
            out.close();
            mNotesView.setText(fileContents);
        } catch (IOException e) {
            Log.e("NotePresenter", "IOException");
        }
    }

    public void saveFile(String text) {
        File storageDir = mNotesView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        Calendar date = mNotesView.getSelectedDate();
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
            File file = createTextFile(mNotesView.getViewActivity());
            writeStringAsFile(file, text);
        } catch (IOException e) {
            Log.e("NotePresenter", "IOException");
        }
    }

    private File createTextFile(Activity activity) throws IOException {
        Calendar date = mNotesView.getSelectedDate();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(date.getTime());
        String imageFileName = "TXT_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        return File.createTempFile(imageFileName, ".txt", storageDir);
    }

    private static class ReadTextFileTask extends AsyncTask<File, Void, String> {
        NoteContract.View view;
        File file;

        ReadTextFileTask(File file, NoteContract.View view){
            this.file = file;
            this.view = view;
        }

        @Override
        protected String doInBackground(File... params) {
            return readFile(params[0]);
        }

        private String readFile(File file){
            StringBuilder stringBuilder = new StringBuilder((int)file.length());
            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
                String lineSeparator = System.getProperty("line.separator");
                try {
                    while(scanner.hasNextLine())
                        stringBuilder.append(scanner.nextLine() + lineSeparator);
                    return stringBuilder.toString();
                } finally {
                    scanner.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            view.setText(result);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}

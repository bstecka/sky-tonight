package com.example.barbara.skytonight.notes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.barbara.skytonight.photos.ImageFile;
import com.example.barbara.skytonight.photos.PhotoGalleryContract;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotesPresenter implements NotesContract.Presenter {

    private final NotesContract.View mNotesView;

    public NotesPresenter(NotesContract.View mNotesView) {
        this.mNotesView = mNotesView;
    }

    @Override
    public void start() {
        mNotesView.clearListInView();
        readPhotosAsync();
    }

    @Override
    public void dispatchTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mNotesView.getViewActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(mNotesView.getViewActivity());
            } catch (IOException ex) {
                Log.e("PhotoGallery", "IOException");
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mNotesView.getContext(), "com.example.barbara.skytonight.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                mNotesView.startPhotoActivity(takePictureIntent);
            }
        }
    }

    private void readPhotosAsync() {
        File storageDir = mNotesView.getViewActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            File[] allFilesInDir = storageDir.listFiles();
            for (File file : allFilesInDir) {
                Calendar modificationTime = Calendar.getInstance();
                modificationTime.setTime(new Date(file.lastModified()));
                Calendar date = mNotesView.getSelectedDate();
                if (date.get(Calendar.DAY_OF_YEAR) == modificationTime.get(Calendar.DAY_OF_YEAR) && date.get(Calendar.YEAR) == modificationTime.get(Calendar.YEAR))
                    new DisplaySingleImageTask(file, mNotesView.getPhotoList(), mNotesView).execute(file);
            }
        }
    }

    private File createImageFile(Activity activity) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private static class DisplaySingleImageTask extends AsyncTask<File, Void, Boolean> {
        NotesContract.View view;
        ArrayList<ImageFile> list;
        File file;

        DisplaySingleImageTask(File file, ArrayList<ImageFile> list, NotesContract.View view){
            this.file = file;
            this.list = list;
            this.view = view;
        }

        @Override
        protected Boolean doInBackground(File... params) {
            return readFiles(params[0], list);
        }

        private Boolean readFiles(File file, ArrayList<ImageFile> list){
            boolean shouldRefresh = false;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (bitmap != null) {
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, false);
                list.add(new ImageFile(scaled, file));
                shouldRefresh = true;
            }
            return shouldRefresh;
        }

        @Override
        protected void onPostExecute(Boolean shouldRefresh) {
            if (shouldRefresh)
                view.refreshListInView();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}

package com.example.barbara.skytonight.photos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.barbara.skytonight.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PhotoGalleryActivity extends AppCompatActivity implements PhotoGalleryContract.View {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private MyPhotoRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Bitmap> photoList;
    private String lastSavedFilePath;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = File.createTempFile(imageFileName, ".jpg", storageDir);
        lastSavedFilePath = file.getAbsolutePath();
        return file;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoList = new ArrayList<>();
        setContentView(R.layout.activity_photo_gallery);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        android.support.design.widget.FloatingActionButton button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        readFilesAsync();
        mAdapter = new MyPhotoRecyclerViewAdapter(photoList);
        recyclerView = findViewById(R.id.photoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    private void readFilesAsync() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (storageDir != null) {
            File[] allFilesInDir = storageDir.listFiles();
            for (int i = 0; i < allFilesInDir.length; i++) {
                File file = allFilesInDir[i];
                Calendar modificationTime = Calendar.getInstance();
                modificationTime.setTime(new Date(file.lastModified()));
                Calendar now = Calendar.getInstance();
                if (now.get(Calendar.DAY_OF_YEAR) == modificationTime.get(Calendar.DAY_OF_YEAR) && now.get(Calendar.YEAR) == modificationTime.get(Calendar.YEAR)) {
                    new DisplaySingleImageTask(file, photoList, this).execute(file);
                }
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("PhotoGallery", "IOException");
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.barbara.skytonight.fileprovider", photoFile);
                Log.e("PhotoGallery", photoURI.getPath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void refreshListInView() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, R.string.photo_saved, Toast.LENGTH_SHORT).show();
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(imageBitmap);
            }
            if (lastSavedFilePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(lastSavedFilePath);
                photoList.add(0, bitmap);
                mAdapter.notifyDataSetChanged();
                Log.e("PhotoGallery", "Notify dataset changed");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setPresenter(PhotoGalleryContract.Presenter presenter) {

    }

    private static class DisplaySingleImageTask extends AsyncTask<File, Void, Boolean> {

        PhotoGalleryContract.View view;
        ArrayList<Bitmap> list;
        File file;

        DisplaySingleImageTask(File file, ArrayList<Bitmap> list, PhotoGalleryContract.View view){
            this.file = file;
            this.list = list;
            this.view = view;
        }

        @Override
        protected Boolean doInBackground(File... params) {
            return readFiles(params[0], list);
        }

        private Boolean readFiles(File file, ArrayList<Bitmap> list){
            boolean shouldRefresh = false;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (bitmap != null) {
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 4, bitmap.getHeight() / 4, false);
                list.add(scaled);
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

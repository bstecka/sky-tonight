package com.example.barbara.skytonight.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.photos.ImageFile;
import com.example.barbara.skytonight.photos.MyPhotoRecyclerViewAdapter;
import com.example.barbara.skytonight.photos.PhotoGalleryContract;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class NotesFragment extends Fragment implements NotesContract.View {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private NotesContract.Presenter mPresenter;
    private ArrayList<ImageFile> photoList;
    private Calendar selectedDate;
    private View view;
    private boolean editModeEnabled = false;

    @Override
    public void setPresenter(NotesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (photoList.isEmpty())
            mPresenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_notes, container, false);
        photoList = new ArrayList<>();
        final android.support.design.widget.FloatingActionButton button = view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!editModeEnabled) {
                    TextView textView = view.findViewById(R.id.textView);
                    EditText editText = view.findViewById(R.id.editText);
                    textView.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.VISIBLE);
                    button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_done));
                    editModeEnabled = true;
                }
                else {
                    TextView textView = view.findViewById(R.id.textView);
                    EditText editText = view.findViewById(R.id.editText);
                    textView.setVisibility(View.VISIBLE);
                    editText.setVisibility(View.INVISIBLE);
                    button.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit));
                    editModeEnabled = false;
                }
            }
        });
        return view;
    }

    @Override
    public Context getContext() { return view.getContext(); }

    @Override
    public ArrayList<ImageFile> getPhotoList() { return photoList; }

    @Override
    public Calendar getSelectedDate() { return selectedDate; }

    @Override
    public void setSelectedDate(Calendar selectedDate) {
        this.selectedDate = selectedDate;
    }

    @Override
    public Activity getViewActivity() { return getActivity(); }

    @Override
    public void clearListInView() {
        photoList.clear();
    }

    @Override
    public void refreshListInView() {
    }

    @Override
    public void startPhotoActivity(Intent intent) { startActivityForResult(intent, REQUEST_TAKE_PHOTO); }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(view.getContext(), R.string.photo_saved, Toast.LENGTH_SHORT).show();
            photoList.clear();
        }
    }
}

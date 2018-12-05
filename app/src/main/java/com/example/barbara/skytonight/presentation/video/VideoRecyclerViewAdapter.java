package com.example.barbara.skytonight.presentation.video;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.barbara.skytonight.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder> {

    private final List<File> mValues;
    public static final String FILE_PATH_TAG = "FILE_PATH";
    private final List<File> selectedFiles;
    private boolean inDeleteMode = false;

    public VideoRecyclerViewAdapter(List<File> items) {
        mValues = items;
        selectedFiles = new ArrayList<>();
    }

    public void clearSelectedFiles() {
        selectedFiles.clear();
    }

    public List<File> getSelectedFiles() {
        return selectedFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        return new ViewHolder(view);
    }

    public void setDeleteMode(boolean value) {
        inDeleteMode = value;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final File file = mValues.get(position);
        final Context context = holder.mTextView.getContext();
        CheckBox checkBox = holder.mView.findViewById(R.id.checkBox);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault());
        Date date;
        try {
            date = sdf.parse(file.getName().substring(4, 17));
            calendar.setTime(date);
            SimpleDateFormat readable = new SimpleDateFormat("MMM dd yyyy, HH:mm", Locale.getDefault());
            holder.mTextView.setText(readable.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.mTextView.setText(file.getName());
        }
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(context, file);
            }
        });
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(context, file);
            }
        });
        if (inDeleteMode)
            holder.mView.findViewById(R.id.checkBox).setVisibility(View.VISIBLE);
        else
            holder.mView.findViewById(R.id.checkBox).setVisibility(View.INVISIBLE);
        checkBox.setChecked(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    selectedFiles.add(file);
                else
                    selectedFiles.remove(file);
            }
        });
    }

    private void onItemClick(Context context, File file) {
        Intent intent = new Intent(context, FullVideoActivity.class);
        intent.putExtra(FILE_PATH_TAG, file.getAbsolutePath());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final FloatingActionButton mButton;
        final TextView mTextView;

        public ViewHolder(final View view) {
            super(view);
            mView = view;
            mButton = view.findViewById(R.id.floatingActionButton);
            mTextView = view.findViewById(R.id.fileTextView);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}

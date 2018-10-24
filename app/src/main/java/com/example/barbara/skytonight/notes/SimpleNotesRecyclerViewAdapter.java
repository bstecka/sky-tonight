package com.example.barbara.skytonight.notes;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barbara.skytonight.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class SimpleNotesRecyclerViewAdapter extends RecyclerView.Adapter<SimpleNotesRecyclerViewAdapter.ViewHolder> {

    private final List<NoteFile> mValues;

    public SimpleNotesRecyclerViewAdapter(List<NoteFile> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_notes_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final File file = mValues.get(position).getFile();
        final Context context = holder.mTextView.getContext();
        holder.mItem = String.valueOf(file.toString());
        holder.mTextView.setText(file.getName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
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
    }

    private void onItemClick(final Context context, File file) {
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra("filePath", file.getName());
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
        public String mItem;

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

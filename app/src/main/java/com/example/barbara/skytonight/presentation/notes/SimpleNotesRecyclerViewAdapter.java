package com.example.barbara.skytonight.presentation.notes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.NoteFile;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault());
        Date date;
        try {
            date = sdf.parse(file.getName().substring(4, 18));
            calendar.setTime(date);
            SimpleDateFormat readable = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
            holder.mTextView.setText(readable.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.mTextView.setText(file.getName());
        }
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

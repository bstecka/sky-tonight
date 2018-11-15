package com.example.barbara.skytonight.presentation.notes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barbara.skytonight.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    private final List<NoteFile> mValues;
    public NotesRecyclerViewAdapter(List<NoteFile> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Context context = holder.mTextView.getContext();
        final NoteFile noteFile = mValues.get(position);
        holder.mItem = noteFile.getContent();
        holder.mTextView.setText(holder.mItem);
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoteActivity.class);
                intent.putExtra("filePath", noteFile.getFile().getName());
                context.startActivity(intent);
            }
        });
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        try {
            Date date = sdf.parse(noteFile.getFile().getName().substring(4, 13));
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mDateTextView.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTextView;
        final TextView mDateTextView;
        public String mItem;

        public ViewHolder(final View view) {
            super(view);
            mView = view;
            mTextView = view.findViewById(R.id.textView);
            mDateTextView = view.findViewById(R.id.dateTextView);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}

package com.example.barbara.skytonight.presentation.photos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.ImageFile;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SimplePhotoRecyclerViewAdapter extends RecyclerView.Adapter<SimplePhotoRecyclerViewAdapter.ViewHolder> {

    private final List<ImageFile> mValues;
    public static final String FILE_PATH = "FILE_PATH";

    public SimplePhotoRecyclerViewAdapter(List<ImageFile> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_photo_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ImageFile imageFile = mValues.get(position);
        final Context context = holder.mImageView.getContext();
        holder.mItem = String.valueOf(imageFile.toString());
        holder.mTextView.setText(imageFile.getFile().getName());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(imageFile.getFile().getName().substring(5, 18));
            calendar.setTime(date);
            SimpleDateFormat readable = new SimpleDateFormat("MMM dd yyyy, HH:mm", Locale.getDefault());
            holder.mTextView.setText(readable.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.mTextView.setText(imageFile.getFile().getName());
        }
        holder.mImageView.setImageBitmap(imageFile.getBitmap());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(context, imageFile.getFile());
            }
        });
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(context, imageFile.getFile());
            }
        });
    }

    private void onItemClick(Context context, File file) {
        Intent intent = new Intent(context, FullPhotoActivity.class);
        intent.putExtra(FILE_PATH, file.getAbsolutePath());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final CircleImageView mImageView;
        final TextView mTextView;
        public String mItem;

        public ViewHolder(final View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.imageView);
            mTextView = view.findViewById(R.id.fileTextView);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
package com.example.barbara.skytonight.presentation.photos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.ImageFile;

import java.util.List;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    private final List<ImageFile> mValues;
    public static final String FILE_PATH = "FILE_PATH";

    public PhotoRecyclerViewAdapter(List<ImageFile> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_gallery_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ImageFile imageFile = mValues.get(position);
        final Context context = holder.mImageView.getContext();
        holder.mItem = String.valueOf(imageFile.toString());
        holder.mImageView.setImageBitmap(imageFile.getBitmap());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullPhotoActivity.class);
                intent.putExtra(FILE_PATH, imageFile.getFile().getAbsolutePath());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImageView;
        public String mItem;

        public ViewHolder(final View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}

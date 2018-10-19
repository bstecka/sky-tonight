package com.example.barbara.skytonight.audio;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.photos.FullPhotoActivity;
import com.example.barbara.skytonight.photos.ImageFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MyAudioRecyclerViewAdapter extends RecyclerView.Adapter<MyAudioRecyclerViewAdapter.ViewHolder> {

    private final List<File> mValues;

    public MyAudioRecyclerViewAdapter(List<File> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final File file = mValues.get(position);
        holder.mItem = String.valueOf(file.toString());
        holder.mTextView.setText(file.getName());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mediaPlayer == null) {
                    holder.mediaPlayer = new MediaPlayer();
                    try {
                        holder.mediaPlayer.setDataSource(file.getAbsolutePath());
                        holder.mediaPlayer.prepare();
                        holder.mediaPlayer.start();
                        holder.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                holder.mButton.setImageResource(R.drawable.ic_play);
                            }
                        });
                    } catch (IOException e) {
                        Log.e("MyAudioRecyclerViewAdapter", "prepare() failed");
                    }
                    holder.mButton.setImageResource(R.drawable.ic_baseline_stop_24px);
                }
                else {
                    holder.mediaPlayer.release();
                    holder.mediaPlayer = null;
                    holder.mButton.setImageResource(R.drawable.ic_play);
                }
            }
        });
        /* final Context context = holder.mTextView.getContext();
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullPhotoActivity.class);
                intent.putExtra(FILE_PATH, imageFile.getFile().getAbsolutePath());
                context.startActivity(intent);
            }
        });*/
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
        MediaPlayer mediaPlayer;

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

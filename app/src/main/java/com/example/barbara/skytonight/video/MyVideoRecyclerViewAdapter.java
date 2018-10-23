package com.example.barbara.skytonight.video;

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

public class MyVideoRecyclerViewAdapter extends RecyclerView.Adapter<MyVideoRecyclerViewAdapter.ViewHolder> {

    private final List<File> mValues;

    public MyVideoRecyclerViewAdapter(List<File> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
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
                        FileInputStream fis = null;
                        fis = new FileInputStream(file.getAbsolutePath());
                        holder.mediaPlayer.setDataSource(fis.getFD());
                        holder.mediaPlayer.prepare();
                        holder.mediaPlayer.start();
                        holder.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                holder.mButton.setImageResource(R.drawable.ic_play);
                                holder.mediaPlayer.release();
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

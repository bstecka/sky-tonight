package com.example.barbara.skytonight.presentation.audio;

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

public class AudioRecyclerViewAdapter extends RecyclerView.Adapter<AudioRecyclerViewAdapter.ViewHolder> {

    private final List<File> mValues;
    private MediaPlayer mediaPlayer;

    public AudioRecyclerViewAdapter(List<File> items) {
        mValues = items;
    }

    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
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
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(holder, file);
            }
        });
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(holder, file);
            }
        });
    }

    private void onItemClick(final ViewHolder holder, File file) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            try {
                FileInputStream fis = null;
                fis = new FileInputStream(file.getAbsolutePath());
                mediaPlayer.setDataSource(fis.getFD());
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        holder.mButton.setImageResource(R.drawable.ic_play);
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                });
            } catch (IOException e) {
                Log.e("AudioRecyclerViewAdapter", "prepare() failed");
            }
            holder.mButton.setImageResource(R.drawable.ic_baseline_stop_24px);
            holder.isPlaying = true;
        }
        else {
            if (holder.isPlaying) {
                mediaPlayer.release();
                mediaPlayer = null;
                holder.mButton.setImageResource(R.drawable.ic_play);
                holder.isPlaying = false;
            }
        }
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
        private boolean isPlaying;

        public ViewHolder(final View view) {
            super(view);
            mView = view;
            mButton = view.findViewById(R.id.floatingActionButton);
            mTextView = view.findViewById(R.id.fileTextView);
            isPlaying = false;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}

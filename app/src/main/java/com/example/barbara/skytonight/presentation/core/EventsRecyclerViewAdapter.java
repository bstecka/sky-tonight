package com.example.barbara.skytonight.presentation.core;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.LunarEclipseEvent;
import com.example.barbara.skytonight.entity.MeteorShowerEvent;
import com.example.barbara.skytonight.entity.SolarEclipseEvent;
import com.example.barbara.skytonight.presentation.core.EventsFragment.OnListFragmentInteractionListener;
import com.example.barbara.skytonight.entity.AstroEvent;
import com.example.barbara.skytonight.presentation.details.LunarDetailsActivity;
import com.example.barbara.skytonight.presentation.details.SolarDetailsActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {

    private final List<AstroEvent> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public EventsRecyclerViewAdapter(List<AstroEvent> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_events_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        AstroEvent event = mValues.get(position);
        try {
            int eclipseTypeStringId = context.getResources().getIdentifier(event.getName(), "string", context.getPackageName());
            holder.mNameView.setText(context.getResources().getString(eclipseTypeStringId));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            holder.mNameView.setText(event.getName());
        }
        SimpleDateFormat readable = new SimpleDateFormat("MMM dd yyyy", Locale.getDefault());
        String dateStr = readable.format(event.getStartDate());
        String peakStr = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getPeakDate());
        if (event.getStartDate().before(event.getEndDate())) {
            dateStr = readable.format(event.getStartDate());
            dateStr += " - ";
            dateStr += readable.format(event.getEndDate());
            peakStr = readable.format(event.getPeakDate());
        }
        holder.mDateView.setText(dateStr);
        holder.mPeakTimeView.setText(context.getString(R.string.astro_event_peak, peakStr));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        if (event instanceof MeteorShowerEvent){
            handleMeteorShowerEvent(holder, (MeteorShowerEvent) event);
            holder.mImageView.setVisibility(View.INVISIBLE);
        } else {
            holder.mZhrView.setVisibility(View.GONE);
            holder.mVisibilityView.setVisibility(View.GONE);
        }
        if (event instanceof LunarEclipseEvent){
            handleLunarEclipse(holder, (LunarEclipseEvent) event);
            holder.mImageView.setVisibility(View.VISIBLE);
        }
        if (event instanceof SolarEclipseEvent){
            handleSolarEclipse(holder, (SolarEclipseEvent) event);
            holder.mImageView.setVisibility(View.VISIBLE);
        }
    }

    private void handleMeteorShowerEvent(final ViewHolder holder, final MeteorShowerEvent meteorShowerEvent) {
        holder.mZhrView.setVisibility(View.VISIBLE);
        holder.mZhrView.setText(context.getString(R.string.zhr, meteorShowerEvent.getZhr()));
        if (meteorShowerEvent.isVisibilityLow()) {
            holder.mVisibilityView.setVisibility(View.VISIBLE);
            holder.mVisibilityView.setText(R.string.low_visibility);
        }
        else
            holder.mVisibilityView.setVisibility(View.GONE);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMeteorShowerClick(holder.mImageView.getContext(), meteorShowerEvent);
            }
        });
    }

    private void handleLunarEclipse(final ViewHolder holder,final LunarEclipseEvent lunarEclipseEvent){
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LunarRecycler", lunarEclipseEvent.getLongitude() + " " + lunarEclipseEvent.getLatitude());
                onLunarEclipseClick(holder.mImageView.getContext(), lunarEclipseEvent);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLunarEclipseClick(holder.mImageView.getContext(), lunarEclipseEvent);
            }
        });
    }

    private void handleSolarEclipse(final ViewHolder holder,final SolarEclipseEvent solarEclipseEvent){
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSolarEclipseClick(holder.mImageView.getContext(), solarEclipseEvent);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSolarEclipseClick(holder.mImageView.getContext(), solarEclipseEvent);
            }
        });
    }

    private void onMeteorShowerClick(Context context, MeteorShowerEvent event) {

    }

    private void onSolarEclipseClick(Context context, SolarEclipseEvent event) {
        Intent intent = new Intent(context, SolarDetailsActivity.class);
        intent.putExtra("event", event);
        context.startActivity(intent);
    }

    private void onLunarEclipseClick(Context context, LunarEclipseEvent event) {
        Intent intent = new Intent(context, LunarDetailsActivity.class);
        intent.putExtra("event", event);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mNameView;
        final TextView mDateView;
        final TextView mPeakTimeView;
        final TextView mZhrView;
        final TextView mVisibilityView;
        final ImageView mImageView;
        public AstroEvent mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.astroEventNameTextView);
            mDateView = view.findViewById(R.id.astroEventDateTextView);
            mPeakTimeView = view.findViewById(R.id.astroEventPeakTextView);
            mZhrView = view.findViewById(R.id.astroEventZhrTextView);
            mVisibilityView = view.findViewById(R.id.astroEventLowVisibilityView);
            mImageView = view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'";
        }
    }
}

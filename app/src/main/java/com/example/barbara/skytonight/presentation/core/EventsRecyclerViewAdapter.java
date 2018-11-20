package com.example.barbara.skytonight.presentation.core;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.MeteorShowerEvent;
import com.example.barbara.skytonight.presentation.core.EventsFragment.OnListFragmentInteractionListener;
import com.example.barbara.skytonight.entity.AstroEvent;

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
        String peakStr = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(event.getPeakDate());
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
            MeteorShowerEvent meteorShowerEvent = (MeteorShowerEvent) event;
            holder.mZhrView.setVisibility(View.VISIBLE);
            holder.mZhrView.setText(context.getString(R.string.zhr, meteorShowerEvent.getZhr()));
        } else {
            holder.mZhrView.setVisibility(View.GONE);
        }
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
        public AstroEvent mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.astroEventNameTextView);
            mDateView = view.findViewById(R.id.astroEventDateTextView);
            mPeakTimeView = view.findViewById(R.id.astroEventPeakTextView);
            mZhrView = view.findViewById(R.id.astroEventZhrTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'";
        }
    }
}

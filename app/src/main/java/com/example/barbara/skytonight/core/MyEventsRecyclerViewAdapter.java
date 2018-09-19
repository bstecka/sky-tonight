package com.example.barbara.skytonight.core;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.core.EventsFragment.OnListFragmentInteractionListener;
import com.example.barbara.skytonight.core.dummy.DummyContent.DummyItem;
import com.example.barbara.skytonight.data.AstroEvent;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MyEventsRecyclerViewAdapter extends RecyclerView.Adapter<MyEventsRecyclerViewAdapter.ViewHolder> {

    private final List<AstroEvent> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context context;

    public MyEventsRecyclerViewAdapter(List<AstroEvent> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_events_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        AstroEvent event = mValues.get(position);
        try {
            int eclipseTypeStringId = context.getResources().getIdentifier(event.getName(), "string", context.getPackageName());
            holder.mNameView.setText(context.getResources().getString(eclipseTypeStringId));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            holder.mNameView.setText(event.getName());
        }
        String dateStr = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(event.getStartDate());
        String timeStr = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(event.getStartDate());
        holder.mDateView.setText(dateStr);
        holder.mPeakTimeView.setText(context.getString(R.string.astro_event_peak, timeStr));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mDateView;
        public final TextView mPeakTimeView;
        public AstroEvent mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.astroEventNameTextView);
            mDateView = (TextView) view.findViewById(R.id.astroEventDateTextView);
            mPeakTimeView = (TextView) view.findViewById(R.id.astroEventPeakTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDateView.getText() + "'";
        }
    }
}

package com.example.barbara.skytonight.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.util.AstroConstants;

import java.util.List;

public class MyTodayRecyclerViewAdapter extends RecyclerView.Adapter<MyTodayRecyclerViewAdapter.ViewHolder> {

    private final List<AstroObject> mValues;
    private final TodayFragment.OnListFragmentInteractionListener mListener;
    private Context context;
    double latitude;
    double longitude;

    public MyTodayRecyclerViewAdapter(List<AstroObject> items, TodayFragment.OnListFragmentInteractionListener listener, double latitude, double longitude) {
        mValues = items;
        mListener = listener;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.fragment_today_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.e("Adapter", latitude + " " + longitude);
        AstroObject object = mValues.get(position);
        holder.mItem = object.getId();
        holder.mNameTextView.setText(object.getName());
        holder.mAltView.setText(context.getString(R.string.astro_object_alt, object.getAltitude(latitude, longitude)));
        try {
            int directionStringId = context.getResources().getIdentifier("dir_" + object.getApproximateDirection().toLowerCase(), "string", context.getPackageName());
            holder.mAzView.setText(context.getResources().getString(directionStringId));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        try {
            int drawableId = context.getResources().getIdentifier("icon_" + object.getName().toLowerCase(), "drawable", context.getPackageName());
            holder.mImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), drawableId, null));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
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
        public final TextView mNameTextView;
        public final ImageView mImageView;
        public final TextView mAltView;
        public final TextView mAzView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameTextView = (TextView) view.findViewById(R.id.astroObjectNameTextView);
            mImageView = (ImageView) view.findViewById(R.id.astroObjectImageView);
            mAltView = (TextView) view.findViewById(R.id.astroObjectAltTextView);
            mAzView = (TextView) view.findViewById(R.id.astroObjectAzTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameTextView.getText() + "'";
        }
    }
}

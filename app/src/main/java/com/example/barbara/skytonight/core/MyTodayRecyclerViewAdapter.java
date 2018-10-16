package com.example.barbara.skytonight.core;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.data.ISSObject;
import com.example.barbara.skytonight.util.AstroConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MyTodayRecyclerViewAdapter extends RecyclerView.Adapter<MyTodayRecyclerViewAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final List<AstroObject> mValues;
    private final TodayFragment.OnListFragmentInteractionListener mListener;
    private Context context;
    private double latitude;
    private double longitude;

    public MyTodayRecyclerViewAdapter(List<AstroObject> items, TodayFragment.OnListFragmentInteractionListener listener, double latitude, double longitude) {
        mValues = items;
        mListener = listener;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_today_list_item, parent, false);
        return new ViewHolder(view);
    }

    private void handlePlanetObject(ViewHolder holder, AstroObject object) {
        holder.mAltView.setText(context.getString(R.string.astro_object_alt, object.getAltitude(latitude, longitude)));
        try {
            int nameStringId = context.getResources().getIdentifier(object.getShortName(), "string", context.getPackageName());
            holder.mNameTextView.setText(context.getResources().getString(nameStringId));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            holder.mNameTextView.setText(object.getName());
        }
        try {
            int directionStringId = context.getResources().getIdentifier("dir_" + object.getApproximateDirectionString().toLowerCase(), "string", context.getPackageName());
            holder.mAzView.setText(context.getResources().getString(directionStringId));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (object.getId() == AstroConstants.ID_MOON) {
                int drawableId = context.getResources().getIdentifier("moon_" + object.getPhaseId(), "drawable", context.getPackageName());
                holder.mImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), drawableId, null));
            }
            else {
                int drawableId = context.getResources().getIdentifier("icon_" + object.getName().toLowerCase(), "drawable", context.getPackageName());
                holder.mImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), drawableId, null));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleISSObject(ViewHolder holder, ISSObject object) {
        DateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        holder.mNameTextView.setText(R.string.astr_obj_1000);
        if (object.getNextFlyby().get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
            holder.mAltView.setText(context.getString(R.string.iss_risetime_today, sdf.format(object.getNextFlyby().getTime())));
        else if (object.getNextFlyby().get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1)
            holder.mAltView.setText(context.getString(R.string.iss_risetime_tomorrow, sdf.format(object.getNextFlyby().getTime())));
        else
            holder.mAltView.setText(context.getString(R.string.iss_risetime, sdf.format(object.getNextFlyby().getTime())));
        holder.mAzView.setText(context.getString(R.string.iss_duration, object.getNextDuration()/60, object.getNextDuration()%60));
        holder.mImageView.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.icon_iss_2, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        AstroObject object = mValues.get(position);
        holder.mItem = String.valueOf(object.getId());
        if (object instanceof ISSObject) {
            ISSObject issObject = (ISSObject) object;
            handleISSObject(holder, issObject);
        } else {
            handlePlanetObject(holder, object);
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
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
        final View mView;
        final TextView mNameTextView;
        final ImageView mImageView;
        final TextView mAltView;
        final TextView mAzView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameTextView = view.findViewById(R.id.astroObjectNameTextView);
            mImageView = view.findViewById(R.id.astroObjectImageView);
            mAltView = view.findViewById(R.id.astroObjectAltTextView);
            mAzView = view.findViewById(R.id.astroObjectAzTextView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameTextView.getText() + "'";
        }
    }
}

package com.example.barbara.skytonight.presentation.details;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.LunarEclipseEvent;

import java.util.Calendar;

public class LunarDetailsFragment extends Fragment implements LunarDetailsContract.View {

    private LunarDetailsContract.Presenter mPresenter;
    private View view;

    @Override
    public void setPresenter(LunarDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_lunar_details, container, false);
        return view;
    }

    @Override
    public void setMoonTimesTextView(String moonrise, String moonset) {
        TextView moonTimes = view.findViewById(R.id.moontimes);
        Context context = moonTimes.getContext();
        moonTimes.setText(context.getString(R.string.moontimes, moonrise, moonset));
    }

    @Override
    public void setSunTimesTextView(String sunrise, String sunset) {
        TextView sunTimes = view.findViewById(R.id.suntimes);
        Context context = sunTimes.getContext();
        sunTimes.setText(context.getString(R.string.suntimes, sunrise, sunset));
    }

    @Override
    public void setTitle(String name, String date) {
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        Context context = dateTextView.getContext();
        try {
            int eclipseTypeStringId = context.getResources().getIdentifier(name, "string", context.getPackageName());
            titleTextView.setText(context.getResources().getString(eclipseTypeStringId));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        dateTextView.setText(date);
    }

    @Override
    public void setDateTextViews(String partB, String partE, String tB, String tE, String gr, String penB, String penE){
        TextView partialBegins = view.findViewById(R.id.partialBegins);
        TextView partialEnds = view.findViewById(R.id.partialEnds);
        TextView totalBegins = view.findViewById(R.id.totalBegins);
        TextView totalEnds = view.findViewById(R.id.totalEnds);
        TextView greatest = view.findViewById(R.id.greatestEclipse);
        TextView penBegins = view.findViewById(R.id.penunmbralBegins);
        TextView penEnds = view.findViewById(R.id.penunmbralEnds);
        Context context = partialBegins.getContext();
        partialBegins.setText(context.getString(R.string.part_begins, partB));
        partialEnds.setText(context.getString(R.string.part_ends, partE));
        greatest.setText(context.getString(R.string.greatest_eclipse, gr));
        if (tB.length() > 1) {
            totalBegins.setText(context.getString(R.string.total_begins, tB));
            totalEnds.setText(context.getString(R.string.total_ends, tE));
        } else {
            totalBegins.setVisibility(View.GONE);
            totalEnds.setVisibility(View.GONE);
        }
        if (penB.length() > 1) {
            penBegins.setText(context.getString(R.string.pen_begins, penB));
            penEnds.setText(context.getString(R.string.pen_ends, penE));
        } else {
            penBegins.setVisibility(View.GONE);
            penEnds.setVisibility(View.GONE);
        }
        view.findViewById(R.id.title1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.title2).setVisibility(View.VISIBLE);
    }

    @Override
    public Context getContext() { return view.getContext(); }

    @Override
    public Activity getViewActivity() { return getActivity(); }

}


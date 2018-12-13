package com.example.barbara.skytonight.presentation.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.entity.AstroEvent;

import java.util.ArrayList;
import java.util.Calendar;

public class EventsFragment extends Fragment implements EventsContract.View {

    private EventsContract.Presenter mPresenter;

    private OnListFragmentInteractionListener mListener;
    private EventsRecyclerViewAdapter mAdapter;
    private Context context;
    private TextView noEventsTextView;
    private TextView monthTextView;
    ArrayList<AstroEvent> list;
    private int currentlyDisplayedMonth;
    private int currentlyDisplayedYear;

    public EventsFragment() {
        this.list = new ArrayList<>();
        this.currentlyDisplayedMonth = Calendar.getInstance().get(Calendar.MONTH);
        this.currentlyDisplayedYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        setCurrentMonthTextView();
        int itemCount = mAdapter.getItemCount();
        if (itemCount < 1)
            displayNoEventsText();
        else
            hideNoEventsText();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.currentlyDisplayedMonth = Calendar.getInstance().get(Calendar.MONTH);
        this.currentlyDisplayedYear = Calendar.getInstance().get(Calendar.YEAR);
        mAdapter = new EventsRecyclerViewAdapter(list, mListener);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    public int getCurrentlyDisplayedMonth() {
        return currentlyDisplayedMonth;
    }

    public int getCurrentlyDisplayedYear() {
        return currentlyDisplayedYear;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_events_list, container, false);
        context = view.getContext();
        noEventsTextView = view.findViewById(R.id.noEventsTextView);
        monthTextView = view.findViewById(R.id.monthTextView);
        RecyclerView recyclerView = view.findViewById(R.id.eventRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);
        AppCompatImageButton nextMonthButton = view.findViewById(R.id.nextMonthButton);
        nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForwards();
                setCurrentMonthTextView();
                mPresenter.showEventsForMonth(currentlyDisplayedMonth+1, currentlyDisplayedYear);
            }
        });
        AppCompatImageButton previousMonthButton = view.findViewById(R.id.previousMonthButton);
        previousMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(currentlyDisplayedYear == Calendar.getInstance().get(Calendar.YEAR) && currentlyDisplayedMonth == Calendar.getInstance().get(Calendar.MONTH))) {
                    goBackwards();
                    setCurrentMonthTextView();
                    mPresenter.showEventsForMonth(currentlyDisplayedMonth+1, currentlyDisplayedYear);
                }
            }
        });
        previousMonthButton.setLongClickable(true);
        previousMonthButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentlyDisplayedMonth = Calendar.getInstance().get(Calendar.MONTH);
                currentlyDisplayedYear = Calendar.getInstance().get(Calendar.YEAR);
                setCurrentMonthTextView();
                mPresenter.showEventsForMonth(currentlyDisplayedMonth+1, currentlyDisplayedYear);
                return false;
            }
        });
        setCurrentMonthTextView();
        return view;
    }

    @Override
    public void showErrorText() {
        if (getView() != null && getContext() != null) {
            View rView = getView().findViewById(R.id.eventRecyclerView);
            rView.setVisibility(View.INVISIBLE);
            TextView textView = getView().findViewById(R.id.noEventsTextView);
            textView.setText(R.string.no_internet);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideErrorText() {
        if (getView() != null && getContext() != null) {
            View rView = getView().findViewById(R.id.eventRecyclerView);
            rView.setVisibility(View.VISIBLE);
            TextView textView = getView().findViewById(R.id.noEventsTextView);
            textView.setText(R.string.no_events_text);
            textView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void clearList() {
        this.list.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateList(ArrayList<AstroEvent> list) {
        this.list.clear();
        this.list.addAll(list);
        if (list.size() < 1)
            displayNoEventsText();
        else
            hideNoEventsText();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setPresenter(EventsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Activity getCurrentActivity(){
        return getActivity();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(AstroEvent event);
    }

    private void setCurrentMonthTextView(){
        try {
            String resourceString = "month_" + currentlyDisplayedMonth;
            int resourceStringId = context.getResources().getIdentifier(resourceString, "string", context.getPackageName());
            monthTextView.setText(context.getResources().getString(resourceStringId, currentlyDisplayedYear));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            monthTextView.setText(R.string.month_unknown);
        }
    }

    private void displayNoEventsText(){
        noEventsTextView.setVisibility(View.VISIBLE);
    }

    private void hideNoEventsText(){
        noEventsTextView.setVisibility(View.INVISIBLE);
    }

    private void goForwards() {
        if (currentlyDisplayedMonth < 11) {
            currentlyDisplayedMonth++;
        }
        else {
            currentlyDisplayedMonth = 0;
            currentlyDisplayedYear++;
        }
    }

    private void goBackwards() {
        if (currentlyDisplayedMonth > 0 && !(currentlyDisplayedYear == Calendar.getInstance().get(Calendar.YEAR) && currentlyDisplayedMonth == Calendar.getInstance().get(Calendar.MONTH) )) {
            currentlyDisplayedMonth--;
        }
        else if (currentlyDisplayedYear > Calendar.getInstance().get(Calendar.YEAR)){
            currentlyDisplayedMonth = 11;
            currentlyDisplayedYear--;
        }
    }
}

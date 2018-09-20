package com.example.barbara.skytonight.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.data.AstroEvent;

import java.util.ArrayList;
import java.util.Calendar;

public class EventsFragment extends Fragment implements EventsContract.View {

    private EventsContract.Presenter mPresenter;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyEventsRecyclerViewAdapter mAdapter;
    private Context context;
    private RecyclerView recyclerView;
    private TextView noEventsTextView;
    private TextView monthTextView;
    ArrayList<AstroEvent> list;
    private int currentlyDisplayedMonth;
    private int currentlyDisplayedYear;

    public EventsFragment() {
        list = new ArrayList<AstroEvent>();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start(currentlyDisplayedMonth, currentlyDisplayedYear);
        int itemCount = mAdapter.getItemCount();
        if (itemCount < 1)
            displayNoEventsText();
        else
            hideNoEventsText();
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
        if (currentlyDisplayedMonth > 0 && !(currentlyDisplayedYear == Calendar.getInstance().get(Calendar.YEAR) && currentlyDisplayedMonth == Calendar.getInstance().get(Calendar.MONTH) ))
            currentlyDisplayedMonth--;
        else if (currentlyDisplayedYear > Calendar.getInstance().get(Calendar.YEAR)){
            currentlyDisplayedMonth = 11;
            currentlyDisplayedYear--;
        }
    }

    public static EventsFragment newInstance(int columnCount) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.currentlyDisplayedMonth = Calendar.getInstance().get(Calendar.MONTH);
        this.currentlyDisplayedYear = Calendar.getInstance().get(Calendar.YEAR);
        Log.e("onAttach","Set currently displayed month");
        mAdapter = new MyEventsRecyclerViewAdapter(list, mListener);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    private void setCurrentMonthTextView(){
        Log.e("SetCurrentMont", currentlyDisplayedMonth + " " + currentlyDisplayedYear);
        try {
            String resourceString = "month_" + currentlyDisplayedMonth;
            int resourceStringId = context.getResources().getIdentifier(resourceString, "string", context.getPackageName());
            monthTextView.setText(context.getResources().getString(resourceStringId, currentlyDisplayedYear));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            monthTextView.setText(R.string.month_unknown);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_events_list2, container, false);
        noEventsTextView = view.findViewById(R.id.noEventsTextView);
        monthTextView = view.findViewById(R.id.monthTextView);
        recyclerView = view.findViewById(R.id.event_recycler_view);
        context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(mAdapter);
        setCurrentMonthTextView();
        Button nextMonthButton = view.findViewById(R.id.nextMonthButton);
        nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForwards();
                setCurrentMonthTextView();
                mPresenter.showEventsForMonth(currentlyDisplayedMonth+1, currentlyDisplayedYear);
            }
        });
        Button previousMonthButton = view.findViewById(R.id.previousMonthButton);
        previousMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackwards();
                setCurrentMonthTextView();
                mPresenter.showEventsForMonth(currentlyDisplayedMonth+1, currentlyDisplayedYear);
            }
        });
        previousMonthButton.setLongClickable(true);
        previousMonthButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                currentlyDisplayedMonth = Calendar.getInstance().get(Calendar.MONTH);
                currentlyDisplayedYear = Calendar.getInstance().get(Calendar.YEAR);
                mPresenter.showEventsForMonth(currentlyDisplayedMonth+1, currentlyDisplayedYear);
                return false;
            }
        });
        return view;
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
}

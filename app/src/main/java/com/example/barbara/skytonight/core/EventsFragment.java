package com.example.barbara.skytonight.core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.core.dummy.DummyContent;
import com.example.barbara.skytonight.core.dummy.DummyContent.DummyItem;
import com.example.barbara.skytonight.data.AstroEvent;
import com.example.barbara.skytonight.data.AstroObject;

import java.util.ArrayList;

public class EventsFragment extends Fragment implements EventsContract.View {

    private EventsContract.Presenter mPresenter;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyEventsRecyclerViewAdapter mAdapter;
    ArrayList<AstroEvent> list;

    public EventsFragment() {
        list = new ArrayList<AstroEvent>();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAdapter = new MyEventsRecyclerViewAdapter(list, mListener);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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

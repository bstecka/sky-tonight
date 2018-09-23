package com.example.barbara.skytonight.core;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.data.AstroObject;
import com.example.barbara.skytonight.util.AppConstants;

import java.util.ArrayList;

public class TodayFragment extends Fragment implements TodayContract.View {

    private TodayContract.Presenter mPresenter;

    private OnListFragmentInteractionListener mListener;
    private MyTodayRecyclerViewAdapter mAdapter;
    ArrayList<AstroObject> list;

    public TodayFragment() {
        list = new ArrayList<>();
    }

    @Override
    public void setPresenter(TodayContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mAdapter = new MyTodayRecyclerViewAdapter(list, mListener, AppConstants.DEFAULT_LATITUDE, AppConstants.DEFAULT_LONGITUDE);
        mPresenter.start();
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        return view;
    }

    public void refreshLocationInAdapter(Location location) {
        Log.e("refreshLocationInAdapter", "mFusedLocationClient success " + location.getLatitude() + " " + location.getLongitude());
        mAdapter.setLatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void clearList() {
        this.list.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateList(AstroObject object) {
        this.list.add(object);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public Activity getCurrentActivity(){
        return getActivity();
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(String item);
    }

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}

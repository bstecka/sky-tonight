package com.example.barbara.skytonight.presentation.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.presentation.audio.AudioActivity;
import com.example.barbara.skytonight.presentation.notes.NoteActivity;
import com.example.barbara.skytonight.presentation.notes.NotesListActivity;
import com.example.barbara.skytonight.presentation.photos.PhotoGalleryActivity;
import com.example.barbara.skytonight.presentation.video.VideoActivity;
import com.ramotion.circlemenu.CircleMenuView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarFragment extends Fragment implements CalendarContract.View {

    private CalendarContract.Presenter mPresenter;
    private OnFragmentInteractionListener mListener;
    private Calendar currentlySelectedDate = Calendar.getInstance();
    private ExpandableLayout exLayoutDay;
    private ExpandableLayout exLayoutMonth;
    private int monthTabMonth;
    private int monthTabYear;
    private View view;
    private Context context;

    public CalendarFragment() { }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(CalendarContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Activity getViewActivity() {
        return getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);
        context = view.getContext();
        exLayoutDay = view.findViewById(R.id.expandableLayout);
        exLayoutMonth = view.findViewById(R.id.expandableLayout2);
        exLayoutDay.collapse(false);
        exLayoutMonth.collapse(false);
        setTabLayout();
        setCircleMenu();
        setPreviousNextButtons();
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                updateDayInfoText(selectedDate);
                currentlySelectedDate = selectedDate;
            }
        });
        return view;
    }

    private void setPreviousNextButtons() {
        AppCompatImageButton nextMonthButton = view.findViewById(R.id.nextMonthButton);
        nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForwards();
                setCurrentMonthTextView();
            }
        });
        AppCompatImageButton previousMonthButton = view.findViewById(R.id.previousMonthButton);
        previousMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackwards();
                setCurrentMonthTextView();
            }
        });
        setCurrentMonthTextView();
    }

    private void initializeExpandableLayoutTab() {
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setDate(Calendar.getInstance().getTimeInMillis());
        currentlySelectedDate = Calendar.getInstance();
        updateDayInfoText(currentlySelectedDate);
        hideCircleMenu();
        exLayoutMonth.collapse();
        if (!exLayoutDay.isExpanded()) {
            exLayoutDay.expand();
        } else {
            exLayoutDay.collapse(false);
            exLayoutDay.expand();
        }
        showButton();
    }

    private void setTabLayout() {
        initializeExpandableLayoutTab();
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_day));
        TabLayout.Tab weekTab = tabLayout.newTab().setText(R.string.tab_week);
        tabLayout.addTab(weekTab);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_month));
        weekTab.select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0 || tab.getPosition() == 1) {
                    initializeExpandableLayoutTab();
                } else if (tab.getPosition() == 2) {
                    showCircleMenu();
                    exLayoutDay.collapse();
                    if (!exLayoutMonth.isExpanded()) {
                        exLayoutMonth.expand();
                    } else {
                        exLayoutMonth.collapse(false);
                        exLayoutMonth.expand();
                    }
                    hideButton();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    CalendarView calendarView = view.findViewById(R.id.calendarView);
                    calendarView.setDate(Calendar.getInstance().getTimeInMillis());
                    currentlySelectedDate = Calendar.getInstance();
                    updateDayInfoText(currentlySelectedDate);
                    hideCircleMenu();
                    exLayoutDay.expand();
                    showButton();
                } else if (tab.getPosition() == 1) {
                    initializeExpandableLayoutTab();
                }
            }
        });
    }

    private void setCircleMenu() {
        final CircleMenuView circleMenuView = view.findViewById(R.id.circleMenu);
        circleMenuView.setDurationRing(200);
        circleMenuView.setEventListener(new CircleMenuView.EventListener() {
            @Override
            public void onButtonClickAnimationEnd(@NonNull CircleMenuView view, int index) {
                if (index == 0)
                    onNotesButtonClick();
                else if (index == 1)
                    onPhotosButtonClick();
                else if (index == 2)
                    onVideoButtonClick();
                else
                    onAudioButtonClick();
            }
        });
        final FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exLayoutDay.collapse();
                floatingActionButton.hide();
                circleMenuView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideCircleMenu() {
        CircleMenuView circleMenuView = view.findViewById(R.id.circleMenu);
        circleMenuView.setVisibility(View.INVISIBLE);
    }

    private void showCircleMenu() {
        final CircleMenuView circleMenuView = view.findViewById(R.id.circleMenu);
        circleMenuView.setVisibility(View.VISIBLE);
    }

    private void showButton() {
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.show();
    }

    private void hideButton() {
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.hide();
    }

    private void goForwards() {
        if (monthTabMonth < 11) {
            monthTabMonth++;
        }
        else {
            monthTabMonth = 0;
            monthTabYear++;
        }
    }

    private void goBackwards() {
        if (monthTabMonth > 0) {
            monthTabMonth--;
        }
        else if (monthTabYear > 2000){
            monthTabMonth = 11;
            monthTabYear--;
        }
    }

    private void setCurrentMonthTextView(){
        TextView monthTextView = view.findViewById(R.id.monthTextView);
        try {
            String resourceString = "month_" + monthTabMonth;
            int resourceStringId = context.getResources().getIdentifier(resourceString, "string", context.getPackageName());
            monthTextView.setText(context.getResources().getString(resourceStringId, monthTabYear));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            monthTextView.setText(R.string.month_unknown);
        }
    }

    @Override
    public void updateDayInfoText(Calendar selectedDate) {
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        if (tabLayout.getSelectedTabPosition() == 0) {
            TextView textView = view.findViewById(R.id.dayInfoTextView);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            textView.setText(sdf.format(selectedDate.getTime()));
        }
        else if (tabLayout.getSelectedTabPosition() == 1) {
            int dayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK);
            if (Locale.getDefault().toLanguageTag().contains("pl"))
                dayOfWeek--;
            Calendar weekStart = Calendar.getInstance();
            weekStart.setTime(selectedDate.getTime());
            weekStart.add(Calendar.DAY_OF_YEAR, -(dayOfWeek - 1));
            Calendar weekEnd = Calendar.getInstance();
            weekEnd.setTime(selectedDate.getTime());
            weekEnd.add(Calendar.DAY_OF_YEAR, (7 - dayOfWeek));
            TextView textView = view.findViewById(R.id.dayInfoTextView);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            textView.setText(context.getString(R.string.week_info_text, sdf.format(weekStart.getTime()), sdf.format(weekEnd.getTime())));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.monthTabMonth = Calendar.getInstance().get(Calendar.MONTH);
        this.monthTabYear = Calendar.getInstance().get(Calendar.YEAR);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void onPhotosButtonClick() {
        Intent intent = new Intent(getActivity(), PhotoGalleryActivity.class);
        startActivityOnMenuButton(intent);
    }

    private void onNotesButtonClick() {
        Intent intent = new Intent(getActivity(), NotesListActivity.class);
        startActivityOnMenuButton(intent);
    }

    private void onAudioButtonClick() {
        Intent intent = new Intent(getActivity(), AudioActivity.class);
        startActivityOnMenuButton(intent);
    }

    private void onVideoButtonClick() {
        Intent intent = new Intent(getActivity(), VideoActivity.class);
        startActivityOnMenuButton(intent);
    }

    private void startActivityOnMenuButton(Intent intent) {
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        if (tabLayout.getSelectedTabPosition() == 0) {
            intent.putExtra("type", CalendarContract.TAB_TYPE_DAY);
            intent.putExtra("year", currentlySelectedDate.get(Calendar.YEAR));
            intent.putExtra("dayOfYear", currentlySelectedDate.get(Calendar.DAY_OF_YEAR));
        }
        else if (tabLayout.getSelectedTabPosition() == 1) {
            intent.putExtra("type", CalendarContract.TAB_TYPE_WEEK);
            intent.putExtra("year", currentlySelectedDate.get(Calendar.YEAR));
            intent.putExtra("dayOfYear", currentlySelectedDate.get(Calendar.DAY_OF_YEAR));
        }
        else {
            intent.putExtra("type", CalendarContract.TAB_TYPE_MONTH);
            intent.putExtra("year", monthTabYear);
            intent.putExtra("month", monthTabMonth);
        }
        startActivity(intent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

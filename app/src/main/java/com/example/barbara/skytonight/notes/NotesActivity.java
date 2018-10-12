package com.example.barbara.skytonight.notes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.barbara.skytonight.R;

import java.util.Calendar;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        Calendar selectedDate = Calendar.getInstance();
        if (bundle != null) {
            selectedDate.set(Calendar.YEAR, bundle.getInt("year"));
            selectedDate.set(Calendar.DAY_OF_YEAR, bundle.getInt("dayOfYear"));
        }
        setContentView(R.layout.activity_notes);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.notesFragment);
        NotesFragment notesFragment = (NotesFragment) currentFragment;
        if (notesFragment == null) {
            notesFragment = new NotesFragment();
            notesFragment.setPresenter(new NotesPresenter(notesFragment));
            notesFragment.setSelectedDate(selectedDate);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.notesFragment, notesFragment);
            transaction.commit();
        }
        else {
            notesFragment.setPresenter(new NotesPresenter(notesFragment));
            notesFragment.setSelectedDate(selectedDate);
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

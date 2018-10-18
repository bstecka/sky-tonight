package com.example.barbara.skytonight.notes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.barbara.skytonight.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotesActivity extends AppCompatActivity {

    NotesFragment notesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        Calendar selectedDate = Calendar.getInstance();
        if (bundle != null) {
            if (bundle.getInt("dayOfYear") != 0) {
                selectedDate.set(Calendar.YEAR, bundle.getInt("year"));
                selectedDate.set(Calendar.DAY_OF_YEAR, bundle.getInt("dayOfYear"));
            }
            else if (bundle.getString("filePath") != null) {
                String filePath = bundle.getString("filePath");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                try {
                    Date date = sdf.parse(filePath.substring(4, 13));
                    selectedDate.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        setContentView(R.layout.activity_notes);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.notesFragment);
        notesFragment = (NotesFragment) currentFragment;
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
    public void onBackPressed() {
        if (!notesFragment.isEditModeEnabled())
            super.onBackPressed();
        else
            showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to return to the Calendar screen and discard changes?").setTitle("Unsaved changes");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!notesFragment.isEditModeEnabled())
                    onBackPressed();
                else
                    showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

package com.example.barbara.skytonight.presentation.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.presentation.core.CalendarContract;
import com.example.barbara.skytonight.presentation.util.LocaleHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    NoteFragment noteFragment;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean weekMode = false;
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
            if (bundle.getInt("type") == CalendarContract.TAB_TYPE_WEEK)
                weekMode = true;
        }
        setContentView(R.layout.activity_note);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.noteFragment);
        noteFragment = (NoteFragment) currentFragment;
        if (noteFragment == null) {
            noteFragment = new NoteFragment();
            noteFragment.setPresenter(new NotePresenter(noteFragment));
            noteFragment.setSelectedDate(selectedDate);
            if (weekMode)
                noteFragment.setWeekMode(true);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.noteFragment, noteFragment);
            transaction.commit();
        }
        else {
            noteFragment.setPresenter(new NotePresenter(noteFragment));
            noteFragment.setSelectedDate(selectedDate);
            if (weekMode)
                noteFragment.setWeekMode(true);
        }
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (!noteFragment.isEditModeEnabled())
            super.onBackPressed();
        else
            showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.exit_dialog_prompt).setTitle(R.string.exit_dialog_title);
        builder.setPositiveButton(R.string.exit_dialog_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.exit_dialog_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!noteFragment.isEditModeEnabled())
                    onBackPressed();
                else
                    showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

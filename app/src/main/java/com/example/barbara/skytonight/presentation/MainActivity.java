package com.example.barbara.skytonight.presentation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.barbara.skytonight.R;
import com.example.barbara.skytonight.presentation.core.CoreActivity;
import com.example.barbara.skytonight.AppConstants;
import com.example.barbara.skytonight.presentation.util.MyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, AppConstants.LANG_EN));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent myIntent = new Intent(MainActivity.this, CoreActivity.class);
        MainActivity.this.startActivity(myIntent);
    }
}

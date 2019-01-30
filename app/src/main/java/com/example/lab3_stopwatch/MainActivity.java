package com.example.lab3_stopwatch;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Properties
    private Handler timeHandler;
    private ArrayAdapter<String> itemsAdapter;
    private TextView txtTimer;
    private Button btnStartPause, btnLapReset;

    //Vars to keep track of time
    private long millisecondTime, startTime, pausedTime, updateTime = 0;

    //Vars to display time
    private int seconds, minutes, milliseconds;

    //Vars to handle Stopwatch state
    private boolean stopWatchStarted, stopWatchPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLapReset = findViewById(R.id.btnLapReset);
    }
}

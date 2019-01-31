package com.example.lab3_stopwatch;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

        ListView lvLaps;
        timeHandler = new Handler();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1);

        txtTimer = findViewById(R.id.txtTimer);
        btnStartPause = findViewById(R.id.btnStartPause);
        btnLapReset = findViewById(R.id.btnLapReset);
        lvLaps = findViewById(R.id.lvLaps);

        //Bind data from Adaptor to ListView
        lvLaps.setAdapter(itemsAdapter);

        //Handles stopwatch actions for Start & stop
        btnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check whether user wants to start to pause
                if (!stopWatchStarted || stopWatchPaused) {
                    stopWatchStarted = true;
                    stopWatchPaused = false;
                    startTime = SystemClock.uptimeMillis();

                    //After specific amount of time, let...?
                    timeHandler.postDelayed(timerRunnable, 0);

                    //Change label strings
                    btnStartPause.setText(R.string.lblPause);
                    btnLapReset.setText(R.string.btnLap);
                } else {
                    pausedTime += milliseconds;
                    stopWatchPaused = true;

                    //Remove pending posts
                    timeHandler.removeCallbacks(timerRunnable);

                    //Change label strings
                    btnStartPause.setText(R.string.lblStart);
                    btnLapReset.setText(R.string.lblReset);
                }

            }

        });

        //Stopwatch actions for creating new lap and resetting
        btnLapReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If action is to create new lap
                if (stopWatchStarted && !stopWatchPaused) {
                    String lapTime = minutes + ":"
                            + String.format("%02d", seconds) + ":"
                            + String.format("%03d", milliseconds);

                    itemsAdapter.add(lapTime);
                } else if (stopWatchStarted) {      //Reset
                    stopWatchStarted = false;
                    stopWatchPaused = false;

                    //Remove pending post
                    timeHandler.removeCallbacks(timerRunnable);

                    //Reset all
                    millisecondTime = 0;
                    startTime = 0;
                    pausedTime = 0;
                    updateTime = 0;
                    seconds = 0;
                    minutes = 0;
                    milliseconds = 0;

                    //Change label Strings
                    txtTimer.setText(R.string.lblTimer);
                    btnLapReset.setText(R.string.btnLap);

                    //Clear the list
                    itemsAdapter.clear();
                } else {
                    Toast.makeText(getApplicationContext(), "Timer hasn't started yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Object to keep track of time
     */
    public Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            String strUpdatedTime;

            millisecondTime = (SystemClock.uptimeMillis() - startTime);

            //Keep track of there the stopwatch left off.
            updateTime = (pausedTime + millisecondTime);
            milliseconds = (int)(updateTime % 1000);
            seconds = (int)(updateTime / 1000);

            //Convert to displayable values
            minutes = (seconds / 60);
            seconds = (seconds % 60);

            strUpdatedTime = (minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliseconds));

            txtTimer.setText(strUpdatedTime);

            //Ready this runnable to be called by the message que.
            timeHandler.postDelayed(this, 0);
        }
    };
}

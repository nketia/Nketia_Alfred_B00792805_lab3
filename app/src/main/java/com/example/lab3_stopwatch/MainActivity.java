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

public class    MainActivity extends AppCompatActivity {

    //Properties
    private Handler timerHandler;
    private ArrayAdapter<String> itemsAdapter;
    private TextView txtTimer;
    private Button btnStartPause, btnLapReset;


    //Vars to keep track of time
    private long millisecondTime, startTime, pausedTime, updateTime = 0;

    //Vars to display time
    private int seconds, minutes, milliSeconds;

    //Vars to handle Stopwatch state
    private boolean stopWatchStarted, stopWatchPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView lvLaps;


        //timeHandler is bound to a thread
        //used to schedule our Runnable to be executed after particular aactions
        timerHandler = new Handler();

        //sets the layout for each item of the list view
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        txtTimer = findViewById(R.id.txtTimer);
        btnStartPause = findViewById(R.id.btnStartPause);
        btnLapReset = findViewById(R.id.btnLapReset);

        lvLaps = findViewById(R.id.lvLaps);
        //binds data from the Adapter to the ListView
        lvLaps.setAdapter(itemsAdapter);

        //begin to insert cut code
        // handles the stopwatch actions for the starting and stopping
        btnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the action is to start or pause
                if (!stopWatchStarted || stopWatchPaused) {
                    stopWatchStarted = true;
                    stopWatchPaused = false;

                    startTime = SystemClock.uptimeMillis();

                    //enqueues the Runnable to be called by the message queue after the specified amount of time elapses.
                    //message queue live on the main thread of processes.
                    long delayMillis;
                    timerHandler.postDelayed(timerRunnable, 0);

                    //switch lable strings
                    btnStartPause.setText(R.string.lblPause);
                    btnLapReset.setText(R.string.btnLap);

                } else {
                    pausedTime += millisecondTime;
                    stopWatchPaused = true;

                    //remove pending posts of timeRunnable in message queue
                    timerHandler.removeCallbacks(timerRunnable);

                    //switch lable strings
                    btnStartPause.setText(R.string.lblStart);
                    btnLapReset.setText(R.string.lblReset);

                }

            }
        });
        //end of inserted  code from setOnClickListener Method

        btnLapReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //check o=if the action is to create a new lap or reset the stopwatch
                if (stopWatchStarted && !stopWatchPaused) {
                    String lapTime = minutes + ":"
                            + String.format("%02d", seconds) + ":"
                            + String.format("%03d", milliSeconds);

                    itemsAdapter.add(lapTime);

                } else if (stopWatchStarted) {
                    stopWatchStarted = false;
                    stopWatchPaused = false;

                    //remove pending posts of timerRunnable in message queue
                    timerHandler.removeCallbacks(timerRunnable);

                    //reset all values
                    millisecondTime = 0;
                    startTime = 0;
                    pausedTime = 0;
                    updateTime = 0;
                    seconds = 0;
                    minutes = 0;
                    milliSeconds = 0;

                    //switch lable strings
                    txtTimer.setText(R.string.lblTimer);
                    btnLapReset.setText(R.string.btnLap);

                    //wipe reseources
                    itemsAdapter.clear();

                } else {
                    Toast.makeText(getApplicationContext(), "Timer hasn't started yet", Toast.LENGTH_SHORT).show();
                }
            }

        });

        //handles the stopwatch actions for creating a new lap and resetting
    }

    /**
     * This instance is executed by handler which is associated with a thread, this means a
     * Runnable Interface Overriding the run () method should e implemented by the class.
     */
    public Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime;

            //values used to keep track of where the stopwatch time left off
            updateTime = pausedTime + millisecondTime;
            milliSeconds = (int) (updateTime % 10000);
            seconds = (int) (updateTime / 10000);

            //convert values to display
            minutes = seconds / 60;
            seconds = seconds % 60;
            String updatedTime = minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSeconds);

            txtTimer.setText(updatedTime);


            //enqueues the Runnable to be called by the message queue after the specified amount of time elapses.


            timerHandler.postDelayed(this,  0);
        }
    };
}

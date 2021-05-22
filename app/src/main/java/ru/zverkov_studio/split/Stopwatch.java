package ru.zverkov_studio.split;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

public class Stopwatch {

    long MillisecondTime_main, MillisecondTime_lap, StartTime_main, StartTime_lap, UpdateTime = 0L ;
    Handler handler = new Handler();
    int Hours, Seconds, Minutes, MilliSeconds ;
    TextView timer, lap_timer;

    public Stopwatch(){
    }

    public void run_setting(TextView main_time, TextView lap_time, long start_time, long start_lap_time){
        StartTime_main = start_time;
        StartTime_lap = start_lap_time;
        timer = main_time;
        lap_timer = lap_time;
        handler.postDelayed(runnable, 0);
    }

    public void stop_setting() {
        handler.removeCallbacks(runnable);
    }

    public Runnable runnable = new Runnable() {

        public void run() {
            MillisecondTime_main = SystemClock.uptimeMillis() - StartTime_main;
            UpdateTime = MillisecondTime_main;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Hours = Minutes / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 10);
            timer.setText(String.format("%02d", Hours) + ":"
                    + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + "."
                    + String.format("%d", MilliSeconds));

            MillisecondTime_lap = SystemClock.uptimeMillis() - StartTime_lap;
            UpdateTime = MillisecondTime_lap;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Hours = Minutes / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 10);

            lap_timer.setText(String.format("%02d", Hours) + ":"
                    + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + "."
                    + String.format("%d", MilliSeconds));

            handler.postDelayed(this, 100);
        }

    };

}

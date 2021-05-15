package ru.zverkov_studio.split;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

public class Stopwatch {

    TextView timer, lap_timer ;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler = new Handler();
    int Hours, Seconds, Minutes, MilliSeconds ;

    public Stopwatch(TextView main_time, TextView lap_time){
        timer = main_time;
        lap_timer = lap_time;
        StartTime = SystemClock.uptimeMillis();

        handler.postDelayed(runnable, 0);
    }

    public Runnable runnable = new Runnable() {

        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Hours = Minutes / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            timer.setText(String.format("%02d", Hours) + ":"
                    + String.format("%02d", Minutes) + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%01d", MilliSeconds));

            lap_timer.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 1000);
        }

    };

}

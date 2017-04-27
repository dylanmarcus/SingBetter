package com.inyourface.singbetter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ToggleRecord extends MainActivity {

    int intervalsPassed = 0;
    public static ArrayList<Double> HzArray = new ArrayList<Double>();
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            // start timer loop
            intervalsPassed++;
            // add current detected pitchInHz into ArrayList
            HzArray.add(pitchInHz);

            // This is to prevent data clutter on screen for demo
            // the idea is there will a max of 20 values before it stops.
            // for real use, this number can be used as a fail safe so the
            // recorder can stop after x amount of time if you forget to manually stop it.
            if(intervalsPassed == 20)
                timer.cancel();
        }
    };

    // start recording data ( task , delayBeforeStart , Intervals)
    public void start() {
        timer.scheduleAtFixedRate(task, 0, 100);   // 1000 ms = 1 sec
    }

    // stop recording data
    public void stop() {
        timer.cancel();
    }

    // clear current data, whatever is in the Arraylist
    public void clear() {
        HzArray.clear();
    }

    // getter for ArrayList
    public ArrayList<Double> getHzArray() {
        return HzArray;
    }
}

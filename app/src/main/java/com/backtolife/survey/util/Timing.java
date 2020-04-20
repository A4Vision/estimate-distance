package com.backtolife.survey.util;

import androidx.annotation.NonNull;

public class Timing {
    public double seconds;

    public Timing(double secondsFromEpoch){
        seconds = secondsFromEpoch;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(seconds % (24 * 3600));
    }
}

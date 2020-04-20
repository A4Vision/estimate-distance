package com.backtolife.survey.gui;

import android.app.Activity;
import android.os.Handler;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.backtolife.survey.R;
import com.backtolife.survey.ble.PacketsStatistics;
import com.backtolife.survey.signal.SignalsStatistics;
import com.backtolife.survey.util.TimeUtil;

import java.util.List;

public class GUI implements GUIInterface {
    private final Activity mainActivity;

    public GUI(Activity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void setSignalsStatistics(final SignalsStatistics statistics) {
        Handler mainHandler = new Handler(mainActivity.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                TextView signals = (TextView) mainActivity.findViewById(R.id.signals1);
                String time = TimeUtil.prettyTime(statistics.lastSignalTiming.seconds, "mm:ss.SSS");
                signals.setText(String.format("LastSignal=%s\nlastMinute=%d\nincoming=%d\noutgoing=%d\n",
                        time, statistics.signalsHeardDuringLastMinute,
                        statistics.incomingCount, statistics.outgoingCount)
                );
            } // This is your code
        };
        mainHandler.post(myRunnable);

    }

    @Override
    public void setPacketsStatistics(PacketsStatistics statistics) {

    }

    @Override
    public void setDistancesInfo(List<Pair<String, Double>> distances) {

    }
}

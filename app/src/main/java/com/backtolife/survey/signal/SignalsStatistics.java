package com.backtolife.survey.signal;

import com.backtolife.survey.util.MySystemClock;
import com.backtolife.survey.util.Timing;

import java.util.List;

public class SignalsStatistics {
    public Timing lastSignalTiming;
    public int signalsHeardDuringLastMinute;
    public int incomingCount;
    public int outgoingCount;

    public SignalsStatistics(List<Timing> heard, List<Timing> sent, TimingsResolver resolver){
        if(!heard.isEmpty()) {
            lastSignalTiming = heard.get(heard.size() - 1);
        }
        signalsHeardDuringLastMinute = 0;
        for(Timing timing: heard){
            if(timing.seconds > MySystemClock.getNowSeconds() - 60.){
                signalsHeardDuringLastMinute++;
            }
        }
        outgoingCount = sent.size();
        incomingCount = heard.size() - outgoingCount;

        System.out.println("heard=" + heard);
        System.out.println("sent=" + sent);
    }
}

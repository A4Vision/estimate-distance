package com.backtolife.survey.signal;

import com.backtolife.survey.util.Id;
import com.backtolife.survey.util.Timing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Assigns timings of locally heard signals to their senders.
 */
public class TimingsResolver {
    private static final double MAX_TIME_DIFF = 1.;
    private List<Timing> heard;
    private Id topPriorityId;
    private Map<Id, List<Timing>> signalsSendTimings;

    public Id getMyId() {
        return topPriorityId;
    }

    public class ExtendedTiming{
        public Timing localTiming;
        public Timing remoteTiming;
        public ExtendedTiming(Timing l, Timing r){
            localTiming = l;
            remoteTiming = r;
        }
    }

    public TimingsResolver(List<Timing> heard, Id myId){
        this.topPriorityId = myId;
        this.heard = new ArrayList<>(heard);
        this.signalsSendTimings = new HashMap<>();
    }

    public void assignTimingsToSender(List<Timing> sentTimings, Id id){
        this.signalsSendTimings.put(id, new ArrayList<>(sentTimings));
    }

    private List<Integer> relevantHeard(Timing timing){
        List<Integer> res = new ArrayList<>();
        for(int i = 0; i < heard.size(); ++i){
            if(Math.abs(timing.seconds - heard.get(i).seconds) < MAX_TIME_DIFF){
                res.add(i);
            }
        }
        return res;
    }

    public Map<Id, List<ExtendedTiming>> resolveSignalsInLocalTime(){
        Map<Integer, Integer> resolvedTimings = new HashMap<>();
        for(int i = 0; i < heard.size(); ++i){
            resolvedTimings.put(i, 0);
        }
        for(Id id: signalsSendTimings.keySet()){
            for(Timing remoteTiming: signalsSendTimings.get(id)){
                for(int i: relevantHeard(remoteTiming)){
                    int count = resolvedTimings.get(i);
                    resolvedTimings.put(i, count + 1);
                }
            }
        }

        Map<Id, List<ExtendedTiming>> result = new HashMap<>();
        for(Id id: signalsSendTimings.keySet()){
            ArrayList<ExtendedTiming> l = new ArrayList<>();
            for(Timing remoteTiming: signalsSendTimings.get(id)){
                for(int i: relevantHeard(remoteTiming)){
                    int count = resolvedTimings.get(i);
                    // Signals we heard, that have only a single correlating outgoing signal
                    // from a neighbor - are assumed to be resolved correctly.
                    // More, we assume that the signals that are sent by this device are heard over
                    // every other external signal.
                    if(count == 1 || id == topPriorityId) {
                        l.add(new ExtendedTiming(heard.get(i), remoteTiming));
                    }
                }
            }
            if(!l.isEmpty()) {
                result.put(id, l);
            }
        }
        return result;
    }
}

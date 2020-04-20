package com.backtolife.survey.signal;

import com.backtolife.survey.util.MyCallback;
import com.backtolife.survey.util.Timing;
import com.backtolife.survey.util.RingBufferFlip;

import java.util.ArrayList;
import java.util.List;

public class InStreamFinder {
    private final SignalFinderInterface finder;
    private final MyCallback callback;
    private RingBufferFlip buffer;
    private int frameRate;
    private double startTime;
    private int bufferOffsetInFrames;
    private int framesForActualSearch;
    private List<Timing> timings;

    public InStreamFinder(SignalFinderInterface finder, MyCallback callback){
        this.finder = finder;
        this.callback = callback;
        timings = new ArrayList<>();
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void initBuffer(int bufferSize, int framesForActualSearch, int frameRate) {
        this.buffer = new RingBufferFlip(bufferSize);
        this.frameRate = frameRate;
        this.framesForActualSearch = framesForActualSearch;
        this.bufferOffsetInFrames = 0;
    }

    private double framesOffsetToTime(int framesOffset) {
        return startTime + (double) framesOffset / (double)frameRate;
    }

    public RingBufferFlip getBuffer() {
        return buffer;
    }

    private void advanceBuffer(int frameToAdvance) {
        assert buffer != null && buffer.available() > frameToAdvance;
        int skipped = buffer.skip(frameToAdvance);
        bufferOffsetInFrames += skipped;
    }

    public void work() {
        if(buffer.available() > framesForActualSearch){
            // TODO(Assaf): search the signal only during the first x seconds of every minute.
            float[] rawDataWithSignal = new float[framesForActualSearch];
            System.out.println("available=" + buffer.available());

            System.out.println("peeking " + framesForActualSearch);
            int read = buffer.peek(rawDataWithSignal, 0, framesForActualSearch);
            System.out.println("read with peek=" + read);

            List<Integer> offsets = finder.findSignalOffsetsInRawData(rawDataWithSignal);
            System.out.println("offsets=" + offsets);
            for(int offset: offsets){
                getTimings(false).add(new Timing(framesOffsetToTime(bufferOffsetInFrames + offset)));
            }
            advanceBuffer(framesForActualSearch - (int)(finder.signalSize() * 1.2));
            if(!offsets.isEmpty()){
                callback.call();
            }
        }
    }

    private synchronized List<Timing> getTimings(boolean copy) {
        if(copy){
            return new ArrayList<>(timings);
        }else {
            return timings;
        }
    }

    public List<Timing> getTimings() {
        return getTimings(true);
    }
}

package com.backtolife.survey.signal;

import java.util.List;

public interface SignalFinderInterface {
    List<Integer> findSignalOffsetsInRawData(float[] rawDataWithSignal);

    int signalSize();
}

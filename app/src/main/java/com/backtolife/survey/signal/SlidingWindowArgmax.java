package com.backtolife.survey.signal;



import com.backtolife.survey.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import com.google.common.io.Resources;
import static com.backtolife.survey.signal.SignalBase.fftConvolve;
import static com.backtolife.survey.signal.SignalBase.normL2;
import static com.backtolife.survey.signal.SignalBase.normL2OfWindows;
import static com.backtolife.survey.util.ListUtil.loadFloatData;
import static com.google.common.io.Resources.getResource;

/**
 * Finds a specific signal (normally, a preamble) in an input signal.
 * Algorithm:
 * 1. Apply high-pass frequencies filter
 * 2. Convolve the signal with the wanted preamble signal
 * 3. Take absolute values of convolution
 * 4. Smooth the signal by naive averaging
 * 5. Select the offset with the highest "smooth" value
 */
public class SlidingWindowArgmax implements SignalFinderInterface {
    private final int windowSize;
    private final float minimalScore;
    private float[] reversedPreamble;

    public SlidingWindowArgmax(
            float[] preambleSignal, int windowSize, float minimalScore) {
        this.windowSize = windowSize;
        this.minimalScore = minimalScore;
        reversedPreamble = new float[preambleSignal.length];
        for(int i = 0; i < reversedPreamble.length; ++i){
            reversedPreamble[i] = preambleSignal[preambleSignal.length - 1 - i];
        }
    }

    @Override
    public List<Integer> findSignalOffsetsInRawData(float[] rawDataWithSignal) {
        float[] scores = calculateAnglesToPreamble(rawDataWithSignal);
        List<Integer> relevant_indices = new ArrayList<>();
        for (int i = 0 ; i < scores.length; ++i){
            if(scores[i] > minimalScore){
                relevant_indices.add(i);
            }
        }
        if(relevant_indices.isEmpty()){
            return new ArrayList<>();
        }

        WindowValues window_view = new WindowValues(scores, toIntegerArray(relevant_indices), windowSize);
        List<Integer> res = new ArrayList<>();
        int prevId;
        do{
            Integer a = window_view.argmax();
            if(a != null) {
                res.add(a);
            }
            prevId = window_view.window_id();
            window_view.advance();
        }while(prevId != window_view.window_id());
        return res;
    }


    @Override
    public int signalSize() {
        return reversedPreamble.length;
    }

    private int[] toIntegerArray(List<Integer> relevant_indices) {
        int[] res = new int[relevant_indices.size()];
        for(int i = 0; i < res.length; ++i){
            res[i] = relevant_indices.get(i);
        }
        return res;
    }

    float[] calculateAnglesToPreamble(float[] rawDataWithSignal) {
        float[] convolved = fftConvolve(rawDataWithSignal, reversedPreamble);
        float preamble_norm = normL2(reversedPreamble);

        float[] windowsNorms = normL2OfWindows(rawDataWithSignal, reversedPreamble.length);
        float[] absAngles = new float[windowsNorms.length];
        for (int i = 0; i < windowsNorms.length; ++i) {
            absAngles[i] = Math.abs(convolved[i] / (windowsNorms[i] * preamble_norm));
        }
        return absAngles;
    }

    public static SlidingWindowArgmax createDefault(InputStream inputStream){
        float[] filteredPreamble = new float[0];
        try {
            filteredPreamble = loadFloatData(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SlidingWindowArgmax(filteredPreamble, 30_000, (float) 0.05);
    }
}

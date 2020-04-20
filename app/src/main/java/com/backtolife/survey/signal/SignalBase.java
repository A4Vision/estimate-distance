package com.backtolife.survey.signal;


import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.backtolife.survey.util.SimpleProfiler.AFTER;
import static com.backtolife.survey.util.SimpleProfiler.BEFORE;

public class SignalBase {

    public static float normL2(float[] vec) {
        return normL2OfWindows(vec, vec.length)[0];
    }

    public static float[] normL2OfWindows(float[] in, int windowSize) {
        float[] out = new float[in.length - windowSize + 1];
        double sumSquares = 0;
        for (int i = 0; i < windowSize; ++i) {
            sumSquares += in[i] * in[i];
        }
        for (int i = 0; i < in.length - windowSize + 1; i++) {
            out[i] = (float) Math.sqrt(sumSquares);
            if (i + windowSize < in.length) {
                sumSquares += in[i + windowSize] * in[i + windowSize] - in[i] * in[i];
            }
        }
        return out;
    }

    private static final Map<Pair<float[], Integer>, float[]> cachedFFTReal = new HashMap<>();
    private static final Map<Pair<float[], Integer>, float[]> cachedFFTImage = new HashMap<>();

    static List<Float> toList(float[] array) {
        List<Float> result = new ArrayList<Float>(array.length);
        for (float f : array) {
            result.add(Float.valueOf(f));
        }
        return result;
    }

    public static float[] fftConvolve(float[] a, float[] b) {
        if (a.length > b.length) {
            return fftConvolve(b, a);
        }
        double ld = Math.log(b.length) / Math.log(2.0);
        int n = (1 << (int) Math.ceil(ld));
        float[] realA;
        float[] imA;
        FFTColumbiaFloat.build(n);
        Pair key = new Pair(a, n);
        if (cachedFFTReal.containsKey(key)){
            realA = cachedFFTReal.get(key);
            imA = cachedFFTImage.get(key);
        }else{
            realA = pad(a, n);
            imA = new float[n];
            System.out.println(n);
            BEFORE("fft");
            FFTColumbiaFloat.build(n).fft(realA, imA);
            AFTER("fft");

            cachedFFTReal.put(key, realA);
            cachedFFTImage.put(key, imA);
        }

        float[] realB = pad(b, n);
        float[] imB = new float[n];
        FFTColumbiaFloat.build(n).fft(realB, imB);

        float[] multImg = new float[n];
        float[] multReal = new float[n];
        for (int i = 0; i < n; ++i) {
            multReal[i] = realA[i] * realB[i] - imA[i] * imB[i];
            multImg[i] = realA[i] * imB[i] + realB[i] * imA[i];
        }
        // apply inverse FFT by swapping real <-> imaginary
        FFTColumbiaFloat.build(n).fft(multImg, multReal);

        float[] res = new float[b.length - a.length + 1];

        for (int i = 0; i < res.length; ++i) {
            res[i] = multReal[i + a.length - 1] / (float) n;
        }
        return res;
    }

    public static float[] pad(float[] a, int n) {
        float[] r = new float[n];
        System.arraycopy(a, 0, r, 0, a.length);
        return r;
    }
}

package com.backtolife.survey.signal;

import org.junit.Assert;
import org.junit.Test;

import static com.backtolife.survey.signal.SignalBase.fftConvolve;
import static com.backtolife.survey.signal.SignalBase.normL2;
import static com.backtolife.survey.signal.SignalBase.normL2OfWindows;
import static com.google.common.truth.Truth.assertThat;


public class SignalBaseTest {
    @Test
    public void compareL2Norm() {
        float[] input = new float[]{(float)1., (float)2., (float)3};
        float[] expected = new float[2];
        expected[0] = (float) Math.sqrt((float)(1 + 2. * 2));
        expected[1] = (float) Math.sqrt((float)(2. * 2 + 3 * 3.));
        float[] norms = normL2OfWindows(input, 2);
        assertThat(norms).isEqualTo(expected);
    }

    @Test
    public void testNorm(){
        float[] input = new float[]{(float)1., (float)2.};
        assertThat(normL2(input)).isEqualTo((float) Math.sqrt(1. + 4.));
    }

    @Test
    public void testConvolve1(){
        float[] vec1 = new float[]{(float)1., (float)2., (float)3., (float)0, (float)0};
        float[] vec2 = new float[]{(float)100., (float)4.};
        float[] convolution = fftConvolve(vec1, vec2);
        float[] expected = new float[]{(float)4 + 2 * 100, (float)4 * 2 + 100 * 3, (float)4 * 3,
                (float)0};
        Assert.assertArrayEquals(convolution, expected, (float) 1e-3);
    }



    @Test
    public void testConvolve2(){
        float[] vec1 = new float[]{(float)1., (float)2., (float)3., (float)0, (float)0};
        float[] vec2 = new float[]{(float)100.};
        float[] convolution = fftConvolve(vec1, vec2);
        float[] expected = new float[]{(float)100, (float)100 * 2, (float)100 * 3,
                (float)0, (float)0};
        Assert.assertArrayEquals(convolution, expected, (float) 1e-3);
    }

    @Test
    public void testConvolveCache(){
        float[] vec1 = new float[]{(float)1., (float)2., (float)3., (float)0, (float)0};
        float[] vec2 = new float[]{(float)100., (float)4.};
        float[] convolution1 = fftConvolve(vec1, vec2);
        float[] convolution2 = fftConvolve(vec1, vec2);
        Assert.assertArrayEquals(convolution1, convolution2, (float) 1e-5);
    }

}
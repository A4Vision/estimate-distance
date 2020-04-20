package com.backtolife.survey.util;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;


public class ListUtilTest {
    private final static double[] original = new double[]{1.5, 3.};
    private final static byte[] converted = new byte[]{
            (byte)63, (byte)248, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0,
            (byte)64, (byte)8, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0};

    @Test
    public void doubleArrayConversionLikePython(){
        assertThat(ListUtil.toByteArray(original)).isEqualTo(converted);
    }

    @Test
    public void byteArrayConversionLikePython(){
        assertThat(ListUtil.toDoubleArray(converted)).isEqualTo(original);
    }
}

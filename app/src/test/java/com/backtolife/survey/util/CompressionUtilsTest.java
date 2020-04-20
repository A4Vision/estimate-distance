package com.backtolife.survey.util;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DataFormatException;

import static com.google.common.truth.Truth.assertThat;


public class CompressionUtilsTest {
    private final static byte[] original = new byte[]{(byte)49, (byte)50, (byte)49, (byte)50, (byte)49, (byte)50, (byte)49, (byte)50, (byte)49, (byte)50};
    private final static byte[] compressed = new byte[]{(byte)120, (byte)156, (byte)51, (byte)52, (byte)50, (byte)132, (byte)66, (byte)0, (byte)10, (byte)170, (byte)1, (byte)240};
    @Test
    public void compressDecompress() throws IOException, DataFormatException {
        byte[] arr0 = new byte[0];
        byte[] arr1 = new byte[]{(byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1, (byte)1,
                (byte)2};
        for(byte[] arr: Arrays.asList(arr0, arr1)) {
            byte[] compressed = CompressionUtils.compress(arr);
            byte[] arr2 = CompressionUtils.decompress(compressed);
            assertThat(arr2).isEqualTo(arr);
        }
    }

    @Test
    public void compressLikePython() throws IOException {
        assertThat(CompressionUtils.compress(original)).isEqualTo(compressed);
    }

    @Test
    public void decompressLikePython() throws IOException, DataFormatException {
        assertThat(CompressionUtils.decompress(compressed)).isEqualTo(original);
    }
}
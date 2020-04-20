package com.backtolife.survey.signal;

import com.backtolife.survey.util.ListUtil;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.google.common.io.Resources.getResource;
import static com.google.common.truth.Truth.assertThat;


public class SlidingWindowArgmaxTest {
    @Test
    public void findSignalInRealData() throws IOException {
        float[] data = ListUtil.loadFloatData(
                new FileInputStream(getResource("test_data_with_preamble.dat").getPath()));
        float[] filteredPreamble = ListUtil.loadFloatData(
                new FileInputStream(getResource("filtered_preamble.dat").getPath()));
        SlidingWindowArgmax slider = new SlidingWindowArgmax(filteredPreamble, 20_000, (float) 0.06);
        List<Integer> offsets = slider.findSignalOffsetsInRawData(data);
        assertThat(offsets).hasSize(3);
        for (int offset : offsets) {
            assertThat((offset % 80_000) < 60 || (offset % 80_000) > 80_000 - 60).isTrue();
        }
    }

}
package com.backtolife.survey.util;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;


public class RingBufferFlipTest {
    @Test
    public void read_empty(){
        RingBufferFlip b = new RingBufferFlip(10);
        float[] into = new float[10];
        assertThat(b.take(into, 100)).isEqualTo(0);
    }

    @Test
    public void read_moreThanAvailable(){
        RingBufferFlip b = new RingBufferFlip(10);
        float[] src = createRange(10);
        b.put(src, 5);
        float[] into = new float[10];
        assertThat(b.take(into, 100)).isEqualTo(5);
        float[] expected = createRange(10);
        for(int i = 5; i < 10; ++i) {
            expected[i] = (float)0.;
        }
        assertThat(into).isEqualTo(expected);
    }

    @Test
    public void putMoreThanCapacity(){
        RingBufferFlip b = new RingBufferFlip(10);
        assertThat(b.put(new float[20], 20)).isEqualTo(10);
    }

    @Test
    public void peek(){
        RingBufferFlip b = new RingBufferFlip(10);
        b.put(createRange(5), 5);
        float[] into = new float[5];
        assertThat(b.peek(into, 0, 5)).isEqualTo(5);
        float[] expected = createRange(5);
        assertThat(into).isEqualTo(expected);

        assertThat(b.peek(into, 3, 5)).isEqualTo(2);
        expected[0] = (float)3;
        expected[1] = (float)4;
        assertThat(into).isEqualTo(expected);
    }

    @Test
    public void takeAroundTheCircle(){
        RingBufferFlip b = new RingBufferFlip(10);
        b.put(createRange(5), 5);
        {
            float[] into5 = new float[5];
            assertThat(b.take(into5, 5)).isEqualTo(5);
            float[] expected5 = createRange(5);
            assertThat(into5).isEqualTo(expected5);
        }
        {
            assertThat(b.put(createRange(7), 7)).isEqualTo(7);
            float[] into7 = new float[7];
            assertThat(b.take(into7, 5)).isEqualTo(5);
            float[] expected7 = createRange(7);
            expected7[5] = 0;
            expected7[6] = (float)0.;
            assertThat(into7).isEqualTo(expected7);
        }
    }

    @Test
    public void putToEdge(){
        RingBufferFlip b = new RingBufferFlip(10);
        b.put(createRange(5), 5);
        assertThat(b.skip(6)).isEqualTo(5);

        assertThat(b.put(createRange(10), 10)).isEqualTo(10);

        float[] into = new float[11];
        assertThat(b.take(into, 11)).isEqualTo(10);
        float[] expected = createRange(11);
        expected[10] = (float)0.;
        assertThat(into).isEqualTo(expected);
    }

    @Test
    public void put3Chunks_readOnce(){
        RingBufferFlip b = new RingBufferFlip(10);
        assertThat(b.put(createRange(4), 4)).isEqualTo(4);
        assertThat(b.put(createRange(4), 4)).isEqualTo(4);
        assertThat(b.put(createRange(4), 4)).isEqualTo(2);
        assertThat(b.isFlipped()).isFalse();
        float[] into = new float[10];
        assertThat(b.available()).isEqualTo(10);
        assertThat(b.take(into, 10)).isEqualTo(10);
        float[] expected = createRange(10);
        expected[4] = (float)0;
        expected[5] = (float)1;
        expected[6] = (float)2;
        expected[7] = (float)3;
        expected[8] = (float)0;
        expected[9] = (float)1;
        assertThat(into).isEqualTo(expected);
    }

    @Test
    public void writeAndFlipRemainingOK(){
        RingBufferFlip buffer = new RingBufferFlip(10);
        assertThat(buffer.remainingCapacity()).isEqualTo(10);
        buffer.put(new float[5], 5);
        assertThat(buffer.remainingCapacity()).isEqualTo(5);
        buffer.put(new float[5], 5);
        assertThat(buffer.remainingCapacity()).isEqualTo(0);
        buffer.skip(3);
        assertThat(buffer.remainingCapacity()).isEqualTo(3);
        buffer.put(new float[3], 3);
        assertThat(buffer.remainingCapacity()).isEqualTo(0);
        buffer.skip(3);
        assertThat(buffer.remainingCapacity()).isEqualTo(3);
    }

    private float[] createRange(int length){
        float[] res = new float[length];
        for(int i = 0; i < length; ++i){
            res[i] = (float)i;
        }
        return res;
    }


}

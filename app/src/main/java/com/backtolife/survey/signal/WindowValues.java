package com.backtolife.survey.signal;


import com.backtolife.survey.util.ComparablePair;

import java.util.SortedSet;
import java.util.TreeSet;

public class WindowValues {
    private final int[] relevantIndices;
    private final float[] arr;
    private final SortedSet<ComparablePair<Float, Integer>> set;
    private int leftPointer;
    private int midPointer;
    private int rightPointer;
    private final int windowSize;

    public WindowValues(float[] arr, int[] relevantIndices, int windowSize){
        this.relevantIndices = relevantIndices;
        this.arr = arr;
        this.leftPointer = 0;
        this.midPointer = 0;
        this.rightPointer = 0;
        this.windowSize = windowSize;
        this.set = new TreeSet<>();
        this.set.add(getKey(0));
        advance_right_pointer();
    }

    private void advance_right_pointer() {
        int pointer = rightPointer;
        int bound = relevantIndices[midPointer] + windowSize;
        while (pointer + 1 < relevantIndices.length && relevantIndices[pointer + 1] <= bound){
            pointer += 1;
            set.add(getKey(pointer));
        }
        rightPointer = pointer;
    }

    private ComparablePair<Float, Integer> getKey(int pointer){
        return ComparablePair.of(-arr[relevantIndices[pointer]], pointer);
    }

    public int window_id(){
        return midPointer;
    }
    public void advance(){
        if (midPointer + 1 < relevantIndices.length) {
            midPointer += 1;
            advance_left_pointer();
            advance_right_pointer();
        }
    }

    private void advance_left_pointer() {
        int pointer = leftPointer;
        int bound = relevantIndices[midPointer] - windowSize;
        while (relevantIndices[pointer] < bound) {
            set.remove(getKey(pointer));
            pointer += 1;
        }
        leftPointer = pointer;
    }

    public Integer argmax(){
        if (!set.isEmpty()) {
            ComparablePair<Float, Integer> key = set.first();
            if (getValue(key) == arr[relevantIndices[midPointer]]) {
                return relevantIndices[midPointer];
            }
        }
        return null;
    }

    private float getValue(ComparablePair<Float, Integer> key) {
        return -key.first;
    }

}

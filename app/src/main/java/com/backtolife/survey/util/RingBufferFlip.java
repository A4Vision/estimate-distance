package com.backtolife.survey.util;

/**
 * Ring buffer - circular buffer that enables continuous writing & reading,
 * without .
 * Not thread-safe for simultaneous read & write.
 */
public class RingBufferFlip {

    private float[] elements;

    private int capacity;
    private int writePos;
    private int readPos;

    public RingBufferFlip(int capacity) {
        this.capacity = capacity + 1;
        this.elements = new float[this.capacity];
        this.readPos = 0;
        this.writePos = 0;
    }

    public void reset() {
        this.writePos = 0;
        this.readPos = 0;
    }

    public int available() {
        if (!isFlipped()) {
            return writePos - readPos;
        }
        return capacity - readPos + writePos;
    }

    public int remainingCapacity() {
        if(isFlipped()){
            return readPos - writePos - 1;
        }else{
            return readPos - writePos - 1 + capacity;
        }
    }


    public int put(float[] newElements, int length) {
        length = Math.min(length, remainingCapacity());
        int elementsAssigned = 0;
        if (!isFlipped()) {
            //readPos lower than writePos - free sections are:
            //1) from writePos to capacity
            //2) from 0 to readPos

            if (length <= capacity - writePos) {
                //new elements fit into top of elements array - copy directly
                for (; elementsAssigned < length; elementsAssigned++) {
                    this.elements[this.writePos++] = newElements[elementsAssigned];
                }

                return elementsAssigned;
            } else {
                //new elements must be divided between top and bottom of elements array

                //writing to top
                for (; this.writePos < capacity; this.writePos++) {
                    this.elements[this.writePos] = newElements[elementsAssigned++];
                }

                //writing to bottom
                this.writePos = 0;
                int endPos = Math.min(this.readPos, length - elementsAssigned);
                for (; this.writePos < endPos; this.writePos++) {
                    this.elements[writePos] = newElements[elementsAssigned++];
                }


                return elementsAssigned;
            }

        } else {
            //readPos higher than writePos - free sections are:
            //1) from writePos to readPos

            int endPos = Math.min(this.readPos, this.writePos + length);

            for (; this.writePos < endPos; this.writePos++) {
                this.elements[this.writePos] = newElements[elementsAssigned++];
            }

            return elementsAssigned;
        }
    }

    public boolean isFlipped() {
        return writePos < readPos;
    }


    public int take(float[] into, int length) {
        peek(into, 0, length);
        return skip(length);
    }

    /**
     * Advances the read position.
     *
     * @param length
     * @return how many floats were actually skipped.
     */
    public int skip(int length) {
        if(length < 0){
            return 0;
        }
        int res = Math.min(length, available());
        this.readPos = realLocation(length);
        return res;

    }

    private int realLocation(int offsetFromReadPos){
        int toSkip = Math.min(available(), offsetFromReadPos);
        int res = readPos + toSkip;
        if(res >= capacity){
            res -= capacity;
        }
        return res;
    }

    public int peek(float[] into, int start, int end) {
        if(start < 0 || start >= end){
            return 0;
        }
        int realStart = realLocation(start);
        int realEnd = realLocation(end);
        if(realStart == realEnd){
            return 0;
        }
        if(realStart < realEnd){
            for(int i = 0; i < realEnd - realStart; ++i){
                into[i] = this.elements[i+ realStart];
            }
        }else{
            for(int i = 0; i < capacity - realStart; ++i){
                into[i] = this.elements[i + realStart];
            }
            for(int i = 0; i < realEnd; ++i){
                into[i + capacity - realStart] = this.elements[i];
            }
        }
        if(available() < start){
            return 0;
        }
        if(available() < end){
            return available() - start;
        }
        return end - start;

    }
}

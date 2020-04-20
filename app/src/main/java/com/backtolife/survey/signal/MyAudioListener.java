package com.backtolife.survey.signal;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;

import com.backtolife.survey.util.MySystemClock;
import com.backtolife.survey.util.RingBufferFlip;

public class MyAudioListener {
    private static final int RECORDER_SAMPLERATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private final static int BytesPerElement = 2; // 2 bytes in 16bit format
    private final static int BufferElements2Rec = 16384 / 2; // want to play 2048 (2K) since 2 bytes we use only 1024

    public static final double INTERVAL_ALLOW_NOT_RECORD_SECONDS = 10.;
    public static final int BUFFER_SIZE_SECONDS = 20;
    // power of 2 for efficiency, because we apply FFT for searching.
    private static final int FRAMES_FOR_SEARCH = 262144 / 2;
    private final InStreamFinder finder;

    private boolean recording = false;
    private double lastTimeRecorded = 0.;
    private int totalRead = 0;
    private Handler handler;
    private HandlerThread listenThread;

    public MyAudioListener(InStreamFinder finder) {
        this.finder = finder;
    }

    public static void convertPCM16ToFloatSignal(float[] floaters, short[] pcms, int size) {
        for (int i = 0; i < size; i++) {
            floaters[i] = (float)pcms[i] / (float)32768.0;
        }
    }

    private void restart() {
        System.out.println("restarting listener");
        if(listenThread != null) {
            if (listenThread.isAlive()) {
                listenThread.quit();
            }
        }
        listenThread = new HandlerThread("MyAudioListenThread");
        listenThread.start();
        handler = new Handler(listenThread.getLooper());
    }

    public void listen() {
        // Did record during the last INTERVAL_ALLOW_NOT_RECORD_SECONDS seconds
        if (MySystemClock.getNowSeconds() < lastTimeRecorded + INTERVAL_ALLOW_NOT_RECORD_SECONDS &&
                recording){
            System.out.println("NoNeedToRestartListener");
            return;
        }
        restart();
        recording = true;
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Should I release this recorder ?
                System.out.println("minBufferSize=" + AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
                        RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING));
                AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                        RECORDER_AUDIO_ENCODING,
                        capturingBufferSize() * 8);
                recorder.startRecording();
                System.out.println("recorder state=" + recorder.getState());
                // TODO(Assaf): Estimate typicalStartRecordDelay
                double typicalStartRecordDelay = 0;
                finder.setStartTime(MySystemClock.getNowSeconds() + typicalStartRecordDelay);
                finder.initBuffer((int) (BUFFER_SIZE_SECONDS * RECORDER_SAMPLERATE),
                        FRAMES_FOR_SEARCH,
                        RECORDER_SAMPLERATE);
                try {
                    recordContinuously(recorder);
                    recorder.stop();
                } finally {
                    recorder.release();
                }
            }
        });
    }

    private void recordContinuously(AudioRecord recorder) {
        // TODO(Assaf): After 15 minutes we reset the whole thing.
        short[] tempBuffer = new short[BufferElements2Rec];
        float[] tempFloatBuffer = new float[BufferElements2Rec];
        double executionTimeSeconds = 60. * 10;

        double totalFramesToRead = executionTimeSeconds * RECORDER_SAMPLERATE;
        int iterations = (int)(totalFramesToRead / BufferElements2Rec);
        for(int i = 0; i < iterations; ++i){
            // All finding preamble logic goes in here.
            int read = recorder.read(tempBuffer, 0, BufferElements2Rec);
            printRead(read);

            // Here we should use a circular buffer, and send that buffer to the finder.
            if(read > 0) {
                lastTimeRecorded = MySystemClock.getNowSeconds();
                convertPCM16ToFloatSignal(tempFloatBuffer, tempBuffer, read);
                finder.getBuffer().put(tempFloatBuffer, read);
                finder.work();
            }else{
                switch (read){
                    case AudioRecord.ERROR_DEAD_OBJECT:
                        System.out.println("recorder is invalid - probably due to OS magic");
                        break;
                    case AudioRecord.ERROR_INVALID_OPERATION:
                        System.out.println("recorder state is invalid - probably to a bug in our code");
                        break;
                    case AudioRecord.ERROR_BAD_VALUE:
                        System.out.println("used an invalid code in value method - must be a BUG" +
                                " in our code");
                        break;
                    default:
                        System.out.println("Unknown error code " + read);
                }
                break;
            }
            try {
                // Sleep to
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        recording = false;
    }

    private void printRead(int read) {
        if(read > 0){
            totalRead += read;
        }
        if(read > 0 && (totalRead % (RECORDER_SAMPLERATE * 10)) <= BufferElements2Rec) {
            System.out.println("totalRead = " + totalRead);
        }
    }

    // Read operation is blocking anyway.
    private int capturingBufferSize() {
        return BufferElements2Rec * BytesPerElement;
    }
}

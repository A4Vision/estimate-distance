package com.backtolife.survey.util;

import android.os.Handler;
import android.os.HandlerThread;

public class MyPeriodicJob {
    private final double intervalSeconds;
    private final Runnable task;
    private final HandlerThread executingThread;
    private Handler handler;

    public MyPeriodicJob(Runnable task, double intervalSeconds, String threadName) {
        this.task = task;
        this.intervalSeconds = intervalSeconds;
        executingThread = new HandlerThread(threadName);
        executingThread.start();
        handler = new Handler(executingThread.getLooper());
    }

    public void startLooping() {
        postDelayedLoop();
    }

    private void postDelayedLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loop();
            }
        }, (int) (intervalSeconds * 1_000));
    }

    private void loop() {
        task.run();
        postDelayedLoop();
    }
}

package com.backtolife.survey.signal;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;

import com.backtolife.survey.util.MySystemClock;
import com.backtolife.survey.util.Timing;
import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class SignalSender implements Runnable {
    private static final int CHUNK = 8192;
    private final int resource;
    private final Context context;
    private final List<Timing> timings;
    private final SecureRandom random;
    private final int maxSleepSeconds;
    byte[] buffer = null;

    public SignalSender(int resource, Context context, int maxSleepSeconds) {
        this.resource = resource;
        this.context = context;
        this.timings = new ArrayList<>();
        this.maxSleepSeconds = maxSleepSeconds;
        random = new SecureRandom();
    }

    private void playSound() {
    }

    @Override
    public void run() {
        // TODO(Assaf): Be sure to use the speaker when ear-phones are connected.
        try {
            Thread.sleep(1000 * random.nextInt(maxSleepSeconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final MediaPlayer player = MediaPlayer.create(context, resource);
        getTimings(false).add(new Timing(MySystemClock.getNowSeconds()));
        player.start();

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                System.out.println("ttrying to stop");
                if(mp!=null) {
                    if(mp.isPlaying()) {
                        System.out.println("stropping");
                        mp.stop();
                    }else{
                        System.out.println("no need to stop");
                    }
                    mp.reset();
                    mp.release();
                }
            }
        });
        try {
            Thread.sleep(1000 );
        } catch (InterruptedException e) {

        }
    }

    private void initBuffer() {
        if(buffer != null){
            return;
        }
        InputStream inputStream = context.getResources().openRawResource(resource);

        buffer = new byte[1];
        try {
            inputStream.skip(46);
            buffer = ByteStreams.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized List<Timing> getTimings(boolean copy) {
        if (copy) {
            return new ArrayList<>(timings);
        } else {
            return timings;
        }
    }

    public List<Timing> getTimings() {
        return getTimings(true);
    }
}

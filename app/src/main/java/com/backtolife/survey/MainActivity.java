package com.backtolife.survey;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.backtolife.survey.gui.GUI;
import com.backtolife.survey.gui.GUIInterface;
import com.backtolife.survey.signal.InStreamFinder;
import com.backtolife.survey.signal.MyAudioListener;
import com.backtolife.survey.signal.SignalSender;
import com.backtolife.survey.signal.SignalsStatistics;
import com.backtolife.survey.signal.SlidingWindowArgmax;
import com.backtolife.survey.signal.TimingsResolver;
import com.backtolife.survey.util.Id;
import com.backtolife.survey.util.MyCallback;
import com.backtolife.survey.util.MyPeriodicJob;
import com.backtolife.survey.util.RecordPermissions;
import com.backtolife.survey.util.Timing;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private InStreamFinder finder;
    private SignalSender sender;
    private MyPeriodicJob senderJob;
    private MyAudioListener listener;
    private MyPeriodicJob resetListen;
    private GUIInterface gui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        System.out.println("onCreate !");
        gui = new GUI(this);
        requestPermissions();
        initSoundProcesses();
    }

    private void requestPermissions() {
        new RecordPermissions().askPermissions(this, this, getApplication());
    }

    MyCallback updateSoundGUICallback = new MyCallback(){
        @Override
        public void call() {
            System.out.println("found sound");
            List<Timing> timingsHeard = finder.getTimings();
            List<Timing> timingsSent = sender.getTimings();

            Id fakeId = new Id(1234);
            TimingsResolver resolver = new TimingsResolver(timingsHeard, fakeId);
            resolver.assignTimingsToSender(timingsSent, fakeId);
            gui.setSignalsStatistics(new SignalsStatistics(
                    timingsHeard, timingsSent, resolver));
        }
    };

    private void initSoundProcesses() {
        System.out.println("settingUpAllSoundProcesses");
        finder = new InStreamFinder(SlidingWindowArgmax.createDefault(getResources().openRawResource(R.raw.filtered_preamble)), updateSoundGUICallback);
        // Send signal once in every 10+r seconds, where r~Uniform[0, 9]
        sender = new SignalSender(R.raw.preamble, this, 9);
        senderJob = new MyPeriodicJob(sender, 6, "SenderThread");
        listener = new MyAudioListener(finder);
        senderJob.startLooping();
        resetListen = new MyPeriodicJob(new Runnable() {
            // Try to restart listening once in every 5 seconds.
            @Override
            public void run() {
                try {
                    listener.listen();
                }catch (Exception ignored){
                }
            }
        }, 5., "ResetListener");
        listener.listen();
        resetListen.startLooping();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

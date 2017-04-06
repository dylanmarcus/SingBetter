package com.inyourface.singbetter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

public class MainActivity extends AppCompatActivity {

    private TextView freqText;
    private Button freqButton;
    private Button historyViewButton;
    private Button noteSelectViewButton;
    float pitchInHz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Buttons
        historyViewButton = (Button) findViewById(R.id.history_view_button);
        noteSelectViewButton = (Button) findViewById(R.id.note_select_view_button);

        pitchInHz = 0;
        freqText = (TextView) findViewById(R.id.freq_text);
        /*AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);

        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result,AudioEvent e) {
                pitchInHz = result.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        freqText.setText("" + pitchInHz);
                    }
                });
            }
        };
        AudioProcessor p = new PitchProcessor(PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(p);
        Thread t = new Thread(dispatcher,"Audio Dispatcher");
        t.start();*/
        freqText.setText("" + pitchInHz + " Hz");
    }

    /** Called when the user taps the History button */
    public void goToHistoryView(View view) {
        Intent intent = new Intent(this, HistoryView.class);
        startActivity(intent);
    }

    /** Called when the user taps the Note Select button */
    public void goToNoteSelectActivity(View view) {
        Intent intent = new Intent(this, NoteSelectActivity.class);
        startActivity(intent);
    }
}

package com.inyourface.singbetter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

import com.inyourface.singbetter.Objects.Note;
import com.inyourface.singbetter.Objects.Session;
import com.inyourface.singbetter.RecordedSessions.SessionsViewActivity;
import com.inyourface.singbetter.db.SessionDAL;


public class MainActivity extends AppCompatActivity
{
    private TextView freqText;
    private Button freqButton;
    private ImageButton recordButton;
    private TextView recordArray;       // delete later
    private TextView selectedNoteText;
    private ImageButton historyViewButton;
    private ImageButton noteSelectViewButton;
    // The octave: C C#(D♭) D D#(E♭) E F F#(G♭) G G#(A♭) A A#(B♭) B
    private double adjustPitchMinDif;
    double pitchInHz;
    double adjustedPitchInHz;
    private Note selectedNote;
    private TextView current_note_text;
    private Note currentNote;
    private double screenRange;
    private View userFrequencyBar;
    private double frequencyBarPosition;
    private boolean isRecording;
    private Calendar calendar = Calendar.getInstance();
    private long timeStartedRecording;
    private ArrayList<Integer> scoreList;
    private SessionDAL db;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedNote = Note.D;
        currentNote = Note.D;
        timeStartedRecording = 0;
        scoreList = new ArrayList<Integer>();
        db = new SessionDAL(this);


        isRecording = false;

        selectedNoteText = (TextView) findViewById(R.id.selected_note_text);
        selectedNoteText.setText(selectedNote.getNoteString());

        // Make user frequency bar accessable
        userFrequencyBar = (View) findViewById(R.id.user_frequency_bar);
        // Access frequency bar xml margin parameters
        final PercentRelativeLayout.LayoutParams layoutParams = (PercentRelativeLayout.LayoutParams) userFrequencyBar.getLayoutParams();
        final PercentLayoutHelper.PercentLayoutInfo percentLayoutInfo = layoutParams.getPercentLayoutInfo();


        // Buttons
        historyViewButton = (ImageButton) findViewById(R.id.history_view_button);
        noteSelectViewButton = (ImageButton) findViewById(R.id.note_select_view_button);
        recordButton = (ImageButton) findViewById(R.id.toggle_button_record);

        // shows frequencies in an array on main view (can delete later once data goes into database)
        //recordArray = (TextView) findViewById(R.id.record_array_text);

        pitchInHz = 0.0;
        freqText = (TextView) findViewById(R.id.freq_text);
        current_note_text = (TextView) findViewById(R.id.current_note_text);
        current_note_text.setText(currentNote.getNoteString());


        // Take the pitch in Hz and convert it into a note
        // Frequency of a note that is +/- n half steps away
        // fn = f0 * (a)^n
        // f0 = fixed note frequency (A4 at 440 Hz)
        // a = (2)^(1/12)
        // n = number of half steps away from the fixed note (+/-)
        // The octave: C C#(D♭) D D#(E♭) E F F#(G♭) G G#(A♭) A A#(B♭) B

        // For a given frequency:
        // Check if it is between 261.63 Hz and 523.25 Hz (C4 and C5)
        // If it is, find which frequency cutoff it is closest to
        /* Cutoffs:
        C4 - 261.63
        C4sharp - 277.18
        D4 - 293.66
        D4sharp - 311.13
        E4 - 329.63
        F4 - 349.23
        F4sharp - 369.99
        G4 - 392.00
        G4sharp - 415.30
        A4 - 440
        Asharp - 466.16
        B4 - 493.88
        */

        // START Pitch Code to comment/uncomment

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result,AudioEvent e) {
                pitchInHz = result.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adjustPitchMinDif = 10000.00;

                        adjustedPitchInHz = pitchInHz;
                        if(adjustedPitchInHz < (261.63-(261.63-246.94)/2.0))
                            adjustedPitchInHz = adjustedPitchInHz * 2;
                        if(adjustedPitchInHz < (261.63-(261.63-246.94)/2.0))
                            adjustedPitchInHz = adjustedPitchInHz * 2;
                        if(adjustedPitchInHz < (261.63-(261.63-246.94)/2.0))
                            adjustedPitchInHz = adjustedPitchInHz * 2;
                        if(adjustedPitchInHz < (261.63-(261.63-246.94)/2.0))
                            adjustedPitchInHz = adjustedPitchInHz * 2;
                        if(adjustedPitchInHz > (493.88+(523.25-493.88)/2.0))
                            adjustedPitchInHz = adjustedPitchInHz / 2;
                        if(adjustedPitchInHz > (493.88+(523.25-493.88)/2.0))
                            adjustedPitchInHz = adjustedPitchInHz / 2;
                        if(adjustedPitchInHz > (493.88+(523.25-493.88)/2.0))
                            adjustedPitchInHz = adjustedPitchInHz / 2;
                        if(adjustedPitchInHz > (493.88+(523.25-493.88)/2.0))
                            adjustedPitchInHz = adjustedPitchInHz / 2;


                        // When adjusted pitch is within octave 4, set a variable to measure the difference
                        // between the adjusted frequency and each cutoff frequency. Whichever is the smallest,
                        // that is the current note


                        Note[] notes = Note.values();

                        for (int i = 0; i < notes.length; i++) {
                            double currentDif = Math.abs((double) notes[i].getNoteFrequency() - adjustedPitchInHz);
                            if (currentDif < adjustPitchMinDif) {
                                adjustPitchMinDif = currentDif;
                                currentNote = notes[i];
                            }
                        }

                        String freqString = String.format("%.2f", pitchInHz);
                        freqText.setText("" + freqString + " Hz ");
                        current_note_text.setText(currentNote.getNoteString());

                        screenRange = selectedNote.getMaxFrequency() - selectedNote.getMinFrequency();


                        // calculate percentage value for bar position
                        // EDGE BAR POSITIONS DO NOT WORK CORRECTLY FOR B AND C

                        if (adjustedPitchInHz > selectedNote.getMaxFrequency()) {
                            frequencyBarPosition = 0;
                            userFrequencyBar.setBackgroundColor(Color.parseColor("#FF4081"));

                        }
                        else if (adjustedPitchInHz < selectedNote.getMinFrequency()) {
                            frequencyBarPosition = 0.99;
                            userFrequencyBar.setBackgroundColor(Color.parseColor("#FF4081"));
                        }

                        else {
                            frequencyBarPosition = (((selectedNote.getMaxFrequency() - adjustedPitchInHz) / screenRange) * 100) * 0.01f;
                            if (adjustedPitchInHz < selectedNote.getNoteFrequency() + (selectedNote.getNoteFrequency() / 400) &&
                                adjustedPitchInHz > selectedNote.getNoteFrequency() - (selectedNote.getNoteFrequency() / 400)) {
                                userFrequencyBar.setBackgroundColor(Color.parseColor("#11EAA7"));
                            } else {
                                userFrequencyBar.setBackgroundColor(Color.parseColor("#F2F2F2"));
                            }
                        }

                        if (pitchInHz == -1)
                            frequencyBarPosition = 2;


                        // change frequency bar position
                        percentLayoutInfo.topMarginPercent = (float) frequencyBarPosition;
                        userFrequencyBar.setLayoutParams(layoutParams);

                        // If we are recording, get the frequency bar position every 0.1s
                        if(isRecording) {
                            if((calendar.getTimeInMillis() - timeStartedRecording)%100==0)
                                scoreList.add((int)(frequencyBarPosition*20.0-10.0));
                        }
                    }
                });
            }
        };
        AudioProcessor p = new PitchProcessor(PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(p);
        Thread t = new Thread(dispatcher,"Audio Dispatcher");
        t.start();

		// END Pitch Code to comment/uncomment

		historyViewButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, SessionsViewActivity.class);
				intent.putExtra("currentNote", currentNote.getNoteString()); // TODO: Constant ID
				startActivity(intent);
			}
		});

		noteSelectViewButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, NoteSelectActivity.class);
				int requestCode = 1; // TODO: Constant for meaningful request code
				startActivityForResult(intent, requestCode);
			}
		});
        // END Pitch Code to comment/uncomment
    }

    /** Called when the user taps the History button */
    public void toggleRecordBtn(View view) {
        if(isRecording) {
            // We have stopped recording
            isRecording = false;

            // Update the record button image to the record start icon
            recordButton.setImageResource(R.drawable.record_start_icon);

            // Establish a database connection

            db.open();

            // Create a new session
            Session session = new Session();
            session.setNote(selectedNote);
            session.setData(scoreList);
            session.setDateCreated(calendar.getTimeInMillis());
            session.setAssociatedMP3("/bin/SongName.mp3");
            session.setCustomName("Song Name");

            // Create a db entry in the current note
            db.insertSession(session);
            //db.insertSession(Util.generateSession());
            db.close();

            scoreList.clear();
        }
        else {
            // We have started recording
            isRecording = true;

            // Update the record button image to the record stop icon
            recordButton.setImageResource(R.drawable.record_stop_icon);

            // Initialize the time when we started recording
            timeStartedRecording = calendar.getTimeInMillis();
        }
    }

    // Called when a startActivityForResult is finished. Request codes MUST be checked to ensure you're getting the right data.
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK) // TODO: Check request code
        {
            selectedNote = Util.stringToNote(data.getStringExtra("selectedNote")); // TODO: This needs a meaningful id
            selectedNoteText.setText(selectedNote.getNoteString());
        }
    }

    /** Called when the user taps the Record Button */
    /*public void changeRecordState (View view) {
        ToggleRecord recordtimer = new ToggleRecord();
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
            // do this when ON ...
            recordArray.setText("Recording...");
            // start recording data from pitchHz and store it into an ArrayList
            recordtimer.start();
            // Change button image
        }
        else {
            // do this when OFF ...
            // stop recording data
            recordtimer.stop();
            // copy ArrayList into a new ArrayList
            ArrayList<Double> HzArray = recordtimer.getHzArray();
            // Double ArrayList toString
            String HzArrayToString = TextUtils.join(", ", HzArray);
            // print the array string onto the screen
            recordArray.setText(HzArrayToString);
            // clear ArrayList (inside ToggleRecord.java)
            recordtimer.clear();
        }
    }*/
}

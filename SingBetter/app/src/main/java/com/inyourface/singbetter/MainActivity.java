package com.inyourface.singbetter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.inyourface.singbetter.Objects.Note;
import com.inyourface.singbetter.RecordedSessions.SessionsViewActivity;
import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import com.inyourface.singbetter.Objects.Session;
import com.inyourface.singbetter.db.SessionDAL;


public class MainActivity extends AppCompatActivity
{
    //private TextView current_note_text;
    //private TextView freqText;
    private TextView selectedNoteText;
    //private Button freqButton;
    private Button toggleRecordButton;
    private Button historyViewButton;
    private Button noteSelectViewButton;
    private View userFrequencyBar;

    private Note selectedNote;
    private Note currentNote;

    private SessionDAL sessionsDatabase;

    private double pitchInHz;
    private double adjustedPitchInHz;
    private double adjustPitchMinDif;

    private double screenRange;
    private double frequencyBarPosition;

    private boolean isRecording;
    private ArrayList<Integer> scoreList;

    private long lastRecordedTime;
    private String inputDialogResultText;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastRecordedTime = 0;
        selectedNote = Note.D;
        currentNote = Note.D;
        scoreList = new ArrayList<Integer>();
        sessionsDatabase = new SessionDAL(this);
        isRecording = false;

        selectedNoteText = (TextView) findViewById(R.id.selected_note_text);
        selectedNoteText.setText(selectedNote.getNoteString());

        // Make user frequency bar accessable
        userFrequencyBar = (View) findViewById(R.id.user_frequency_bar);
        // Access frequency bar xml margin parameters
        final PercentRelativeLayout.LayoutParams layoutParams = (PercentRelativeLayout.LayoutParams) userFrequencyBar.getLayoutParams();
        final PercentLayoutHelper.PercentLayoutInfo percentLayoutInfo = layoutParams.getPercentLayoutInfo();


        // Buttons
        historyViewButton = (Button) findViewById(R.id.history_view_button);
        noteSelectViewButton = (Button) findViewById(R.id.note_select_view_button);
        toggleRecordButton = (Button) findViewById(R.id.toggle_button_record);

        pitchInHz = 0.0;
        /*
        freqText = (TextView) findViewById(R.id.freq_text);
        current_note_text = (TextView) findViewById(R.id.current_note_text);
        current_note_text.setText(currentNote.getNoteString());
        */

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

        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(Constants.AUDIO_SAMPLE_RATE, Constants.AUDIO_BUFFER_SIZE, Constants.AUDIO_BUFFER_OVERLAP);
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
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

                        // CURRENT NOTE TEXT LOGIC
                        /*
                        String freqString = String.format("%.2f", pitchInHz);
                        freqText.setText("" + freqString + " Hz ");
                        current_note_text.setText(currentNote.getNoteString());
                        */

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
                            frequencyBarPosition = 1.01;

                        // change frequency bar position
                        percentLayoutInfo.topMarginPercent = (float) frequencyBarPosition;
                        userFrequencyBar.setLayoutParams(layoutParams);

                        // If we are recording, get the frequency bar position every 0.1s
                        long currentTime = Util.getCurrentTimeInMilliseconds();
                        if(isRecording) {
                            if(((currentTime - lastRecordedTime) % (Constants.MILLISECONDS_IN_SECOND / Constants.INTERVAL)) > 0)
                            {
                                lastRecordedTime = currentTime;
                                scoreList.add((int)(100 - (frequencyBarPosition*100.0)));
                            }
                        }
                    }
                });
            }
        };
        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, Constants.AUDIO_SAMPLE_RATE, Constants.AUDIO_BUFFER_SIZE, pdh);
        dispatcher.addAudioProcessor(p);
        Thread t = new Thread(dispatcher,"Audio Dispatcher");
        t.start();

		historyViewButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, SessionsViewActivity.class);
				intent.putExtra(Constants.EXTRA_SESSIONS_ACTIVITY_SELECTED_NOTE, selectedNote.getNoteString());
				startActivity(intent);
			}
		});

		noteSelectViewButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(MainActivity.this, NoteSelectActivity.class);
				int requestCode = Constants.NOTE_SELECTED_REQUEST_CODE;
				startActivityForResult(intent, requestCode);
			}
		});

        toggleRecordButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isRecording) {
                    // We have stopped recording
                    isRecording = false;

                    // Update the record button image to the record start icon
                    toggleRecordButton.setBackgroundResource(R.drawable.record_start_icon);

                    // Re-enable note switch and history view buttons
                    historyViewButton.setEnabled(true);
                    historyViewButton.setVisibility(View.VISIBLE);
                    noteSelectViewButton.setEnabled(true);
                    noteSelectViewButton.setVisibility(View.VISIBLE);


                    // Construct a dialog to prompt the user to enter a name for their recorded session
                    final long timeEnded = Util.getCurrentTimeInMilliseconds();
                    final String hintText = selectedNote.getNoteString() + " " + Util.convertEpochToReadable(timeEnded);

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.input_dialog));
                    builder.setTitle("Please enter a name for your recorded session.");

                    // Setup our input text field
                    final EditText input = new EditText(MainActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    input.setHint(hintText);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            inputDialogResultText = input.getText().toString();
                            // If nothing was entered by the user, use a default name (note + current time)
                            if(inputDialogResultText.equals(""))
                            {
                                inputDialogResultText = hintText;
                            }

                            // Create a new session
                            Session session = new Session();
                            session.setNote(selectedNote);
                            session.setData(scoreList);
                            session.setDateCreated(timeEnded);
                            session.setCustomName(inputDialogResultText);

                            // Create a sessionsDatabase entry in the current note
                            sessionsDatabase.open();
                            sessionsDatabase.insertSession(session);
                            sessionsDatabase.close();

                            scoreList.clear();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();

                            scoreList.clear();
                        }
                    });

                    builder.show();
                }
                else {
                    // We have started recording
                    isRecording = true;

                    // Update the record button image to the record stop icon
                    toggleRecordButton.setBackgroundResource(R.drawable.record_stop_icon);

                    // Disable note switch and history view buttons
                    historyViewButton.setEnabled(false);
                    historyViewButton.setVisibility(View.INVISIBLE);
                    noteSelectViewButton.setEnabled(false);
                    noteSelectViewButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    // Called when a startActivityForResult is finished. Request codes MUST be checked to ensure you're getting the right data.
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if((resultCode == RESULT_OK) && (requestCode == Constants.NOTE_SELECTED_REQUEST_CODE))
        {
            selectedNote = Util.stringToNote(data.getStringExtra(Constants.EXTRA_NOTE_SELECT_ACTIVITY_SELECTED_NOTE));
            selectedNoteText.setText(selectedNote.getNoteString());
        }
    }
}

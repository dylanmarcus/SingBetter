package com.inyourface.singbetter.RecordedSessions.RecyclerView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inyourface.singbetter.Objects.Note;
import com.inyourface.singbetter.R;
import com.inyourface.singbetter.Objects.Session;
import com.inyourface.singbetter.Util;
import com.inyourface.singbetter.db.SessionDataSource;

import java.util.ArrayList;

/**
 * Created by Justin on 4/6/2017.
 */

public class SessionsViewActivity extends AppCompatActivity
{
	private TextView currentNoteTextView;
	private Button leftButton;
	private Button rightButton;
	private RecyclerView sessionsRecycler;
	private LinearLayoutManager sessionsLayoutManager;

	private ArrayList<Session> displayedSessions;
	private RecyclerViewAdapter adapter;
	private Note currentNote;
	private SessionDataSource db;
	public static SessionsViewActivity act;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sessions_view);

		act = this;

		currentNote = Note.C_SHARP;
		currentNoteTextView = (TextView) findViewById(R.id.sessions_current_note);
		leftButton = (Button) findViewById(R.id.sessions_note_left);
		rightButton = (Button) findViewById(R.id.session_note_right);

		/**
		 * Database interactions should be performed on a background thread, no matter
		 * what kind of query they are.
		 * TODO: Thread DB stuff (good practice even if not necessary?)
		 */
		db = new SessionDataSource(this);
		db.open();

		// The loop below just generates mostly random data.
		//for(int i = 0; i < 100; i++)
		//{
		//	db.insertSession(Util.generateSession());
		//}

		displayedSessions = db.getSessionsWithNote(currentNote.getNote());

		sessionsRecycler = (RecyclerView) findViewById(R.id.sessions_recycler);
		sessionsLayoutManager = new LinearLayoutManager(getBaseContext());
		sessionsRecycler.setLayoutManager(sessionsLayoutManager);
		adapter = new RecyclerViewAdapter(displayedSessions);
		sessionsRecycler.setAdapter(adapter);

		// TODO: These switch methods presumes we can move either direction AT LEAST ONCE on activity startup. IT WILL BREAK OTHERWISE. This behavior should be fixed.
		leftButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Update the current note icon.
				Note x = Note.noteToLeftOf(currentNote);
				currentNote = x;
				currentNoteTextView.setText(x.getNote());

				// We moved left, so we can move right (if, for example, we were at the very end of our Notes list)
				rightButton.setEnabled(true);

				// Update the recycler view.
				displayedSessions.clear();
				displayedSessions.addAll(db.getSessionsWithNote(currentNote.getNote()));
				adapter.notifyDataSetChanged();

				// Check to see if there are any notes to the left. Disable the button if not.
				if(Note.noteToLeftOf(currentNote) == null)
				{
					leftButton.setEnabled(false);
				}
			}
		});

		rightButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Update the current note icon.
				Note x = Note.noteToRightOf(currentNote);
				currentNote = x;
				currentNoteTextView.setText(x.getNote());

				// We moved right, so we can move left (if, for example, we were at the very beginning of our Notes list)
				leftButton.setEnabled(true);

				// Update the recycler view.
				displayedSessions.clear();
				displayedSessions.addAll(db.getSessionsWithNote(currentNote.getNote()));
				adapter.notifyDataSetChanged();

				// Check to see if there are any notes to the right. Disable the button if not.
				if(Note.noteToRightOf(currentNote) == null)
				{
					rightButton.setEnabled(false);
				}
			}
		});
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		// Close the database when we leave this activity.
		db.close();
	}
}

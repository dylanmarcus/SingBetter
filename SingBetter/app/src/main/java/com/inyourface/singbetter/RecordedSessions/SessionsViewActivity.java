package com.inyourface.singbetter.RecordedSessions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.inyourface.singbetter.Objects.Note;
import com.inyourface.singbetter.R;
import com.inyourface.singbetter.Objects.Session;
import com.inyourface.singbetter.RecordedSessions.RecyclerView.RecyclerViewAdapter;
import com.inyourface.singbetter.Util;
import com.inyourface.singbetter.db.SessionDAL;

import java.util.ArrayList;

/**
 * Created by Justin on 4/6/2017.
 *
 * TODO: Add a description here?
 */

public class SessionsViewActivity extends AppCompatActivity
{
	private boolean deleteMode;
	private Note currentNote;
	private TextView currentNoteTextView;

	private ImageButton leftButton;
	private ImageButton rightButton;
	private ImageButton enterDeleteModeButton;

	private Button deleteCancelButton;
	private ImageButton deleteUndoButton;
	private Button deleteFinishButton;

	private ArrayList<Session> toBeRemoved;
	private ArrayList<Session> displayedSessions;

	private RecyclerView sessionsRecycler;
	private LinearLayoutManager sessionsLayoutManager;
	private RecyclerViewAdapter adapter;

	private SessionDAL db;

	// TODO: Fix. Android studio says the below variable is bad.
	public static SessionsViewActivity act;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sessions_view);

		// Set the current note to what the selected note.
		Intent intent = getIntent();
		currentNote = Util.stringToNote(intent.getStringExtra("currentNote")); // TODO: Constant ID
		if(currentNote == null)
		{
			currentNote = Note.C_SHARP;
		}

		// Singleton? I probably did this wrong.
		act = this;

		// Establish a database connection
		db = new SessionDAL(this);
		db.open();

		// Setup for delete mode
		deleteMode = false;
		toBeRemoved = new ArrayList<Session>();

		// Set our buttons
		currentNoteTextView = (TextView) findViewById(R.id.sessions_current_note);

		leftButton = (ImageButton) findViewById(R.id.sessions_note_left);
		rightButton = (ImageButton) findViewById(R.id.session_note_right);
		enterDeleteModeButton = (ImageButton) findViewById(R.id.session_enter_delete_mode);

		deleteCancelButton = (Button) findViewById(R.id.session_delete_cancel);
		deleteUndoButton = (ImageButton) findViewById(R.id.session_delete_undo);
		deleteFinishButton = (Button) findViewById(R.id.session_delete_finish);

		// TODO: Delete loop below before final product.
		// The loop below just generates mostly random data.
		for(int i = 0; i < 50; i++)
		{
			//db.insertSession(Util.generateSession());
		}

		// Setup the recyclerview
		displayedSessions = db.getSessionsWithNote(currentNote);
		sessionsRecycler = (RecyclerView) findViewById(R.id.sessions_recycler);
		sessionsLayoutManager = new LinearLayoutManager(getBaseContext());
		sessionsRecycler.setLayoutManager(sessionsLayoutManager);
		adapter = new RecyclerViewAdapter(displayedSessions);
		sessionsRecycler.setAdapter(adapter);

		// This adds the diving lines between each item in the recycler view
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(sessionsRecycler.getContext(), sessionsLayoutManager.getOrientation());
		sessionsRecycler.addItemDecoration(dividerItemDecoration);

		// Check if we are already at one extreme of the allowed notes
		if(Util.noteToLeftOf(currentNote) == null)
		{
			leftButton.setEnabled(false);
		}
		if(Util.noteToRightOf(currentNote) == null)
		{
			rightButton.setEnabled(false);
		}


		leftButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Update the current note icon.
				Note x = Util.noteToLeftOf(currentNote);
				currentNote = x;
				currentNoteTextView.setText(x.getNoteString());

				// We moved left, so we can move right (if, for example, we were at the very end of our Notes list)
				rightButton.setEnabled(true);

				// Update the recycler view.
				displayedSessions.clear();
				displayedSessions.addAll(db.getSessionsWithNote(currentNote));
				adapter.notifyDataSetChanged();

				// Check to see if there are any notes to the left. Disable the button if not.
				if(Util.noteToLeftOf(currentNote) == null)
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
				Note x = Util.noteToRightOf(currentNote);
				currentNote = x;
				currentNoteTextView.setText(x.getNoteString());

				// We moved right, so we can move left (if, for example, we were at the very beginning of our Notes list)
				leftButton.setEnabled(true);

				// Update the recycler view.
				displayedSessions.clear();
				displayedSessions.addAll(db.getSessionsWithNote(currentNote));
				adapter.notifyDataSetChanged();

				// Check to see if there are any notes to the right. Disable the button if not.
				if(Util.noteToRightOf(currentNote) == null)
				{
					rightButton.setEnabled(false);
				}
			}
		});

		// This button enables delete mode by calling our helper function setDeleteMode.
		enterDeleteModeButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setDeleteMode(true);
			}
		});

		// Moves items from toBeRemoved back into displaySessions and turns off delete mode.
		deleteCancelButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				for(Session session : toBeRemoved)
				{
					displayedSessions.add(session);
				}
				toBeRemoved.clear();
				adapter.notifyDataSetChanged();
				setDeleteMode(false);
			}
		});

		// Moves the most recently added item of toBeRemoved back into displayedSessions.
		deleteUndoButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				displayedSessions.add(toBeRemoved.get(toBeRemoved.size() - 1));
				toBeRemoved.remove(toBeRemoved.size() - 1);
				adapter.notifyDataSetChanged();

				if(toBeRemoved.size() == 0)
				{
					deleteUndoButton.setEnabled(false);
				}
			}
		});

		// Deletes all items in toBeRemoved from the database. Exits delete mode.
		deleteFinishButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				for(Session session : toBeRemoved)
				{
					db.deleteSession(session);
				}
				toBeRemoved.clear();
				setDeleteMode(false);
			}
		});
	}

	// Override this so we can close the database connection when we exit this activity.
	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		db.close();
	}

	// toBeRemoved contains sessions that will be permanently deleted on clicking "finish."
	// These sessions are removed from the display but can be "undone" before finishing.
	public void insertToBeRemoved(int pos)
	{
		deleteUndoButton.setEnabled(true);
		toBeRemoved.add(displayedSessions.get(pos));
		displayedSessions.remove(pos);
		adapter.notifyItemRemoved(pos);
	}

	private void setDeleteMode(boolean value)
	{
		if(deleteMode != value)
		{
			deleteMode = value;
			deleteModeChanged();
		}
	}

	// Used by the recycler view holder to determine if a click on an item results in a delete or viewing item information.
	public boolean getDeleteMode()
	{
		return deleteMode;
	}

	// Handles the changing of button states when changing delete modes.
	private void deleteModeChanged()
	{
		if(deleteMode)
		{
			// Disable the note switching buttons and hide them
			leftButton.setEnabled(false);
			rightButton.setEnabled(false);
			leftButton.setVisibility(View.INVISIBLE);
			rightButton.setVisibility(View.INVISIBLE);

			// Disable and hide the "enter delete mode" button
			enterDeleteModeButton.setEnabled(false);
			enterDeleteModeButton.setVisibility(View.INVISIBLE);

			// Enable and show the cancel, undo (not getting enabled), and finish buttons
			deleteCancelButton.setEnabled(true);
			deleteCancelButton.setVisibility(View.VISIBLE);
			deleteUndoButton.setVisibility(View.VISIBLE);
			deleteFinishButton.setEnabled(true);
			deleteFinishButton.setVisibility(View.VISIBLE);
		}
		else
		{
			// Re-enable and show note-switching buttons
			leftButton.setEnabled(true);
			rightButton.setEnabled(true);
			leftButton.setVisibility(View.VISIBLE);
			rightButton.setVisibility(View.VISIBLE);

			// Re-enable and show the "enter delete mode" button
			enterDeleteModeButton.setEnabled(true);
			enterDeleteModeButton.setVisibility(View.VISIBLE);

			// Disable and hide the cancel, undo, and finish buttons
			deleteCancelButton.setEnabled(false);
			deleteCancelButton.setVisibility(View.INVISIBLE);
			deleteUndoButton.setEnabled(false);
			deleteUndoButton.setVisibility(View.INVISIBLE);
			deleteFinishButton.setEnabled(false);
			deleteFinishButton.setVisibility(View.INVISIBLE);
		}
	}
}

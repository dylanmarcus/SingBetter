package com.inyourface.singbetter.RecordedSessions;

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

	public static SessionsViewActivity act;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sessions_view);

		act = this;

		db = new SessionDAL(this);
		db.open();

		deleteMode = false;
		toBeRemoved = new ArrayList<Session>();

		currentNote = Note.C_SHARP;
		currentNoteTextView = (TextView) findViewById(R.id.sessions_current_note);

		leftButton = (ImageButton) findViewById(R.id.sessions_note_left);
		rightButton = (ImageButton) findViewById(R.id.session_note_right);
		enterDeleteModeButton = (ImageButton) findViewById(R.id.session_enter_delete_mode);

		deleteCancelButton = (Button) findViewById(R.id.session_delete_cancel);
		deleteUndoButton = (ImageButton) findViewById(R.id.session_delete_undo);
		deleteFinishButton = (Button) findViewById(R.id.session_delete_finish);

		/**
		 * Database interactions should be performed on a background thread, no matter
		 * what kind of query they are.
		 * TODO: Thread DB stuff (good practice even if not necessary?)
		 */

		// The loop below just generates mostly random data.
		for(int i = 0; i < 50; i++)
		{
			db.insertSession(Util.generateSession());
		}

		// This sets up the recycler view
		displayedSessions = db.getSessionsWithNote(currentNote);
		sessionsRecycler = (RecyclerView) findViewById(R.id.sessions_recycler);
		sessionsLayoutManager = new LinearLayoutManager(getBaseContext());
		sessionsRecycler.setLayoutManager(sessionsLayoutManager);
		adapter = new RecyclerViewAdapter(displayedSessions);
		sessionsRecycler.setAdapter(adapter);

		// This adds the diving lines between each item in the recycler view
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(sessionsRecycler.getContext(), sessionsLayoutManager.getOrientation());
		sessionsRecycler.addItemDecoration(dividerItemDecoration);

		// TODO: These switch methods presumes we can move either direction AT LEAST ONCE on activity startup. IT WILL BREAK OTHERWISE. This behavior should be fixed.
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

		enterDeleteModeButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				setDeleteMode(true);
			}
		});

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

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		// Close the database when we leave this activity.
		db.close();
	}

	public void setUndoButton(boolean value)
	{
		deleteUndoButton.setEnabled(value);
	}

	public void insertToBeRemoved(int pos)
	{
		toBeRemoved.add(displayedSessions.get(pos));
		displayedSessions.remove(pos);
		adapter.notifyItemRemoved(pos);
	}

	private void setDeleteMode(boolean value)
	{
		if(deleteMode == value)
		{
			return;
		}
		else
		{
			deleteMode = value;
			deleteModeChanged();
		}
	}

	public boolean getDeleteMode()
	{
		return deleteMode;
	}

	private void deleteModeChanged()
	{
		if(deleteMode)
		{
			leftButton.setEnabled(false);
			rightButton.setEnabled(false);
			enterDeleteModeButton.setEnabled(false);
			enterDeleteModeButton.setVisibility(View.INVISIBLE);
			deleteCancelButton.setEnabled(true);
			deleteCancelButton.setVisibility(View.VISIBLE);
			//deleteUndoButton.setEnabled(true);
			deleteUndoButton.setVisibility(View.VISIBLE);
			deleteFinishButton.setEnabled(true);
			deleteFinishButton.setVisibility(View.VISIBLE);
		}
		else
		{
			leftButton.setEnabled(true);
			rightButton.setEnabled(true);
			enterDeleteModeButton.setEnabled(true);
			enterDeleteModeButton.setVisibility(View.VISIBLE);
			deleteCancelButton.setEnabled(false);
			deleteCancelButton.setVisibility(View.INVISIBLE);
			deleteUndoButton.setEnabled(false);
			deleteUndoButton.setVisibility(View.INVISIBLE);
			deleteFinishButton.setEnabled(false);
			deleteFinishButton.setVisibility(View.INVISIBLE);
		}
	}
}

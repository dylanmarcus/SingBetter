package com.inyourface.singbetter.RecordedSessions.RecyclerView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.inyourface.singbetter.R;
import com.inyourface.singbetter.Session;
import com.inyourface.singbetter.Util;
import com.inyourface.singbetter.db.SessionDataSource;

import java.util.ArrayList;

/**
 * Created by Justin on 4/6/2017.
 */

public class SessionsViewActivity extends AppCompatActivity
{
	private RecyclerView sessionsRecycler;
	private LinearLayoutManager sessionsLayoutManager;
	private SessionDataSource db;
	public static SessionsViewActivity act;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sessions_view);

		act = this;

		/**
		 * Database interactions should be performed on a background thread, no matter
		 * what kind of query they are.
		 * TODO: Thread this stuff
		 */
		db = new SessionDataSource(this);
		db.open();

		for(int i = 0; i < 10; i++)
		{
			//db.insertSession(Util.generateSession());
		}

		ArrayList<Session> sessions = db.getAllSessions();

		sessionsRecycler = (RecyclerView) findViewById(R.id.sessions_recycler);
		sessionsLayoutManager = new LinearLayoutManager(getBaseContext());
		sessionsRecycler.setLayoutManager(sessionsLayoutManager);
		RecyclerViewAdapter adapter = new RecyclerViewAdapter(sessions);
		sessionsRecycler.setAdapter(adapter);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		db.close(); // Close the database when we leave this activity.
	}
}

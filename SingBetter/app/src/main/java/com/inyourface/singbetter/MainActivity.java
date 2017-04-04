package com.inyourface.singbetter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.inyourface.singbetter.db.Session;
import com.inyourface.singbetter.db.SessionDataSource;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/**
		 * Database interactions should be performed on a background thread, no matter
		 * what kind of query they are.
		 */
		SessionDataSource dataSource = new SessionDataSource(this);
		dataSource.open();

		List<Session> values = dataSource.getAllSessions();
	}
}

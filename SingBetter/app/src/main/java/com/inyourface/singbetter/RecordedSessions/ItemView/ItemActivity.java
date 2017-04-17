package com.inyourface.singbetter.RecordedSessions.ItemView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.inyourface.singbetter.R;
import com.inyourface.singbetter.RecordedSessions.RecyclerView.RecyclerViewAdapter;
import com.inyourface.singbetter.Objects.Session;

/**
 * Created by Justin on 4/15/2017.
 */

public class ItemActivity extends AppCompatActivity
{
	private TextView customNameTextView;
	private TextView noteTextView;
	private TextView dateCreatedTextView;
	private TextView associatedMP3TextView;
	private TextView dataTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);

		customNameTextView = (TextView) findViewById(R.id.item_custom_name);
		noteTextView = (TextView) findViewById(R.id.item_note);
		dateCreatedTextView = (TextView) findViewById(R.id.item_date_created);
		associatedMP3TextView = (TextView) findViewById(R.id.item_associated_mp3);
		dataTextView = (TextView) findViewById(R.id.item_data);

		Intent intent = getIntent();
		int pos = intent.getExtras().getInt("clickPosition");

		Session session = RecyclerViewAdapter.getSession(pos);

		customNameTextView.setText(session.getCustomName());
		noteTextView.setText(session.getNote());
		dateCreatedTextView.setText(session.getDateCreated());
		associatedMP3TextView.setText(session.getAssociatedMP3());
		dataTextView.setText(session.getData());
	}
}

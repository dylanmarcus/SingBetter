package com.inyourface.singbetter.RecordedSessions.ItemView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inyourface.singbetter.R;
import com.inyourface.singbetter.RecordedSessions.RecyclerView.RecyclerViewAdapter;
import com.inyourface.singbetter.Objects.Session;
import com.inyourface.singbetter.Util;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

/**
 * Created by Justin on 4/15/2017.
 */

public class ItemActivity extends AppCompatActivity
{
	private TextView customNameTextView;
	private TextView noteTextView;
	private TextView dateCreatedTextView;
	private GraphView graphView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item);

		customNameTextView = (TextView) findViewById(R.id.item_custom_name);
		noteTextView = (TextView) findViewById(R.id.item_note);
		dateCreatedTextView = (TextView) findViewById(R.id.item_date_created);
		graphView = (GraphView) findViewById(R.id.graph);

		Intent intent = getIntent();
		int pos = intent.getExtras().getInt("clickPosition");

		Session session = RecyclerViewAdapter.getSession(pos);

		customNameTextView.setText(session.getCustomName());
		noteTextView.setText(session.getNote().getNoteString());
		dateCreatedTextView.setText(Util.convertEpochToReadable(session.getDateCreated()));

		ArrayList<Integer> sessionData = session.getData();
		DataPoint[] dataPoints = new DataPoint[sessionData.size()];
		for(int i = 0; i < sessionData.size(); i++)
		{
			dataPoints[i] = new DataPoint(i, sessionData.get(i));
		}

		LineGraphSeries<DataPoint> graphSeries = new LineGraphSeries<>(dataPoints);
		graphView.addSeries(graphSeries);
	}
}

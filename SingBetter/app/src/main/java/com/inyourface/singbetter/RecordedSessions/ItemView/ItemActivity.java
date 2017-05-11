package com.inyourface.singbetter.RecordedSessions.ItemView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.inyourface.singbetter.Constants;
import com.inyourface.singbetter.R;
import com.inyourface.singbetter.RecordedSessions.RecyclerView.RecyclerViewAdapter;
import com.inyourface.singbetter.Objects.Session;
import com.inyourface.singbetter.Util;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
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

		Intent intent = getIntent();
		int pos = intent.getExtras().getInt(Constants.EXTRA_ITEM_VIEW_CLICK_POSITION);

		customNameTextView = (TextView) findViewById(R.id.item_custom_name);
		noteTextView = (TextView) findViewById(R.id.item_note);
		dateCreatedTextView = (TextView) findViewById(R.id.item_date_created);
		graphView = (GraphView) findViewById(R.id.item_graph);

		// Retrieve the data for the session we clicked
		Session session = RecyclerViewAdapter.getSession(pos);

		customNameTextView.setText(session.getCustomName());
		noteTextView.setText(session.getNote().getNoteString());
		dateCreatedTextView.setText(Util.convertEpochToReadable(session.getDateCreated()));


		// Populate our graph with the session data
		ArrayList<Integer> sessionData = session.getData();
		DataPoint[] dataPoints = new DataPoint[sessionData.size()];
		for(int i = 0; i < sessionData.size(); i++)
		{
			// Our x axis will represent time, so use our interval to "calculate the delay" between points
			// The interval IS NOT SAVED, if it changes in a future version then all past data will be wrong
			long ms = (long) i * (Constants.MILLISECONDS_IN_SECOND / Constants.INTERVAL);
			dataPoints[i] = new DataPoint(ms, sessionData.get(i));
		}
		LineGraphSeries<DataPoint> graphSeries = new LineGraphSeries<>(dataPoints);

		// Customize the graph to make the data readable
		graphSeries.setColor(Color.RED);

		graphView.getViewport().setYAxisBoundsManual(true);
		graphView.getViewport().setMinY(0);
		graphView.getViewport().setMaxY(100);

		graphView.getViewport().setXAxisBoundsManual(true);
		graphView.getViewport().setMinX(0);
		graphView.getViewport().setMaxX(Constants.ITEM_ACTIVITY_SECONDS_TO_SHOW_ON_GRAPH * Constants.MILLISECONDS_IN_SECOND); // How many seconds we want visible

		graphView.getViewport().setScrollable(true);
		graphView.getViewport().setScrollableY(true);

		final StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
		// The Y axis will use static labels, which just represent the note above and below the saved note
		staticLabelsFormatter.setVerticalLabels(Util.getAdjacentNotes(session.getNote()));
		// The X axis will use dynamic labels (because it represents time) --WE DO NOT NEED TO USE LOCAL FORMATTING, OUR TIME VALUES ALWAYS START FROM 0--
		staticLabelsFormatter.setDynamicLabelFormatter(new DateAsXAxisLabelFormatter(ItemActivity.this, new SimpleDateFormat(Constants.ITEM_ACTIVITY_GRAPH_X_AXIS_SIMPLE_DATE_FORMAT)));
		graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

		graphView.addSeries(graphSeries);
	}
}

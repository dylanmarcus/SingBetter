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
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.NumberFormat;
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

		customNameTextView = (TextView) findViewById(R.id.item_custom_name);
		noteTextView = (TextView) findViewById(R.id.item_note);
		dateCreatedTextView = (TextView) findViewById(R.id.item_date_created);
		graphView = (GraphView) findViewById(R.id.item_graph);

		Intent intent = getIntent();
		int pos = intent.getExtras().getInt(Constants.EXTRA_ITEM_VIEW_CLICK_POSITION);

		Session session = RecyclerViewAdapter.getSession(pos);

		customNameTextView.setText(session.getCustomName());
		noteTextView.setText(session.getNote().getNoteString());
		dateCreatedTextView.setText(Util.convertEpochToReadable(session.getDateCreated()));

		// Custom label for Y axis
		StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
		staticLabelsFormatter.setVerticalLabels(Util.getAdjacentNotes(session.getNote()));
		graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

		// Custom label for X axis
		graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(ItemActivity.this, new SimpleDateFormat("mm:ss")));

		ArrayList<Integer> sessionData = session.getData();
		DataPoint[] dataPoints = new DataPoint[sessionData.size()];
		for(int i = 0; i < sessionData.size(); i++)
		{
			// Convert this to milliseconds so we can take advantage of the mm:ss format of the x axis
			double ms = (double) i * (1000 / Constants.INTERVAL);
			dataPoints[i] = new DataPoint(ms, sessionData.get(i));
		}

		LineGraphSeries<DataPoint> graphSeries = new LineGraphSeries<>(dataPoints);

		graphSeries.setColor(Color.RED);

		graphView.getViewport().setYAxisBoundsManual(true);
		graphView.getViewport().setMinY(-100);
		graphView.getViewport().setMaxY(100);

		graphView.getViewport().setXAxisBoundsManual(true);
		graphView.getViewport().setMinX(0);
		graphView.getViewport().setMaxX(40 * 1000); // How many seconds we want visible

		graphView.getViewport().setScrollable(true);
		graphView.getViewport().setScrollableY(true);

		graphView.addSeries(graphSeries);
	}
}

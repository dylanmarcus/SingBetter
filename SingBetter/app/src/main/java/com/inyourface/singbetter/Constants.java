package com.inyourface.singbetter;

/**
 * Created by Justin on 5/5/2017.
 *
 * im getting rid of the magic numbers, are you happy now, fisher?!
 */

public class Constants
{
	// How many times per second we save a data point
	// Cannot do anything higher than 50, we do not receive events fast enough for that
	public static final int INTERVAL = 10;

	public static final int NOTE_SELECTED_REQUEST_CODE = 1;
	public static final String EXTRA_ITEM_VIEW_CLICK_POSITION = "itemViewClickPosition";
	public static final String EXTRA_SESSIONS_ACTIVITY_SELECTED_NOTE = "sessionsActivitySelectedNote";
	public static final String EXTRA_NOTE_SELECT_ACTIVITY_SELECTED_NOTE = "noteSelectActivitySelectedNote";

	public static final int AUDIO_SAMPLE_RATE = 22050;
	public static final int AUDIO_BUFFER_SIZE = 2480;
	public static final int AUDIO_BUFFER_OVERLAP = 0;

	public static final int ITEM_ACTIVITY_SECONDS_TO_SHOW_ON_GRAPH = 40;
	public static final String ITEM_ACTIVITY_GRAPH_X_AXIS_SIMPLE_DATE_FORMAT = "mm:ss";

	public static final int MILLISECONDS_IN_SECOND = 1000;
}

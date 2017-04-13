package com.inyourface.singbetter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inyourface.singbetter.R;
import com.inyourface.singbetter.Session;

/**
 * Created by Justin on 4/6/2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder
{
	private TextView sessionCustomNameTextView;
	private TextView sessionNoteTextView;
	private TextView sessionDateCreatedTextView;

	// TEMPORARY ITEMS

	private TextView sessionIDTextView;
	private TextView sessionIntervalTextView;
	private TextView sessionAssociatedMP3TextView;

	private TextView sessionDataTextView;

	public RecyclerViewHolder(View itemView)
	{
		super(itemView);

		sessionCustomNameTextView = (TextView) itemView.findViewById(R.id.recycler_item_custom_name);
		sessionNoteTextView = (TextView) itemView.findViewById(R.id.recycler_item_note);
		sessionDateCreatedTextView = (TextView) itemView.findViewById(R.id.recycler_item_date_created);

		// TEMPORARY ITEMS

		sessionIDTextView = (TextView) itemView.findViewById(R.id.recycler_item_id);
		sessionIntervalTextView = (TextView) itemView.findViewById(R.id.recycler_item_interval);
		sessionAssociatedMP3TextView = (TextView) itemView.findViewById(R.id.recycler_item_associated_mp3);

		sessionDataTextView = (TextView) itemView.findViewById(R.id.recycler_item_data);
	}

	public void bindView(Session session)
	{
		sessionCustomNameTextView.setText(session.getCustomName());
		sessionNoteTextView.setText(session.getNote());
		sessionDateCreatedTextView.setText(session.getDateCreated()); // TODO: This could use some formatting to look nicer.

		// TEMPORARY ITEMS

		sessionIDTextView.setText(Long.toString(session.getID()));
		sessionIntervalTextView.setText(Integer.toString(session.getInterval()));
		sessionAssociatedMP3TextView.setText(session.getAssociatedMP3());

		sessionDataTextView.setText(session.getData());
	}
}

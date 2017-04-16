package com.inyourface.singbetter.RecordedSessions.RecyclerView;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inyourface.singbetter.R;
import com.inyourface.singbetter.RecordedSessions.newview.ItemActivity;
import com.inyourface.singbetter.Session;

/**
 * Created by Justin on 4/6/2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
	private TextView sessionCustomNameTextView;
	private TextView sessionNoteTextView;
	private TextView sessionDateCreatedTextView;

	public RecyclerViewHolder(View itemView)
	{
		super(itemView);

		itemView.setOnClickListener(this);

		sessionCustomNameTextView = (TextView) itemView.findViewById(R.id.recycler_item_custom_name);
		sessionNoteTextView = (TextView) itemView.findViewById(R.id.recycler_item_note);
		sessionDateCreatedTextView = (TextView) itemView.findViewById(R.id.recycler_item_date_created);
	}

	public void bindView(Session session)
	{
		sessionCustomNameTextView.setText(session.getCustomName());
		sessionNoteTextView.setText(session.getNote());
		sessionDateCreatedTextView.setText(session.getDateCreated()); // TODO: This could use some formatting to look nicer.
	}

	@Override
	public void onClick(View v)
	{
		Intent intent = new Intent(SessionsViewActivity.act, ItemActivity.class);
		int pos = getAdapterPosition();
		intent.putExtra("clickPosition", pos);
		SessionsViewActivity.act.startActivity(intent);
	}
}

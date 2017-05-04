package com.inyourface.singbetter.RecordedSessions.RecyclerView;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inyourface.singbetter.R;
import com.inyourface.singbetter.RecordedSessions.ItemView.ItemActivity;
import com.inyourface.singbetter.Objects.Session;
import com.inyourface.singbetter.RecordedSessions.SessionsViewActivity;
import com.inyourface.singbetter.Util;

/**
 * Created by Justin on 4/6/2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
	public TextView sessionCustomNameTextView;
	public TextView sessionDateCreatedTextView;

	public RecyclerViewHolder(View itemView)
	{
		super(itemView);

		itemView.setOnClickListener(this);

		sessionCustomNameTextView = (TextView) itemView.findViewById(R.id.recycler_item_custom_name);
		sessionDateCreatedTextView = (TextView) itemView.findViewById(R.id.recycler_item_date_created);
	}

	public void bindView(Session session)
	{
		sessionCustomNameTextView.setText(session.getCustomName());
		sessionDateCreatedTextView.setText(Util.convertEpochToReadable(session.getDateCreated())); // TODO: This could use some formatting to look nicer.
	}

	@Override
	public void onClick(View v)
	{
		// We get a value of -1 if a click is received in empty space (possible during spam click of item deletion)
		if(getAdapterPosition() == -1)
		{
			return;
		}

		if(SessionsViewActivity.act.getDeleteMode())
		{
			SessionsViewActivity.act.insertToBeRemoved(getAdapterPosition());
		}
		else
		{
			Intent intent = new Intent(SessionsViewActivity.act, ItemActivity.class);
			int pos = getAdapterPosition();
			intent.putExtra("clickPosition", pos);
			SessionsViewActivity.act.startActivity(intent);
		}
	}
}

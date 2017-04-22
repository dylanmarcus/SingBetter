package com.inyourface.singbetter.RecordedSessions.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.inyourface.singbetter.R;
import com.inyourface.singbetter.RecordedSessions.ItemView.ItemActivity;
import com.inyourface.singbetter.Objects.Session;
import com.inyourface.singbetter.RecordedSessions.SessionsViewActivity;
import com.inyourface.singbetter.Util;

/**
 * Created by Justin on 4/6/2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
{
	public TextView sessionCustomNameTextView;
	public TextView sessionDateCreatedTextView;
	private boolean selected;

	public RecyclerViewHolder(View itemView)
	{
		super(itemView);

		selected = false;

		itemView.setOnClickListener(this);
		itemView.setOnLongClickListener(this);

		sessionCustomNameTextView = (TextView) itemView.findViewById(R.id.recycler_item_custom_name);
		sessionDateCreatedTextView = (TextView) itemView.findViewById(R.id.recycler_item_date_created);
	}

	public void bindView(Session session)
	{
		sessionCustomNameTextView.setText(session.getCustomName());
		sessionDateCreatedTextView.setText(Util.convertEpochToReadable(session.getDateCreated())); // TODO: This could use some formatting to look nicer.
	}

	@Override
	public boolean onLongClick(View v)
	{
		if(!SessionsViewActivity.act.getDeleteMode())
		{
			SessionsViewActivity.act.setDeleteMode(true);
		}
		return false;
	}

	@Override
	public void onClick(View v)
	{
		if(SessionsViewActivity.act.getDeleteMode())
		{
			if(selected)
			{
				SessionsViewActivity.act.selectedItems.remove(this);
				sessionCustomNameTextView.setBackgroundColor(Color.TRANSPARENT);
				sessionDateCreatedTextView.setBackgroundColor(Color.TRANSPARENT);
				selected = false;
			}
			else
			{
				sessionCustomNameTextView.setBackgroundColor(Color.parseColor("#1f000000"));
				sessionDateCreatedTextView.setBackgroundColor(Color.parseColor("#1f000000"));
				SessionsViewActivity.act.selectedItems.add(this);
				selected = true;
			}
		}
		else
		{
			Intent intent = new Intent(SessionsViewActivity.act, ItemActivity.class);
			int pos = getAdapterPosition();
			intent.putExtra("clickPosition", pos);
			SessionsViewActivity.act.startActivity(intent);
		}
	}

	public void setSelected(boolean value)
	{
		selected = value;
	}
}

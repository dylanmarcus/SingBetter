package com.inyourface.singbetter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inyourface.singbetter.R;
import com.inyourface.singbetter.Session;
import com.inyourface.singbetter.viewholder.RecyclerViewHolder;

import java.util.ArrayList;

/**
 * Created by Justin on 4/6/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>
{
	private ArrayList<Session> bindableCollection;

	public RecyclerViewAdapter(ArrayList<Session> collection)
	{
		this.bindableCollection = collection;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sessions_view_item, parent, false);

		return new RecyclerViewHolder(inflatedView);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position)
	{
		Session session = bindableCollection.get(position);
		holder.bindView(session);
	}

	@Override
	public int getItemCount()
	{
		return this.bindableCollection.size();
	}
}

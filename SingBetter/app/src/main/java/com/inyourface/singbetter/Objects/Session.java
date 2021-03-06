package com.inyourface.singbetter.Objects;

import java.util.ArrayList;

/**
 * Created by Justin on 4/4/2017.
 *
 */

public class Session
{
	private long id;
	private Note note;
	private String customName;
	private long dateCreated;
	private ArrayList<Integer> data;

	public Session()
	{

	}

	public ArrayList<Integer> getData()
	{
		return data;
	}

	public void setData(ArrayList<Integer> data)
	{
		this.data = data;
	}

	public long getID()
	{
		return id;
	}

	public void setID(long id)
	{
		this.id = id;
	}

	public Note getNote()
	{
		return note;
	}

	public void setNote(Note note)
	{
		this.note = note;
	}

	public String getCustomName()
	{
		return customName;
	}

	public void setCustomName(String customName)
	{
		this.customName = customName;
	}

	public long getDateCreated()
	{
		return dateCreated;
	}

	public void setDateCreated(long dateCreated)
	{
		this.dateCreated = dateCreated;
	}
}

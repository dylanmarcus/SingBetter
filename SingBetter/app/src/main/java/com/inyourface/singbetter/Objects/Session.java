package com.inyourface.singbetter.Objects;

import java.io.Serializable;

/**
 * Created by Justin on 4/4/2017.
 *
 */

@SuppressWarnings("serial")
public class Session implements Serializable
{
	private long id;
	private String note;
	private String customName;
	private long dateCreated;
	private String associatedMP3;
	private String data;


	public String getData()
	{
		return data;
	}

	public void setData(String data)
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

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
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

	public String getAssociatedMP3()
	{
		return associatedMP3;
	}

	public void setAssociatedMP3(String associatedMP3)
	{
		this.associatedMP3 = associatedMP3;
	}
}

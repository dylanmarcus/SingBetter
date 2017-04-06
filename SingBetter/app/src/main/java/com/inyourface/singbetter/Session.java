package com.inyourface.singbetter;

/**
 * Created by Justin on 4/4/2017.
 *
 */

public class Session
{
	private long id;
	private String note;
	private int interval;
	private String customName;
	private String dateCreated;
	private String associatedMP3;

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

	public int getInterval()
	{
		return interval;
	}

	public void setInterval(int interval)
	{
		this.interval = interval;
	}

	public String getCustomName()
	{
		return customName;
	}

	public void setCustomName(String customName)
	{
		this.customName = customName;
	}

	public String getDateCreated()
	{
		return dateCreated;
	}

	public void setDateCreated(String dateCreated)
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

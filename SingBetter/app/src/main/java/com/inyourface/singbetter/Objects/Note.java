package com.inyourface.singbetter.Objects;

/**
 * Created by Justin on 4/17/2017.
 */

public enum Note
{
	A ("A"),
	A_SHARP ("A#"),
	B ("B"),
	B_SHARP ("B#"),
	C ("C"),
	C_SHARP ("C#"),
	D ("D"),
	D_SHARP ("D#"),
	E ("E"),
	E_SHARP ("E#"),
	F ("F"),
	F_SHARP ("F#");

	private String note;

	private Note(String value)
	{
		this.note = value;
	}

	public String getNoteString()
	{
		return this.note;
	}
}

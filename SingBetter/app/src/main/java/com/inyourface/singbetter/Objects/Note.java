package com.inyourface.singbetter.Objects;

/**
 * Created by Justin on 4/17/2017.
 *
 * I have no idea what the range should actually be.
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
		note = value;
	}

	public String getNote()
	{
		return note;
	}

	public static Note noteToLeftOf(Note note)
	{
		Note[] notes = Note.values();
		for(int i = 0; i < notes.length; i++)
		{
			if(notes[i] == note)
			{
				if(i == 0)
				{
					break;
				}
				else
				{
					return notes[i-1];
				}
			}
		}

		return null;
	}

	public static Note noteToRightOf(Note note)
	{
		Note[] notes = Note.values();
		for(int i = 0; i < notes.length; i++)
		{
			if(notes[i] == note)
			{
				if(i == (notes.length - 1))
				{
					break;
				}
				else
				{
					return notes[i+1];
				}
			}
		}

		return null;
	}
}

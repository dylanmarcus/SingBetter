package com.inyourface.singbetter;

import com.inyourface.singbetter.Objects.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Justin on 4/13/2017.
 *
 * A collection of functions to assist with
 * application functionality.
 */

public class Util
{
	public static long getCurrentTimeInMilliseconds()
	{
		Calendar c = Calendar.getInstance();
		return c.getTimeInMillis();
	}

	public static String convertEpochToReadable(long time)
	{
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		return sdf.format(date);
	}

	public static ArrayList<Integer> convertStringToIntArr(String input)
	{
		String[] inputStrings = input.split(",");
		ArrayList<Integer> resultInts = new ArrayList<Integer>();

		for(int i = 0; i < inputStrings.length; i++)
		{
			resultInts.add(Integer.parseInt(inputStrings[i]));
		}

		return resultInts;
	}

	public static String convertIntArrToString(ArrayList<Integer> input)
	{
		String result = "";

		for(int i = 0; i < input.size() - 1; i++)
		{
			result += input.get(i) + ",";
		}

		result += input.get(input.size() - 1);

		return result;
	}

	public static Note stringToNote(String value)
	{
		Note[] notes = Note.values();
		for(int i = 0; i < notes.length; i++)
		{
			if(notes[i].getNoteString().equals(value))
			{
				return notes[i];
			}
		}

		return null;
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

	public static String[] getAllNoteStrings()
	{
		Note[] notes = Note.values();
		String[] stringNotes = new String[notes.length];
		for(int i = 0; i < notes.length; i++)
		{
			stringNotes[i] = notes[i].getNoteString();
		}

		return stringNotes;
	}

	public static String[] getAdjacentNotes(Note note)
	{
		String[] notes = new String[3];

		Note note1 = noteToLeftOf(note);
		Note note2 = note;
		Note note3 = noteToRightOf(note);

		if(note1 == null)
		{
			notes[0] = "";
		}
		else
		{
			notes[0] = note1.getNoteString();
		}

		if(note3 == null)
		{
			notes[2] = "";
		}
		else
		{
			notes[2] = note3.getNoteString();
		}

		notes[1] = note2.getNoteString();

		return notes;
	}
}

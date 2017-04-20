package com.inyourface.singbetter;

import com.inyourface.singbetter.Objects.Note;
import com.inyourface.singbetter.Objects.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Justin on 4/13/2017.
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


	/**
		TODO: Stuff below to be deleted before final product is done.
	 */


	private static String[] songNames =
			{
				"MySong",
				"CoolSong",
				"BadSong",
				"Song18",
				"SongSong",
				"FriendshipSong",
				"SongSongSong",
				"Moo",
				"Name"
			};
	private static String[] customNames =
			{
				"CustomName1",
				"CustomName2",
				"CustomName3",
				"CustomName4",
				"CustomName5",
				"CustomName6",
				"CustomName7",
				"CustomName8",
				"CustomName9"
			};

	public static Session generateSession()
	{
		Session newSession = new Session();

		newSession.setDateCreated(getCurrentTimeInMilliseconds());
		newSession.setNote(generateNote());
		String songName = generateSongName();
		newSession.setAssociatedMP3("/bin/" + songName + ".mp3");
		newSession.setCustomName(generateCustomName());
		newSession.setData(convertStringToIntArr(convertIntArrToString(convertStringToIntArr("10,9,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-10"))));

		return newSession;
	}

	public static String generateSongName()
	{
		Random ran = new Random();
		int x = ran.nextInt(songNames.length);

		return songNames[x];
	}

	public static Note generateNote()
	{
		Random ran = new Random();
		Note[] notes = Note.values();
		int x = ran.nextInt(notes.length);

		return notes[x];
	}

	public static String generateCustomName()
	{
		Random ran = new Random();
		int x = ran.nextInt(customNames.length);

		return customNames[x];
	}
}

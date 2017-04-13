package com.inyourface.singbetter;

import java.util.Random;

/**
 * Created by Justin on 4/13/2017.
 */

public class Util
{
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
	private static String[] notes =
			{
					"C",
					"C#",
					"D",
					"D#",
					"E",
					"E#",
					"F",
					"F#"
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

		newSession.setDateCreated("now");
		newSession.setInterval(generateInterval());
		newSession.setNote(generateNote());
		String songName = generateSongName();
		newSession.setAssociatedMP3("/bin/" + songName + ".mp3");
		newSession.setCustomName(generateCustomName());
		newSession.setData("10,9,8,7,6,5,4,3,2,1,0,-1,-2,-3,-4,-5,-6,-7,-8,-9,-10");

		return newSession;
	}

	public static String generateSongName()
	{
		Random ran = new Random();
		int x = ran.nextInt(songNames.length);

		return songNames[x];
	}

	public static String generateNote()
	{
		Random ran = new Random();
		int x = ran.nextInt(notes.length);

		return notes[x];
	}

	public static String generateCustomName()
	{
		Random ran = new Random();
		int x = ran.nextInt(customNames.length);

		return customNames[x];
	}

	public static int generateInterval()
	{
		Random ran = new Random();
		return ran.nextInt(200) + 1;
	}
}

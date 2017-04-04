package com.inyourface.singbetter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Justin on 4/3/2017.
 *
 * This class handles the creation of the Session database as well as upgrading the database
 * to a newer version.
 * Methods in this class should not be used outside of the helpers files that serve as the
 * Data Access Layer.
 */

public class SessionOpenHelper extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "sessions.db";

	public static final String TABLE_SESSIONS = "sessions";
	public static final String COLUMN_ID = "ID";
	public static final String COLUMN_NOTE = "Note";
	public static final String COLUMN_INTERVAL = "Interval";
	public static final String COLUMN_DATECREATED = "DateCreated";
	public static final String COLUMN_CUSTOMNAME = "CustomName";
	public static final String COLUMN_ASSOCIATEDMP3 = "AssociatedMP3";
	private static final String SESSION_TABLE_CREATE = "CREATE TABLE " + TABLE_SESSIONS + " (" +
			"ID INT PRIMARY KEY, " +
			"Note String NOT NULL, " +
			"Interval INT NOT NULL, " +
			"DateCreated TEXT DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
			"CustomName String, " +
			"AssociatedMP3 String" +
			");";

	public SessionOpenHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(SESSION_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS); // TODO: This is bad. change/remove this
		onCreate(db);
	}
}

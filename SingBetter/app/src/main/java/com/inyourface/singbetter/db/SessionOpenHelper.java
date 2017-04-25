package com.inyourface.singbetter.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Justin on 4/3/2017.
 *
 * This class handles the creation of the Session database as well as upgrading the database
 * to a newer version.
 * Methods in this class should not be used outside of the helpers file that serve as the
 * Data Access Layer (db/SessionDAL).
 */

public class SessionOpenHelper extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1; // Do not update the database version without changing the onUpgrade method.
	private static final String DATABASE_NAME = "SingBetter.db";

	protected static final String TABLE_SESSIONS = "Recorded_Sessions";
	protected static final String COLUMN_ID = "ID";
	protected static final String COLUMN_NOTE = "Note";
	protected static final String COLUMN_DATECREATED = "Date_Created";
	protected static final String COLUMN_CUSTOMNAME = "Custom_Name";
	protected static final String COLUMN_ASSOCIATEDMP3 = "Associated_MP3";
	protected static final String COLUMN_DATA = "Data";
	private static final String SESSION_TABLE_CREATE = "CREATE TABLE " + TABLE_SESSIONS + " (" +
			COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			COLUMN_NOTE + " TEXT NOT NULL, " +
			COLUMN_DATECREATED + " INTEGER NOT NULL, " +
			COLUMN_CUSTOMNAME + " TEXT NOT NULL, " +
			COLUMN_ASSOCIATEDMP3 + " TEXT, " +
			COLUMN_DATA + " TEXT NOT NULL" +
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
		// Do something useful at a later date
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
		onCreate(db);
	}
}

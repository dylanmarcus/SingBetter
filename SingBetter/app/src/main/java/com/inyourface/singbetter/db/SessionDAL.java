package com.inyourface.singbetter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inyourface.singbetter.Objects.Note;
import com.inyourface.singbetter.Objects.Session;
import com.inyourface.singbetter.Util;

import java.util.ArrayList;

/**
 * Created by Justin on 4/4/2017.
 *
 * This class is used as the "Data Access Layer" for the SingBetter table in the database.
 * Methods in this class are what should be used to retrieve and update data in the database.
 */

public class SessionDAL
{
	private SQLiteDatabase db;
	private SessionOpenHelper dbHelper;
	private String[] sessionColumns =
			{
					SessionOpenHelper.COLUMN_ID,
					SessionOpenHelper.COLUMN_NOTE,
					SessionOpenHelper.COLUMN_CUSTOMNAME,
					SessionOpenHelper.COLUMN_DATECREATED,
					SessionOpenHelper.COLUMN_ASSOCIATEDMP3,
					SessionOpenHelper.COLUMN_DATA
			};

	public SessionDAL(Context context)
	{
		dbHelper = new SessionOpenHelper(context);
		// Uncomment this line to clear the database on app start
		//dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 0, 1);
	}

	public void open()
	{
		db = dbHelper.getWritableDatabase();
	}

	public void close()
	{
		dbHelper.close();
	}

	public Session insertSession(Session session)
	{
		// Insert the values into the database
		ContentValues values = new ContentValues();
		values.put(SessionOpenHelper.COLUMN_NOTE, session.getNote().getNoteString());
		values.put(SessionOpenHelper.COLUMN_CUSTOMNAME, session.getCustomName());
		values.put(SessionOpenHelper.COLUMN_DATECREATED, session.getDateCreated());
		values.put(SessionOpenHelper.COLUMN_ASSOCIATEDMP3, session.getAssociatedMP3());
		values.put(SessionOpenHelper.COLUMN_DATA, Util.convertIntArrToString(session.getData()));

		long insertID = db.insert(SessionOpenHelper.TABLE_SESSIONS, null, values);

		// Query the db for the item we just inserted so we can return a new object which will now have an id set
		Cursor cursor = db.query(SessionOpenHelper.TABLE_SESSIONS, sessionColumns, SessionOpenHelper.COLUMN_ID + "=" + insertID, null, null, null, null);
		cursor.moveToFirst();
		Session newSession = cursorToSession(cursor);
		cursor.close();
		return newSession;
	}

	public Session updateSession(Session session)
	{
		// Update an existing session
		ContentValues values = new ContentValues();
		values.put(SessionOpenHelper.COLUMN_NOTE, session.getNote().getNoteString());
		values.put(SessionOpenHelper.COLUMN_CUSTOMNAME, session.getCustomName());
		values.put(SessionOpenHelper.COLUMN_DATECREATED, session.getDateCreated());
		values.put(SessionOpenHelper.COLUMN_ASSOCIATEDMP3, session.getAssociatedMP3());
		values.put(SessionOpenHelper.COLUMN_DATA, Util.convertIntArrToString(session.getData()));

		String where = SessionOpenHelper.COLUMN_ID + "=?";
		String[] whereArgs = new String[]
				{
					Long.toString(session.getID())
				};

		long updateID = db.update(SessionOpenHelper.TABLE_SESSIONS, values, where, whereArgs);

		// Query the db for the updated item so we can return a new object with the updated information
		Cursor cursor = db.query(SessionOpenHelper.TABLE_SESSIONS, sessionColumns, SessionOpenHelper.COLUMN_ID + "=" + updateID, null, null, null, null);
		cursor.moveToFirst();
		Session newSession = cursorToSession(cursor);
		cursor.close();
		return newSession;
	}

	public void deleteSession(Session session)
	{
		db.delete(SessionOpenHelper.TABLE_SESSIONS, SessionOpenHelper.COLUMN_ID + "=" + session.getID(), null);
	}

	public ArrayList<Session> getSessionsWithNote(Note note)
	{
		ArrayList<Session> sessions = new ArrayList<Session>();

		Cursor cursor = db.query(SessionOpenHelper.TABLE_SESSIONS, sessionColumns, SessionOpenHelper.COLUMN_NOTE + "=\"" + note.getNoteString() + "\"", null, null, null, null);
		cursor.moveToFirst();

		while(!cursor.isAfterLast())
		{
			Session session = cursorToSession(cursor);
			sessions.add(session);
			cursor.moveToNext();
		}

		cursor.close();

		return sessions;
	}

	private Session cursorToSession(Cursor cursor)
	{
		Session session = new Session();
		session.setID(cursor.getLong(0));
		// Convert the string to enum
		session.setNote(Util.stringToNote(cursor.getString(1)));
		session.setCustomName(cursor.getString(2));
		session.setDateCreated(cursor.getLong(3));
		session.setAssociatedMP3(cursor.getString(4));
		// Convert the String to ArrayList<Integer>
		session.setData(Util.convertStringToIntArr(cursor.getString(5)));
		return session;
	}
}

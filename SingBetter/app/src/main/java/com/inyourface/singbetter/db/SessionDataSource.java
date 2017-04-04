package com.inyourface.singbetter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 4/4/2017.
 *
 * This class is used as the "Data Access Layer" for the Session table in the database.
 * Methods in this class are what should be used to retrieve and
 * update data in the Session table of the database.
 */

public class SessionDataSource
{
	private SQLiteDatabase db;
	private SessionOpenHelper dbHelper;
	private String[] allColumns =
			{
					SessionOpenHelper.COLUMN_ID,
					SessionOpenHelper.COLUMN_NOTE,
					SessionOpenHelper.COLUMN_INTERVAL,
					SessionOpenHelper.COLUMN_CUSTOMNAME,
					SessionOpenHelper.COLUMN_DATECREATED,
					SessionOpenHelper.COLUMN_ASSOCIATEDMP3
			};

	public SessionDataSource(Context context)
	{
		dbHelper = new SessionOpenHelper(context);
	}

	public void open()
	{
		try
		{
			db = dbHelper.getWritableDatabase();
		}
		catch(Exception e)
		{
			String error = e.getMessage();
		}
	}

	public void close()
	{
		dbHelper.close();
	}

	public Session createSession(String note, int interval, String customName, String dateCreated, String associatedMP3)
	{
		ContentValues values = new ContentValues();
		values.put(SessionOpenHelper.COLUMN_NOTE, note);
		values.put(SessionOpenHelper.COLUMN_INTERVAL, interval);
		values.put(SessionOpenHelper.COLUMN_CUSTOMNAME, customName);
		values.put(SessionOpenHelper.COLUMN_DATECREATED, dateCreated);
		values.put(SessionOpenHelper.COLUMN_ASSOCIATEDMP3, associatedMP3);

		long insertID = db.insert(SessionOpenHelper.TABLE_SESSIONS, null, values);

		Cursor cursor = db.query(SessionOpenHelper.TABLE_SESSIONS, allColumns, SessionOpenHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
		cursor.moveToFirst();
		Session newSession = cursorToSession(cursor);
		cursor.close();
		return newSession;
	}

	public void deleteSession(Session session)
	{
		long id = session.getId();
		db.delete(SessionOpenHelper.TABLE_SESSIONS, SessionOpenHelper.COLUMN_ID + " = " + id, null);
	}

	public List<Session> getAllSessions()
	{
		List<Session> sessions = new ArrayList<Session>();

		Cursor cursor = db.query(SessionOpenHelper.TABLE_SESSIONS, allColumns, null, null, null, null, null);
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
		session.setId(cursor.getLong(0));
		session.setNote(cursor.getString(1));
		session.setInterval(cursor.getInt(2));
		session.setCustomName(cursor.getString(3));
		session.setDateCreated(cursor.getString(4));
		session.setAssociatedMP3(cursor.getString(5));
		return session;
	}
}

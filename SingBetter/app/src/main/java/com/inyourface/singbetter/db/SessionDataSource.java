package com.inyourface.singbetter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.inyourface.singbetter.Objects.Session;

import java.util.ArrayList;

/**
 * Created by Justin on 4/4/2017.
 *
 * This class is used as the "Data Access Layer" for the Session table in the database.
 * Methods in this class are what should be used to retrieve and
 * update data in the Session table of the database.
 *
 * TODO: Clean up this file
 */

public class SessionDataSource
{
	private SQLiteDatabase db;
	private SessionOpenHelper dbHelper;
	private String[] sessionColumns =
			{
					SessionOpenHelper.COLUMN_ID,
					SessionOpenHelper.COLUMN_NOTE,
					SessionOpenHelper.COLUMN_INTERVAL,
					SessionOpenHelper.COLUMN_CUSTOMNAME,
					SessionOpenHelper.COLUMN_DATECREATED,
					SessionOpenHelper.COLUMN_ASSOCIATEDMP3,
					SessionOpenHelper.COLUMN_DATA
			};

	public SessionDataSource(Context context)
	{
		dbHelper = new SessionOpenHelper(context);
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
		ContentValues values = new ContentValues();
		values.put(SessionOpenHelper.COLUMN_NOTE, session.getNote());
		values.put(SessionOpenHelper.COLUMN_INTERVAL, session.getInterval());
		values.put(SessionOpenHelper.COLUMN_CUSTOMNAME, session.getCustomName());
		values.put(SessionOpenHelper.COLUMN_DATECREATED, session.getDateCreated());
		values.put(SessionOpenHelper.COLUMN_ASSOCIATEDMP3, session.getAssociatedMP3());
		values.put(SessionOpenHelper.COLUMN_DATA, session.getData());

		long insertID = db.insert(SessionOpenHelper.TABLE_SESSIONS, null, values);

		Cursor cursor = db.query(SessionOpenHelper.TABLE_SESSIONS, sessionColumns, SessionOpenHelper.COLUMN_ID + "=" + insertID, null, null, null, null);
		cursor.moveToFirst();
		Session newSession = cursorToSession(cursor);
		cursor.close();
		return newSession;
	}

	public Session updateSession(Session session)
	{
		ContentValues values = new ContentValues();
		values.put(SessionOpenHelper.COLUMN_NOTE, session.getNote());
		values.put(SessionOpenHelper.COLUMN_INTERVAL, session.getInterval());
		values.put(SessionOpenHelper.COLUMN_CUSTOMNAME, session.getCustomName());
		values.put(SessionOpenHelper.COLUMN_DATECREATED, session.getDateCreated());
		values.put(SessionOpenHelper.COLUMN_ASSOCIATEDMP3, session.getAssociatedMP3());
		values.put(SessionOpenHelper.COLUMN_DATA, session.getData());

		String where = SessionOpenHelper.COLUMN_ID + "=?";
		String[] whereArgs = new String[]
				{
					Long.toString(session.getID())
				};

		long updateID = db.update(SessionOpenHelper.TABLE_SESSIONS, values, where, whereArgs);

		Cursor cursor = db.query(SessionOpenHelper.TABLE_SESSIONS, sessionColumns, SessionOpenHelper.COLUMN_ID + "=" + updateID, null, null, null, null);
		cursor.moveToFirst();
		Session newSession = cursorToSession(cursor);
		cursor.close();
		return newSession;
	}

	public void deleteSession(Session session)
	{
		long id = session.getID();
		db.delete(SessionOpenHelper.TABLE_SESSIONS, SessionOpenHelper.COLUMN_ID + "=" + id, null);
	}

	public ArrayList<Session> getSessionsWithNote(String note)
	{
		ArrayList<Session> sessions = new ArrayList<Session>();

		Cursor cursor = db.query(SessionOpenHelper.TABLE_SESSIONS, sessionColumns, SessionOpenHelper.COLUMN_NOTE + "=\"" + note + "\"", null, null, null, null);
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

	public ArrayList<Session> getAllSessions()
	{
		ArrayList<Session> sessions = new ArrayList<Session>();

		Cursor cursor = db.query(SessionOpenHelper.TABLE_SESSIONS, sessionColumns, null, null, null, null, null);
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
		session.setID(cursor.getInt(0));
		session.setNote(cursor.getString(1));
		session.setInterval(cursor.getInt(2));
		session.setCustomName(cursor.getString(3));
		session.setDateCreated(cursor.getString(4));
		session.setAssociatedMP3(cursor.getString(5));
		session.setData(cursor.getString(6));
		return session;
	}
}

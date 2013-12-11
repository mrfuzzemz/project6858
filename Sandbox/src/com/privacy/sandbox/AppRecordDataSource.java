package com.privacy.sandbox;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AppRecordDataSource {

	// Database fields
	private SQLiteDatabase database;
	private AppRecordOpenHelper dbHelper;
	private String[] allColumns = { AppRecordOpenHelper.ID,
			AppRecordOpenHelper.APP_NAME, AppRecordOpenHelper.BROADCAST_LABEL };

	public AppRecordDataSource(Context context) {
		dbHelper = new AppRecordOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public AppRecord createAppRecordIfNotExists(String appName, String broadcastLabel) {
		String[] where = {appName};

		Cursor c = database.query(AppRecordOpenHelper.TABLE_NAME,
				allColumns, AppRecordOpenHelper.APP_NAME + " = ?", where,
				null, null, null);

		AppRecord ap;

		if (!c.moveToFirst()){
			// check to make sure permission isn't already in use, if it is
			// then don't save anything to DB, return null
			where = new String[] {broadcastLabel};
			Cursor c2 = database.query(AppRecordOpenHelper.TABLE_NAME,
					allColumns, AppRecordOpenHelper.BROADCAST_LABEL + " = ?", where,
					null, null, null);

			if (c2.moveToFirst()){
				ap = null;
			} else{
				ContentValues values = new ContentValues();
				values.put(AppRecordOpenHelper.APP_NAME, appName);
				values.put(AppRecordOpenHelper.BROADCAST_LABEL, broadcastLabel);

				long insertId = database.insert(AppRecordOpenHelper.TABLE_NAME, null,
						values);
				Cursor cursor = database.query(AppRecordOpenHelper.TABLE_NAME,
						allColumns, AppRecordOpenHelper.ID + " = " + insertId, null,
						null, null, null);
				cursor.moveToFirst();
				ap = cursorToAppRecord(cursor);
				cursor.close();
			}
		} else {
			ap = cursorToAppRecord(c);
		}
		c.close();

		return ap;
	}

	public void deleteAppRecord(AppRecord ap) {
		long id = ap.getId();
		System.out.println("App record deleted with id: " + id);
		database.delete(AppRecordOpenHelper.TABLE_NAME, AppRecordOpenHelper.ID
				+ " = " + id, null);
	}

	public List<String> getAllAppRecords() {
		List<String> appRecords = new ArrayList<String>();

		Cursor cursor = database.query(AppRecordOpenHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			appRecords.add(cursorToAppRecord(cursor).getName());
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return appRecords;
	}

	public String getBroadcastLabel(String appName){
		String[] where = {appName};

		Cursor c = database.query(AppRecordOpenHelper.TABLE_NAME,
				allColumns, AppRecordOpenHelper.APP_NAME + " = ?", where,
				null, null, null);

		String broadcastLabel = "";
		if (c.moveToFirst()){
			broadcastLabel = cursorToAppRecord(c).getBroadcastLabel();
		}
		c.close();

		return broadcastLabel;
	}

	private AppRecord cursorToAppRecord(Cursor cursor) {
		AppRecord ap = new AppRecord();
		ap.setId(cursor.getLong(0));
		ap.setName(cursor.getString(1));
		ap.setBroadcastLabel(cursor.getString(2));

		return ap;
	}


}

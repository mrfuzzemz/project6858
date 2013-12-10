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
	private PermissionsOpenHelper dbHelper;
	private String[] allColumns = { AppRecordOpenHelper.ID,
			AppRecordOpenHelper.APP_NAME };

	public AppRecordDataSource(Context context) {
		dbHelper = new PermissionsOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public AppRecord createAppRecord(String appName) {
		ContentValues values = new ContentValues();
		values.put(AppRecordOpenHelper.APP_NAME, appName);
		long insertId = database.insert(AppRecordOpenHelper.TABLE_NAME, null,
				values);
		Cursor cursor = database.query(AppRecordOpenHelper.TABLE_NAME,
				allColumns, PermissionsOpenHelper.ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		AppRecord newAP = cursorToAppRecord(cursor);
		cursor.close();
		return newAP;
	}
	
	public void deleteAppRecord(AppRecord ap) {
		long id = ap.getId();
		System.out.println("App record deleted with id: " + id);
		database.delete(AppRecordOpenHelper.TABLE_NAME, AppRecordOpenHelper.ID
				+ " = " + id, null);
	}

	public List<AppRecord> getAllAppRecords() {
		List<AppRecord> appRecords = new ArrayList<AppRecord>();

		Cursor cursor = database.query(AppRecordOpenHelper.TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			AppRecord ap = cursorToAppRecord(cursor);
			appRecords.add(ap);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return appRecords;
	}

	private AppRecord cursorToAppRecord(Cursor cursor) {
		AppRecord ap = new AppRecord();
		ap.setId(cursor.getLong(0));
		ap.setName(cursor.getString(1));
		
		return ap;
	}

	
}

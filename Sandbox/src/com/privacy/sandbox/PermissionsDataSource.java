package com.privacy.sandbox;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PermissionsDataSource {

	// Database fields
	private SQLiteDatabase database;
	private PermissionsOpenHelper dbHelper;
	private String[] allColumns = { PermissionsOpenHelper.COLUMN_ID,
			PermissionsOpenHelper.COLUMN_NAME, PermissionsOpenHelper.COLUMN_PROFILE };

	public PermissionsDataSource(Context context) {
		dbHelper = new PermissionsOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Permission createPermission(String permissionName, String permissionProfileValue) {
		ContentValues values = new ContentValues();
		values.put(PermissionsOpenHelper.COLUMN_NAME, permissionName);
		values.put(PermissionsOpenHelper.COLUMN_PROFILE, permissionProfileValue);
		long insertId = database.insert(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME, null,
				values);
		Cursor cursor = database.query(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME,
				allColumns, PermissionsOpenHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Permission newPermission = cursorToPermission(cursor);
		cursor.close();
		return newPermission;
	}

	public void deletePermission(Permission Permission) {
		long id = Permission.getId();
		System.out.println("Permission deleted with id: " + id);
		database.delete(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME, PermissionsOpenHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Permission> getAllPermissions() {
		List<Permission> Permissions = new ArrayList<Permission>();

		Cursor cursor = database.query(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Permission Permission = cursorToPermission(cursor);
			Permissions.add(Permission);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return Permissions;
	}

	private Permission cursorToPermission(Cursor cursor) {
		Permission Permission = new Permission();
		Permission.setId(cursor.getLong(0));
		Permission.setName(cursor.getString(1));
		Permission.setRead_profile(cursor.getString(2));
		return Permission;
	}
}

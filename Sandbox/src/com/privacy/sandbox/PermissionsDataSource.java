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
	private String[] allColumns = { PermissionsOpenHelper.ID,
			PermissionsOpenHelper.APP_NAME, PermissionsOpenHelper.PERM_NAME, PermissionsOpenHelper.PERM_VALUE};

	public PermissionsDataSource(Context context) {
		dbHelper = new PermissionsOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Permission createPermission(String appName, String permName, String permValue) {
		ContentValues values = new ContentValues();
		values.put(PermissionsOpenHelper.APP_NAME, appName);
		values.put(PermissionsOpenHelper.PERM_NAME, permName);
		values.put(PermissionsOpenHelper.PERM_VALUE, permValue);
		long insertId = database.insert(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME, null,
				values);
		Cursor cursor = database.query(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME,
				allColumns, PermissionsOpenHelper.ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Permission newPermission = cursorToPermission(cursor);
		cursor.close();
		return newPermission;
	}
	
	public void editPermission(String appName, String permName, String permValue){
		ContentValues values = new ContentValues();
		values.put(PermissionsOpenHelper.APP_NAME, appName);
		values.put(PermissionsOpenHelper.PERM_NAME, permName);
		values.put(PermissionsOpenHelper.PERM_VALUE, permValue);
		
		String[] where = {appName, permName};
		
		database.update(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME, values, PermissionsOpenHelper.APP_NAME 
				+ " = ? and " + PermissionsOpenHelper.PERM_NAME + " = ?", where);
	}
	
	public Permission createOrUpdatePermission(String appName, String permName, String permValue){
		ContentValues values = new ContentValues();
		values.put(PermissionsOpenHelper.APP_NAME, appName);
		values.put(PermissionsOpenHelper.PERM_NAME, permName);
		values.put(PermissionsOpenHelper.PERM_VALUE, permValue);
		
		String[] where = {appName, permName};
		Cursor c = database.query(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME, allColumns, PermissionsOpenHelper.APP_NAME 
				+ " = ? and " + PermissionsOpenHelper.PERM_NAME + " = ?", where, null, null, null);
		
		if (c.moveToFirst()){
			database.update(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME, values, PermissionsOpenHelper.APP_NAME 
					+ " = ? and " + PermissionsOpenHelper.PERM_NAME + " = ?", where);
		} else {
			database.insert(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME, null,
					values);
		}
		
		return getPermission(appName, permName);
	}
	
	public Permission getPermission(String appName, String permName) {
		String[] where = {appName, permName};
		
		Cursor c = database.query(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME, allColumns, PermissionsOpenHelper.APP_NAME 
				+ " = ? and " + PermissionsOpenHelper.PERM_NAME + " = ?", where, null, null, null);
		
		Permission result = null;
		if (c.moveToFirst()){
			result = cursorToPermission(c);
		}
		c.close();
		return result;
	}
	
	public void deletePermission(Permission permission) {
		long id = permission.getId();
		System.out.println("Permission deleted with id: " + id);
		database.delete(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME, PermissionsOpenHelper.ID
				+ " = " + id, null);
	}

	public List<Permission> getAllPermissions() {
		List<Permission> permissions = new ArrayList<Permission>();

		Cursor cursor = database.query(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Permission permission = cursorToPermission(cursor);
			permissions.add(permission);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return permissions;
	}
	
	public List<Permission> getAllPermissions(String appName) {
		String[] where = {appName};
		List<Permission> permissions = new ArrayList<Permission>();

		Cursor cursor = database.query(PermissionsOpenHelper.PERMISSIONS_TABLE_NAME,
				allColumns, PermissionsOpenHelper.APP_NAME + " = ?", where, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Permission permission = cursorToPermission(cursor);
			permissions.add(permission);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return permissions;
	}

	private Permission cursorToPermission(Cursor cursor) {
		Permission permission = new Permission();
		permission.setId(cursor.getLong(0));
		permission.setAppName(cursor.getString(1));
		permission.setPermName(cursor.getString(2));
		permission.setPermValue(cursor.getString(3));
		return permission;
	}
}

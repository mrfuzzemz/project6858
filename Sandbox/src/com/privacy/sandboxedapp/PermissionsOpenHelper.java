package com.privacy.sandboxedapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PermissionsOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "comment";
    public static final String COLUMN_PROFILE = "read_profile";
    public static final String COLUMN_PROFILE_VALUE = "read_profile_value";
    private static final String DATABASE_NAME = "permissions.db";
    private static final String PERMISSIONS_TABLE_NAME = "permissions";

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
        + PERMISSIONS_TABLE_NAME + "(" + COLUMN_ID
        + " integer primary key autoincrement, " + COLUMN_NAME 
        + " text not null, " + COLUMN_PROFILE + " text not null, " + COLUMN_PROFILE_VALUE + " text not null);";

    public PermissionsOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
package com.privacy.sandbox;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PermissionsOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    public static final String ID = "_id";
    public static final String APP_NAME = "appName";
    public static final String PERM_NAME = "permName";
    public static final String PERM_VALUE = "permValue";

    private static final String DATABASE_NAME = "permissions.db";
    public static final String PERMISSIONS_TABLE_NAME = "permissions";

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
        + PERMISSIONS_TABLE_NAME + "(" + ID
        + " integer primary key autoincrement, " + APP_NAME 
        + " text not null, " + PERM_NAME + " text not null, " 
        + PERM_VALUE + " text not null);";

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
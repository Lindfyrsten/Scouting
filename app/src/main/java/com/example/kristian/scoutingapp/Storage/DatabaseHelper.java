package com.example.kristian.scoutingapp.Storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


	private static final String SQL_CREATE_TABLE_MODEL =
			"CREATE TABLE IF NOT EXISTS " + PersistentColumnReader.ModelEntry.TABLE_NAME + " (" +
					PersistentColumnReader.ModelEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					PersistentColumnReader.ModelEntry.COLUMN_NAME_NAVN + " TEXT, " +
					PersistentColumnReader.ModelEntry.COLUMN_NAME_ALDER + " INTEGER, " +
					PersistentColumnReader.ModelEntry.COLUMN_NAME_NUMMER + " INTEGER, " +
					PersistentColumnReader.ModelEntry.COLUMN_NAME_LNG + " DOUBLE, " +
					PersistentColumnReader.ModelEntry.COLUMN_NAME_LAT + " DOUBLE, " +
					PersistentColumnReader.ModelEntry.COLUMN_NAME_DATE + " TEXT, " +
					PersistentColumnReader.ModelEntry.COLUMN_NAME_IMAGE + " TEXT)";
	private static final String SQL_DELETE_TABLE_AKTIVITET =
			"DROP TABLE IF EXISTS " + PersistentColumnReader.ModelEntry.TABLE_NAME;

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "scoutdb";
	private static Context applicationContext;
	private static DatabaseHelper databaseHelper;

	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DatabaseHelper getDatabaseHelperInstance() {
		if (databaseHelper == null) {
			databaseHelper = new DatabaseHelper(applicationContext);
		}
		return databaseHelper;
	}

	public static void setApplicationContext(Context applicationContext) {
		DatabaseHelper.applicationContext = applicationContext;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		updateMyDatabase(sqLiteDatabase, 0, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		updateMyDatabase(sqLiteDatabase, oldVersion, newVersion);
	}

	private void updateMyDatabase(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
		if (oldVersion < 1){
			sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MODEL);
		}
	}
}

package com.example.kristian.scoutingapp.Storage;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.example.kristian.scoutingapp.Model.Model;
import com.example.kristian.scoutingapp.Storage.Wrapper.ModelCursorWrapper;

import java.text.ParseException;

public class DatabaseStorage {

	private static DatabaseStorage databaseStorage;
	private DatabaseHelper databaseHelper = DatabaseHelper.getDatabaseHelperInstance();

	private DatabaseStorage() {}

	public static DatabaseStorage getInstance() {
		if (databaseStorage == null) {
			databaseStorage = new DatabaseStorage();
			databaseStorage.initDatabase();
		}
		return databaseStorage;
	}


	// Aktiviter

	public long insertModel(Model model){
		SQLiteDatabase db = DatabaseStorage.getInstance().databaseHelper.getWritableDatabase();
		ContentValues modelValues = new ContentValues();
		modelValues.put(PersistentColumnReader.ModelEntry.COLUMN_NAME_NAVN, model.getNavn());
		modelValues.put(PersistentColumnReader.ModelEntry.COLUMN_NAME_ALDER, model.getAlder());
		modelValues.put(PersistentColumnReader.ModelEntry.COLUMN_NAME_NUMMER, model.getNummer());
		modelValues.put(PersistentColumnReader.ModelEntry.COLUMN_NAME_LNG, model.getLng());
		modelValues.put(PersistentColumnReader.ModelEntry.COLUMN_NAME_LAT, model.getLat());
		modelValues.put(PersistentColumnReader.ModelEntry.COLUMN_NAME_DATE, model.getDato());
		modelValues.put(PersistentColumnReader.ModelEntry.COLUMN_NAME_IMAGE, model.getImagePath());
		return db.insert(PersistentColumnReader.ModelEntry.TABLE_NAME, null, modelValues);
	}

	public void removeModel(int modelId) throws ParseException{
		SQLiteDatabase db = DatabaseStorage.getInstance().databaseHelper.getReadableDatabase();
		db.delete(PersistentColumnReader.ModelEntry.TABLE_NAME, "_ID = ?", new String[] {""+modelId});
	}


	public ModelCursorWrapper getModeller() {
		SQLiteDatabase db = DatabaseStorage.getInstance().databaseHelper.getReadableDatabase();
		Cursor cursor = db.query(PersistentColumnReader.ModelEntry.TABLE_NAME,
				null,
				null,
				null,
				null,
				null,
				null,
				null);
		return new ModelCursorWrapper(cursor);
	}


	public Model getModelById(int modelId) throws ParseException {
		SQLiteDatabase db = DatabaseStorage.getInstance().databaseHelper.getReadableDatabase();

		Cursor cursor = db.query(PersistentColumnReader.ModelEntry.TABLE_NAME,
				null,
				PersistentColumnReader.ModelEntry._ID + " = ?",
				new String[]{"" + modelId},
				null,
				null,
				null);
		cursor.moveToFirst();
		return new ModelCursorWrapper(cursor).getModel();
	}



	public void nukeData(){
		SQLiteDatabase db = DatabaseStorage.getInstance().databaseHelper.getReadableDatabase();
		db.execSQL("delete from "+ PersistentColumnReader.ModelEntry.TABLE_NAME);
	}



	private void initDatabase(){

		// Tilføjer test modeller
		if (getModeller().getCount() == 0){
			Model m1 = new Model("Signe", 15, 46710523, 10.205083, 56.145293, "03/11/2019");
			Model m2 = new Model("Peter", 16, 67123478, 10.210694, 56.151497, "04/11/2019");
			insertModel(m1);
			insertModel(m2);
		}
	}


}

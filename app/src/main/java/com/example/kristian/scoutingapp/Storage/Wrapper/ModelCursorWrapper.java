package com.example.kristian.scoutingapp.Storage.Wrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.kristian.scoutingapp.Model.Model;
import com.example.kristian.scoutingapp.Storage.PersistentColumnReader;

import java.text.ParseException;

public class ModelCursorWrapper extends CursorWrapper {

	public ModelCursorWrapper(Cursor cursor){
		super(cursor);
	}

	public Model getModel() throws ParseException{
		long id = getLong(getColumnIndex(PersistentColumnReader.ModelEntry._ID));
		String navn = getString(getColumnIndex(PersistentColumnReader.ModelEntry.COLUMN_NAME_NAVN));
		int alder = getInt(getColumnIndex(PersistentColumnReader.ModelEntry.COLUMN_NAME_ALDER));
		int nummer = getInt(getColumnIndex(PersistentColumnReader.ModelEntry.COLUMN_NAME_NUMMER));
		double lng = getDouble(getColumnIndex(PersistentColumnReader.ModelEntry.COLUMN_NAME_LNG));
		double lat = getDouble(getColumnIndex(PersistentColumnReader.ModelEntry.COLUMN_NAME_LAT));
		String dato = getString(getColumnIndex(PersistentColumnReader.ModelEntry.COLUMN_NAME_DATE));
		String image = getString(getColumnIndex(PersistentColumnReader.ModelEntry.COLUMN_NAME_IMAGE));
		return new Model(id, navn, alder, nummer, lng, lat, dato, image);
	}
}

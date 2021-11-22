package com.example.kristian.scoutingapp.Storage;

import android.provider.BaseColumns;

public final class PersistentColumnReader {

    private PersistentColumnReader() {}

    public static class ModelEntry implements BaseColumns {
        public static final String TABLE_NAME = "model";
	    public static final String COLUMN_NAME_NAVN = "navn";
	    public static final String COLUMN_NAME_ALDER = "alder";
	    public static final String COLUMN_NAME_NUMMER = "nummmer";
	    public static final String COLUMN_NAME_LNG = "longtitude";
	    public static final String COLUMN_NAME_LAT = "latitude";
	    public static final String COLUMN_NAME_DATE = "dato";
	    public static final String COLUMN_NAME_IMAGE = "image";

    }

}

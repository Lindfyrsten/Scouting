package com.example.kristian.scoutingapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kristian.scoutingapp.Adapter.ModelArrayAdapter;
import com.example.kristian.scoutingapp.Model.Model;
import com.example.kristian.scoutingapp.Storage.DatabaseStorage;
import com.example.kristian.scoutingapp.Storage.PersistentColumnReader;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ListModelActivity extends AppCompatActivity {

	private List<Model> modelList = new ArrayList<>();
	private ModelArrayAdapter ArrayAdapter;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_model);

		populateModelList();

		ArrayAdapter= new ModelArrayAdapter(this, -1, modelList);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(ArrayAdapter);
		listView.setEmptyView(findViewById(R.id.emptyView));
		TextView tvHeader = findViewById(R.id.activity_list_model_header);
		tvHeader.setTypeface(MainActivity.font);
	}


	private void populateModelList(){

		DatabaseStorage db = DatabaseStorage.getInstance();
		Cursor cursor = db.getModeller();
		// Går igennem listen af modeller og tilføjer dem til en midlertidig liste som bruges i ArrayAdapter
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Model model = null;
			try {
				model = db.getModelById(cursor.getInt(cursor.getColumnIndex(PersistentColumnReader.ModelEntry._ID)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (!modelList.contains(model)){
				modelList.add(model);
			}
			cursor.moveToNext();
		}
	}

	@Override
	protected void onResume() {

		// Jeg havde problemer med at få mit listview opdateret når jeg slettede en model
		// Dette virker, men jeg ved godt det ikke er en pæn løsning og at der helt sikkert
		// er en bedre måde at gøre det på
		modelList.clear();
		populateModelList();
		ArrayAdapter= new ModelArrayAdapter(this, -1, modelList);
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(ArrayAdapter);
		listView.setEmptyView(findViewById(R.id.emptyView));
		super.onResume();
	}
}

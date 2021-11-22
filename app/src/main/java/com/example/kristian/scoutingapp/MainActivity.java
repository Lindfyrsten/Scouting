package com.example.kristian.scoutingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kristian.scoutingapp.Storage.DatabaseHelper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

	public static Typeface font;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		DatabaseHelper.setApplicationContext(this);

		font = Typeface.createFromAsset(this.getAssets(), "fonts/Voga.otf");
		new_Model();
		list_Model();
	}

	public void new_Model(){
		Button btn = (Button) findViewById(R.id.btn_new);
		btn.setTypeface(font);
		btn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				Intent newModel = new Intent(getApplicationContext(), NewModelActivity.class);
				startActivity(newModel);
			}
		});
	}

	private void list_Model(){
		Button btn = (Button) findViewById(R.id.btn_view);
		btn.setTypeface(font);
		btn.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				Intent listModel = new Intent(getApplicationContext(), ListModelActivity.class);
				startActivity(listModel);
			}
		});
	}

	@Override
	public void onBackPressed() {
		int count = getSupportFragmentManager().getBackStackEntryCount();
		if (count == 0) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(R.string.exit_dialog);
			alert.setCancelable(false);
			alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					MainActivity.this.finish();
				}
			});
			alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Do nothing
				}
			});
			alert.show();
		} else {
			getSupportFragmentManager().popBackStack();
		}

	}
}

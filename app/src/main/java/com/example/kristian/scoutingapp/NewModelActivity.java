package com.example.kristian.scoutingapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kristian.scoutingapp.Model.Model;
import com.example.kristian.scoutingapp.Storage.DatabaseStorage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NewModelActivity extends AppCompatActivity {

	private FusedLocationProviderClient client;
	private double lat, lng;
	private Typeface font = MainActivity.font;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private Uri photoURI;
	private Model model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_model);
		// Sætter font
		TextView tvHead = findViewById(R.id.activity_new_model_title);
		tvHead.setTypeface(font);
		EditText etName = findViewById(R.id.activity_new_model_name);
		etName.setTypeface(font);
		EditText etAge = findViewById(R.id.activity_new_model_age);
		etAge.setTypeface(font);
		EditText etNumber = findViewById(R.id.activity_new_model_number);
		etNumber.setTypeface(font);
		TextView tvMail = findViewById(R.id.activity_new_model_mail);
		tvMail.setTypeface(font);
		// Opsætning af FAB
		FloatingActionButton fabDone = findViewById(R.id.activity_new_model_done);
		fabDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder alert = new AlertDialog.Builder(NewModelActivity.this);
				alert.setTitle("Ny model").setMessage("Vil du oprette ny model?").setPositiveButton("Bekræft", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						btnOk();
					}
				}).setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create().show();
			}
		});
		// Opsætning af billede knappen
		final Button btnBillede = findViewById(R.id.activity_new_model_btnPhoto);
		btnBillede.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				btnBillede();
			}
		});
		client = LocationServices.getFusedLocationProviderClient(this);
//		requestPermission();
		getLocation();
	}

	public void btnOk(){
		final EditText etName = findViewById(R.id.activity_new_model_name);
		final EditText etAge = findViewById(R.id.activity_new_model_age);
		final EditText etNumber = findViewById(R.id.activity_new_model_number);
		final CheckBox cbMail = findViewById(R.id.activity_new_model_cbmail);
		if (etName.getText().length() == 0){
			etName.setError("Navn mangler");
			return;
		}
		if (etAge.getText().length() == 0){
			etAge.setError("Alder mangler");
			return;
		}
		if (etNumber.getText().length() == 0){
			etNumber.setError("Nummer mangler");
			return;
		}
		String name = etName.getText().toString();
		int age = Integer.parseInt(etAge.getText().toString());
		int number = Integer.parseInt(etNumber.getText().toString());
		String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()); // Gemmer dato i String

		// Gemmer model
		saveModel(name, age, number, lng, lat, date);

		// Sender mail hvis valgt
		if (cbMail.isChecked()){
			sendMail();
		}
	}

	public void btnBillede(){

		// Tjekker om mobilen har et kamera
		PackageManager pm = getPackageManager();
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
			Toast.makeText(this, "UPS! Der blev ikke fundet et kamera på enheden", Toast.LENGTH_SHORT).show();
			return;
		}
		// Tjekker om der er indtastet et navn som skal bruges i fil-navnet
		final EditText etName = findViewById(R.id.activity_new_model_name);
		if (etName.getText().length() == 0){
			etName.setError("Navn mangler");
			return;
		}
		dispatchTakePictureIntent();

	}



	private void getLocation(){
		// Tjekker om GPS er slået til
		final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
		if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			buildAlertMessageNoGps();
		}
		// Tjekker om app har tilladelse til at bruge GPS
		if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermission();
			return;
		}
		// Gemmer longitude og latitude
		client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
			@Override
			public void onSuccess(Location location) {
				if (location!= null){
					lng = location.getLongitude();
					lat = location.getLatitude();
				}
			}
		});
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				photoURI = FileProvider.getUriForFile(this,
						"com.example.kristian.scoutingapp.fileprovider",
						photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			ImageView thumb = findViewById(R.id.activity_new_model_thumbnail);
			thumb.setImageURI(photoURI);
		}
	}



	private void sendMail(){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822"); // Specificerer data typen
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"kristianlindbjerg@live.dk"}); // Hardcoded email adresse at sende til
		i.putExtra(Intent.EXTRA_SUBJECT, "Scout report");
		i.putExtra(Intent.EXTRA_TEXT   , "Navn: " + model.getNavn() + "\nAlder: " + model.getAlder() + "\nKontaktnummer: " + model.getNummer() +"\nDato: " + model.getDato());
		try {
			this.startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "Der er ingen email klienter installeret.", Toast.LENGTH_SHORT).show();
		}
	}

	private void saveModel(String name, int age, int number, double lng, double lat, String date){
		// Opretter ny model. Tjekker om der er taget et photo ved at se om photoURI er null
		if (photoURI != null){
			String imagePath = photoURI.toString();
			model = new Model(name, age, number, lng, lat, date, imagePath);
		}
		else {
			model = new Model(name, age, number, lng, lat, date);
		}
		long added = DatabaseStorage.getInstance().insertModel(model);
		if (added == -1) {
			Toast.makeText(NewModelActivity.this, "Ups! Databasen kunne ikke indsætte modellen", Toast.LENGTH_SHORT).show();
		}
		else {
			setResult(Activity.RESULT_OK);
			finish();
			Toast.makeText(NewModelActivity.this, "Model oprettet!", Toast.LENGTH_SHORT).show();
		}
	}

	private File createImageFile() throws IOException {
		final EditText etName = findViewById(R.id.activity_new_model_name);
		String navn = etName.getText().toString();
		String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()); // Gemmer dato i String
		// Laver image fil-navnet
		String imageFileName = navn +"_" + date + "_";
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				storageDir      /* directory */
		);
		Log.d("KRILLE", image.getAbsolutePath());
		return image;
	}

	private void requestPermission(){
		ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Din GPS er slået fra, vil du slå den til?")
				.setCancelable(false)
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
						startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				})
				.setNegativeButton("Nej", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}
}



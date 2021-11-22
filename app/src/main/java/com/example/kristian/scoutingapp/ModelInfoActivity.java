package com.example.kristian.scoutingapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kristian.scoutingapp.Model.Model;
import com.example.kristian.scoutingapp.Storage.DatabaseStorage;

import java.io.File;
import java.text.ParseException;

public class ModelInfoActivity extends AppCompatActivity {

	private WebView webView;
	private String mapPath;
	private Model m;
	private Typeface font;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_model_info);

		font = MainActivity.font;
		final String id = getIntent().getStringExtra("ID");

		// Finder model
		final int modelId = Integer.parseInt(id);
		DatabaseStorage db = DatabaseStorage.getInstance();
		try {
			m = db.getModelById(modelId);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		TextView tvNavn = findViewById(R.id.activity_model_info_navn);
		TextView tvAlder = findViewById(R.id.activity_model_info_alder);
		TextView tvNummer = findViewById(R.id.activity_model_info_nummer);
		TextView tvDato = findViewById(R.id.activity_model_info_dato);
		TextView tvLoc = findViewById(R.id.activity_model_info_ingenloc);
		TextView tvPhoto = findViewById(R.id.activity_model_info_nophoto);
		Button btnSlet = findViewById(R.id.activity_model_info_slet);
		webView = findViewById(R.id.webView);
		tvNavn.setText(m.getNavn());
		tvAlder.setText("Alder: "+ m.getAlder());
		tvNummer.setText("Nummer: "+ m.getNummer());
		tvDato.setText("Dato: " + m.getDato());

		tvNavn.setTypeface(font);
		tvAlder.setTypeface(font);
		tvNummer.setTypeface(font);
		tvDato.setTypeface(font);
		btnSlet.setTypeface(font);

		if (!m.getImagePath().equals("void")){
			setImage();
		}
		else {
			tvPhoto.setText("Intet billede gemt");
		}

		if (m.getLng() > 0){
			loadMap();
		}
		else {
			tvLoc.setText("Ingen lokalitet registreret");
			webView.setVisibility(View.INVISIBLE);

		}

		btnSlet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder alert = new AlertDialog.Builder(ModelInfoActivity.this);
				alert.setTitle("Ny model").setMessage("Vil du slette modellen? Dette kan ikke fortrydes!").setPositiveButton("Bekræft", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sletModel(modelId);
					}
				}).setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create().show();
			}
		});
	}

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch(view.getId()) {
			case R.id.rb_gps:
				if (checked) {
					RelativeLayout llgps = findViewById(R.id.activity_model_info_layoutgps);
					RelativeLayout llimage = findViewById(R.id.activity_model_info_layoutimage);
					llgps.setVisibility(View.VISIBLE);
					llimage.setVisibility(View.INVISIBLE);
				}
				break;
			case R.id.rb_photo:
				if (checked){
					RelativeLayout llgps = findViewById(R.id.activity_model_info_layoutgps);
					RelativeLayout llimage = findViewById(R.id.activity_model_info_layoutimage);
					llgps.setVisibility(View.INVISIBLE);
					llimage.setVisibility(View.VISIBLE);
				}
				break;
		}
	}

	private void loadMap(){
		mapPath = "http://www.google.com/maps?q=" + m.getLat() +"," + m.getLng();
		// Webview
		webView = findViewById(R.id.webView);
		webView.setWebViewClient(new WebViewClient() {});
		WebSettings settings = webView.getSettings();
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		settings.setJavaScriptEnabled(true);
		webView.loadUrl(mapPath);
	}

	private void setImage(){
		String imagePath = m.getImagePath();
		if (imagePath.equals("void")){
			return;
		}
		ImageView image = findViewById(R.id.activity_model_info_image);
		image.setImageURI(Uri.parse(m.getImagePath()));
	}

	private void sletModel(int modelId){
		DatabaseStorage db = DatabaseStorage.getInstance();
		try {
			db.removeModel(modelId);
			// Sletter billede fil hvis findes
			if (!m.getImagePath().equals("void"));{
				File file = new File(Uri.parse(m.getImagePath()).getPath());
				file.delete();
			}
			finish();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}

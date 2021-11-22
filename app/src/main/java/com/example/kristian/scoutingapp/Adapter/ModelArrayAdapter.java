package com.example.kristian.scoutingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kristian.scoutingapp.MainActivity;
import com.example.kristian.scoutingapp.Model.Model;
import com.example.kristian.scoutingapp.ModelInfoActivity;
import com.example.kristian.scoutingapp.R;

import java.util.List;

public class ModelArrayAdapter extends ArrayAdapter<Model> {


	private Typeface font;

	public ModelArrayAdapter(Context context, int resource, List<Model> models){
		super(context, resource, models);
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent){
		if (convertView == null){
			convertView = View.inflate(getContext(), R.layout.list_item, null);
		}

		font = MainActivity.font;
		final TextView tvName = convertView.findViewById(R.id.list_name);
		final TextView tvDate = convertView.findViewById(R.id.list_date);
		final Button btnMail = convertView.findViewById(R.id.btn_mail);
		final Button btnInfo = convertView.findViewById(R.id.btn_info);
		final Model model = getItem(position);

		tvName.setText(model.getNavn());
		tvDate.setText(model.getDato());
		tvName.setTypeface(font);
		tvDate.setTypeface(font);
		btnMail.setTypeface(font);
		btnInfo.setTypeface(font);


		btnMail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822"); // Viser de applikationer som støtter SEND intent
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"kristianlindbjerg@live.dk"});
				i.putExtra(Intent.EXTRA_SUBJECT, "Scout report");
				i.putExtra(Intent.EXTRA_TEXT   , "Navn: " + model.getNavn() + "\nDato: " + model.getDato() + "\nAlder: " + model.getAlder() + "\nKontaktnummer: " + model.getNummer());
				try {
					getContext().startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getContext(), ModelInfoActivity.class);
				i.putExtra("ID", "" + model.getId());
				getContext().startActivity(i);
			}
		});
		return convertView;
	}




}

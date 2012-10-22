package com.android.activity;

import models.Monument;
import preferences.Prefs;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import exceptions.AttributNoValidException;

public class MonumentReportActivity extends Activity implements LocationListener, OnClickListener{

	private final String LOG_ID = this.getClass().getName();
	private Location location;

	private long update_time = 1000;
	private float update_distance = 5000;

	private EditText editTextName;
	private EditText editTextDescription;
	private Button buttonCancel;
	private Button buttonShare;
	
	private LocationManager locationManager;
	private String provider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monumentreport);

		this.initViews();
		this.initLocation();
	}


	private void initViews() {
		this.editTextDescription = (EditText) findViewById(R.monumentreport.EditTextDescription);
		this.editTextName  		 = (EditText) findViewById(R.monumentreport.EditTextName);
		this.buttonCancel		 = (Button) findViewById(R.monumentreport.ButtonCancel);
		this.buttonShare		 = (Button) findViewById(R.monumentreport.ButtonShare);
		
		this.buttonShare.setOnClickListener(this);
		this.buttonCancel.setOnClickListener(this);
	}


	public void initLocation()
	{
		// On récupère les coordonnées GPS
		/// On récupère le gestionnaire de localisation
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		/// On définit les critère de choix du meilleur système de localisation
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(true);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		provider = locationManager.getBestProvider(criteria, true);
		/// On demande au gestionnaire de localisation de faire des mise-a-jour de la position.
		locationManager.requestLocationUpdates(provider, update_time, update_distance, this);
	}



	public void onLocationChanged(Location l) {
		this.location = l;
		TextView textViewLocation = (TextView) findViewById(R.monumentreport.location);
		textViewLocation.setText(location.getAccuracy() + "m");
		if(this.location.hasAccuracy() && this.location.getAccuracy() < 10)
			this.locationManager.removeUpdates(this);
	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	// OnclickListener
	public void onClick(View v) {
		switch (v.getId())
		{
			case R.monumentreport.ButtonShare:
				this.shareAction();
				break;
			case R.monumentreport.ButtonCancel:
				this.cancelAction();
				break;
			}

	}


	private void cancelAction() {
		
	}


	private void shareAction() {
		// Test si un nom est présent
		String nom 			= this.editTextName.getText().toString();
		String description 	= this.editTextDescription.getText().toString();
		
		if(nom.length() == 0)
		{
			Toast.makeText(getApplicationContext(), "Le nom du monument est requis", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(description.length() == 0)
			description = "Pas de description.";
		
		// Récupération de la position du monument
		Location monumentLocation = null;
		if(location != null)
			monumentLocation = location;
		else
		{
			Toast.makeText(getApplicationContext(), "Vous devez attendre que le gps détecte votre position.", Toast.LENGTH_LONG).show();
			return;
		}
			
		
		// Récupération de l'utilisateur
		Prefs pref = new Prefs(getApplicationContext());
		int idUser = Integer.valueOf(pref.getPreference("idUser"));
		
		Monument monument = new Monument(getApplicationContext());
		monument.setIdUser(idUser);
		monument.setLibelle(nom);
		monument.setLocation(monumentLocation);
		monument.setDescription(description);
		
		
		
		try {
			monument.save();
			Toast.makeText(getApplicationContext(), "Le monument à été partagé", Toast.LENGTH_LONG).show();
		} catch (AttributNoValidException e) {
			Toast.makeText(getApplicationContext(), "Echec du partage du monument", Toast.LENGTH_LONG).show();
		}
			
		
	}

	
	public void onDestroy()
	{
		super.onDestroy();
		this.locationManager.removeUpdates(this);
	}
}

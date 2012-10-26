package com.android.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import models.Monument;
import adapters.AdapterListCommentaires;
import adapters.AdapterListMonuments;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import exceptions.CommentNotFoundException;
import exceptions.MonumentNotFoundException;
import exceptions.UserNotFoundException;

public class MonumentPageActivity extends Activity implements LocationListener{

	private Monument monument;

	private long update_time = 1000;
	private float update_distance = 5000;
	
	private TextView distanceTextView;
	private TextView libelleTextView;
	private TextView descriptionTextView;
	private ListView commentairesListView;
	private Button ajouterCommentaireButton;
	
	public DecimalFormat format;
	
	private Location location;
	private LocationManager locationManager;
	private String provider;
	/**
	 * Called when the activity is first created. 
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monumentpage);

		Bundle objetbunble  = this.getIntent().getExtras(); 
		
		int idMonument = objetbunble.getInt("idMonument");
		try {
			this.monument = new Monument(this, idMonument);
		} catch (MonumentNotFoundException e) {
			this.returnToHome();
			e.printStackTrace();
		} catch (CommentNotFoundException e) {
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		
		this.initAtributs();
		this.initViews();
		this.initLocation();
		this.initListView();
		this.populateViews();
		
		

	}
	
	private void initListView() {
	
		
		// On place les parcours dans la listView
		final AdapterListCommentaires adapter = new AdapterListCommentaires(getBaseContext(), this.monument.getListeDeCommentaires());
		commentairesListView.setAdapter(adapter);

		// Click sur un monument
		commentairesListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});
		
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
	
	private void populateViews() {
		this.libelleTextView.setText(monument.getLibelle());
		this.descriptionTextView.setText(monument.getDescription());
	}

	private void initAtributs()
	{
		format = new DecimalFormat ( ) ; 
		format.setMaximumFractionDigits ( 1 ) ; 
		format.setMinimumFractionDigits ( 0 ) ; 
		format.setDecimalSeparatorAlwaysShown ( true ) ; 
	}
	
	private void initViews() {
		distanceTextView 			= (TextView) findViewById(R.monumentpage.distanceTextView);
		libelleTextView 			= (TextView) findViewById(R.monumentpage.libelleTextView);
		descriptionTextView 		= (TextView) findViewById(R.monumentpage.descriptionTextView);
//		commentairesListView;
		
//		ajouterCommentaireButton;
		
	}

	/**
	 * Permet de retourner à la page d'accueil
	 */
	private void returnToHome()
	{
		startActivity(new Intent(this, MonumentDroidActivity.class));
	}

	
	/**
	 * ----------------------------------------------
	 * Location listener
	 * ----------------------------------------------
	 */
	public void onLocationChanged(Location loc) {
		this.location = loc;
		float distance = loc.distanceTo(this.monument.getLocation());
		this.distanceTextView.setText("à " + format.format(distance) + " m");
		
		if(this.location.hasAccuracy() && this.location.getAccuracy() < 10)
			this.locationManager.removeUpdates(this);
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}

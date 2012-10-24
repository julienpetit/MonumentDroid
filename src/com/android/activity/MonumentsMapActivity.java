package com.android.activity;

import java.util.List;

import map.MyItimizedOverlay;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MonumentsMapActivity extends MapActivity implements LocationListener {

	private MapView mapView;
	private MapController mapController;

	@SuppressWarnings("unused")
	private final String LOG_ID = this.getClass().getName();
	private Location location;
	private long update_time = 5000; //milliseconds
	private float update_distance = 5; //meters

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mapmonuments);

		// On récupère les coordonnées GPS
		/// On récupère le gestionnaire de localisation
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		/// On définit les critère de choix du meilleur système de localisation
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(true);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = lm.getBestProvider(criteria, true);
		/// On demande au gestionnaire de localisation de faire des mise-a-jour de la position.
		lm.requestLocationUpdates(provider, update_time, update_distance, this);
		/// On récupère la dernière position connu
		this.location = lm.getLastKnownLocation(provider);

		mapView = (MapView) this.findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);

		Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
		//MyItimizedOverlay itemizedoverlay = new MyItimizedOverlay(drawable);
		MyItimizedOverlay itemizedoverlay = new MyItimizedOverlay(drawable,mapView);

		GeoPoint geoPoint = new GeoPoint((int)((47.637942)*1E6),(int)((6.862813) * 1E6));
		OverlayItem overlayitem = new OverlayItem(geoPoint, "Lion de Belfort", "All�e du Souvenir Fran�ais, 90000 Belfort");
		itemizedoverlay.addOverlayItem(overlayitem);

		List<Overlay> mapOverlays = mapView.getOverlays();
		mapOverlays.add(itemizedoverlay);

		mapController = mapView.getController();
		mapController.setCenter(geoPoint);
		mapController.setZoom(10);	
	}


	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

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

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}

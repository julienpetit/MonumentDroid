package com.android.activity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import map.MyItemizedOverlay;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

//public class MonumentsMapActivity extends MapActivity implements LocationListener {
//
//	private MapView mapView;
//	private MapController mapController;
//
//	@SuppressWarnings("unused")
//	private final String LOG_ID = this.getClass().getName();
//	private Location location;
//	private long update_time = 5000; //milliseconds
//	private float update_distance = 5; //meters
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.mapmonuments);
//
//		// On récupère les coordonnées GPS
//		/// On récupère le gestionnaire de localisation
//		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//		/// On définit les critère de choix du meilleur système de localisation
//		Criteria criteria = new Criteria();
//		criteria.setAccuracy(Criteria.ACCURACY_FINE);
//		criteria.setAltitudeRequired(true);
//		criteria.setBearingRequired(false);
//		criteria.setCostAllowed(true);
//		criteria.setPowerRequirement(Criteria.POWER_LOW);
//		String provider = lm.getBestProvider(criteria, true);
//		/// On demande au gestionnaire de localisation de faire des mise-a-jour de la position.
//		lm.requestLocationUpdates(provider, update_time, update_distance, this);
//		/// On récupère la dernière position connu
//		this.location = lm.getLastKnownLocation(provider);
//
//		mapView = (MapView) this.findViewById(R.id.map);
//		mapView.setBuiltInZoomControls(true);
//
//		Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
//		//MyItimizedOverlay itemizedoverlay = new MyItimizedOverlay(drawable);
//		MyItimizedOverlay itemizedoverlay = new MyItimizedOverlay(drawable,mapView);
//
//		GeoPoint geoPoint = new GeoPoint((int)((47.637942)*1E6),(int)((6.862813) * 1E6));
//		OverlayItem overlayitem = new OverlayItem(geoPoint, "Lion de Belfort", "All�e du Souvenir Fran�ais, 90000 Belfort");
//		itemizedoverlay.addOverlayItem(overlayitem);
//
//		List<Overlay> mapOverlays = mapView.getOverlays();
//		mapOverlays.add(itemizedoverlay);
//
//		mapController = mapView.getController();
//		mapController.setCenter(geoPoint);
//		mapController.setZoom(10);	
//	}
//
//
//	public void onLocationChanged(Location arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void onProviderDisabled(String arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void onProviderEnabled(String arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected boolean isRouteDisplayed() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//}

public class MonumentsMapActivity extends MapActivity implements LocationListener{ 



	private MapController mapController; 

	private MapView mapView; 

	private LocationManager locationManager; 

	MyLocationOverlay myLocationOverlay;

	private MyItemizedOverlay itemizedoverlay;

	public void onCreate(Bundle bundle) { 

		super.onCreate(bundle); 

		setContentView(R.layout.mapmonuments);



		mapView = (MapView) findViewById(R.id.map); 

		mapView.setBuiltInZoomControls(true); 

		mapView.setSatellite(true); 



		mapController = mapView.getController(); 

		mapController.setZoom(18); 



		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this); 



		myLocationOverlay = new MyLocationOverlay(this, mapView);

		mapView.getOverlays().add(myLocationOverlay);



		myLocationOverlay.runOnFirstFix(new Runnable() {

			public void run() {

				mapView.getController().animateTo(

						myLocationOverlay.getMyLocation());

			}

		});



		Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);

		itemizedoverlay = new MyItemizedOverlay(drawable, mapView);

		afficherMarker();

	} 



	@Override

	protected void onResume() {

		super.onResume();

		myLocationOverlay.enableMyLocation();

		myLocationOverlay.enableCompass();

	}



	@Override

	protected void onPause() {

		super.onResume();

		myLocationOverlay.disableMyLocation();

		myLocationOverlay.disableCompass();

	}



	@Override 

	protected boolean isRouteDisplayed() { 

		return false; 

	} 



	public void onLocationChanged(Location location) { 

		int lat = (int) (location.getLatitude() * 1E6); 

		int lng = (int) (location.getLongitude() * 1E6); 

		GeoPoint point = new GeoPoint(lat, lng); 



		mapController.animateTo(point);

		mapController.setCenter(point); 

		afficherMarker();

	} 



	public void onProviderDisabled(String provider) { 

	} 



	public void onProviderEnabled(String provider) { 

	} 



	public void onStatusChanged(String provider, int status, Bundle extras) { 

	} 

	private void afficherMarker() {

		GeoPoint p = mapView.getMapCenter(); 

		Geocoder gcd = new Geocoder(MonumentsMapActivity.this, Locale.getDefault());

		String addr ="";

		List<Address> addresses;

		try {

			addresses = gcd.getFromLocation( (p.getLatitudeE6() / 1E6) , (p.getLongitudeE6() / 1E6) ,1);



			if (addresses.size() > 0 && addresses != null) {

				addr = addresses.get(0).getAddressLine(0); 

				OverlayItem overlayitem = new OverlayItem(p, "Adress",addr);

				itemizedoverlay.addOverlay(overlayitem);

				mapView.getOverlays().add(itemizedoverlay);

			}

		} catch (IOException e) {

			e.printStackTrace();

		} 

	} 

}
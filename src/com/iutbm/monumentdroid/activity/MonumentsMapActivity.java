package com.iutbm.monumentdroid.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.iutbm.monumentdroid.exceptions.CommentNotFoundException;
import com.iutbm.monumentdroid.exceptions.MonumentNotFoundException;
import com.iutbm.monumentdroid.exceptions.UserNotFoundException;
import com.iutbm.monumentdroid.map.MyItemizedOverlay;
import com.iutbm.monumentdroid.map.MyOverlayItem;
import com.iutbm.monumentdroid.models.Monument;

public class MonumentsMapActivity extends MapActivity implements LocationListener {

	private MapController mapController;
	private MapView mapView;

	private LocationManager locationManager;
	private MyLocationOverlay myLocationOverlay;
	private MyItemizedOverlay itemizedoverlay;

	private ProgressDialog progressDialog;
	private boolean satellite;

	/**
	 * ----------------------------------------------
	 * Events
	 * ----------------------------------------------
	 */
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.mapmonuments);

		Bundle bundleIntent = getIntent().getExtras();

		this.initAttributs();
		this.initViews();
		this.initMap();
		this.initLocation();

		
		// Tentative de récupération de l'id d'un monument
		// Si on en récupère un, on positionne la vue dessu.
		try 
		{
			Monument monument = new Monument(getApplicationContext(), bundleIntent.getInt("idMonument"));
			this.animateToMonument(monument);
		} catch( Exception e)
		{
			e.printStackTrace();
			myLocationOverlay.runOnFirstFix(new Runnable() {
				public void run() {
					mapView.getController().animateTo(
							myLocationOverlay.getMyLocation());
				}
			});
		}
		
		


		try {
			afficheListeDeMonuments(Monument.getAllMonuments(getApplicationContext(), 0, null));
		} catch (MonumentNotFoundException e) {
			e.printStackTrace();
		} catch (CommentNotFoundException e) {
			e.printStackTrace();
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}

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



	/**
	 * ----------------------------------------------
	 * Initialize
	 * ----------------------------------------------
	 */
	private void initAttributs()
	{
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		this.satellite = false;
		progressDialog = new ProgressDialog(this);
	}

	private void initViews()
	{
		mapView = (MapView) findViewById(R.id.map);
	}

	private void initMap()
	{
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(false);
		mapController = mapView.getController();
		mapController.setZoom(18);

		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);

		Drawable drawable = this.getResources().getDrawable(
				R.drawable.red_marker_b);

		itemizedoverlay = new MyItemizedOverlay(drawable, mapView);

	}

	private void initLocation()
	{	
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 10, this);
	}

	private void afficheListeDeMonuments(ArrayList<Monument> monuments)
	{
		for (Monument monument : monuments)
		{
			Location loc = monument.getLocation();
			GeoPoint point = new GeoPoint((int) (loc.getLatitude() * 1E6), (int) (loc.getLongitude() * 1E6));
			afficherMarker(point, monument.getLibelle(), monument.getDescription(), monument.getId());
		}

	}

	private void animateToMyLocation()
	{
		if(myLocationOverlay.getMyLocation() != null)
			mapView.getController().animateTo(myLocationOverlay.getMyLocation());

	}

	private void animateToMonument(Monument monument)
	{
		Location loc = monument.getLocation();
		GeoPoint point = new GeoPoint((int) (loc.getLatitude() * 1E6), (int) (loc.getLongitude() * 1E6));
		mapView.getController().animateTo(point);
	}

	/**
	 * ----------------------------------------------
	 * Location listener
	 * ----------------------------------------------
	 */

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}



	public void onLocationChanged(Location location) {

	}

	public void onProviderDisabled(String provider) {

	}

	public void onProviderEnabled(String provider) {

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	/**
	 * ----------------------------------------------
	 * Affichage d'un monument
	 * ----------------------------------------------
	 */

	private void afficherMarker(GeoPoint point, String libelle, String description, int idMonument)
	{
		Geocoder gcd = new Geocoder(MonumentsMapActivity.this,
				Locale.getDefault());

		String addr = "";

		List<Address> addresses;

		try {

			addresses = gcd.getFromLocation((point.getLatitudeE6() / 1E6),
					(point.getLongitudeE6() / 1E6), 1);

			if (addresses.size() > 0 && addresses != null) {

				addr = addresses.get(0).getAddressLine(0);

				MyOverlayItem overlayitem = new MyOverlayItem(point, libelle, description, idMonument);
				
				itemizedoverlay.addOverlay(overlayitem);
				mapView.getOverlays().add(itemizedoverlay);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet de passer d'un affichage satellite à un afichage plan 
	 * en fonction de l'attribut boolean 'satellite'
	 */
	private void invertMap()
	{
		this.mapView.setSatellite(!this.satellite);
		this.satellite = !this.satellite;
	}

	// =============================
	// Menu
	// =============================

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_monument_map, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_monument_map, menu);

		// Modification du titre du boutou menu en fonction de la vue du plan
		MenuItem menuSatellite = (MenuItem) menu.findItem(R.menumonumentmap.satellite);
		if(this.satellite)
			menuSatellite.setTitle("Plan");
		else
			menuSatellite.setTitle("Satellite");

		return super.onPrepareOptionsMenu(menu);
	}

	//Méthode qui se déclenchera au clic sur un item
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//On regarde quel item a été cliqué grâce à  son id et on déclenche une action
		switch (item.getItemId()) {

		case R.menumonumentmap.satellite:
			invertMap();
			return true;

		case R.menumonumentmap.myPosition:
			this.animateToMyLocation();
			return true;
		}
		return false;
	}

}
package com.iutbm.monumentdroid.activity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.OverlayItem;
import com.iutbm.monumentdroid.exceptions.AttributNoValidException;
import com.iutbm.monumentdroid.exceptions.CommentNotFoundException;
import com.iutbm.monumentdroid.exceptions.MonumentNotFoundException;
import com.iutbm.monumentdroid.exceptions.UserNotFoundException;
import com.iutbm.monumentdroid.models.Comment;
import com.iutbm.monumentdroid.models.Monument;
import com.iutbm.monumentdroid.models.User;
import com.iutbm.monumentdroid.preferences.Prefs;

public class MonumentPageActivity extends Activity implements LocationListener{

	private Monument monument;

	private long update_time = 1000;
	private float update_distance = 5000;

	private TextView distanceTextView;
	private TextView libelleTextView;
	private TextView descriptionTextView;
	private TextView adresseTextView;
	private Button ajouterCommentaireButton;
	private LinearLayout commentairesLinearLayout;

	private DecimalFormat format;
	private SimpleDateFormat formatDate;

	private Location location;
	private LocationManager locationManager;
	private String provider;
	private Prefs preferences;
	
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
		this.populateViews();

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
		if(provider != null)
			locationManager.requestLocationUpdates(provider, update_time, update_distance, this);
	}

	private void populateViews() {
		this.libelleTextView.setText(monument.getLibelle());
		this.descriptionTextView.setText(monument.getDescription());

		this.updateListComments();
	}

	private void initAtributs()
	{
		preferences = new Prefs(this);
		format = new DecimalFormat ( ) ; 
		format.setMaximumFractionDigits ( 1 ) ; 
		format.setMinimumFractionDigits ( 0 ) ; 
		format.setDecimalSeparatorAlwaysShown ( true ) ; 
		formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	}

	private void initViews() {
		distanceTextView 			= (TextView) findViewById(R.monumentpage.distanceTextView);
		libelleTextView 			= (TextView) findViewById(R.monumentpage.libelleTextView);
		descriptionTextView 		= (TextView) findViewById(R.monumentpage.descriptionTextView);
		commentairesLinearLayout   	= (LinearLayout) findViewById(R.monumentpage.commentairesLinearLayout);
		adresseTextView 			= (TextView) findViewById(R.monumentpage.adresseTextView);
		//		ajouterCommentaireButton;
	}

	/**
	 * Permet de retourner à la page d'accueil
	 */
	private void returnToHome()
	{
		startActivity(new Intent(this, MonumentDroidActivity.class));
	}


	protected void addComment(String message) {
		// Récupération de l'utilisateur connecté.
		Prefs pref = new Prefs(getBaseContext());
		int idUser = Integer.valueOf(pref.getPreference("idUser"));

		User user = null;
		try {
			user = new User(getApplication(), idUser);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return;
		}

		Comment comment = new Comment(getBaseContext());
		comment.setIdMonument(monument.getId());
		comment.setUser(user);
		comment.setMessage(message);
		comment.setDate(new Date());
		try {
			comment.save();
		} catch (AttributNoValidException e) {
			e.printStackTrace();
			return;
		}

		this.monument.getListeDeCommentaires().add(comment);
		updateListComments();
	}

	private void updateListComments()
	{
		commentairesLinearLayout.removeAllViews();

		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

		if(this.monument.getListeDeCommentaires().size() == 0)
		{
			TextView pasDeMonumentTextView = new TextView(this);
			pasDeMonumentTextView.setText("Il n'y à pas encore de commentaires. Appuyez sur le bouton menu pour en ajouter.");
			commentairesLinearLayout.addView(pasDeMonumentTextView);
			return;
		}

		for (Comment comment : this.monument.getListeDeCommentaires()){           

			LinearLayout llItem = (LinearLayout) inflater.inflate(R.layout.itemlistecommentaires, null);

			TextView pseudo = (TextView) llItem.findViewById(R.itemlistecommentaires.nomUser);
			pseudo.setText(comment.getUser().getLogin());

			TextView message = (TextView) llItem.findViewById(R.itemlistecommentaires.commentaire);
			message.setText(comment.getMessage());

			TextView date = (TextView) llItem.findViewById(R.itemlistecommentaires.date);
			date.setText(formatDate.format(comment.getDate()));

			// To know wich item has been clicked
			llItem.setTag(comment.getId());

			// In the onClickListener just get the id using getTag() on the view
			llItem.setOnLongClickListener(new OnLongClickListener() {

				public boolean onLongClick(View v) {



					return false;
				}
			});
			commentairesLinearLayout.addView(llItem);
		}
	}

	/**
	 * Met l'adresse du moument à jour
	 */
	private void updateAdresse()
	{
		Geocoder gcd = new Geocoder(MonumentPageActivity.this, Locale.getDefault());

		String adresse = "";
		List<Address> addresses;

		try {
			addresses = gcd.getFromLocation((location.getLatitude()),
					(location.getLongitude()), 1);

			if (addresses.size() > 0 && addresses != null) {
				Address addres = addresses.get(0);
				adresse = addres.getAddressLine(0) + ", " + addres.getPostalCode() + " " + addres.getLocality() + ", " + addres.getCountryName() ;
			}
			this.adresseTextView.setText(adresse);

		} catch (IOException e) {
			e.printStackTrace();
		}

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

		this.updateAdresse();
		
		int distancePref = preferences.getPreferenceInt("gps_accuracy");
		if(this.location.hasAccuracy() && this.location.getAccuracy() < distance)
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


	// =================================================================
	// Menu
	// =================================================================
	//Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du téléphone
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		//Création d'un MenuInflater qui va permettre d'instancier un Menu XML en un objet Menu
		MenuInflater inflater = getMenuInflater();
		//Instanciation du menu XML spécifier en un objet Menu
		inflater.inflate(R.menu.menu_monument_page, menu);
		return true;
	}

	//Méthode qui se déclenchera au clic sur un item
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//On regarde quel item a été cliqué grâce à son id et on déclenche une action
		switch (item.getItemId()) {
		case R.menu_monument_page.addComment:

			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Ajouter un commentaire");
			alert.setMessage("Message");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Editable value = input.getText();
					MonumentPageActivity.this.addComment(value.toString());
				}
			});

			alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// Canceled.
				}
			});

			alert.show();
			return true;
			
		case R.menu_monument_page.viewOnMap:
			Intent intent = new Intent(getApplicationContext(), MonumentsMapActivity.class);
			intent.putExtra("idMonument", this.monument.getId());
			startActivity(intent);
			return true;

		}
		return false;
	}


}

package com.android.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import models.Comment;
import models.Monument;
import models.User;
import preferences.Prefs;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
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
import exceptions.AttributNoValidException;
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
	private LinearLayout commentairesLinearLayout;

	private DecimalFormat format;
	private SimpleDateFormat formatDate;

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
		locationManager.requestLocationUpdates(provider, update_time, update_distance, this);
	}

	private void populateViews() {
		this.libelleTextView.setText(monument.getLibelle());
		this.descriptionTextView.setText(monument.getDescription());

		this.updateListComments();
	}

	private void initAtributs()
	{
		format = new DecimalFormat ( ) ; 
		format.setMaximumFractionDigits ( 1 ) ; 
		format.setMinimumFractionDigits ( 0 ) ; 
		format.setDecimalSeparatorAlwaysShown ( true ) ; 
		formatDate = new SimpleDateFormat("dd/MM/yyyy");
	}

	private void initViews() {
		distanceTextView 			= (TextView) findViewById(R.monumentpage.distanceTextView);
		libelleTextView 			= (TextView) findViewById(R.monumentpage.libelleTextView);
		descriptionTextView 		= (TextView) findViewById(R.monumentpage.descriptionTextView);
		commentairesListView        = (ListView) findViewById(R.monumentpage.listView);
		commentairesLinearLayout   	= (LinearLayout) findViewById(R.monumentpage.commentairesLinearLayout);
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


	// =================================================================
	// Menu
	// =================================================================
	//Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du téléphone
	public boolean onCreateOptionsMenu(Menu menu) {

		//Création d'un MenuInflater qui va permettre d'instancier un Menu XML en un objet Menu
		MenuInflater inflater = getMenuInflater();
		//Instanciation du menu XML spécifier en un objet Menu
		inflater.inflate(R.menu.menu_monument_page, menu);
		return true;
	}

	//Méthode qui se déclenchera au clic sur un item
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

		}
		return false;
	}


}

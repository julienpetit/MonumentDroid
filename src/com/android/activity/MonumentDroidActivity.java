package com.android.activity;
import models.User;
import preferences.Prefs;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import exceptions.UserNotFoundException;

public class MonumentDroidActivity extends Activity implements OnClickListener{

	private final String LOG_ID = this.getClass().getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Si il n'y a pas d'utilisateurs enregistrés dans l'application, on affiche le formulaire de création de compte.
		if(User.getNumberOfUsers(this) == 0)
			startActivity(new Intent(this, CreateAccountActivity.class));
		else
			initMain();
	}

	// =================================================================
	// Initialize
	// =================================================================
	private void initMain()
	{
		setContentView(R.layout.main);

		// Récupération de l'utilisateur connecté
		Prefs prefs = new Prefs(getBaseContext());
		
		int idUser = Integer.valueOf(prefs.getPreference("idUser"));
		
		try {
			User user = new User(this, idUser);
			Log.d(LOG_ID, "user : " + user.toString());
			
			TextView titleWelcome = (TextView) findViewById(R.main.title_welcome);
			titleWelcome.setText("Bienvenue, " + user.getLogin());
		} catch (UserNotFoundException e) {
			Toast.makeText(getApplicationContext(), "Utilisateur non trouvé", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, LoginActivity.class));
		}
		
		
		
		
		Button buttonMap = (Button) findViewById(R.main.mapButton);
		buttonMap.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), MonumentsMapActivity.class));
			}
		});
		
		
		Button buttonReport = (Button) findViewById(R.main.declarerButton);
		buttonReport.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), MonumentReportActivity.class));
			}
		});
	}


	// =================================================================
	// Menu
	// =================================================================
	//Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du téléphone
	public boolean onCreateOptionsMenu(Menu menu) {

		//Création d'un MenuInflater qui va permettre d'instancier un Menu XML en un objet Menu
		MenuInflater inflater = getMenuInflater();
		//Instanciation du menu XML spécifier en un objet Menu
		inflater.inflate(R.menu.menu_monument_droid, menu);
		return true;
	}

	//Méthode qui se déclenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		//On regarde quel item a été cliqué grâce à son id et on déclenche une action
		switch (item.getItemId()) {
		case R.menu_monument_droid.goToPreferences:
			startActivity(new Intent(this, PreferencesActivity.class));
			return true;
			
		case R.menu_monument_droid.switch_account:
			startActivity(new Intent(this, LoginActivity.class));
			return true;
		}
		return false;
	}

	// =================================================================
	// Return Button
	// =================================================================
	@Override
	public void onBackPressed() {
		Log.d(LOG_ID, "onBackPressed Called");
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent); 
		return;
	}

	// =================================================================
	// Events
	// =================================================================
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}   
}
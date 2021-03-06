package com.iutbm.monumentdroid.activity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.iutbm.monumentdroid.exceptions.UserNotFoundException;
import com.iutbm.monumentdroid.models.User;
import com.iutbm.monumentdroid.preferences.Prefs;



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

		

		try {
			int idUser = Integer.valueOf(prefs.getPreference("idUser"));
			
			User user = new User(this, idUser);
			Log.d(LOG_ID, "user : " + user.toString());

			TextView titleWelcome = (TextView) findViewById(R.main.title_welcome);
			titleWelcome.setText("Bienvenue, " + user.getLogin());
		} catch (UserNotFoundException e) {
			Toast.makeText(getApplicationContext(), "Utilisateur non trouvé", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, LoginActivity.class));
		}


		final Button buttonMap = (Button) findViewById(R.main.mapButton);
		buttonMap.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
//				buttonMap.setBackgroundColor(0);
				
				if(isOnline())
					startActivity(new Intent(getBaseContext(), MonumentsMapActivity.class));
				else 
					Toast.makeText(getApplicationContext(), "Une connexion internet est requise pour accèder à la carte.", Toast.LENGTH_SHORT).show();
			}
		});


		Button buttonReport = (Button) findViewById(R.main.declarerButton);
		buttonReport.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), MonumentReportActivity.class));
			}
		});

		Button buttonList = (Button) findViewById(R.main.listButton);
		buttonList.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), MonumentListActivity.class));

			}
		});

		Button buttonQuit = (Button) findViewById(R.main.quit);
		buttonQuit.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent setIntent = new Intent(Intent.ACTION_MAIN);
				setIntent.addCategory(Intent.CATEGORY_HOME);
				setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(setIntent); 
			}

		});
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
		inflater.inflate(R.menu.menu_monument_droid, menu);
		return true;
	}

	//Méthode qui se déclenchera au clic sur un item
	@Override
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

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}
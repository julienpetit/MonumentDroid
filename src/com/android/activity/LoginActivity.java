package com.android.activity;
import java.util.ArrayList;
import java.util.HashMap;

import preferences.Prefs;

import models.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import exceptions.UserNotFoundException;

public class LoginActivity extends Activity {

	@SuppressWarnings("unused")
	private final String LOG_ID = this.getClass().getName();

	private ArrayList<User> listOfUsers;
	private ListView listViewUsers;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initActivity();

	}

	private void initActivity()
	{
		setContentView(R.layout.login);
		initListView();
	}


	private void initListView()
	{
		try {
			listOfUsers = User.getAllUsers(this);
		} catch (UserNotFoundException e) {
			Toast.makeText(getApplicationContext(), "Un utilisateur n'a pas été trouvé", Toast.LENGTH_LONG).show();
		}
		Log.d(LOG_ID, "nb users : " + listOfUsers.size());

		//Récupération de la listview créée dans le fichier main.xml
		listViewUsers = (ListView) findViewById(R.login.listViewUsers);




		//Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichageitem
		SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), getHashMapOfUser(), R.layout.listview_item_user,
				new String[] {"login", "date"}, new int[] {R.login.loginUser, R.login.dateInscription});

		//On attribut à notre listView l'adapter que l'on vient de créer
		listViewUsers.setAdapter(mSchedule);

		//Enfin on met un écouteur d'évènement sur notre listView
		listViewUsers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
				//on récupère la HashMap contenant les infos de notre item (titre, description, img)
				HashMap<String, String> map = (HashMap<String, String>) listViewUsers.getItemAtPosition(position);

				//on créer une boite de dialogue
				AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);


				adb.setTitle("Connexion");

				adb.setMessage("Entrez votre mot de passe");
				final EditText input = new EditText(getBaseContext());
				adb.setView(input);

				adb.setPositiveButton("Connexion", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String password = input.getText().toString();
						
						if(LoginActivity.this.listOfUsers.get(position).getMdp().equals(password))
						{
							LoginActivity.this.connection(position);
						}
						else
						{
							Toast.makeText(getApplicationContext(), "Mot de passe incorrect.", Toast.LENGTH_LONG).show();
						}
							
					}
				});
				adb.setNegativeButton("Annuler", null);
				adb.show();
			}
		});

	}

	private ArrayList<HashMap<String, String>> getHashMapOfUser()
	{
		//Création de la ArrayList qui nous permettra de remplire la listView
		ArrayList<HashMap<String, String>> listOfUsersHashMap = new ArrayList<HashMap<String, String>>();

		for(User user : listOfUsers)
		{
			HashMap<String, String> map = new HashMap<String, String>();

			map.put("login", user.getLogin());
			map.put("date", user.getDateInscription().toGMTString());
			listOfUsersHashMap.add(map);
		}

		return listOfUsersHashMap;
	}
	
	private void connection(int positionUser)
	{
		User user = listOfUsers.get(positionUser);
		
		// Ajout de l'utilisateur aux préférences
		Prefs prefs = new Prefs(getBaseContext());
		prefs.setPreference("idUser", user.getId() + "");
		
		goToHome();
	}

    private void goToHome()
    {
    	startActivity(new Intent(this, MonumentDroidActivity.class));
    }
    
    
	// =================================================================
	// Menu
	// =================================================================
	//Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du téléphone
	public boolean onCreateOptionsMenu(Menu menu) {

		//Création d'un MenuInflater qui va permettre d'instancier un Menu XML en un objet Menu
		MenuInflater inflater = getMenuInflater();
		//Instanciation du menu XML spécifier en un objet Menu
		inflater.inflate(R.menu.menu_login, menu);
		return true;
	}

	//Méthode qui se déclenchera au clic sur un item
	public boolean onOptionsItemSelected(MenuItem item) {
		//On regarde quel item a été cliqué grâce à son id et on déclenche une action
		switch (item.getItemId()) {
			
		case R.menu_login.create_account:
			startActivity(new Intent(this, CreateAccountActivity.class));
			return true;
		}
		return false;
	}
}
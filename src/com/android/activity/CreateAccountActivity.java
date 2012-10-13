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


public class CreateAccountActivity extends Activity {
    
	@SuppressWarnings("unused")
	private final String LOG_ID = this.getClass().getName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initCreateAccount();
    }
    
    
    
    private void initCreateAccount()
    {
    	setContentView(R.layout.createaccount);
    	
    	Button buttonValidateForm = (Button) findViewById(R.createAccount.validate_account);
    	buttonValidateForm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Récupération du logion et du mot de passe
				TextView loginTextView = (TextView) findViewById(R.createAccount.input_createaccount_login);
				TextView passwordTextView = (TextView) findViewById(R.createAccount.input_createaccount_password);
				
				if(loginTextView.getText().length() == 0)
				{
			        Toast.makeText(getApplicationContext(), "Nom d'utilisateur obligatoire", Toast.LENGTH_LONG).show();  
			        return;
				}
				if(passwordTextView.getText().length() == 0)
				{
			        Toast.makeText(getApplicationContext(), "Mot de passe obligatoire", Toast.LENGTH_LONG).show(); 
			        return;
				}
				
				User newUser = new User(getApplicationContext());
				newUser.setLogin(loginTextView.getText().toString());
				newUser.setMdp(passwordTextView.getText().toString());
				newUser.save();
				
				// Ajout de l'utilisateur aux préférences
				Prefs prefs = new Prefs(getBaseContext());
				prefs.setPreference("idUser", newUser.getId() + "");
				
				Log.d(LOG_ID, "idUser Pref " + prefs.getPreference("idUser"));
				
				goToHome();
			}
		});
    }
    
    private void goToHome()
    {
    	startActivity(new Intent(this, MonumentDroidActivity.class));
    }
}
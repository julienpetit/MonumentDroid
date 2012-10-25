package com.android.activity;

import models.Monument;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import exceptions.CommentNotFoundException;
import exceptions.MonumentNotFoundException;

public class MonumentPageActivity extends Activity {

	private Monument monument;

	private TextView distanceTextView;
	private TextView libelleTextView;
	private TextView descriptionTextView;
	private ListView commentairesListView;
	
	private Button ajouterCommentaireButton;
	
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
		}
		
		
		this.initViews();
		this.populateViews();

	}
	
	private void populateViews() {
		this.libelleTextView.setText(monument.getLibelle());
		this.descriptionTextView.setText(monument.getDescription());
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
}

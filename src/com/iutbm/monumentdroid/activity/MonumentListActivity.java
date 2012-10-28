package com.iutbm.monumentdroid.activity;


import java.util.ArrayList;

import com.iutbm.monumentdroid.adapters.AdapterListMonuments;
import com.iutbm.monumentdroid.exceptions.CommentNotFoundException;
import com.iutbm.monumentdroid.exceptions.MonumentNotFoundException;
import com.iutbm.monumentdroid.models.Comment;
import com.iutbm.monumentdroid.models.Monument;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class MonumentListActivity extends Activity {
	
	@SuppressWarnings("unused")
	private final String LOG_ID = this.getClass().getName();
	ListView list;

	/**
	 * Called when the activity is first created. 
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listmonuments);

		// On récupère la vue ListView
		list = (ListView) this.findViewById(R.listmonument.listView);
		
		ArrayList<Monument> listDeMonuments = new ArrayList<Monument>();
		try {
			listDeMonuments = Monument.getAllMonuments(getBaseContext(), 0, null);
		} catch (MonumentNotFoundException e) {
			Log.d(LOG_ID, e.toString());
		} catch (CommentNotFoundException e) {
			Log.d(LOG_ID, e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// On place les monuments dans la listView
		final AdapterListMonuments adapter = new AdapterListMonuments(getBaseContext(), listDeMonuments);
		list.setAdapter(adapter);

		// Click sur un monument
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getBaseContext(), MonumentPageActivity.class);
				intent.putExtra("idMonument", ((Monument) parent.getItemAtPosition(position)).getId());
				startActivity(intent);
			}
		});
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}

package com.android.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MonumentDroidActivity extends Activity {
    
	private final String LOG_ID = "MonumentDroidActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button buttonMap = (Button) findViewById(R.id.map);
        
        
        buttonMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), MonumentsMapActivity.class));
				
				
			}
		});
        
        
        TextView textView = (TextView) findViewById(R.id.user);
        textView.setText("bonjour");
        
        
//        Users julien = new Users(this);
//        julien.setLogin("patrocle");
//        julien.setMdp("azerty");
//        julien.save();
        
//        User user = null;
//        try {
//			user = new User(this, 3);
//			
//			
//		} catch (UserNotFoundException e) {
//			Log.d(LOG_ID, "L'utilisateur n'existe pas");
//		} catch (Exception e) {
//			Log.d(LOG_ID, e.getMessage());
//		}
//        
//        textView.setText(user.toString());
    }
}
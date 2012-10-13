package com.android.activity;
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

import com.android.models.User;

public class LoginActivity extends Activity {
    
	@SuppressWarnings("unused")
	private final String LOG_ID = this.getClass().getName();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        initActivity();
        
    }
    
    private void initActivity()
    {
    	setContentView(R.layout.login);
    	
    }
    
    

}
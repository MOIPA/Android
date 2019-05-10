package com.example.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Regesiter extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regesiter);
	}
	
	public void register(View v){
		
		EditText username = (EditText)findViewById(R.id.r_username);
		EditText userpassword = (EditText)findViewById(R.id.r_userpassword);
		
		String name = username.getText().toString().trim();
		String pwd = userpassword.getText().toString().trim();
		
		ManageUser mu = new ManageUser(getApplicationContext());
		mu.add(new User(name,pwd));
		
		Intent intent = new Intent(getApplicationContext(),MainActivity.class);
		startActivity(intent);
		
	}
}

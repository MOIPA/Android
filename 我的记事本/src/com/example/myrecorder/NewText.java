package com.example.myrecorder;

import database.MyOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewText extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_text);
		
		Button submit = (Button) findViewById(R.id.submit);
		submit.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EditText et_thing = (EditText) findViewById(R.id.et_things);
				EditText et_time = (EditText) findViewById(R.id.et_time);
				String thing = et_thing.getText().toString().trim();
				String time = et_time.getText().toString().trim();
				
				if(thing.equals("")||time.equals("")){
					Toast.makeText(getApplicationContext(), "пео╒спнС", Toast.LENGTH_SHORT);
				}
				
				MyOpenHelper mh = new MyOpenHelper(getApplicationContext());
				SQLiteDatabase db = mh.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("thing", thing);
				values.put("time", time);
				db.insert("thing", null, values);
				db.close();
				mh.close();
				
				Intent intent = new Intent(NewText.this,MainActivity.class);
				startActivity(intent);
			}
		});
		
	}

	
}

package com.example.androidprovider;

import android.support.v7.app.ActionBarActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        MyOpenHelper helper = new MyOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("user", new String[]{"name","phone"}, "phone=?", new String[]{"123"}, null, null, null);
        
        if(cursor.getCount()!=0&&cursor!=null){
        	while(cursor.moveToNext()){
        		System.out.println(cursor.getString(0));
        		System.out.println(cursor.getString(1));
        	}
        }
        db.close();
        
        
    }


}

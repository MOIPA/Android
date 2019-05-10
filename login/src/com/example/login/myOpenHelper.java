package com.example.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class myOpenHelper extends SQLiteOpenHelper {

	public myOpenHelper(Context context) {
		super(context, "user", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("create table user (id integer primary key autoincrement,name varchar(20),password varchar(20))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

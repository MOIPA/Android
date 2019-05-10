package com.example.androidprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

	public MyOpenHelper(Context context) {
		super(context, "Account", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table user(name varchar(20),phone varchar(20))");
		db.execSQL("insert into user(name,phone)values('tr','117')");
		db.execSQL("insert into user(name,phone)values('tzq','115')");
		db.execSQL("insert into user(name,phone)values('test','123')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}

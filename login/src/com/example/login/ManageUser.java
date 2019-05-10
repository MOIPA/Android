package com.example.login;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ManageUser {
	
	private Context context;
	
	public ManageUser(Context context){
		this.context = context;
	}

	myOpenHelper db = new myOpenHelper(context);
	
	public int add(User user){
		
		myOpenHelper db = new myOpenHelper(context);
		SQLiteDatabase datebases = db.getWritableDatabase();
		
		ContentValues values =  new ContentValues();
		values.put("name", user.getName());
		values.put("password", user.getPassword());
		long insert = datebases.insert("user", null, values);
		db.close();
		
		if(insert>0)return 1;
		else return 0;
	}
	
	public int delete(String name){
		
		return 0;
	}
	
	public int update(User user,String name, String password){
		
		return 0;
	}
	
	public List<User> reterive(){
		List<User> lists = new ArrayList<User>();
		myOpenHelper db = new myOpenHelper(context);
		SQLiteDatabase database = db.getReadableDatabase();
		Cursor cursor = database.rawQuery("select * from user", null);
		
		while(cursor.moveToNext()){
			String name = cursor.getString(1);
			String pwd = cursor.getString(2);
			lists.add(new User(name,pwd));
		}
		
		return lists;
	}
	
}	




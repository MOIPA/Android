package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

	public MyOpenHelper(Context context) {
		super(context, "thing", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table thing(thing varchar(40),time varchar(10))");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	

}

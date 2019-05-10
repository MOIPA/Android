package com.example.androidprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AccountProvider extends ContentProvider{
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int QUERYSUCCESS = 0;
	private MyOpenHelper helper;
	
	static{
		sURIMatcher.addURI("com.example.provider", "query", QUERYSUCCESS);
	}

	@Override
	public boolean onCreate() {
		helper = new MyOpenHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		int code = sURIMatcher.match(uri);
		if(code==QUERYSUCCESS){
			SQLiteDatabase db = helper.getWritableDatabase();
			Cursor cursor = db.query("user", projection, selection, selectionArgs, null, null, sortOrder);
			return cursor;
		}else{
			throw new IllegalArgumentException("wrong path");
		}
		
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}

package com.example.tr.instantcool2.LocalDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by trlvtzq on 2017/11/27.
 */

public class MySqlite extends SQLiteOpenHelper {
    public MySqlite(Context context) {
        super(context, "userIcon", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table UserInfo(" +
                "name varchar(20)" +
                ",pwd varchar(20)"+
                ",isFirstLogin integer"+
                ")");
        db.execSQL("insert into table ICON(id)values(1,2,3,4,5)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.sky;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ServiceSQLiteOpenHelper extends SQLiteOpenHelper {

	public ServiceSQLiteOpenHelper(Context context){
		super(context, ServiceDef.DBNAME, null, ServiceDef.VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ServiceDef.VOTETNAME + "(" + 
        		ServiceDef.VOTEID + " integer primary key autoincrement not null," +  
        		ServiceDef.VOTENAME + " text not null);"); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}

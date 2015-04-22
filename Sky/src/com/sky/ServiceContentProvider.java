package com.sky;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ServiceContentProvider extends ContentProvider {
	
	private ServiceSQLiteOpenHelper serviceSQLiteOpenHelper = null;
	
	private static final UriMatcher sMatcher;
	static {
		sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sMatcher.addURI(ServiceDef.AUTOHORITY, ServiceDef.VOTETNAME, ServiceDef.VOTE_ITEM); 
		sMatcher.addURI(ServiceDef.AUTOHORITY, ServiceDef.VOTETNAME + "/#", ServiceDef.VOTE_ITEM_ID); 
	}
	
	public ServiceContentProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = serviceSQLiteOpenHelper.getWritableDatabase(); 
        int count = 0; 
        switch (sMatcher.match(uri)) { 
        case ServiceDef.VOTE_ITEM:
        	count = db.delete(ServiceDef.VOTETNAME, selection, selectionArgs); 
            break; 
        case ServiceDef.VOTE_ITEM_ID:
        	String id = uri.getPathSegments().get(1);
        	count = db.delete(ServiceDef.VOTETNAME, ServiceDef.VOTEID + "=" + id, selectionArgs); 
            break; 
        default:
        	throw new IllegalArgumentException("Unknown URI"+uri); 
        } 
        
        getContext().getContentResolver().notifyChange(uri, null); 
        return count;
	}

	@Override
	public String getType(Uri uri) {
        switch (sMatcher.match(uri)) { 
        case ServiceDef.VOTE_ITEM:
        	return ServiceDef.VOTE_ITEM_TYPE;
        case ServiceDef.VOTE_ITEM_ID:
        	return ServiceDef.VOTE_ITEM_ID_TYPE;
        default:
        	throw new IllegalArgumentException("Unknown URI"+uri); 
        } 
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = serviceSQLiteOpenHelper.getWritableDatabase(); 
        long rowId; 
        if(sMatcher.match(uri) != ServiceDef.VOTE_ITEM){ 
                throw new IllegalArgumentException("Unknown URI"+uri); 
        } 
        
        rowId = db.insert(ServiceDef.VOTETNAME ,ServiceDef.VOTEID, values);
        if(rowId > 0) {
        	Uri noteUri = ContentUris.withAppendedId(ServiceDef.VOTE_URI, rowId);
        	getContext().getContentResolver().notifyChange(noteUri, null); 
            return noteUri;
        }
        
        throw new IllegalArgumentException("Unknown URI" + uri);
	}

	@Override
	public boolean onCreate() {
		serviceSQLiteOpenHelper = new ServiceSQLiteOpenHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = serviceSQLiteOpenHelper.getWritableDatabase();                 
        Cursor c; 
        switch (sMatcher.match(uri)) { 
        case ServiceDef.VOTE_ITEM:
        	c = db.query(ServiceDef.VOTETNAME, projection, selection, selectionArgs, null, null, null); 
            break; 
        case ServiceDef.VOTE_ITEM_ID: 
            String id = uri.getPathSegments().get(1); 
            c = db.query(ServiceDef.VOTETNAME, projection, ServiceDef.VOTEID + "=" + id, selectionArgs, null, null, sortOrder); 
        break; 
        default:
        	throw new IllegalArgumentException("Unknown URI"+uri); 
        } 
        c.setNotificationUri(getContext().getContentResolver(), uri); 
        return c; 
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}
}
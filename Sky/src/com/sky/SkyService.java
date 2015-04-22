package com.sky;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;

public class SkyService extends Service {
	
	private Bitmap mbitmap = null;
	private static SkyService sSkyService = null;
	
	public SkyService() {
		sSkyService = SkyService.this;
	}
	
	public static SkyService getInstance() {
		return sSkyService;
	}
	
	public class SkyBinder extends AIDLSkyService.Stub {
		@Override
		public void show(String showText) throws RemoteException {
			SkyService.this.show(showText);
		}
		
		@Override
		public void insertVote(Vote vote) throws RemoteException {
			if (null != vote)
			{
		        ContentValues values = new ContentValues(); 
		        values.put(ServiceDef.VOTENAME, vote.voteName);
		        SkyService.this.getContentResolver().insert(ServiceDef.VOTE_URI, values);
			}
		}

		@Override
		public void remoteAllVote() throws RemoteException {
			SkyService.this.getContentResolver().delete(ServiceDef.VOTE_URI, null, null);
		}

		@Override
		public List<Vote> getAllVote() throws RemoteException {
			List<Vote> list = new ArrayList<Vote>();
			
			ContentResolver contentResolver = SkyService.this.getContentResolver();
			Cursor cursor = contentResolver.query(ServiceDef.VOTE_URI, new String[] {ServiceDef.VOTEID, ServiceDef.VOTENAME}, null, null, null); 
			while (cursor.moveToNext()) {
				Vote vote = new Vote();
				vote.voteId = cursor.getInt(cursor.getColumnIndex(ServiceDef.VOTEID));
				vote.voteName = cursor.getString(cursor.getColumnIndex(ServiceDef.VOTENAME));
				list.add(vote);
	        }
			
			cursor.close();
			
			return list;
		}
		
		@Override
		public void setBitmap(Bitmap bitmap) throws RemoteException {
			mbitmap = bitmap;
		}

		@Override
		public Bitmap getBitmap() throws RemoteException {
			return mbitmap;
		}

		public SkyService getService() {
			return SkyService.this;
		}

		@Override
		public Bitmap loadUrlBitmap(String strUrl) throws RemoteException {
			URL myFileUrl = null;   
			Bitmap bitmap = null;
			try
			{
				myFileUrl = new URL(strUrl);
			}
		  	catch (MalformedURLException e)
		  	{
		  		e.printStackTrace();
		  	}
			
			try
			{
				HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
		  		   
				//BitmapFactory.Options opts = new BitmapFactory.Options();
				//opts.inSampleSize = 4;
				//bitmap = BitmapFactory.decodeStream(is, null, opts);
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			return bitmap;
		}
	}
	
	private SkyBinder mBinder = new SkyBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public void show(String showText) {
		Intent intent = new Intent(Def.SHOWACTIVITY_ACTION_STRING);
		intent.putExtra(Def.SHOW_STRING, showText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}

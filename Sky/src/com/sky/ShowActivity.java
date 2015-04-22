package com.sky;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class ShowActivity extends Activity {
	
	private AIDLSkyService mAidlSkyService = null;
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mAidlSkyService = null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mAidlSkyService = AIDLSkyService.Stub.asInterface(service);
			Bitmap bitmap = null;
			try {
				bitmap = mAidlSkyService.getBitmap();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != bitmap) {
				RelativeLayout layout = (RelativeLayout)findViewById(R.id.show_layout);
				BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
				layout.setBackgroundDrawable(bd);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);
		
		// Start service.
		Intent intent = new Intent(Def.SKYSERVICE_ACTION_STRING);
		bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
		
		// extends binder
		((Button)findViewById(R.id.finish)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onFinishClickListener();
			}
		});

		intent = getIntent();
		((EditText)findViewById(R.id.showText)).setText(intent.getStringExtra(Def.SHOW_STRING));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		unbindService(mServiceConnection);
		super.onDestroy();
	}

	private void onFinishClickListener() {
		finish();
	};

}

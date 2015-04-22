package com.sky;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SkyActivity extends Activity {
	
	private Messenger mMessenger = null;
	
	private SkyService mSkyService = null;
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mSkyService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mSkyService = ((SkyService.SkyBinder)service).getService();
		}
	};
	
	private ServiceConnection mMessagerServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mMessenger = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mMessenger = new Messenger(service);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sky);
		
		// Start service.
		Intent intent = new Intent(Def.SKYSERVICE_ACTION_STRING);
		startService(intent);
		bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
		
		intent = new Intent(Def.SKYMESSAGERSERVICE_ACTION_STRING);
		bindService(intent, mMessagerServiceConnection, BIND_AUTO_CREATE);
		
		////////////////////////////////////////////////////////////////////////////////////
		
		// intent
		((Button)findViewById(R.id.intent)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onIntentClickListener();
			}
		});
		
		// broadcast
		((Button)findViewById(R.id.broadcast)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBroadcastClickListener();
			}
		});
		
		// extends binder
		((Button)findViewById(R.id.extends_binder)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onExtendsbinderClickListener();
			}
		});
		
		// messager
		((Button)findViewById(R.id.messager)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onMessagerClickListener();
			}
		});
		
		// static method
		((Button)findViewById(R.id.static_method)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onStaticmethodClickListener();
			}
		});
		
		// moon
		((Button)findViewById(R.id.moon)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onMoonClickListener();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sky, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		unbindService(mServiceConnection);
		unbindService(mMessagerServiceConnection);
		Intent intent = new Intent(Def.SKYSERVICE_ACTION_STRING);
		stopService(intent);
		
		super.onDestroy();
	}

	private void onIntentClickListener() {
		Intent intent = new Intent(Def.SHOWACTIVITY_ACTION_STRING);
		intent.putExtra(Def.SHOW_STRING, ((EditText)findViewById(R.id.editShowText)).getText().toString());
		startActivity(intent);
	};
	
	private void onBroadcastClickListener() {
		Intent intent = new Intent(Def.BROADCAST_SHOWSTATIC_STRING);
		intent.putExtra(Def.SHOW_STRING, ((EditText)findViewById(R.id.editShowText)).getText().toString());
		sendBroadcast(intent);
	};
	
	private void onExtendsbinderClickListener() {
		mSkyService.show(((EditText)findViewById(R.id.editShowText)).getText().toString());
	};

	private void onMessagerClickListener() {
		Message msg = Message.obtain();
		msg.what = Def.MSG_SHOW;
		Bundle bundle = new Bundle();
		bundle.putString(Def.SHOW_STRING, ((EditText)findViewById(R.id.editShowText)).getText().toString());
		msg.obj = bundle;
		try {
			mMessenger.send(msg);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	
	private void onStaticmethodClickListener() {
		SkyService.getInstance().show(((EditText)findViewById(R.id.editShowText)).getText().toString());
	}
	
	private void onMoonClickListener() {
		Intent intent = new Intent(Def.MOONACTIVITY_ACTION_STRING);
		startActivity(intent);
	}
}

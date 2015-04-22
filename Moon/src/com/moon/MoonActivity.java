package com.moon;

import java.util.List;

import com.sky.AIDLSkyService;
import com.sky.Def;
import com.sky.Image;
import com.sky.Vote;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MoonActivity extends Activity {
	
	private Messenger mMessenger = null;
	
	private AIDLSkyService mAidlSkyService = null;
	
	private Bitmap mBitmap = null;
	
	private Handler mHandler = new Handler() {

		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg) {
			BitmapDrawable bd = new BitmapDrawable(getResources(), mBitmap);
			((RelativeLayout)findViewById(R.id.moon_layout)).setBackgroundDrawable(bd);
			
			super.handleMessage(msg);
		}
		
	};
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mAidlSkyService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mAidlSkyService = AIDLSkyService.Stub.asInterface(service);
			
			try {
				mAidlSkyService.setBitmap(null);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
        setContentView(R.layout.activity_moon);
        
		// Start service.
		Intent intent = new Intent(Def.SKYSERVICE_ACTION_STRING);
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
		
		// messager
		((Button)findViewById(R.id.messager)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onMessagerClickListener();
			}
		});
		
		// static method
		((Button)findViewById(R.id.aidl)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onAidlClickListener();
			}
		});
		
		////////////////////////////////////////////////////////////// ContentProvider
		
		// insert
		((Button)findViewById(R.id.insert)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onInsertClickListener();
			}
		});
		
		// delete
		((Button)findViewById(R.id.delete)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onDeleteClickListener();
			}
		});
		
		// show vote
		((Button)findViewById(R.id.show_vote)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onShowVoteClickListener();
			}
		});
		
		////////////////////////////////////////////////////////////// Send bitmap
		
		((Button)findViewById(R.id.intent_bitmap)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Image img = new Image();
				img.bitmap = mBitmap;
				
				Intent intent = new Intent(Def.BROADCAST_SHOWSTATIC_STRING);
				intent.putExtra(Def.SHOW_STRING, ((EditText)findViewById(R.id.editShowText)).getText().toString());
				intent.putExtra("Image", img);
				sendBroadcast(intent);
			}
		});
		
		((Button)findViewById(R.id.aidl_bitmap)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					mAidlSkyService.setBitmap(mBitmap);
					Intent intent = new Intent(Def.SHOWACTIVITY_ACTION_STRING);
					intent.putExtra(Def.SHOW_STRING, ((EditText)findViewById(R.id.editShowText)).getText().toString());
					startActivity(intent);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		((Button)findViewById(R.id.aidl_contentprovider_bitmap)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		
		((Button)findViewById(R.id.load_url_bitmap)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	new Thread()
		    	{
		    		@Override
		    	    public void run() {
		    			Bitmap urlBitmap = null;
						try {
							urlBitmap = mAidlSkyService.loadUrlBitmap(((EditText)findViewById(R.id.urlEditText)).getText().toString());
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
		    			if (null == urlBitmap)
		    			{
		    				Log.d("Load url bitmap", "Error");
		    			}
		    			else
		    			{
		    				Log.d("Load url bitmap", "Sucess");
		    				mBitmap = urlBitmap;
		    				mHandler.sendEmptyMessage(0);
		    			}
		    	    }
		    	}.start();
			}
		});
		
		//////////////////////////////////////////////////////////////////////////////////////
		
		// sky
		((Button)findViewById(R.id.sky)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSkyClickListener();
			}
		});
		
		//////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_moon, menu);
        return true;
    }
    
	@SuppressWarnings("deprecation")
	@Override
	protected void onStart() {
		mBitmap = null;
		BitmapDrawable bd = null;
		((RelativeLayout)findViewById(R.id.moon_layout)).setBackgroundDrawable(bd);
		
		try {
			if (null != mAidlSkyService)
			{
				mAidlSkyService.setBitmap(mBitmap);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		unbindService(mServiceConnection);
		unbindService(mMessagerServiceConnection);
		
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
	
	private void onAidlClickListener() {
		try {
			mAidlSkyService.show(((EditText)findViewById(R.id.editShowText)).getText().toString());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void onInsertClickListener() {
		Vote vote = new Vote();
		vote.voteName = ((EditText)findViewById(R.id.editShowText)).getText().toString();
		try {
			mAidlSkyService.insertVote(vote);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void onDeleteClickListener() {
		try {
			mAidlSkyService.remoteAllVote();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void onShowVoteClickListener() {
		try {
			List<Vote> list = mAidlSkyService.getAllVote();
			for (int i = 0; i < list.size(); i++) {
				Toast.makeText(MoonActivity.this, list.get(i).voteId + "." + list.get(i).voteName, Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void onSkyClickListener() {
		Intent intent = new Intent(Def.SKYACTIVITY_ACTION_STRING);
		startActivity(intent);
	}
}

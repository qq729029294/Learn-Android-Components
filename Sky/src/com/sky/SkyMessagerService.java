package com.sky;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;

@SuppressLint("HandlerLeak")
public class SkyMessagerService extends Service {
	public SkyMessagerService() {
	}
	
	private Messenger mMessengerr = new Messenger(new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == Def.MSG_SHOW) {
				SkyMessagerService.this.show(((Bundle)msg.obj).getString(Def.SHOW_STRING));
			}
		};
	});
	
	public void show(String showText) {
		Intent intent = new Intent(Def.SHOWACTIVITY_ACTION_STRING);
		intent.putExtra(Def.SHOW_STRING, showText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mMessengerr.getBinder();
	}
}
package com.sky;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShowStaticReceiver extends BroadcastReceiver {
	public ShowStaticReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intentShow = new Intent(Def.SHOWACTIVITY_ACTION_STRING);
		intentShow.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intentShow.putExtra(Def.SHOW_STRING, intent.getStringExtra(Def.SHOW_STRING));
		context.startActivity(intentShow);
	}
}
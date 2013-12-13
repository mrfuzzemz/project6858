package com.privacy.sandbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NameReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context arg0, Intent arg1) {		
		String appName = arg1.getExtras().getString("name");
		String broadcastLabel = arg1.getExtras().getString("broadcastLabel");
		
		Toast.makeText(arg0, "Sandbox received app name " + appName + " label " + broadcastLabel, Toast.LENGTH_SHORT).show();

		AppRecord newRecord = MainActivity.addAppRecord(appName, broadcastLabel);
				
		if (newRecord != null){
			Toast.makeText(arg0, newRecord.toString(), Toast.LENGTH_SHORT).show();
		}
	}
}
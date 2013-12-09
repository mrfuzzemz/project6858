package com.privacy.sandbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RequestReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context arg0, Intent arg1) {		
		String request = arg1.getExtras().getString("request");
		String appName = arg1.getExtras().getString("name");
		String data = "";

		Toast.makeText(arg0, "Sandbox received request for " + request, Toast.LENGTH_SHORT).show();

		Permission perm = MainActivity.getPermission(appName, request);
	
		if (perm != null){
			data = perm.toString();
		} else {
			data = "no perm set";
		}

		Intent i = new Intent();
		i.putExtra("data", data);
		i.setAction("com.privacy.sandbox.SEND_VALUE");

		arg0.sendBroadcast(i);		
	}

}
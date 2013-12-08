package com.privacy.sandbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class RequestReceiver extends BroadcastReceiver {

	 @Override
	 public void onReceive(Context arg0, Intent arg1) {
		String request = arg1.getExtras().getString("request");
		String data = "";
		
		Toast.makeText(arg0, "Sandbox received request for " + request, Toast.LENGTH_LONG).show();
		
		if (request.equals("location")){
			data = "location value!";
		} else{
			data = "something else!";
		}
		
		Intent i = new Intent();
		i.putExtra("data", data);
		i.setAction("com.privacy.sandbox.SEND_VALUE");

		arg0.sendBroadcast(i, "com.privacy.sandbox.SANDBOX_LOCATION");
	 }

}
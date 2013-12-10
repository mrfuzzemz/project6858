package com.privacy.sandbox;

import java.util.Random;

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

		if (perm.toString().contains("Real")){
			//grab real value and send that along
			if (request.equals("location")){
				// grab location
				data = "Cambridge, MA";
			} else if (request.equals("profile")){
				// grab profile
				data = MainActivity.getUserName();
			} else if (request.equals("imei")){
				// grab imei
				data = MainActivity.getPhoneIMEI();
			} else if (request.equals("carrier")){
				// grab carrier
				data = MainActivity.getCarrierName();
			}
		}
		else if (perm.toString().contains("Bogus")){
			//send bogus data along
			Random rand = new Random();
			data = String.valueOf(rand.nextInt());	
		}
		else if (perm.toString().contains("Custom:")){
			// Send custom value (Cut off "Custom: ")
			data = data.substring(MainActivity.CUSTOM_OFFSET);
			// data = data;
		}
		
		
		
		
		Intent i = new Intent();
		i.putExtra("data", data);
		i.setAction("com.privacy.sandbox.SEND_VALUE");

		arg0.sendBroadcast(i);		
	}

}
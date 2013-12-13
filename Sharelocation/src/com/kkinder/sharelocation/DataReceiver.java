package com.kkinder.sharelocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;

public class DataReceiver extends BroadcastReceiver {

		 @Override
		 public void onReceive(Context arg0, Intent arg1) {
			String data = arg1.getExtras().getString("data");
			Log.d("test",data);
			String[] coords = data.split(";");
			if(coords.length != 2) {
				Toast.makeText(arg0, "Please set permissions for Sharelocation in Sandbox", Toast.LENGTH_LONG).show();
			} else {
				Sharelocation.lastLocation = new GeoPoint(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
				Sharelocation.haveLocation = true;
				Toast.makeText(arg0, "ShareLocation received " + data, Toast.LENGTH_LONG).show();
			}
		 }

}

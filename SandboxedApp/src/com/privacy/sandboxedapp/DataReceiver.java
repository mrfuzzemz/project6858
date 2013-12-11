package com.privacy.sandboxedapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DataReceiver extends BroadcastReceiver {

		 @Override
		 public void onReceive(Context arg0, Intent arg1) {
			String data = arg1.getExtras().getString("data");
			String name = arg1.getExtras().getString("name");

			if (name.equals(SandboxedMainActivity.APP_NAME)){
				Toast.makeText(arg0, "SandboxedApp received " + data, Toast.LENGTH_LONG).show();
			}
		 }

}

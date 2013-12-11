package com.privacy.sandboxedapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;


public class SandboxedMainActivity extends Activity {
	private static final String APP_NAME = "AppA";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sandboxed_main);
		
		Intent i = new Intent();
		i.putExtra("name", APP_NAME);
		i.setAction("com.privacy.sandbox.SEND_NAME");

		sendBroadcast(i);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sandboxed_main, menu);
		return true;
	}

	public void requestLocation(View view) {
		Intent i = new Intent();
		i.putExtra("request", "location");
		i.putExtra("name", APP_NAME);
		i.setAction("com.privacy.sandbox.REQUEST_VALUE");

		sendBroadcast(i);		
	}
	
	public void requestProfile(View view) {
		Intent i = new Intent();
		i.putExtra("request", "profile");
		i.putExtra("name", APP_NAME);
		i.setAction("com.privacy.sandbox.REQUEST_VALUE");

		sendBroadcast(i);
	}
	
	public void requestIMEI(View view) {
		Intent i = new Intent();
		i.putExtra("request", "imei");
		i.putExtra("name", APP_NAME);
		i.setAction("com.privacy.sandbox.REQUEST_VALUE");

		sendBroadcast(i);
		
	}

	public void requestCarrier(View view) {
		Intent i = new Intent();
		i.putExtra("request", "carrier");
		i.putExtra("name", APP_NAME);
		i.setAction("com.privacy.sandbox.REQUEST_VALUE");

		sendBroadcast(i);
		
	}
	
	public void requestContacts(View view) {
		Intent i = new Intent();
		i.putExtra("request", "contacts");
		i.putExtra("name", APP_NAME);
		i.setAction("com.privacy.sandbox.REQUEST_VALUE");

		sendBroadcast(i);
		
	}
	
	
}


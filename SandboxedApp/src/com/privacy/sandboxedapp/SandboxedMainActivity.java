package com.privacy.sandboxedapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;


public class SandboxedMainActivity extends Activity {
	public static final String APP_NAME = "AppA";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sandboxed_main);
		
		Intent i = new Intent();
		i.putExtra("name", APP_NAME);
		i.putExtra("broadcastLabel", "com.privacy.sandbox.SANDBOX_APP_A");
		i.setAction("com.privacy.sandbox.SEND_NAME");

		sendBroadcast(i, "com.privacy.sandbox.SANDBOX_MSG");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sandboxed_main, menu);
		return true;
	}

	public void requestLocation(View view) {
		sendRequest("location");	
	}
	
	public void requestProfile(View view) {
		sendRequest("profile");
	}
	
	public void requestIMEI(View view) {
		sendRequest("imei");
	}

	public void requestCarrier(View view) {
		sendRequest("carrier");
	}
	
	public void requestContacts(View view) {
		sendRequest("contacts");
	}
	
	public void sendRequest(String request){
		Intent i = new Intent();
		i.putExtra("request", request);
		i.putExtra("name", APP_NAME);
		i.setAction("com.privacy.sandbox.REQUEST_VALUE");

		sendBroadcast(i, "com.privacy.sandbox.SANDBOX_MSG");
	}
}

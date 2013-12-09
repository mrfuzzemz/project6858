package com.privacy.sandboxedapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SandboxedMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sandboxed_main);
		
		Intent i = new Intent();
		i.putExtra("name", "SandboxedApp");
		i.setAction("com.privacy.sandbox.REQUEST_IDENTIFIER");

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
		i.putExtra("name", "SandboxedApp");
		i.setAction("com.privacy.sandbox.REQUEST_VALUE");

		sendBroadcast(i);		
	}
	
	public void requestProfile(View view) {
		Intent i = new Intent();
		i.putExtra("request", "profile");
		i.putExtra("name", "SandboxedApp");
		i.setAction("com.privacy.sandbox.REQUEST_VALUE");

		sendBroadcast(i);
	}
	
	public void requestIMEI(View view) {
		Intent i = new Intent();
		i.putExtra("request", "imei");
		i.putExtra("name", "SandboxedApp");
		i.setAction("com.privacy.sandbox.REQUEST_VALUE");

		sendBroadcast(i);
		
	}
	
}

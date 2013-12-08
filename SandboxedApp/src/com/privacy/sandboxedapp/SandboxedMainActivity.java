package com.privacy.sandboxedapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class SandboxedMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sandboxed_main);
		
		Intent i = new Intent();
		i.putExtra("request", "location");
		i.setAction("com.privacy.sandbox.REQUEST_VALUE");

		sendBroadcast(i, "com.privacy.sandbox.SANDBOX_LOCATION");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sandboxed_main, menu);
		return true;
	}

	public void requestLocation(View view) {
		Intent i = new Intent();
		i.putExtra("data", "a value");
		i.setAction("com.privacy.sandbox.REQUEST_LOCATION");

		sendBroadcast(i);
		
//		Button requestLocationButton =(Button)view.findViewById(R.id.buttonLoc);
//		requestLocationButton.setText("Request sent");
		
//		try {
//		Thread.sleep(5000);
//		}
//		catch (InterruptedException e){
//			e.printStackTrace();
//		}
//		
//		requestLocationButton.setText("Request Location");
		
	}
	
}

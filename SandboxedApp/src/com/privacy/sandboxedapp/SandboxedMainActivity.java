package com.privacy.sandboxedapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SandboxedMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sandboxed_main);
		
		Intent i = new Intent();
		i.putExtra("data", "a value");
		i.setAction("com.privacy.sandbox.REQUEST_LOCATION");

		sendBroadcast(i);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sandboxed_main, menu);
		return true;
	}

	// Let us see if this changes
	
}

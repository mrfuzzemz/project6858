package com.privacy.sandbox;

import android.os.Bundle;
import android.provider.ContactsContract;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		TextView requestLabel =(TextView)findViewById(R.id.textView1);
		// requestLabel.setText("Profile");
		
		// Get the current carrier
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String carrierName = telephonyManager.getNetworkOperatorName();
        
		// requestLabel.setText(carrierName);
        
		// Get IMEI
		String phoneIMEI = telephonyManager.getDeviceId();
		// requestLabel.setText(phoneIMEI);
		
		// Get the phone owner's name (be sure to set this up on the phone!!!)
		Cursor c = this.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
		c.moveToFirst();
		String userName = c.getString(c.getColumnIndex("DISPLAY_NAME"));
		c.close();
		requestLabel.setText(userName);


		
		
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    
    
}

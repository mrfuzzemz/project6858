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

	public static String userName = "";
	public static String phoneIMEI = "";
	public static String carrierName = "";
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		TextView requestLabel =(TextView)findViewById(R.id.textView1);
		
		// Get the current carrier
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		carrierName = telephonyManager.getNetworkOperatorName();
        
		// Initialize IMEI
		phoneIMEI = telephonyManager.getDeviceId();
		
		// Initialize userName
		Cursor c = this.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
		c.moveToFirst();
		userName = c.getString(c.getColumnIndex("DISPLAY_NAME"));
		c.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    
    // Functions to get the good stuff
    
	// Get the phone owner's name (be sure to set this up on the phone!!!)
    public static String getUserName(){
		return userName;
    }
    
	// Get the phone IMEI
    public static String getPhoneIMEI(){
		return phoneIMEI;
    }
    
    // Get the phone wireless carrier name
    public static String getCarrierName(){
    	return carrierName;
    }
    
}

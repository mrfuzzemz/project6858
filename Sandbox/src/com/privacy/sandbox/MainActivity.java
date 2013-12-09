package com.privacy.sandbox;

import android.os.Bundle;

import android.provider.ContactsContract;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static PermissionsDataSource datasource;

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
		if (c.moveToFirst()){
			userName = c.getString(c.getColumnIndex("DISPLAY_NAME"));
		} else{
			userName = "";
		}
		c.close();
    
		datasource = new PermissionsDataSource(this);
		datasource.open();

		Toast.makeText(this, datasource.getAllPermissions().toString(), Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void saveToDB(View view) {
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		String value = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId() )).getText().toString();
		EditText mEdit   = (EditText)findViewById(R.id.editText1);

		String permissionSetting = "";

		if (value.equals("Real")) {
			permissionSetting = "Real";
		} else if (value.equals("Bogus")){
			permissionSetting = "Bogus";
		} else {
			permissionSetting = "Custom: " + mEdit.getText().toString();
		}

		datasource.createOrUpdatePermission("SandboxedApp", "location", permissionSetting);
		
		Toast.makeText(this, datasource.getAllPermissions().toString(), Toast.LENGTH_LONG).show();
		
	}
	
	public static Permission getPermission(String appName, String permName){
		return datasource.getPermission(appName, permName);
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

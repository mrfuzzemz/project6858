package com.privacy.sandbox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	private static String userName = "";
	private static String phoneIMEI = "";
	private static String carrierName = "";
	
	//for removing Custom label from custom permissions
	public static final int CUSTOM_OFFSET = "Custom: ".length(); 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        		
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
		
		checkCurrentSettings();
		
		Toast.makeText(this, datasource.getAllPermissions().toString(), Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void checkCurrentSettings(){
		List<Permission> perms = datasource.getAllPermissions("SandboxedApp");
		Iterator<Permission> iter = perms.iterator();
		
		while(iter.hasNext()){
			Permission p = iter.next();
			String permissionName = p.getPermName();
			String value = p.getPermValue();
			
			RadioGroup rg;
			RadioButton selected;
			
			if (permissionName.equals("location")){
				rg = (RadioGroup) findViewById(R.id.locationRadioGroup);
				if (value.equals("Real")) {
					selected = (RadioButton) findViewById(R.id.realLoc);
				} else if (value.equals("Bogus")){
					selected = (RadioButton) findViewById(R.id.bogusLoc);
				} else { //custom
					selected = (RadioButton) findViewById(R.id.customLoc);
					((EditText)findViewById(R.id.locEditText)).setText(value.substring(CUSTOM_OFFSET));
				}
			} else if(permissionName.equals("imei")){
				rg = (RadioGroup) findViewById(R.id.IMEIRadioGroup);
				if (value.equals("Real")) {
					selected = (RadioButton) findViewById(R.id.realIMEI);
				} else if (value.equals("Bogus")){
					selected = (RadioButton) findViewById(R.id.bogusIMEI);
				} else { //custom
					selected = (RadioButton) findViewById(R.id.customIMEI);
					((EditText)findViewById(R.id.IMEIEditText)).setText(value.substring(CUSTOM_OFFSET));
				}
			} else if (permissionName.equals("profile")){
				rg = (RadioGroup) findViewById(R.id.profileRadioGroup);
				if (value.equals("Real")) {
					selected = (RadioButton) findViewById(R.id.realProfile);
				} else if (value.equals("Bogus")){
					selected = (RadioButton) findViewById(R.id.bogusProfile);
				} else { //custom
					selected = (RadioButton) findViewById(R.id.customProfile);
					((EditText)findViewById(R.id.profileEditText)).setText(value.substring(CUSTOM_OFFSET));
				}
			} else { //carrier
				rg = (RadioGroup) findViewById(R.id.carrierRadioGroup);
				if (value.equals("Real")) {
					selected = (RadioButton) findViewById(R.id.realCarrier);
				} else if (value.equals("Bogus")){
					selected = (RadioButton) findViewById(R.id.bogusCarrier);
				} else { //custom
					selected = (RadioButton) findViewById(R.id.customCarrier);
					((EditText)findViewById(R.id.locEditText)).setText(value.substring(CUSTOM_OFFSET));
				}
			}
			rg.check(selected.getId());

		}
	}
	
	public void saveToDB(View view){
		saveLocationToDB(view);
		saveIMEIToDB(view);
		saveProfileToDB(view);
		saveCarrierToDB(view);
	}
	
	public void saveLocationToDB(View view) {
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.locationRadioGroup);
		String value = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId() )).getText().toString();
		EditText mEdit  = (EditText)findViewById(R.id.locEditText);

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
	
	
	public void saveIMEIToDB(View view) {
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.IMEIRadioGroup);
		String value = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId() )).getText().toString();
		EditText mEdit   = (EditText)findViewById(R.id.IMEIEditText);

		String permissionSetting = "";

		if (value.equals("Real")) {
			permissionSetting = "Real";
		} else if (value.equals("Bogus")){
			permissionSetting = "Bogus";
		} else {
			permissionSetting = "Custom: " + mEdit.getText().toString();
		}

		datasource.createOrUpdatePermission("SandboxedApp", "imei", permissionSetting);
		
		Toast.makeText(this, datasource.getAllPermissions().toString(), Toast.LENGTH_LONG).show();
		
	}
	
	public void saveProfileToDB(View view) {
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.profileRadioGroup);
		String value = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId() )).getText().toString();
		EditText mEdit   = (EditText)findViewById(R.id.profileEditText);

		String permissionSetting = "";

		if (value.equals("Real")) {
			permissionSetting = "Real";
		} else if (value.equals("Bogus")){
			permissionSetting = "Bogus";
		} else {
			permissionSetting = "Custom: " + mEdit.getText().toString();
		}

		datasource.createOrUpdatePermission("SandboxedApp", "profile", permissionSetting);
		
		Toast.makeText(this, datasource.getAllPermissions().toString(), Toast.LENGTH_LONG).show();
		
	}	
	
	public void saveCarrierToDB(View view) {
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.carrierRadioGroup);
		String value = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId() )).getText().toString();
		EditText mEdit   = (EditText)findViewById(R.id.carrierEditText);

		String permissionSetting = "";

		if (value.equals("Real")) {
			permissionSetting = "Real";
		} else if (value.equals("Bogus")){
			permissionSetting = "Bogus";
		} else {
			permissionSetting = "Custom: " + mEdit.getText().toString();
		}

		datasource.createOrUpdatePermission("SandboxedApp", "carrier", permissionSetting);
		
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

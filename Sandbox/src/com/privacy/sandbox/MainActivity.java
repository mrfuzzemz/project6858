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
	private static contactListHelper cLH;

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
		
		cLH = new contactListHelper();
		cLH.updateContactList(getBaseContext());
    
		datasource = new PermissionsDataSource(this);
		datasource.open();
		
		checkCurrentSettings("SandboxedApp");
		
		Toast.makeText(this, datasource.getAllPermissions().toString(), Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void checkCurrentSettings(String appName){
		List<Permission> perms = datasource.getAllPermissions(appName);
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
			} else if(permissionName.equals("contacts")) {
				rg = (RadioGroup) findViewById(R.id.contactsRadioGroup);
				if (value.equals("Real")) {
					selected = (RadioButton) findViewById(R.id.realContacts);
				} else if (value.equals("Name Only")) {
					selected = (RadioButton) findViewById(R.id.nameContacts);
				} else if (value.equals("Bogus")){
					selected = (RadioButton) findViewById(R.id.bogusContacts);
				} else { //custom
					selected = (RadioButton) findViewById(R.id.customContacts);
					((EditText)findViewById(R.id.contactsEditText)).setText(value.substring(CUSTOM_OFFSET));
				}
			} else { //carrier
				rg = (RadioGroup) findViewById(R.id.carrierRadioGroup);
				if (value.equals("Real")) {
					selected = (RadioButton) findViewById(R.id.realCarrier);
				} else if (value.equals("Bogus")){
					selected = (RadioButton) findViewById(R.id.bogusCarrier);
				} else { //custom
					selected = (RadioButton) findViewById(R.id.customCarrier);
					((EditText)findViewById(R.id.carrierEditText)).setText(value.substring(CUSTOM_OFFSET));
				}
			}
			rg.check(selected.getId());

		}
	}
	
	public void saveAllToDB(View view){
		String appName = "SandboxedApp";
		saveToDB(view, appName, "location", R.id.locationRadioGroup, R.id.locEditText);
		saveToDB(view, appName, "imei", R.id.IMEIRadioGroup, R.id.IMEIEditText);
		saveToDB(view, appName, "profile", R.id.profileRadioGroup, R.id.profileEditText);
		saveToDB(view, appName, "carrier", R.id.carrierRadioGroup, R.id.carrierEditText);
		saveToDB(view, appName, "contacts", R.id.contactsRadioGroup, R.id.contactsEditText);
		Toast.makeText(this, datasource.getAllPermissions().toString(), Toast.LENGTH_LONG).show();
	}
	
	public void saveToDB(View view, String appName, String permName, int rgID, int editTextID) {
		RadioGroup radioGroup = (RadioGroup) findViewById(rgID);
		String permissionSetting = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId() )).getText().toString();
		EditText mEdit  = (EditText)findViewById(editTextID);

		if (permissionSetting.equals("Custom")) {
			permissionSetting = "Custom: " + mEdit.getText().toString();
		}

		datasource.createOrUpdatePermission(appName, permName, permissionSetting);
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
    
    //Get phone contacts
    public static String getContactsList(Context c){
    	cLH.updateContactList(c);
    	return cLH.getAllContacts();
    }
    
  //Get phone contact names
    public static String getContactsNames(Context c){
    	cLH.updateContactList(c);
    	return cLH.getAllNames();
    }
}

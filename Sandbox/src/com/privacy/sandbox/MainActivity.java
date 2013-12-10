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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static PermissionsDataSource datasource;
	private static AppRecordDataSource recordsource;
	
	private Spinner spinnerApps;

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
		
		recordsource = new AppRecordDataSource(this);
		recordsource.open();
		
		List<AppRecord> knownApps = recordsource.getAllAppRecords();
		
		if (! knownApps.isEmpty()){
			checkCurrentSettings(knownApps.get(0).getName());
		} else {
			Toast.makeText(this, "No apps known!", Toast.LENGTH_LONG).show();
		}
		
		Toast.makeText(this, datasource.getAllPermissions().toString(), Toast.LENGTH_LONG).show();
		
		// Example function to add App with name "testAppName" to dropdown menu
		List<String> appList = new ArrayList<String>();
		appList.add("App1");
		appList.add("App2");
		


		populateSpinnerApps(appList);
		// How to check what is currently selected
		String selectedApp = String.valueOf(spinnerApps.getSelectedItem());
		
		// Add a listener for the list to select the appropriate App in the database
		addListenerOnSpinnerItemSelection();

	}

    // Setup Apps list, adapted from http://www.mkyong.com/android/android-spinner-drop-down-list-example/
    // add item into spinnerApps
    public void populateSpinnerApps(List applist) {
   
	  	spinnerApps = (Spinner) findViewById(R.id.spinnerApps);
	  	
	  	//List<String> list = new ArrayList<String>();
	  	//list.add(appName);
	  	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	  		android.R.layout.simple_spinner_item, applist);
	  	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	spinnerApps.setAdapter(dataAdapter);
    }    
    
    public void addListenerOnSpinnerItemSelection() {
    	spinnerApps = (Spinner) findViewById(R.id.spinnerApps);
    	spinnerApps.setOnItemSelectedListener(new CustomOnItemSelectedListener());
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
	
	public void saveAllToDB(View view){
		String appName = "SandboxedApp";
		saveToDB(view, appName, "location", R.id.locationRadioGroup, R.id.locEditText);
		saveToDB(view, appName, "imei", R.id.IMEIRadioGroup, R.id.IMEIEditText);
		saveToDB(view, appName, "profile", R.id.profileRadioGroup, R.id.profileEditText);
		saveToDB(view, appName, "carrier", R.id.carrierRadioGroup, R.id.carrierEditText);
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
	
	public static AppRecord addAppRecord(String appName){
		return recordsource.createAppRecordIfNotExists(appName);
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

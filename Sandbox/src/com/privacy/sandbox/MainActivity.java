package com.privacy.sandbox;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static PermissionsDataSource datasource;
	private static contactListHelper cLH;
	private static AppRecordDataSource recordsource;
	
	private static String userName = "";
	private static String phoneIMEI = "";
	private static String carrierName = "";
	
	
	private Spinner spinnerApps;
    private final int[] locButtonIDs = {R.id.realLoc, R.id.bogusLoc, R.id.customLoc};		
    private final int[] imeiButtonIDs = {R.id.realIMEI, R.id.bogusIMEI, R.id.customIMEI};
	private final int[] profileButtonIDs = {R.id.realProfile, R.id.bogusProfile, R.id.customProfile};
	private final int[] contactsButtonIDs = {R.id.realContacts, R.id.bogusContacts, R.id.customContacts, R.id.nameContacts};
	private final int[] carrierButtonIDs = {R.id.realCarrier, R.id.bogusCarrier, R.id.customCarrier};

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
		
		recordsource = new AppRecordDataSource(this);
		recordsource.open();
		
		//populate the spinner with the app list
		List<String> knownApps = recordsource.getAllAppRecords();

		populateSpinnerApps(knownApps);
		
		if (! knownApps.isEmpty()){
			// fill in radio buttons with settings for first app in DB
			selectCurrentSettings(knownApps.get(0));
		} else {
			Toast.makeText(this, "No apps known!", Toast.LENGTH_LONG).show();
		}
		
		// Add a listener for the list to select the appropriate app in the database
		addListenerOnSpinnerItemSelection();
	}
    
    @Override
    protected void onResume(){
		populateSpinnerApps(recordsource.getAllAppRecords());
		super.onResume();
    }
    
    @Override
    protected void onStart(){
		populateSpinnerApps(recordsource.getAllAppRecords());
		super.onStart();
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    // Setup apps list, adapted from http://www.mkyong.com/android/android-spinner-drop-down-list-example/
    // add item into spinnerApps
    public void populateSpinnerApps(List<String> applist) {
	  	spinnerApps = (Spinner) findViewById(R.id.spinnerApps);
	  	
	  	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	  		android.R.layout.simple_spinner_item, applist);
	  	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  	spinnerApps.setAdapter(dataAdapter);
    }    
    
    // add a listener to change the radio button selections when the user selects
    // a different app
    public void addListenerOnSpinnerItemSelection() {
    	spinnerApps = (Spinner) findViewById(R.id.spinnerApps);
    	spinnerApps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
        		String appName = parent.getItemAtPosition(pos).toString();
        		selectCurrentSettings(appName);
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				return;
			}
        });
      }
    
    
    // selects the appropriate radio buttons for the given app name
    public void selectCurrentSettings(String appName){    	
		List<Permission> perms = datasource.getAllPermissions(appName);
		Iterator<Permission> iter = perms.iterator();
		
		while(iter.hasNext()){
			Permission p = iter.next();
			String permissionName = p.getPermName();
			String value = p.getPermValue();
			
			int selectedIndex;
			
			if (value.equals("Real")) {
				selectedIndex = 0;
				value = "";
			} else if (value.equals("Bogus")){
				selectedIndex = 1; 			
				value = "";
			} else if (value.contains("Custom")){ 
				selectedIndex = 2;
				value = value.substring(CUSTOM_OFFSET);
			} else { //name only option for Contacts
				selectedIndex = 3;
				value = "";
			}
			
			

			if (permissionName.equals("location")){
				((RadioGroup) findViewById(R.id.locationRadioGroup)).check(locButtonIDs[selectedIndex]);
				((EditText)findViewById(R.id.locEditText)).setText(value);
			} else if(permissionName.equals("imei")){
				((RadioGroup) findViewById(R.id.IMEIRadioGroup)).check(imeiButtonIDs[selectedIndex]);
				((EditText)findViewById(R.id.IMEIEditText)).setText(value);
			} else if (permissionName.equals("profile")){
				((RadioGroup) findViewById(R.id.profileRadioGroup)).check(profileButtonIDs[selectedIndex]);
				((EditText)findViewById(R.id.profileEditText)).setText(value);
			} else if(permissionName.equals("contacts")) {
				((RadioGroup) findViewById(R.id.contactsRadioGroup)).check(contactsButtonIDs[selectedIndex]);
				((EditText)findViewById(R.id.contactsEditText)).setText(value);
			} else { //carrier
				((RadioGroup) findViewById(R.id.carrierRadioGroup)).check(carrierButtonIDs[selectedIndex]);
				((EditText)findViewById(R.id.carrierEditText)).setText(value);
			}
		}
	}
	
    // saves the current radio button selections for the current app selection
	public void saveAllToDB(View view){
		if (spinnerApps.getCount() == 0){
			Toast.makeText(this, "No apps! Can't save settings!", Toast.LENGTH_SHORT).show();
			return;
		}

		String appName = (String) spinnerApps.getSelectedItem();
		saveToDB(view, appName, "location", R.id.locationRadioGroup, R.id.locEditText);
		saveToDB(view, appName, "imei", R.id.IMEIRadioGroup, R.id.IMEIEditText);
		saveToDB(view, appName, "profile", R.id.profileRadioGroup, R.id.profileEditText);
		saveToDB(view, appName, "carrier", R.id.carrierRadioGroup, R.id.carrierEditText);
		saveToDB(view, appName, "contacts", R.id.contactsRadioGroup, R.id.contactsEditText);
	}
	
	// Function to save user input permission settings
	public void saveToDB(View view, String appName, String permName, int rgID, int editTextID) {
		RadioGroup radioGroup = (RadioGroup) findViewById(rgID);
		String permissionSetting = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId() )).getText().toString();
		EditText mEdit  = (EditText)findViewById(editTextID);

		if (permissionSetting.equals("Custom")) {
			permissionSetting = "Custom: " + mEdit.getText().toString();
		}

		datasource.createOrUpdatePermission(appName, permName, permissionSetting);
	}	
	
	// Function to create entries for permissions when Sandbox learns about an app
	// Default value for all permissions is Bogus
	public static void saveToDB(String appName, String permName){
		datasource.createOrUpdatePermission(appName, permName, "Bogus");
	}
	
	
	// Saves a record of the app name and sets all permissions to Bogus default
	public static AppRecord addAppRecord(String appName, String broadcastLabel){
		AppRecord ap = recordsource.createAppRecordIfNotExists(appName, broadcastLabel);
				
		if (ap != null){
			saveToDB(appName, "location");
			saveToDB(appName, "imei");
			saveToDB(appName, "profile");
			saveToDB(appName, "carrier");
			saveToDB(appName, "contacts");
		} 
		return ap;
	}
	
	public static String getBroadcastLabel(String appName){
		return recordsource.getBroadcastLabel(appName);
	}
	 
	public static Permission getPermission(String appName, String permName){
		Permission p = datasource.getPermission(appName, permName);
		return p;
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

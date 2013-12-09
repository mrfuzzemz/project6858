package com.privacy.sandbox;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends Activity {
	private PermissionsDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		Toast.makeText(this, "button was " + value, Toast.LENGTH_SHORT).show();

		String permissionSetting = "";

		if (value.equals("Real")) {
			permissionSetting = "Real";
		} else if (value.equals("Bogus")){
			permissionSetting = "Bogus";
		} else {
			permissionSetting = "Custom: " + mEdit.getText().toString();
		}

		Permission perm = null;
		perm = datasource.createPermission("App A", "Location", permissionSetting);	

		Toast.makeText(this, "added " + perm.toString(), Toast.LENGTH_SHORT).show();
		Toast.makeText(this, datasource.getAllPermissions().toString(), Toast.LENGTH_LONG).show();
	}
}

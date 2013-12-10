package com.privacy.sandbox;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class contactListHelper {
	
	public ArrayList<String> contactList;
	
	public contactListHelper() {
		contactList = new ArrayList<String>();
	}
	
	public void updateContactList(Context c) {
		contactList.clear();
		ContentResolver cr = c.getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		while(cur.moveToNext()) {
			   int nameFieldColumnIndex = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			   String contact = cur.getString(nameFieldColumnIndex);
			   contactList.add(contact+":"+"phone");
		}
			
		cur.close();
	}
	
	public String getAllNames() {
		StringBuilder out = new StringBuilder();
		for (String str : contactList) {
			String[] parts = str.split(":");
			out.append(parts[0]+";");
		}
		return out.toString();
	}
	
	public String getAllContacts() {
		StringBuilder out = new StringBuilder();
		for (String str : contactList) {
		  out.append(str+";");
		}
		return out.toString();
	}
}

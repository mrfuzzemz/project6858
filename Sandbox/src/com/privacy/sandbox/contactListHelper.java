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
			   String contact = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			   String contactNumber = "phone_not_available";
		       String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
		       if(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
		    	   Cursor pcur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
		    	   if(pcur.moveToFirst()) {
		    		   contactNumber = pcur.getString(pcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		    	   }
		    	   pcur.close();
		       }
			   contactList.add(contact+":"+contactNumber);
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

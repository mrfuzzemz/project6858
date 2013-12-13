package com.privacy.sandbox;

public class AppRecord {
	private long id;
	private String name;
	private String broadcastLabel;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getBroadcastLabel() {
		return broadcastLabel;
	}

	public void setBroadcastLabel(String broadcastLabel) {
		this.broadcastLabel = broadcastLabel;
	}
	
	// Will be used by the ArrayAdapter in the ListView
		@Override
		public String toString() {
			return "AppRecord: " + name + ", uses broadcast permission: " + broadcastLabel;
		}

}

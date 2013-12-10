package com.privacy.sandbox;

public class Permission {
	private long id;
	private String appName;
	private String permName;
	private String permValue;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPermName() {
		return permName;
	}

	public void setPermName(String permName) {
		this.permName = permName;
	}

	public String getPermValue() {
		return permValue;
	}

	public void setPermValue(String permValue) {
		this.permValue = permValue;
	}

	@Override
	public String toString() {
		return "App: " + appName + ", permission " + permName + ", set to " + permValue;
	}

}

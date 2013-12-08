package com.privacy.sandbox;

public class Permission {
	private long id;
	private String name;
	private String read_profile;

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

	public String getRead_profile() {
		return read_profile;
	}

	public void setRead_profile(String read_profile) {
		this.read_profile = read_profile;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return name + " read_profile: " + read_profile;
	}

}

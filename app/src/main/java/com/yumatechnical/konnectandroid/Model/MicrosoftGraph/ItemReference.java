package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

public class ItemReference {

	private String displayName;
	private String id;


	ItemReference(String displayName, String id) {
		this.displayName = displayName;
		this.id = id;
	}


	public String getDisplayName() {
		return displayName;
	}

	public String getId() {
		return id;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setId(String id) {
		this.id = id;
	}
}

package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

class SystemFacet {

	private String displayName;
	private String id;


	SystemFacet(String displayName, String id) {
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

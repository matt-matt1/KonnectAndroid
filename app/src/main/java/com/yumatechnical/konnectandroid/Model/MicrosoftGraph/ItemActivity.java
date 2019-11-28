package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

class ItemActivity {

	//{
	//  "id": "string (identifier)",
	//  "action": {"@odata.type": "microsoft.graph.itemActionSet"},
	//  "actor": {"@odata.type": "microsoft.graph.identitySet"},
	//  "driveItem": {"@odata.type": "microsoft.graph.driveItem"},
	//  "listItem": {"@odata.type": "microsoft.graph.listItem"},
	//  "times": {"@odata.type": "microsoft.graph.itemActivityTimeSet"}
	//}
	private String displayName;
	private String id;


	ItemActivity(String displayName, String id) {
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

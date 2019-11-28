package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

public class Folder {

	//https://docs.microsoft.com/en-us/onedrive/developer/rest-api/resources/folder?view=odsp-graph-online
	//{
	//  "childCount": 1024,
	//  "view": { "@odata.type": "microsoft.graph.folderView" }
	//}
	private String displayName;
	private String id;


	Folder(String displayName, String id) {
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

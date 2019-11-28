package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

import java.util.List;

public class Drive {
	//{
	//  "activities": [{"@odata.type": "microsoft.graph.itemActivity"}],
	//  "id": "string",
	//  "createdBy": { "@odata.type": "microsoft.graph.identitySet" },
	//  "createdDateTime": "string (timestamp)",
	//  "description": "string",
	//  "driveType": "personal | business | documentLibrary",
	//  "items": [ { "@odata.type": "microsoft.graph.driveItem" } ],
	//  "lastModifiedBy": { "@odata.type": "microsoft.graph.identitySet" },
	//  "lastModifiedDateTime": "string (timestamp)",
	//  "name": "string",
	//  "owner": { "@odata.type": "microsoft.graph.identitySet" },
	//  "quota": { "@odata.type": "microsoft.graph.quota" },
	//  "root": { "@odata.type": "microsoft.graph.driveItem" },
	//  "sharepointIds": { "@odata.type": "microsoft.graph.sharepointIds" },
	//  "special": [ { "@odata.type": "microsoft.graph.driveItem" }],
	//  "system": { "@odata.type": "microsoft.graph.systemFacet" },
	//  "webUrl": "url"
	//}
	private List<ItemActivity> activities;
	private String id;
	private IdentitySet createdBy;
	private String createdDateTime;
	private String description;
	private String driveType;
	private List<Item> items;
	private IdentitySet lastModifiedBy;
	private String lastModifiedDateTime;
	private String name;
	private IdentitySet owner;
	private Quota quota;
	private DriveItem root;
	private SharepointIDs sharepointIds;
	private List<DriveItem> special;
	private SystemFacet system;
	private String webUrl;
}

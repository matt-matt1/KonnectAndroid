package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

import java.util.List;

public class Identity {
	//{
	//  "displayName": "string",
	//  "id": "string",
	//  "thumbnails": [{ "@odata.type": "microsoft.graph.thumbnailSet" }]
	//}
	private String displayName;
	private String id;
	private List<Thumbnail> thumbnails;
}

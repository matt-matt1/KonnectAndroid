package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DriveItem {

	//https://docs.microsoft.com/en-us/onedrive/developer/rest-api/resources/driveitem?view=odsp-graph-online
	//{
	//  "audio": { "@odata.type": "microsoft.graph.audio" },
	//  "content": { "@odata.type": "Edm.Stream" },
	//  "cTag": "string (etag)",
	//  "deleted": { "@odata.type": "microsoft.graph.deleted"},
	//  "description": "string",
	//  "file": { "@odata.type": "microsoft.graph.file" },
	//  "fileSystemInfo": { "@odata.type": "microsoft.graph.fileSystemInfo" },
	//  "folder": { "@odata.type": "microsoft.graph.folder" },
	//  "image": { "@odata.type": "microsoft.graph.image" },
	//  "location": { "@odata.type": "microsoft.graph.geoCoordinates" },
	//  "malware": { "@odata.type": "microsoft.graph.malware" },
	//  "package": { "@odata.type": "microsoft.graph.package" },
	//  "photo": { "@odata.type": "microsoft.graph.photo" },
	//  "publication": {"@odata.type": "microsoft.graph.publicationFacet"},
	//  "remoteItem": { "@odata.type": "microsoft.graph.remoteItem" },
	//  "root": { "@odata.type": "microsoft.graph.root" },
	//  "searchResult": { "@odata.type": "microsoft.graph.searchResult" },
	//  "shared": { "@odata.type": "microsoft.graph.shared" },
	//  "sharepointIds": { "@odata.type": "microsoft.graph.sharepointIds" },
	//  "size": 1024,
	//  "specialFolder": { "@odata.type": "microsoft.graph.specialFolder" },
	//  "video": { "@odata.type": "microsoft.graph.video" },
	//  "webDavUrl": "string",
	//
	//  /* relationships */
	//  "activities": [{"@odata.type": "microsoft.graph.itemActivity"}],
	//  "children": [{ "@odata.type": "microsoft.graph.driveItem" }],
	//  "permissions": [ {"@odata.type": "microsoft.graph.permission"} ],
	//  "thumbnails": [ {"@odata.type": "microsoft.graph.thumbnailSet"}],
	//  "versions": [ {"@odata.type": "microsoft.graph.driveItemVersion"}],
	//
	//  /* inherited from baseItem */
	//  "id": "string (identifier)",
	//  "createdBy": {"@odata.type": "microsoft.graph.identitySet"},
	//  "createdDateTime": "String (timestamp)",
	//  "eTag": "string",
	//  "lastModifiedBy": {"@odata.type": "microsoft.graph.identitySet"},
	//  "lastModifiedDateTime": "String (timestamp)",
	//  "name": "string",
	//  "parentReference": {"@odata.type": "microsoft.graph.itemReference"},
	//  "webUrl": "string",
	//
	//  /* instance annotations */
	//  "@microsoft.graph.conflictBehavior": "string",
	//  "@microsoft.graph.downloadUrl": "url",
	//  "@microsoft.graph.sourceUrl": "url"
	//}

	//{
	//  "@microsoft.graph.downloadUrl":"http://public-sn3302.files.1drv.com/y2pcT7OaUEExF7EHOl",
	//  "createdDateTime": "2014-10-31T03:37:04.72Z",
	//  "eTag": "aRDQ2NDhGMDZDOTFEOUQzRCE1NDkyNy4w",
	//  "id":"D4648F06C91D9D3D!54927",
	//  "lastModifiedBy": {
	//    "user": {
	//      "displayName": "daron spektor",
	//      "id": "d4648f06c91d9d3d"
	//    }
	//  },
	//  "name":"BritishShorthair.docx",
	//  "size":35212,
	//  "file": {
	//    "hashes":{
	//      "sha1Hash":"cf23df2207d99a74fbe169e3eba035e633b65d94"
	//    }
	//  }
	//}

	private Audio audio;
	private Stream content;
	private String cTag;
	private Deleted deleted;
	private String description;
	private File file;
	private FileSystemInfo fileSystemInfo;
	private Folder folder;
	private Image image;
	private GeoCoordinates location;
	private Malware malware;
	@SerializedName("package")
	private Package packageName;
	private Photo photo;
	private PublicationFacet publication;
	private RemoteItem remoteItem;
	private Root root;
	private SearchResult searchResult;
	private Shared shared;
	private SharepointIDs sharepointIds;
	private int size;
	private SpecialFolder specialFolder;
	private Video video;
	private String webDavUrl;

		  /* relationships */
	private List<ItemActivity> activities;
	private List<DriveItem> children;
	private List<String> permissions;// [ {"@odata.type": "microsoft.graph.permission"} ],
	private List<Thumbnail> thumbnails;// [ {"@odata.type": "microsoft.graph.thumbnailSet"}],
	private List<String> versions;// [ {"@odata.type": "microsoft.graph.driveItemVersion"}],

		  /* inherited from baseItem */
	private String id;
	private IdentitySet createdBy;
	private DateTimeOffset createdDateTime;
	private String eTag;
	private IdentitySet lastModifiedBy;
	private DateTimeOffset lastModifiedDateTime;
	private String name;
	private ItemReference parentReference;
	private String webUrl;

		  /* instance annotations */
	private String conflictBehavior;
	private String downloadUrl;
	private String sourceUrl;

/*
	@SerializedName("@microsoft.graph.downloadUrl")
	private String downloadUrl;
	private String createdDateTime;
	private String eTag;
	private String id;
	private MSPerson lastModifiedBy;
	private String name;
	private String size;
	private File file;
*/
}

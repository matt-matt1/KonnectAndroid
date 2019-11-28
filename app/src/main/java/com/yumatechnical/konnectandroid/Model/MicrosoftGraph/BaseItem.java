package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

class BaseItem {
	//{
	//  "id": "string (identifier)",
	//  "createdBy": { "@odata.type": "microsoft.graph.identitySet" },
	//  "createdDateTime": "datetime",
	//  "description": "string",
	//  "eTag": "string",
	//  "lastModifiedBy": { "@odata.type": "microsoft.graph.identitySet" },
	//  "lastModifiedDateTime": "datetime",
	//  "name": "string",
	//  "parentReference": { "@odata.type": "microsoft.graph.itemReference" },
	//  "webUrl": "url"
	//}

	private String id;
	private IdentitySet createdBy;
	private String createdDateTime;
	private String description;
	private String eTag;
	private IdentitySet lastModifiedBy;
	private String lastModifiedDateTime;
	private String name;
	private ItemReference parentReference;
	private String webUrl;


	BaseItem(String id, IdentitySet createdBy, String createdDateTime, String description,
	         String eTag, IdentitySet lastModifiedBy, String lastModifiedDateTime, String name,
	         ItemReference parentReference, String webUrl) {
		this.id = id;
		this.createdBy = createdBy;
		this.createdDateTime = createdDateTime;
		this.description = description;
		this.eTag = eTag;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDateTime = lastModifiedDateTime;
		this.name = name;
		this.parentReference = parentReference;
		this.webUrl = webUrl;
	}


	public String getId() {
		return id;
	}

	public IdentitySet getCreatedBy() {
		return createdBy;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public String getDescription() {
		return description;
	}

	public String geteTag() {
		return eTag;
	}

	public IdentitySet getLastModifiedBy() {
		return lastModifiedBy;
	}

	public String getLastModifiedDateTime() {
		return lastModifiedDateTime;
	}

	public String getName() {
		return name;
	}

	public ItemReference getParentReference() {
		return parentReference;
	}

	public String getWebUrl() {
		return webUrl;
	}


	public void setId(String id) {
		this.id = id;
	}

	public void setCreatedBy(IdentitySet createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}

	public void setLastModifiedBy(IdentitySet lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public void setLastModifiedDateTime(String lastModifiedDateTime) {
		this.lastModifiedDateTime = lastModifiedDateTime;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParentReference(ItemReference parentReference) {
		this.parentReference = parentReference;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

}

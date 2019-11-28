package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfo {
	//{
	//														// "@odata.context":"https:\/\/graph.microsoft.com\/v1.0\/$metadata#users\/$entity",
	//														// "displayName":"Yuma Scott",
	//														// "surname":"Scott",
	//														// "givenName":"Yuma",
	//														// "id":"1e0a9009dfdfda27",
	//														// "userPrincipalName":"sales@yumatechnical.com",
	//														// "businessPhones":[],
	//														// "jobTitle":null,
	//														// "mail":null,
	//														// "mobilePhone":null,
	//														// "officeLocation":null,
	//														// "preferredLanguage":null
	//														// }
	@SerializedName("@data.content")
	private String dataContent;
	private String displayName;
	private String surname;
	private String givenName;
	private String id;
	private String userPrincipalName;
	private List<String> businessPhones;
	private String jobTitle;
	private String mail;
	private String mobilePhone;
	private String officeLocation;
	private String preferredLanguage;

	public UserInfo() {}


	public String getDataContent() {
		return dataContent;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getSurname() {
		return surname;
	}

	public String getGivenName() {
		return givenName;
	}

	public String getId() {
		return id;
	}

	public String getUserPrincipalName() {
		return userPrincipalName;
	}

	public List<String> getBusinessPhones() {
		return businessPhones;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getMail() {
		return mail;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public String getOfficeLocation() {
		return officeLocation;
	}

	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}

	public void setBusinessPhones(List<String> businessPhones) {
		this.businessPhones = businessPhones;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public void setOfficeLocation(String officeLocation) {
		this.officeLocation = officeLocation;
	}

	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

}

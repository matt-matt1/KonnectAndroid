package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

import com.google.gson.annotations.SerializedName;

public class InnerError {

	@SerializedName("request-id")
	private String requestId;
	private String date;
//	private DateTimeOffset date;


	InnerError(String requestId, String date) {
//	InnerError(String requestId, DateTimeOffset date) {
		this.requestId = requestId;
		this.date = date;
	}


	public String getRequestId() {
		return requestId;
	}

	public String getDate() {
//	public DateTimeOffset getDate() {
		return date;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setDate(String date) {
//	public void setDate(DateTimeOffset date) {
		this.date = date;
	}

}

package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

public class JsonError {

	private String code;
	private String message;


	JsonError(String code, String message) {
		this.code = code;
		this.message = message;
	}


	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

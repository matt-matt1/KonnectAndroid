package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

public class Error {

	public String code;
	public String message;
//	private Object innerErrorObj;
	public InnerError innerError;

/*
	Error(String code, String message, InnerError innerError) {
		this.code = code;
		this.message = message;
		this.innerError = innerError;
	}

	Error(String code, String message, Object innerError) {
		this.code = code;
		this.message = message;
		this.innerErrorObj = innerError;
	}

	Error(String code, String message) {
		this.code = code;
		this.message = message;
	}
*/

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public InnerError getInnerError() {
		return innerError;
	}
/*
	public Object getInnerErrorObj() {
		return innerErrorObj;
	}
*/
	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setInnerError(InnerError innerError) {
		this.innerError = innerError;
	}
/*
	public void setInnerErrorObj(Object innerErrorObj) {
		this.innerErrorObj = innerErrorObj;
	}
*/
}

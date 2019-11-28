package com.yumatechnical.konnectandroid.Model;

public class BOXError {

	/**
	 * Box-specific error code
	 * Value is one of "created", "accepted", "no_content", "redirect", "not_modified",
	 * "bad_request", "unauthorized", "forbidden", "not_found", "method_not_allowed", "conflict",
	 * "precondition_failed", "too_many_requests", "internal_server_error", "unavailable"
	 */
	private String code;//eg. item_name_invalid
	/**
	 * A URL that links to more information about why this error occurred.
	 */
	private String help_url;//eg. http://developers.box.com/docs/#errors
	/**
	 * A short message describing the error.
	 */
	private String message;//eg. Method Not Allowed
	 /**
	  * A unique identifier for this response, which can be used when contacting Box support.
	  */
	private String request_id;//eg. abcdef123456
	 /**
	 * The HTTP status of the response.
	 */
	private int status;//eg. 400
	/**
	 * Value is always "error"
	 */
	private String type;

	public String getCode() {
		return code;
	}

	public String getHelp_url() {
		return help_url;
	}

	public String getMessage() {
		return message;
	}

	public String getRequest_id() {
		return request_id;
	}

	public String getType() {
		return type;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public void setHelp_url(String help_url) {
		this.help_url = help_url;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public void setType(String type) {
		this.type = type;
	}

}

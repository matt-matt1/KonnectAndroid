package com.yumatechnical.konnectandroid.Model;

import androidx.annotation.NonNull;

public class ConnectionItem {

	private int type;
	private String string;


	public ConnectionItem (int type, String string) {
		this.type = type;
		this.string = string;
	}


	public int getType() {
		return type;
	}

	public String getString() {
		return string;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setString(String string) {
		this.string = string;
	}

	@NonNull
	@Override
	public String toString() {
		return super.toString();
	}
}

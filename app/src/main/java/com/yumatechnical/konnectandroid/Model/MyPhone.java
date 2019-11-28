package com.yumatechnical.konnectandroid.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyPhone {

	private int id;
	private String phoneString;
	private long number;
	private int type;

	public MyPhone(String phoneString, long number, int type) {
		this.number = number;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public String getPhoneString() {
		return phoneString;
	}

	public int getType() {
		return type;
	}

	public long getNumber() {
		return number;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPhoneString(String phoneString) {
		this.phoneString = phoneString;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public void setType(int type) {
		this.type = type;
	}

	@NonNull
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@NonNull
	@Override
	public String toString() {
		return "MyPhone: type="+ getId() +", PHONE_STRING="+ getPhoneString() +", TYPE="+ getType()
				+", NUMBER="+ getNumber() +"\n";
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}

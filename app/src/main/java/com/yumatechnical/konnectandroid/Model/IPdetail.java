package com.yumatechnical.konnectandroid.Model;

import androidx.annotation.NonNull;

public class IPdetail {
	private String address;
	private int type;
	private int flags;
	private String mac;
	private String mask;
	private String device;

	public IPdetail(String address, int type, int flags, String mac, String mask, String device) {
		this.address = address;
		this.type = type;
		this.flags = flags;
		this.mac = mac;
		this.mask = mask;
		this.device = device;
	}

	public String getAddress() {
		return address;
	}

	public int getType() {
		return type;
	}

	public int getFlags() {
		return flags;
	}

	public String getMac() {
		return mac;
	}

	public String getMask() {
		return mask;
	}

	public String getDevice() {
		return device;
	}


	public void setAddress(String address) {
		this.address = address;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public void setDevice(String device) {
		this.device = device;
	}


	@NonNull
	@Override
	public String toString() {
		return "IPdetail {\n" +
				"\taddress = "+ address+ "\n" +
				"\ttype = "+ type+ "\n" +
				"\tflags = "+ flags+ "\n" +
				"\tmac = "+ mac+ "\n" +
				"\tmask = "+ mask+ "\n" +
				"\tdevice = "+ device+ "\n}";
	}

}

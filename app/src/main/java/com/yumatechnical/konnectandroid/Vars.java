package com.yumatechnical.konnectandroid;

import android.app.Application;

import com.yumatechnical.konnectandroid.Model.ConnectionItem;

import java.util.ArrayList;

/*
 * This class holds application-wide (very global) variables
 *
 * example use:
 * // set
 * ((Vars) this.getApplication()).setSomeVariable("foo");
 *
 * // get
 * String s = ((Vars) this.getApplication()).getSomeVariable();
 */
public class Vars extends Application {

	private int iconSize = 100;

	public int getIconSize() {
		return iconSize;
	}

	public void setIconSize(int iconSize) {
		this.iconSize = iconSize;
	}


	private ArrayList<ConnectionItem> connectionItems = new ArrayList<>();

	public ArrayList<ConnectionItem> getConnectionItems() {
		return connectionItems;
	}

	public void setConnectionItems(ArrayList<ConnectionItem> connectionItems) {
		this.connectionItems = connectionItems;
	}

	public ConnectionItem getConnectItem(int index) {
		return connectionItems.get(index);
	}

	public void addConnectionItem(ConnectionItem item) {
		if (item != null && connectionItems != null)
		connectionItems.add(item);
	}


	//dropbox for KonnectAndroid
	private final static String DROPBOX_KEY = "0qb7zuth7zcx40s";
	private final static String DROPBOX_SECRET = "16uik7f7abk0b8g";
	private static String DROPBOX_ACCESS_TOKEN = "";

	public static String getDropboxKey() {
		return DROPBOX_KEY;
	}

	public static String getDropboxSecret() {
		return DROPBOX_SECRET;
	}

	public static String getDropboxAccessToken() {
		return DROPBOX_ACCESS_TOKEN;
	}

	public static void setDropboxAccessToken(String dropboxAccessToken) {
		DROPBOX_ACCESS_TOKEN = dropboxAccessToken;
	}
}

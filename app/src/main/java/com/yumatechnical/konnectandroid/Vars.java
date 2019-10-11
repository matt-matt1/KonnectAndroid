package com.yumatechnical.konnectandroid;

import android.app.Application;

import com.yumatechnical.konnectandroid.Adapter.LeftArrayAdapter;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;
import com.yumatechnical.konnectandroid.Model.FileItem;
import com.yumatechnical.konnectandroid.Model.ListItem;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Locale;

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

	public static final int MY_PHOTOS_ID = 1;
	public static final int MY_MUSIC_ID = 2;
	public static final int MY_CONTACTS_ID = 3;
	public static final int MY_FILES_ID = 4;
	public LeftArrayAdapter leftAdapter;

	public ArrayList<ListItem> leftList = new ArrayList<>();

	public boolean WifiConn = false;
	public boolean MobileConn = false;
	public boolean networkConnected = false;

	public boolean isMobileConn() {
		return MobileConn;
	}

	public void setMobileConn(boolean mobileConn) {
		MobileConn = mobileConn;
	}

	public boolean isWifiConn() {
		return WifiConn;
	}

	public void setWifiConn(boolean wifiConn) {
		WifiConn = wifiConn;
	}

	public boolean isNetworkConnected() {
		return networkConnected;
	}

	public void setNetworkConnected(boolean networkConnected) {
		this.networkConnected = networkConnected;
	}

	//ip address
	private int[] myIP = new int[4];

	public int[] getMyIP() {
		return myIP;
	}

	public String getMyIPString() {
		return String.format(Locale.CANADA,"%d.%d.%d.%d", myIP[0], myIP[1], myIP[2], myIP[3]);
	}
	public void setMyIPString(String string) {
		String[] parts = string.split("\\.");
		for (int i = 0; i < 4; i++) {
			myIP[i] = Integer.parseInt(parts[i]);
		}
	}

	public void setMyIP(int[] myIP) {
		this.myIP = myIP;
	}

	//iconSize
	private int iconSize = 100;

	public int getIconSize() {
		return iconSize;
	}

	public void setIconSize(int iconSize) {
		this.iconSize = iconSize;
	}


	//connectionItems
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

	public ConnectionItem getConnectionItemByID(int id) {
		for (ConnectionItem item : connectionItems) {
			if (item.getID() == id) {
				return item;
			}
		}
		return null;
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

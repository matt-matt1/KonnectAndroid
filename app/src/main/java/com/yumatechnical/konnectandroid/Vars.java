package com.yumatechnical.konnectandroid;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yumatechnical.konnectandroid.Adapter.LeftArrayAdapter;
import com.yumatechnical.konnectandroid.Adapter.RightAdapter;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;
import com.yumatechnical.konnectandroid.Model.ListItem;

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
	public static final int MY_LOCAL_HOSTS = 5;
	public static final int NUM_THIS_DEVICE = 5;
	public LeftArrayAdapter leftAdapter;
	public ListItem tempListItem;
	public RightAdapter rightAdapter;
	public RecyclerView recyclerView;

	public boolean WifiConn = false;
	public boolean MobileConn = false;
	public boolean networkConnected = false;

	@SuppressWarnings("unused")
	public boolean isMobileConn() {
		return MobileConn;
	}

	@SuppressWarnings("unused")
	public void setMobileConn(boolean mobileConn) {
		MobileConn = mobileConn;
	}

	@SuppressWarnings("unused")
	public boolean isWifiConn() {
		return WifiConn;
	}

	@SuppressWarnings("unused")
	public void setWifiConn(boolean wifiConn) {
		WifiConn = wifiConn;
	}

	@SuppressWarnings("unused")
	public boolean isNetworkConnected() {
		return networkConnected;
	}

	@SuppressWarnings("unused")
	public void setNetworkConnected(boolean networkConnected) {
		this.networkConnected = networkConnected;
	}


	/**
	 * leftItem
	 */
	private boolean isOpen = true;
	@SuppressWarnings("unused")
	public void markLeftPanelOpen(boolean isOpen) {
		if (this.isOpen == isOpen) {
			Log.d("Vars", "markLeftPanelOpen error");
		}
		this.isOpen = isOpen;
	}
	@SuppressWarnings("unused")
	public boolean isLeftPanelOpen() {
		return isOpen;
	}

	public ArrayList<ListItem> leftList = new ArrayList<>();

	public ListItem getLeftListItemByID(int id) {
		for (ListItem item : leftList) {
			if (item.getID() == id) {
				return item;
			}
		}
		return null;
	}

	public int getLeftListItemNextID() {
		int id = 0;
		for (ListItem item : leftList) {
			if (item.getID() > id) {
				id = item.getID();
			}
		}
		return id+1;
	}


	/**
	 * ip address
	 */
	private int[] myIP = new int[4];

	@SuppressWarnings("unused")
	public int[] getMyIP() {
		return myIP;
	}

	@SuppressWarnings("unused")
	public String getMyIPString() {
		return String.format(Locale.CANADA,"%d.%d.%d.%d", myIP[0], myIP[1], myIP[2], myIP[3]);
	}

	@SuppressWarnings("unused")
	public void setMyIPString(String string) {
		String[] parts = string.split("\\.");
		for (int i = 0; i < 4; i++) {
			myIP[i] = Integer.parseInt(parts[i]);
		}
	}

	@SuppressWarnings("unused")
	public void setMyIP(int[] myIP) {
		this.myIP = myIP;
	}


	/**
	 * iconSize
	 */
	private int iconSize = 100;

	public int getIconSize() {
		return iconSize;
	}

	public void setIconSize(int iconSize) {
		this.iconSize = iconSize;
	}


	/**
	 * connectionItems
	 */
	private ArrayList<ConnectionItem> connectionItems = new ArrayList<>();

	@SuppressWarnings("unused")
	public ArrayList<ConnectionItem> getConnectionItems() {
		return connectionItems;
	}

	@SuppressWarnings("unused")
	public void setConnectionItems(ArrayList<ConnectionItem> connectionItems) {
		this.connectionItems = connectionItems;
	}

	@SuppressWarnings("unused")
	public ConnectionItem getConnectItem(int index) {
		if (index >= getConnectionItems().size())
			return null;
		return connectionItems.get(index);
	}

	@SuppressWarnings("unused")
	public void addConnectionItem(ConnectionItem item) {
		if (item != null && connectionItems != null)
		connectionItems.add(item);
	}

	@SuppressWarnings("unused")
	public void removeConnectionItem(ConnectionItem item) {
		if (item != null && connectionItems != null)
		connectionItems.remove(item);
	}

	@SuppressWarnings("unused")
	public void removeConnectionItem(int index) {
		connectionItems.remove(index);
	}

	@SuppressWarnings("unused")
	public ConnectionItem getConnectionItemByID(int id) {
		for (ConnectionItem item : connectionItems) {
			if (item.getID() == id) {
				return item;
			}
		}
		return null;
	}


	/**
	 * dropbox for KonnectAndroid
	 */
	private final static String DROPBOX_KEY = "0qb7zuth7zcx40s";
	private final static String DROPBOX_SECRET = "16uik7f7abk0b8g";
	private static String DROPBOX_ACCESS_TOKEN = "";

	@SuppressWarnings("unused")
	public static String getDropboxKey() {
		return DROPBOX_KEY;
	}

	@SuppressWarnings("unused")
	public static String getDropboxSecret() {
		return DROPBOX_SECRET;
	}

	@SuppressWarnings("unused")
	public static String getDropboxAccessToken() {
		return DROPBOX_ACCESS_TOKEN;
	}

	@SuppressWarnings("unused")
	public static void setDropboxAccessToken(String dropboxAccessToken) {
		DROPBOX_ACCESS_TOKEN = dropboxAccessToken;
	}


	/**
	 * dimentions
	 */
	@SuppressWarnings("unused")
	public static int pxFrom8dp(Context context) {
		return Tools.dpToPx(8, context);
	}
	@SuppressWarnings("unused")
	public static int pxFrom10dp(Context context) {
		return Tools.dpToPx(10, context);
	}
	@SuppressWarnings("unused")
	public static int pxFrom16dp(Context context) {
		return Tools.dpToPx(16, context);
	}
	@SuppressWarnings("unused")
	public static int pxFrom18dp(Context context) {
		return Tools.dpToPx(18, context);
	}


	/**
	 * connections
	 */
	public static Drawable gdrive_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_google_drive)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable dropbox_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_dropbox)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable local_network_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_network_wired)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	@SuppressWarnings("unused")
	public static Drawable onedrive_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_cloud)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	@SuppressWarnings("unused")
	public static Drawable box_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_box)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	@SuppressWarnings("unused")
	public static Drawable ftp_icon(Context context) {
		Drawable ftp_d = context.getResources().getDrawable(R.drawable.ftp_connection);
		Bitmap ftp_b = ((BitmapDrawable)ftp_d).getBitmap();
		return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(ftp_b,
				64, 64, true));
	}
	@SuppressWarnings("unused")
	public static Drawable onedrive_icon2(Context context) {
		Drawable oneDrive_d = context.getResources().getDrawable(R.drawable.ms_onedrive);
		Bitmap oneDrive_b = ((BitmapDrawable)oneDrive_d).getBitmap();
		return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(oneDrive_b,
				64, 64, true));
	}
	@SuppressWarnings("unused")
	public static Drawable box_icon2(Context context) {
		Drawable box_d = context.getResources().getDrawable(R.drawable.box_icon);
		Bitmap box_b = ((BitmapDrawable)box_d).getBitmap();
		return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(box_b,
				64, 64, true));
	}
	@SuppressWarnings("unused")
	public static Drawable myPhotos_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_images)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable myMusic_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_music)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable myContacts_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_address_book)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable myFiles_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_file)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable myLocalHosts_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_network_wired)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable myPhotos_icon2(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_photo_video)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}

}

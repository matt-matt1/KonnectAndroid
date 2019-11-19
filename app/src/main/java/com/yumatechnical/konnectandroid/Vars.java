package com.yumatechnical.konnectandroid;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
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
import com.yumatechnical.konnectandroid.Model.SMBConnection;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.crypto.SecretKey;


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
	public static final int SMB_CONN = 6;
	public static final int FTP_COMM = 7;
	public static final int GDRIVE_CONN = 8;
	public static final int DBOX_CONN = 9;
	public static final int BOX_CONN = 10;
	public static final int ONED_CONN = 11;
	public static final int ICLOUD_CONN = 12;
	public static final int CONN_TO = 13;
	public static final int EDIT_CONN = 14;
	public static final int RENAME_CONN = 15;
	public static final int REMOVE_CONN = 16;
	public static final int MOVE_CONN = 17;
	public static int NUM_CONN;
	public LeftArrayAdapter leftAdapter;
	public ListItem tempListItem;
	public RightAdapter rightAdapter;
	public RecyclerView recyclerView;
	public static SecretKey secretKey;
	{
		try {
			secretKey = Tools.generateKey("QWERTY qwerty Qwerty QWerty QWErty QWERty QWERTy.");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}
	public boolean WifiConn = false;
	public boolean MobileConn = false;
	public boolean networkConnected = false;

	private static Vars instance = null;

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
	 * SMB
	 *
	 */
	private List<SMBConnection> connections = new ArrayList<>();

	public SMBConnection getConnection(int index) {
		if (connections.size() > 0 && index > -1 && index < connections.size())
			return connections.get(index);
		return null;
	}

	public void addConnection(SMBConnection connection) {
		this.connections.add(connection);
	}

	public void setConnectionShareNull(int index) {
		if (connections.size() > 0 && index > -1 && index < connections.size()) {
			connections.get(index).setShare(null);
		}
	}

	public void setConnectionDiskshareNull(int index) {
		if (connections.size() > 0 && index > -1 && index < connections.size()) {
			connections.get(index).setDiskShare(null);
		}
	}

	/**
	 * database of possible list items
	 */
	static final ArrayList<ListItem> sourceList = new ArrayList<>();
	static List<ListItem> addConnList = new ArrayList<>();


//	public ArrayList<ListItem> getSourceItems() {
//		return sourceList;
//	}
	/**
	 * adds an item to the source list
	 * @param item to add
	 * @return true if null, else false - no errors
	 */
/*	public boolean addSourceItem(ListItem item) {
		if (item != null) {
			return sourceList.add(item);
//			return false;
		}
		return true;
	}*/

	/**
	 * gets an item from the source list
	 * @param id of item
	 * @return item - if found, otherwise null
	 */
	public ListItem getSourceItemById(int id) {
		for (ListItem item : sourceList) {
			if (item.getID() == id)
				return item;
		}
		return null;
	}
	/* *
	 * removes an item from the source list
	 * @param item to remove
	 * @return true if null, else false - no errors
	 */
/*	public boolean removeSourceItem(ListItem item) {
		if (item != null) {
			return sourceList.remove(item);
//			return false;
		}
		return true;
	}*/
/**/
	public void initSourceList(Context context) {
		int leftListDefaultLeftPadding = Tools.dpToPx(16, context);
		int leftListDefaultTopPadding = Tools.dpToPx(16, context);
		int leftListDefaultBottomPadding = Tools.dpToPx(18, context);
		int leftListDefaultBetweenPadding = Tools.dpToPx(8, context);
		Boolean fade_photos = false, fade_music = false, fade_contacts = false, fade_files = false,
				fade_hosts = false;

		sourceList.add(new ListItem(0, MY_PHOTOS_ID, context.getString(R.string.photos),
				null, myPhotos_icon2(context), leftListDefaultLeftPadding,
				leftListDefaultTopPadding, leftListDefaultBottomPadding, true,
				leftListDefaultBetweenPadding, fade_photos, "", ""));
		sourceList.add(new ListItem(0, MY_MUSIC_ID, context.getString(R.string.music),
				null, myMusic_icon(context), leftListDefaultLeftPadding,
				leftListDefaultTopPadding, leftListDefaultBottomPadding, true,
				leftListDefaultBetweenPadding, fade_music, "", ""));
		sourceList.add(new ListItem(0, MY_CONTACTS_ID, context.getString(R.string.contacts),
				null, myContacts_icon(context), leftListDefaultLeftPadding,
				leftListDefaultTopPadding, leftListDefaultBottomPadding, true,
				leftListDefaultBetweenPadding, fade_contacts, "", ""));
		sourceList.add(new ListItem(0, MY_FILES_ID, context.getString(R.string.files),
				null, myFiles_icon(context), leftListDefaultLeftPadding,
				leftListDefaultTopPadding, leftListDefaultBottomPadding, true,
				leftListDefaultBetweenPadding, fade_files, "", ""));
		sourceList.add(new ListItem(0, MY_LOCAL_HOSTS, context.getString(R.string.my_network),
				null, myLocalHosts_icon(context), leftListDefaultLeftPadding,
				leftListDefaultTopPadding, leftListDefaultBottomPadding, true,
				leftListDefaultBetweenPadding, fade_hosts,"", ""));

		sourceList.add(new ListItem(0, GDRIVE_CONN, context.getString(R.string.gdrive),
				null, gdrive_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, DBOX_CONN, context.getString(R.string.dropbox),
				null, dropbox_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, BOX_CONN, context.getString(R.string.box),
				null, box_icon2(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, ONED_CONN, context.getString(R.string.onedrive),
				null, onedrive_icon2(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, ICLOUD_CONN, context.getString(R.string.icloud),
				null, icloud_icon(context), pxFrom16dp(context),
				pxFrom16dp(context), pxFrom18dp(context), true, pxFrom8dp(context),
				false, "", ""));
		sourceList.add(new ListItem(0, FTP_COMM, context.getString(R.string.ftp),
				null, ftp_icon(context), pxFrom16dp(context),
				pxFrom16dp(context), pxFrom18dp(context), true, pxFrom8dp(context),
				false, "", ""));
//		sourceList.add(new ListItem(0, FTP_COMM, context.getString(R.string.ftp),
//				null, network_server_icon(context), pxFrom16dp(context),
//				pxFrom16dp(context), pxFrom18dp(context), true, pxFrom8dp(context),
//				false, "", ""));
		sourceList.add(new ListItem(0, SMB_CONN, context.getString(R.string.local_network),
				null, local_network_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));

		sourceList.add(new ListItem(0, CONN_TO, Tools.toTitleCase(context.getString(R.string.connto)),
				null, conn_to_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, EDIT_CONN, Tools.toTitleCase(context.getString(R.string.edit)),
				null, edit_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, RENAME_CONN, Tools.toTitleCase(context.getString(R.string.rename, "")),
				null, rename_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, REMOVE_CONN, Tools.toTitleCase(context.getString(R.string.delete)),
				null, remove_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, MOVE_CONN, Tools.toTitleCase(context.getString(R.string.move)),
				null, move_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
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
	 * this device icons
	 *
	 * @param context context
	 * @return icon
	 */
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
		return new IconicsDrawable(context, FontAwesome.Icon.faw_sitemap)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable myPhotos_icon2(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_photo_video)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	/**
	 * connection icons
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
		return new IconicsDrawable(context, FontAwesome.Icon.faw_exchange_alt)//faw_network_wired
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
//	@SuppressWarnings("unused")
	public static Drawable ftp_icon(Context context) {
		Drawable ftp_d = context.getResources().getDrawable(R.drawable.ftp_connection);
		ftp_d.setColorFilter(context.getResources().getColor(R.color.tealAndroid), PorterDuff.Mode.SRC_IN);
		Bitmap ftp_b = ((BitmapDrawable)ftp_d).getBitmap();
		return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(ftp_b,
				64, 64, true));
	}
//	@SuppressWarnings("unused")
	public static Drawable onedrive_icon2(Context context) {
		Drawable oneDrive_d = context.getResources().getDrawable(R.drawable.ms_onedrive);
		Bitmap oneDrive_b = ((BitmapDrawable)oneDrive_d).getBitmap();
		return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(oneDrive_b,
				64, 64, true));
	}
//	@SuppressWarnings("unused")
	public static Drawable box_icon2(Context context) {
		Drawable box_d = context.getResources().getDrawable(R.drawable.box_icon);
		Bitmap box_b = ((BitmapDrawable)box_d).getBitmap();
		return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(box_b,
				64, 64, true));
	}
//	@SuppressWarnings("unused")
	public static Drawable icloud_icon(Context context) {
		Drawable drawable = context.getResources().getDrawable(R.drawable.apple_silver_icloud);
		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		return new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap,
				64, 64, true));
	}
	/**
	 * menu icons
	 *
	 * @param context context
	 * @return icon
	 */
	public static Drawable myHamburger_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_bars)
				.color(IconicsColor.colorRes(R.color.white)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable menu_add_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_plus)
				.color(IconicsColor.colorRes(R.color.white)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable menu_settings_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_sliders_h)
				.color(IconicsColor.colorRes(R.color.white)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable menu_resize_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_expand_arrows_alt)
				.color(IconicsColor.colorRes(R.color.white)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable conn_to_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_ellipsis_h)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable rename_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_newspaper1)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable edit_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_edit1)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable remove_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_eraser)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable move_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_arrows_alt)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
//	public static Drawable copyicon(Context context) {
//		return new IconicsDrawable(context, FontAwesome.Icon.faw_arrows_alt)
//				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE);
//	}
	/**
	 * default icons
	 *
	 * @param context context
	 * @return icon
	 */
	public static Drawable file_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_file)
				.color(IconicsColor.colorRes(R.color.gray_gray))
//				.size(IconicsSize.dp(100));
//				.size(IconicsSize.TOOLBAR_ICON_SIZE);
				.size(IconicsSize.dp(((Vars)context).getIconSize()));
	}
	public static Drawable folder_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_folder)
				.color(IconicsColor.colorRes(R.color.gray_gray))
//				.size(IconicsSize.dp(100));
//				.size(IconicsSize.TOOLBAR_ICON_SIZE);
				.size(IconicsSize.dp(((Vars)context).getIconSize()));
	}
	public static Drawable network_device_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_desktop)//faw_exchange_alt
				.color(IconicsColor.colorRes(R.color.gray_gray))
//				.size(IconicsSize.dp(100));
//				.size(IconicsSize.TOOLBAR_ICON_SIZE);
				.size(IconicsSize.dp(((Vars)context).getIconSize()));
	}
	public static Drawable network_server_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_server)
				.color(IconicsColor.colorRes(R.color.Teal))
				.size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	@SuppressWarnings("unused")
	public static Drawable contact_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_person_booth)
				.color(IconicsColor.colorRes(R.color.white))
//				.size(IconicsSize.TOOLBAR_ICON_SIZE);
				.size(IconicsSize.dp(((Vars)context).getIconSize()));
	}
	public static Drawable user_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_user_circle1)
				.color(IconicsColor.colorRes(R.color.white))
				.size(IconicsSize.dp(((Vars)context).getIconSize()));
//				.size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable img_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_image)
				.color(IconicsColor.colorRes(R.color.white))
				.size(IconicsSize.dp(((Vars) context).getIconSize()));
//				.size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable def_music_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_music)
				.color(IconicsColor.colorRes(R.color.white))
//				.size(IconicsSize.dp(100));
//				.size(IconicsSize.TOOLBAR_ICON_SIZE);
				.size(IconicsSize.dp(((Vars)context).getIconSize()));
	}
	/**
	 * other icons
	 *
	 * @param context context
	 * @return icon
	 */
	public static Drawable scoll_up_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_hand_point_up)
				.color(IconicsColor.colorRes(R.color.lightGray)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable scroll_down_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_hand_point_down)
				.color(IconicsColor.colorRes(R.color.lightGray)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable scroll_mid_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_ellipsis_v)
				.color(IconicsColor.colorRes(R.color.lightGray)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	public static Drawable success_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_check)
				.color(IconicsColor.colorRes(R.color.springGreen)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}
	@SuppressWarnings("unused")
	public static Drawable failed_icon(Context context) {
		return new IconicsDrawable(context, FontAwesome.Icon.faw_times)
				.color(IconicsColor.colorRes(R.color.springGreen)).size(IconicsSize.TOOLBAR_ICON_SIZE);
	}

	/**
	 * List of connections to add
	 */
	public static List<ListItem> fillAddConnections(Application context) {
		if (addConnList.size() > 0)
			return addConnList;
		ArrayList<ListItem> items = new ArrayList<>();
		items.add(((Vars)context).getSourceItemById(GDRIVE_CONN));
		items.add(((Vars)context).getSourceItemById(ONED_CONN));
		items.add(((Vars)context).getSourceItemById(DBOX_CONN));
		items.add(((Vars)context).getSourceItemById(BOX_CONN));
		items.add(((Vars)context).getSourceItemById(ICLOUD_CONN));
		items.add(((Vars)context).getSourceItemById(FTP_COMM));
		items.add(((Vars)context).getSourceItemById(SMB_CONN));
		addConnList = Collections.unmodifiableList(items);
		return items;
	}

//	protected Vars(){}
//	private Vars(){}
	public static synchronized Vars getInstance() {
		if (null == instance)
			instance = new Vars();
		return instance;
	}

}

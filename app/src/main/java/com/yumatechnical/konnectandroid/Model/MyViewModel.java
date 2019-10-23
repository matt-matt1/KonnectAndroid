package com.yumatechnical.konnectandroid.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.yumatechnical.konnectandroid.Adapter.LeftArrayAdapter;
import com.yumatechnical.konnectandroid.Adapter.RightAdapter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MyViewModel extends ViewModel {

	public static final int MY_PHOTOS_ID = 1;
	public static final int MY_MUSIC_ID = 2;
	public static final int MY_CONTACTS_ID = 3;
	public static final int MY_FILES_ID = 4;
	public LeftArrayAdapter leftAdapter;
	public ListItem tempListItem;
	public RightAdapter rightAdapter;
	public RecyclerView recyclerView;

	//connectivity
	private boolean WifiConn = false;
	private boolean MobileConn = false;
	private boolean networkConnected = false;

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

	//leftList
	private MutableLiveData<ArrayList<ListItem>> leftList;
	public LiveData<ArrayList<ListItem>> getLeftList() {
		if (leftList == null) {
			leftList = new MutableLiveData<>();
			loadULeftList();
		}
		return leftList;
	}

	private void loadULeftList() {
		// Do an asynchronous operation to fetch users.
	}

	public void setLeftList(ArrayList<ListItem> items) {
		if (leftList != null) {
			leftList.setValue(items);
		}
	}

	public void removeLeftListItem(ListItem item) {
		if (item != null && leftList != null) {
			Objects.requireNonNull(getLeftList().getValue()).remove(item);
		}
	}

	public void removeLeftListItem(int index) {
		if (leftList != null) {
			Objects.requireNonNull(getLeftList().getValue()).remove(index);
		}
	}

	public void addLeftListItem(ListItem item) {
		if (item != null && leftList != null) {
			Objects.requireNonNull(getLeftList().getValue()).add(item);
		}
	}

	public ListItem getLeftListItemByID(int id) {
//		for (ListItem item : leftList) {
		ArrayList<ListItem> arrayList = getLeftList().getValue();
		if (arrayList == null)
			return null;
		for (ListItem item : arrayList) {
			if (item.getID() == id) {
				return item;
			}
		}
		return null;
	}

	public int getLeftListItemNextID() {
		int id = 0;
		ArrayList<ListItem> arrayList = getLeftList().getValue();
		if (arrayList == null)
			return 0;
		for (ListItem item : arrayList) {
			if (item.getID() > id) {
				id = item.getID();
			}
		}
		return id+1;
	}

	//ip address
	private MutableLiveData<int[]> myIP;
	//	private int[] myIP = new int[4];
	public LiveData<int[]> getInt4IP() {
		if (myIP == null) {
			myIP = new MutableLiveData<>();
			loadMyIP();
		}
		return myIP;
	}

	public void loadMyIP() {
	}

	public void setInt4IP(int[] ip) {
		myIP.setValue(ip);
	}

	public int[] getMyIP() {
		return myIP.getValue();
	}

	public String getMyIPString() {
		int[] tempIP = myIP.getValue();//
		if (tempIP == null)//
			return null;//
//		return String.format(Locale.CANADA,"%d.%d.%d.%d", myIP[0], myIP[1], myIP[2], myIP[3]);
		return String.format(Locale.CANADA,"%d.%d.%d.%d", tempIP[0], tempIP[1], tempIP[2], tempIP[3]);
	}
	public void setMyIPString(String string) {
		String[] parts = string.split("\\.");
		int[] tempIP = new int[4];//
		for (int i = 0; i < 4; i++) {
//			myIP[i] = Integer.parseInt(parts[i]);
			tempIP[i] = Integer.parseInt(parts[i]);
		}
		myIP.setValue(tempIP);//
	}

	public void setMyIP(int[] ip) {
//		this.myIP = myIP;
		myIP.setValue(ip);
	}


	//iconSize
	private MutableLiveData<Integer> iconSize = new MutableLiveData<>();

	public LiveData<Integer> getIconSize() {
		if (iconSize == null) {
			iconSize = new MutableLiveData<>();
//			loadIconSize();
		}
		return iconSize;
	}

	public void setIconSize(Integer myIconSize) {
//		this.iconSize = iconSize;
		iconSize.setValue(myIconSize);
	}


	//connectionItems
	private MutableLiveData<ArrayList<ConnectionItem>> connectionItems = new MutableLiveData<>();

	public LiveData<ArrayList<ConnectionItem>> getConnectionItems() {
		if (connectionItems == null) {
			connectionItems = new MutableLiveData<>();
//			loadConnectionItems();
		}
		return connectionItems;
	}

	public void setConnectionItems(ArrayList<ConnectionItem> items) {
		connectionItems.setValue(items);
	}

	public ConnectionItem getConnectItem(int index) {
		ArrayList<ConnectionItem> items = connectionItems.getValue();
		if (items == null || index > items.size())
			return null;
		return items.get(index);
	}

	public void addConnectionItem(ConnectionItem item) {
		if (item != null && connectionItems != null) {
			ArrayList<ConnectionItem> items = connectionItems.getValue();
			items.add(item);
		}
	}

	public void removeConnectionItem(ConnectionItem item) {
		if (item != null && connectionItems != null) {
			ArrayList<ConnectionItem> items = connectionItems.getValue();
			items.remove(item);
		}
	}

	public void removeConnectionItem(int index) {
		ArrayList<ConnectionItem> items = connectionItems.getValue();
		if (index < items.size()) {
			items.remove(index);
		}
	}

	public ConnectionItem getConnectionItemByID(int id) {
		ArrayList<ConnectionItem> arrayList = getConnectionItems().getValue();
		if (arrayList == null)
			return null;
		for (ConnectionItem item : arrayList) {
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
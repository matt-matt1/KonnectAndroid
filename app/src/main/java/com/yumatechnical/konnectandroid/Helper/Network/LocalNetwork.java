package com.yumatechnical.konnectandroid.Helper.Network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.yumatechnical.konnectandroid.Model.FileItem;
import com.yumatechnical.konnectandroid.Model.IPdetail;
import com.yumatechnical.konnectandroid.Vars;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.net.telnet.TelnetCommand.IP;


public class LocalNetwork {

	private static final String TAG = LocalNetwork.class.getSimpleName();
//	private MyViewModel model;
	private static List<IPdetail> iPdetails = new ArrayList<>();


	/**
	 * needs: android.permission.ACCESS_WIFI_STATE
	 */
	@SuppressWarnings("unused")
	public static class AsyncMacName extends AsyncTask<Void, Void, Void> {

//		private static final String TAG = macName.class.getSimpleName();
		private WeakReference<Context> mContextRef;
		private String mac;


		@SuppressWarnings("unused")
		public AsyncMacName(Context context, String macAddress) {
			mContextRef = new WeakReference<>(context);
			this.mac = macAddress;
		}

		@Override
		protected Void doInBackground(Void... voids) {
			macToManufacturor(mac);
			return null;
		}

	}

	@SuppressWarnings("unused")
	public static void macToManufacturor(String macAddress) {
//		String macAdress = "5caafd1b0019";
		Log.d(TAG, "macToManufacturor for "+ macAddress);
		String dataUrl = "http://api.macvendors.com/" + macAddress;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(dataUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.flush();
			wr.close();
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {response.append(line);response.append('\r');}
			rd.close();
			String responseStr = response.toString();
			Log.d("Server response", responseStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}


	/**
	 * needs: android.permission.ACCESS_WIFI_STATE
	 */
	public static class ConnectionInfoTask extends AsyncTask<Void, Void, Void> {

		private static final String TAG = ConnectionInfoTask.class.getSimpleName();
		private WeakReference<Context> mContextRef;

		public interface OnNetworkConnectionInfo {
			void WifiConnected(boolean isConnection);
			void MobileConnected(boolean isConnection);
			void NetworkConnected(boolean isConnected);
		}
		public OnNetworkConnectionInfo listener;
		boolean networkConnection = false;
		boolean wifiConnection = false;
		boolean mobileConnection = false;
//		private MyViewModel model;


		public ConnectionInfoTask(Context context, OnNetworkConnectionInfo listener) {
			mContextRef = new WeakReference<>(context);
//			model = ViewModelProviders.of((FragmentActivity) this).get(MyViewModel.class);
			this.listener = listener;
		}

		@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
		@Override
		protected Void doInBackground(Void... voids) {
			Log.d(TAG, "Let's gather the network connection info");
			try {
				Context context = mContextRef.get();
				if (context != null) {
					ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo networkInfo1 = null;
					if (cm != null) {
						networkInfo1 = cm.getActiveNetworkInfo();
					}
					if (networkInfo1 != null && networkInfo1.isConnected()) {
						networkConnection = true;
					}
					NetworkInfo activeNetwork;
					if (cm != null) {
						activeNetwork = cm.getActiveNetworkInfo();
						Log.d(TAG, "activeNetwork: "+ activeNetwork);
						for (Network network : cm.getAllNetworks()) {
							NetworkInfo networkInfo = cm.getNetworkInfo(network);
							if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
								wifiConnection |= networkInfo.isConnected();
//								wifiConnection = !networkInfo.isConnected();
//								((Vars)mContextRef.get()).setWifiConn(wifiConnection);
//								model.setWifiConn(wifiConnection);
							}
							if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
								mobileConnection |= networkInfo.isConnected();
//								mobileConnection = !networkInfo.isConnected();
//								((Vars)mContextRef.get()).setMobileConn(mobileConnection);
//								model.setMobileConn(mobileConnection);
							}
						}
						Log.d(TAG, "Wifi connected: "+ wifiConnection);
						Log.d(TAG, "Mobile connected: "+ mobileConnection);
					}
					WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

					WifiInfo connectionInfo;
					if (wm != null) {
						connectionInfo = wm.getConnectionInfo();
						int ipInt = connectionInfo.getIpAddress();
						if (ipInt == 0) {
							Log.d(TAG, "cannot get IP on emulator");
							return null;
						}
//						Log.d(TAG, "ip as int: "+ ipInt);
						String ipString = InetAddress.getByAddress(
								ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array())
								.getHostAddress();
						Log.d(TAG, "IP Address: "+ ipString);
						Vars.getInstance().setMyIPString(ipString);
//						((Vars)context).setMyIPString(ipString);
//						model.setMyIPString(ipString);
					}
//					int ipAddress = connectionInfo.getIpAddress();
				}
			} catch (Throwable t) {
				Log.e(TAG, "Well that's not good.", t);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			listener.NetworkConnected(networkConnection);
			listener.WifiConnected(wifiConnection);
			listener.MobileConnected(mobileConnection);
		}
	}

	/**
	 * needs: android.permission.ACCESS_WIFI_STATE
	 */
	@SuppressWarnings("unused")
	public static class NetworkSniffTask extends AsyncTask<Void, Void, Void> {

		private static final String TAG = NetworkSniffTask.class.getSimpleName();
		private WeakReference<Context> mContextRef;
//		private MyViewModel model;
		private ArrayList<FileItem> listofLocalHosts = new ArrayList<>();


		@SuppressWarnings("unused")
		public NetworkSniffTask(Context context) {
			mContextRef = new WeakReference<>(context);
		}

		@Override
		protected Void doInBackground(Void... voids) {
			doSniff(mContextRef);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
//			if (listofLocalHosts.size() < 0) {
//
//			} else {
//				((RightFragment) Fragment)
//			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}

	public static void doSniff(WeakReference<Context> myContext) {
//		Log.d(TAG, "Let's sniff the network");
		try {
			Context context = myContext.get();
			if (context != null) {
				ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetwork;
				if (cm != null) {
					activeNetwork = cm.getActiveNetworkInfo();
					Log.d(TAG, "activeNetwork: "+ activeNetwork);
				}
				WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
				WifiInfo connectionInfo;
				if (wm != null) {
					connectionInfo = wm.getConnectionInfo();
					int ipInt = connectionInfo.getIpAddress();
					if (ipInt == 0) {
						Log.d(TAG, "cannot work on emulator");
						return;
					}
					Log.d(TAG, "ip as int: "+ ipInt);
					String ipString = InetAddress.getByAddress(
							ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array())
							.getHostAddress();
					Log.d(TAG, "ipString: "+ ipString);
					String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
					Log.d(TAG, "prefix: " + prefix);
					for (int i = 0; i < 255; i++) {
						String testIp = prefix + i;
						InetAddress address = InetAddress.getByName(testIp);
//						boolean reachable = address.isReachable(1000);
						boolean reachable = testAddress(testIp);
						String hostName = address.getCanonicalHostName();
						String mac = getMacAddressFromARPTable(testIp);
						if (reachable) {
							if (testIp.equals(ipString))
								mac = getMyMacAddr();
							Log.i(TAG, "Host: "+ hostName+ "("+ testIp + ")("+ mac+ ") is reachable!");
						}
					}
				}
			}
		} catch (Throwable t) {
			Log.e(TAG, "Well that's not good.", t);
		}
	}

	public static boolean testAddress(String address) {
		int returnVal = 0;
		Process p1;
		try {
			p1 = Runtime.getRuntime().exec("ping -c 1 "+ address);
			//...exec("ping -n 1 www.google.com");
			returnVal = p1.waitFor();
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return (returnVal==0);
	}

	public static boolean isReachable2(String addr, int openPort, int timeOutMillis) {
		try {
			try (Socket soc = new Socket()) {
				soc.connect(new InetSocketAddress(addr, openPort), timeOutMillis);
			}
			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	public static String getMyMacAddr() {
		try {
			List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface nif : all) {
				if (!nif.getName().equalsIgnoreCase("wlan0"))
					continue;
				byte[] macBytes = nif.getHardwareAddress();
				if (macBytes == null) {
					return "";
				}
				StringBuilder res1 = new StringBuilder();
				for (byte b : macBytes) {
					res1.append(String.format("%02X:",b));
				}
				if (res1.length() > 0) {
					res1.deleteCharAt(res1.length() - 1);
				}
				return res1.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "02:00:00:00:00:00";
	}

	public static void makeArrayFromARPTable() {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("IP"))
					continue;
				String[] splitted = line.split("\\s+");
				String ip = splitted[0];
//					int hwType = Integer.decode(splitted[1]);
//					int flags = Integer.decode(splitted[2]);
				String mac = splitted[3];
//					String mask = splitted[4];
//					String device = splitted[5];
//					iPdetails.add(new IPdetail(ip, hwType, flags, mac, mask, device));
				iPdetails.add(new IPdetail(ip, Integer.decode(splitted[1]),
						Integer.decode(splitted[2]), mac.toUpperCase(), splitted[4], splitted[5]));
//					Log.i(TAG, "getMacAddressFromIP: "+ cnt+ ":"+ ip+ " has mac "+ mac);
//					Log.i(TAG, "getMacAddressFromARPTable: " + ip + "-" + hwType + "-" +
//							flags + "-" + mac + "-" + mask + "-" + device + "-");
//					}
			}//while
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Find device mac address within ARP table
	 */
	public static String getMacAddressFromARPTable(@NonNull String ipFinding)
	{
		for (IPdetail detail : iPdetails) {
			if (detail.getAddress().equals(ipFinding))
				return detail.getMac();
		}
		return "unknown";
//		return "00:00:00:00:00:00";
	}


	@SuppressLint("DefaultLocale")
	@SuppressWarnings("unused")
	private String getSubnetAddress (int address)
	{
		return String.format(
				"%d.%d.%d",
				(address & 0xff),
				(address >> 8 & 0xff),
				(address >> 16 & 0xff));
	}

	/**
	 * needs: android.permission.ACCESS_WIFI_STATE
	 */

	@SuppressWarnings("unused")
	private void checkHosts(String subnet)
	{
		try
		{
			int timeout = 5;
			for (int i=1; i<255; i++)
			{
				String host = subnet + "." + i;
				if (InetAddress.getByName(host).isReachable(timeout))
				{
					Log.d(TAG, "checkHosts() :: "+host + " is reachable");
				}
			}
		}
		catch (UnknownHostException e)
		{
			Log.d(TAG, "checkHosts() :: UnknownHostException e : "+e);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Log.d(TAG, "checkHosts() :: IOException e : "+e);
			e.printStackTrace();
		}
	}

	private ArrayList<IpAddress> mIpAddressesList;

	@SuppressWarnings("unused")
	private boolean getIpFromArpCache()
	{
		BufferedReader br = null;
		char[] buffer = new char[65000];
		String currentLine;
		try
		{
			br = new BufferedReader(new FileReader("/proc/net/arp"));

			while ((currentLine = br.readLine()) !=  null)
			{
				Log.d(TAG, "getIpFromArpCache() :: "+ currentLine);

				String[] splitted = currentLine.split(" +");
				if (splitted != null && splitted.length >= 4)
				{
					String ip = splitted[0];
					String mac = splitted[3];
					if (!splitted[3].equals(""))//emptyMac))
					{
						if (!splitted[0].equals(IP))
						{
							//                          int remove = mac.lastIndexOf(':');
							//                          mac = mac.substring(0,remove) + mac.substring(remove+1);
							mac = mac.replace(":", "");
							Log.i(TAG, "getIpFromArpCache() :: ip : "+ip+" mac : "+mac);
							mIpAddressesList.add(new IpAddress(ip, mac));
						}
					}
				}
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (br != null) {
					br.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}


	public class IpAddress
	{
		private String ipAddressName;
		private String macAddress;


		@SuppressWarnings("unused")
		IpAddress(String ipAddressName, String macAddress)
		{
			setIpAddressName(ipAddressName);
			setMacAddress(macAddress);
		}

		@SuppressWarnings("unused")
		void setIpAddressName(String ipAddressName)
		{
			this.ipAddressName = ipAddressName;
		}

		@SuppressWarnings("unused")
		public String getIpAddressName()
		{
			return this.ipAddressName;
		}


		@SuppressWarnings("unused")
		void setMacAddress(String macAddress)
		{
			this.macAddress = macAddress;
		}


		@SuppressWarnings("unused")
		public String getMacAddress()
		{
			return this.macAddress;
		}
	}
}

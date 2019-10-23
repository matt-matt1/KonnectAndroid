package com.yumatechnical.konnectandroid.Helper.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.yumatechnical.konnectandroid.Model.MyViewModel;
import com.yumatechnical.konnectandroid.Vars;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import static org.apache.commons.net.telnet.TelnetCommand.IP;

public class LocalNetwork {

	private static final String TAG = LocalNetwork.class.getSimpleName();
//	private MyViewModel model;


	/**
	 * needs: android.permission.ACCESS_WIFI_STATE
	 */
	public static class AsyncMacName extends AsyncTask<Void, Void, Void> {

//		private static final String TAG = macName.class.getSimpleName();
		private WeakReference<Context> mContextRef;
		private String mac;


		public AsyncMacName(Context context, String macAddress) {
			mContextRef = new WeakReference<Context>(context);
			this.mac = macAddress;
		}

		@Override
		protected Void doInBackground(Void... voids) {
			macToManufacturor(mac);
			return null;
		}

	}

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
			StringBuffer response = new StringBuffer();
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
		public OnNetworkConnectionInfo listener = null;
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
					NetworkInfo networkInfo1 = cm.getActiveNetworkInfo();
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
	public static class NetworkSniffTask extends AsyncTask<Void, Void, Void> {

		private static final String TAG = NetworkSniffTask.class.getSimpleName();
		private WeakReference<Context> mContextRef;
		private MyViewModel model;


		public NetworkSniffTask(Context context) {
			mContextRef = new WeakReference<Context>(context);
		}

		@Override
		protected Void doInBackground(Void... voids) {
			Log.d(TAG, "Let's sniff the network");
			try {
				Context context = mContextRef.get();
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
							return null;
						}
						Log.d(TAG, "ip as int: "+ ipInt);
						String ipString = InetAddress.getByAddress(
								ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array())
								.getHostAddress();

						Log.d(TAG, "ipString: "+ ipString);
//						((Vars)context).setMyIPString(ipString);
//						mo.setMyIPString(ipString);

						String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
						Log.d(TAG, "prefix: " + prefix);

						for (int i = 0; i < 255; i++) {
							String testIp = prefix + i;

							InetAddress address = InetAddress.getByName(testIp);
							boolean reachable = address.isReachable(1000);
							String hostName = address.getCanonicalHostName();

							if (reachable)
								Log.i(TAG, "Host: "+ hostName + "("+ testIp+ ") is reachable!");
						}
					}
//					int ipAddress = connectionInfo.getIpAddress();
				}
			} catch (Throwable t) {
				Log.e(TAG, "Well that's not good.", t);
			}

			return null;
		}
	}

/*
	private String getMyIP() {
		String ip = "";
		Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();

		while (enumNetworkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
			Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();

			while (enumInetAddress.hasMoreElements()) {
				InetAddress inetAddress = enumInetAddress.nextElement();
				String ipAddress = "";
				if (inetAddress.isSiteLocalAddress()) {
					ipAddress = "SiteLocalAddress: ";
				}
				ip += ipAddress + inetAddress.getHostAddress() + "\n";
				String subnet = getSubnetAddress(ip);
			}
		}
	}
*/
	private String getSubnetAddress (int address)
	{
		String ipString = String.format(
				"%d.%d.%d",
				(address & 0xff),
				(address >> 8 & 0xff),
				(address >> 16 & 0xff));

		return ipString;
	}

	/**
	 * needs: android.permission.ACCESS_WIFI_STATE
	 */
//	private void myIP() {
//		WifiManager mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//		WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
//		String subnet = getSubnetAddress(mWifiManager.getDhcpInfo().gateway);
//	}

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

	ArrayList<IpAddress> mIpAddressesList;

	private boolean getIpFromArpCache()
	{
		BufferedReader br = null;
		char buffer[] = new char[65000];
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
				br.close();
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


		public IpAddress(String ipAddressName, String macAddress)
		{
			setIpAddressName(ipAddressName);
			setMacAddress(macAddress);
		}

		public void setIpAddressName(String ipAddressName)
		{
			this.ipAddressName = ipAddressName;
		}

		public String getIpAddressName()
		{
			return this.ipAddressName;
		}


		public void setMacAddress(String macAddress)
		{
			this.macAddress = macAddress;
		}


		public String getMacAddress()
		{
			return this.macAddress;
		}
	}
}

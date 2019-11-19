package com.yumatechnical.konnectandroid;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMBApiException;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.rapid7.client.dcerpc.mssrvs.ServerService;
import com.rapid7.client.dcerpc.transport.RPCTransport;
import com.rapid7.client.dcerpc.transport.SMBTransportFactories;
import com.yumatechnical.konnectandroid.Helper.Network.SMBoperation;
import com.yumatechnical.konnectandroid.Model.SMBConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SMB {

	/**/
//	private List<SMBConnection> connections = new ArrayList<>();
//
//	public SMBConnection getConnection(int index) {
//		if (connections.size() > 0 && index > -1 && index < connections.size()) {
//			return connections.get(index);
//		}
//		return null;
//	}
//
//	public void addConnection(SMBConnection connection) {
//		this.connections.add(connection);
//	}
	/**/

	private final static String TAG = SMB.class.getSimpleName();
	private getListAsyncTask task = null;
	/**
	 * level of debugging
	 * 9 = verbose - all messages
	 * 8 = trying new connections, etc. & down
	 * 7 = new successful connections, etc. & down
	 * 3 = warning details & down
	 * 2 = warnings & down
	 * 1 = errors
	 * 0 = none
	 */
	private int DEBUG_LEVEL = 1;

	public int getDebugLevel() {
		return DEBUG_LEVEL;
	}

	public void setDebugLevel(int LEVEL) {
		this.DEBUG_LEVEL = LEVEL;
	}

	/**
	 * public listener that is called when results are given
	 */
	public interface OnSMBResult {
		void listedFiles(List<Object> files, int connectionIndex);
//		void listedFiles(List<FileIdBothDirectoryInformation> files);
	}
	private OnSMBResult listener;


	public void setListener(OnSMBResult listener) {
		this.listener = listener;
	}

	/**
	 * public method to initiate a SMB list files async call
	 * (when results are obtained, the listener is called)
	 *
	 * @param connectionIndex int - 0 for new connection, otherwise an index of connections
	 * @param server string
	 * @param domain optional
	 * @param username optional
	 * @param password optional
	 * @param share string
	 * @param path optional
	 * @return connectionIndex number (only needed for persistent connections)
	 */
	public int getList(int connectionIndex, String server, String domain,
	                     String username, String password, String share, String path) {
		task = (getListAsyncTask)new getListAsyncTask().execute(String.valueOf(connectionIndex),
				server, domain, username, password, share, path);
//		return (connectionIndex != 0) ? connectionIndex : connections.size();
		return Math.max(0, connectionIndex);
	}
	public void cancelGetList() {
		if (task == null) {
			if (DEBUG_LEVEL == 1)
				Log.d(TAG, "AsyncTask is not started");
		} else task.cancel(true);
	}

	@SuppressLint("StaticFieldLeak")
	private class getListAsyncTask extends AsyncTask<String, Void, Void> {

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
		@Override
		protected Void doInBackground(String... strings) {
			if (!isCancelled()) {
				int index = -1;
				try {
					index = Integer.parseInt(strings[0]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				try {
					String server = strings[1];
					String domain = strings[2];
					String usern = strings[3];
					String passwd = strings[4];
					String sharen = strings[5];
					String path = strings[6];
					return listSMBFiles(index, server, domain, usern, passwd, sharen, path);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			return null;
		}

	}

	/**
	 * Calls the listener when a susscessful SMB connection, session and authentication is made
	 * @param connectionIndex - 0 for a new connection; or number of a saved persistent connection
	 * @param server or hostname
	 * @param domain optional
	 * @param username optional
	 * @param password optional
	 * @param share string
	 * @param path string
	 * @return null
	 */
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	private Void listSMBFiles(int connectionIndex, String server, String domain,
	                          String username, String password, String share, String path) {
		List<Object> objects = new ArrayList<>();
		SMBConnection connection = new SMBConnection();
		if (connectionIndex > -1)
			connection = Vars.getInstance().getConnection(connectionIndex);
		if (server.equals("") && (connection.getServer() == null || connection.getServer().equals(""))) {
			if (DEBUG_LEVEL == 1)
				Log.d(TAG, "listSMBFiles connection needs server/host name");
			objects.add("listSMBFiles connection needs server/host name");
			listener.listedFiles(objects, connectionIndex);
			return null;
		} else
			connection.setServer(server);
//				|| connection.getUsername() == null || connection.getUsername().equals("")
//				|| connection.getPassword() == null || connection.getPassword().equals("")
//				|| connection.getDomain() == null || connection.getDomain().equals("")
//				|| connection.getWorkgroup() == null || connection.getWorkgroup().equals("")
/*		if (share.equals("") && (connection.getShare() == null || connection.getShare().equals(""))) {
		}*/
		if (connection.getClient() == null) {
			if (DEBUG_LEVEL <= 8)
				Log.d(TAG, "listSMBFiles getting new client...");
			if (connection.getConfig() == null) {
//				connection.setConfig(new SmbConfig());
				connection.setClient(new SMBClient());
			} else
				connection.setClient(new SMBClient(connection.getConfig()));
			if (DEBUG_LEVEL <= 7)
				Log.d(TAG, "listSMBFiles client: "+ connection.getClient().getPathResolver().toString());
		}
		if (connection.getConnection() == null) {
			if (DEBUG_LEVEL <= 8)
				Log.d(TAG, "listSMBFiles getting new connection...");
			try {
				connection.setConnection(connection.getClient().connect(connection.getServer()));
				if (DEBUG_LEVEL <= 7)
					Log.d(TAG, "listSMBFiles connection to "+ connection.getConnection().getRemoteHostname());
			} catch (IOException e) {
				if (DEBUG_LEVEL <= 2)
					Log.d(TAG, "listSMBFiles connection warning: "+ e.getLocalizedMessage());
				if (DEBUG_LEVEL <= 3)
					e.printStackTrace();
				objects.add(e.getLocalizedMessage());
				listener.listedFiles(objects, connectionIndex);
				return null;
			}
		}
		if (connection.getAuthenticationContext() == null) {
			if (DEBUG_LEVEL <= 8)
				Log.d(TAG, "listSMBFiles getting new authentication...");
			if (!username.equals("")) {
				connection.setUsername(username);
				if (!password.equals(""))
					connection.setPassword(password);
			}
			if (!domain.equals(""))
				connection.setDomain(domain);
			else connection.setDomain("");
			connection.setAuthenticationContext(new AuthenticationContext(
					connection.getUsername(), connection.getPassword().toCharArray(),
					connection.getDomain()));
			if (DEBUG_LEVEL <= 7) {
				if (connection.getDomain() != null && connection.getDomain().equals(""))
					Log.d(TAG, "listSMBFiles authenticated " + connection.getAuthenticationContext().getDomain()
						+ "\\" + connection.getAuthenticationContext().getUsername());
				else
					Log.d(TAG, "listSMBFiles authenticated "+ connection.getAuthenticationContext().getUsername());
			}
		}
		if (connection.getSession() == null) {
			if (DEBUG_LEVEL <= 8)
				Log.d(TAG, "listSMBFiles getting new session...");
			connection.setSession(connection.getConnection().authenticate(connection.getAuthenticationContext()));
			if (DEBUG_LEVEL <= 7)
				Log.d(TAG, "listSMBFiles session established ("+ connection.getSession().getSessionId()+ ")");
		}
		if (connection.getDiskShare() == null) {
			if (DEBUG_LEVEL <= 8)
				Log.d(TAG, "listSMBFiles getting new diskshare...");
			if (connection.getShare() != null) {
				connection.setDiskShare((DiskShare) connection.getSession().connectShare(connection.getShare()));
				if (DEBUG_LEVEL <= 7)
					Log.d(TAG, "listSMBFiles diskshare from saved share (" + connection.getShare() + ")");
			}
			else if (share != null && !share.equals("")) {
				connection.setDiskShare((DiskShare) connection.getSession().connectShare(share));
				if (DEBUG_LEVEL <= 7)
					Log.d(TAG, "listSMBFiles diskshare from given share ("+ share+ ")");
				connection.setShare(share);
			} else {
				getShares(connection.getSession(), Math.max(0, connectionIndex));
				if (DEBUG_LEVEL <= 7)
					Log.d(TAG, "listSMBFiles listing available shares instead");
				Vars.getInstance().addConnection(connection);
				return null;
			}
			if (DEBUG_LEVEL <= 7)
				Log.d(TAG, "listSMBFiles share total space: "+ connection.getDiskShare().getShareInformation().getTotalSpace());
		}
		List<FileIdBothDirectoryInformation> files = new ArrayList<>();
		if (path.equals("") || path.equals("/"))
			path = "";
//			path = "\\";
		connection.setPath(path);
//		String dir = connection.getPath() == null ? path : connection.getPath();
//		String dir = connection.getPath() == null ? "" : connection.getPath();
//		if (DEBUG_LEVEL <= 8)
//			Log.d(TAG, "listSMBFiles getting list of files ("+ dir+ ")");
//		files = connection.getDiskShare().list(dir);
		if (DEBUG_LEVEL <= 8)
			Log.d(TAG, "listSMBFiles getting list of files ("+ connection.getPath()+ "):");
		try {
			files = connection.getDiskShare().list(connection.getPath());
			objects = new ArrayList<>(files);
		} catch (SMBApiException e) {
			objects.add(e);
		}
		Vars.getInstance().addConnection(connection);
		listener.listedFiles(objects, Math.max(0, connectionIndex));
		if (DEBUG_LEVEL <= 7) {
//				for (Object obj : objects) {
//					FileIdBothDirectoryInformation file = (FileIdBothDirectoryInformation)obj;
//					Log.d(TAG, "listSMBFiles file: "+ file.getFileName());
//				}
			for (FileIdBothDirectoryInformation file : files) {
				Log.d(TAG, "listSMBFiles file: "+ file.getFileName());
			}
		}
		return null;
	}


	private void getShares(Session session, int connectionIndex) {
		final RPCTransport transport;
		try {
			transport = SMBTransportFactories.SRVSVC.getTransport(session);
			final ServerService serverService = new ServerService(transport);
			List<Object> objects = new ArrayList<>(serverService.getShares0());
			objects.addAll(serverService.getShares0());
			listener.listedFiles(objects, connectionIndex);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	private Void listSharesMSSRVS(int connectionIndex, String server, String username,
	                              String password, String domain) {
		SMBConnection connection = new SMBConnection();
		if (connectionIndex > 0)
			connection = Vars.getInstance().getConnection(connectionIndex);
		if (server.equals("") && (connection.getServer() == null || connection.getServer().equals(""))) {
			if (DEBUG_LEVEL == 1)
				Log.d(TAG, "listSMBFiles connection needs server/host name");
			return null;
		}
		if (connection.getClient() == null) {
			if (DEBUG_LEVEL <= 8)
				Log.d(TAG, "listSMBFiles getting new client");
			connection.setClient(new SMBClient());
			if (DEBUG_LEVEL <= 7)
				Log.d(TAG, "listSMBFiles client: "+ connection.getClient().toString());
		}
		if (connection.getConnection() == null) {
			if (DEBUG_LEVEL <= 8)
				Log.d(TAG, "listSMBFiles getting new connection");
			try {
				connection.setConnection(connection.getClient().connect(
						connection.getServer() == null || connection.getServer().equals("") ? server
								: connection.getServer()));
				if (DEBUG_LEVEL <= 7)
					Log.d(TAG, "listSMBFiles connection to "+ connection.getConnection().getRemoteHostname());
			} catch (IOException e) {
				if (DEBUG_LEVEL <= 2)
					Log.d(TAG, "listSMBFiles connection warning: "+ e.getLocalizedMessage());
				if (DEBUG_LEVEL <= 3)
					e.printStackTrace();
			}
		}
		if (connection.getAuthenticationContext() == null) {
			if (DEBUG_LEVEL <= 8)
				Log.d(TAG, "listSMBFiles getting new authentication");
			connection.setAuthenticationContext(new AuthenticationContext(
					connection.getUsername() == null ? username : connection.getUsername(),
					connection.getPassword() == null ? password.toCharArray() : connection.getPassword().toCharArray(),
					connection.getDomain() == null ? domain : connection.getDomain()));
			if (DEBUG_LEVEL <= 7)
				Log.d(TAG, "listSMBFiles authenticated "+ connection.getAuthenticationContext().getDomain()
						+ "\\"+ connection.getAuthenticationContext().getUsername());
		}
		if (connection.getSession() == null) {
			if (DEBUG_LEVEL <= 8)
				Log.d(TAG, "listSMBFiles getting new session");
			connection.setSession(connection.getConnection().authenticate(connection.getAuthenticationContext()));
			if (DEBUG_LEVEL <= 7)
				Log.d(TAG, "listSMBFiles session established ("+ connection.getSession().getSessionId()+ ")");
		}

		getShares(connection.getSession(), connectionIndex);
		return null;
	}

	public void listShares(String server, String username, String password, String domain,
	                       SMB.OnSMBResult listener) {
		this.listener = listener;
		new AsyncSMBlistShares().execute(server, username, password, domain);
	}


	@SuppressLint("StaticFieldLeak")
	public class AsyncSMBlistShares extends AsyncTask<String, Void, Void> {

		private final String TAG = SMBoperation.AsyncSMBlistShares.class.getSimpleName();

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
		@Override
		protected Void doInBackground(String... strings) {
			if (!isCancelled()) {
				int index = 0;
				try {
					index = Integer.parseInt(strings[0]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
				try {
					String server = strings[1];
					String domain = strings[2];
					String usern = strings[3];
					String passwd = strings[4];
					return listSharesMSSRVS(index, server, usern, passwd, domain);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			return null;
		}

	}

}

package com.yumatechnical.konnectandroid.Helper.Network;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMBApiException;
import com.hierynomus.protocol.commons.buffer.Buffer;
import com.hierynomus.protocol.transport.TransportException;
import com.hierynomus.security.SecurityProvider;
import com.hierynomus.security.bc.BCSecurityProvider;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.auth.NtlmAuthenticator;
import com.hierynomus.smbj.common.SMBException;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.rapid7.client.dcerpc.mssrvs.ServerService;
import com.rapid7.client.dcerpc.mssrvs.dto.NetShareInfo0;
import com.rapid7.client.dcerpc.transport.RPCTransport;
import com.rapid7.client.dcerpc.transport.SMBTransportFactories;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SMBoperation {

	private long timeout, soTimeout;
	private TimeUnit units = null, soUnits = null;
	private SMBClient client = new SMBClient();
	private Session session;
	private SMBClient smbClient = new SMBClient();
	private static final String TAG = SMBoperation.class.getSimpleName();

	public interface OnSMBinteraction1 {
		void OnConnection(Session result);
		void OnListFiles(DiskShare files);
	}
	private OnSMBinteraction1 listener1;

	public interface OnSMBinteraction {
		void OnListFiles(List<String> files);
		void OnListFilesFull(List<FileIdBothDirectoryInformation> files);
	}
	private OnSMBinteraction filesListener;

	public interface OnSMBshares {
		//		void OnListShares(List<NetShareInfo0> files);
		void OnListShares(ArrayList<String> shares);
	}
	private OnSMBshares sharesListener;


	public void configSMB(boolean signingRequired, boolean ofsEnabled, boolean multiProtocolNegotiate,
	                      boolean DFSenabled, int bufferSize, long timeout, TimeUnit units,
	                      long soTimeout, TimeUnit soUnits, SecurityProvider securityProvider) {
		SmbConfig config = SmbConfig.builder()
				.withSigningRequired(signingRequired)
				.withDfsEnabled(ofsEnabled)
				.withMultiProtocolNegotiate(multiProtocolNegotiate)
				.withBufferSize(bufferSize)
				.withTransactTimeout(timeout, units)
				.withDfsEnabled(DFSenabled)
				.withSoTimeout(soTimeout, soUnits)
//				.withSecurityProvider(securityProvider)
				.build();
		this.smbClient = new SMBClient(config);
	}

	@SuppressWarnings("unused")
	public SMBoperation() {}

	@SuppressWarnings("unused")
	public SMBoperation(OnSMBinteraction1 listener) {
		this.listener1 = listener;
	}

	@SuppressWarnings("unused")
	public SMBoperation(OnSMBinteraction1 listener, long timeout, TimeUnit units, long soTimeout, TimeUnit soUnits) {
		this.listener1 = listener;
		this.timeout = timeout;
		this.units = units;
		this.soTimeout = soTimeout;
		this.soUnits = soUnits;
	}


	@SuppressWarnings("unused")
	public void connect(String serverName, String username, String password, String domain) {
		new AsyncSMBconnect().execute(serverName, username, password, domain);
	}

	@SuppressWarnings("unused")
	public void listFiles2(String shareName) {
		new AsyncSMBlist().execute(shareName);
	}


	@SuppressLint("StaticFieldLeak")
	public class AsyncSMBconnect extends AsyncTask<String, Void, Session> {

//		private SMBClient client = new SMBClient();

		@Override
		protected Session doInBackground(String... strings) {
			Session result = null;
			try {
				result = SMBconnect(client, strings[0], strings[1], strings[2], strings[3]);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		protected void onPostExecute(Session result) {
//			Log.v("FTPTask","FTP connection complete");
			listener1.OnConnection(result);
		}
	}


	private Session SMBconnect(SMBClient client, String serverName, String username, String password, String domain) throws IOException {
//		SMBClient client;
/*		if (this.units != null) {
			SmbConfig config = SmbConfig.builder()
//					.withTimeout(120, TimeUnit.SECONDS) // Timeout sets Read, Write, and Transact timeouts (default is 60 seconds)
					.withTimeout(this.timeout, this.units)
//					.withSoTimeout(180, TimeUnit.SECONDS) // Socket Timeout (default is 0 seconds, blocks forever)
					.withSoTimeout(this.soTimeout, this.soUnits)
//					.withAuthenticators(Arrays.<NtlmAuthenticator.Factory.Named<Authenticator>>asList(new NtlmAuthenticator.Factory()))
					.withAuthenticators(Arrays.asList(new NtlmAuthenticator.Factory()))
					.withSecurityProvider(new BCSecurityProvider())
					.build();
			client = new SMBClient(config);
		} else {*/
//			client = new SMBClient();
/*		}*/

		try {
			Connection connection = client.connect(serverName);
			AuthenticationContext ac = new AuthenticationContext(username, password.toCharArray(), domain);
			Session session = connection.authenticate(ac);
			this.session = session;
			return session;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
//		try (Connection connection = client.connect(serverName)) {
//			AuthenticationContext ac = new AuthenticationContext(username, password.toCharArray(), domain);
//			Session session = connection.authenticate(ac);
//			this.session = session;
//			return session;
//		}
	}

	@SuppressWarnings("unused")
	public void setTimeout(long time, TimeUnit units, long soTime, TimeUnit soUnits) {
		this.timeout = time;
		this.units = units;
		this.soTimeout = soTime;
		this.soUnits = soUnits;
//		SMBClient client;
		SmbConfig config = SmbConfig.builder()
				.withTimeout(time, units)
				.withSoTimeout(soTime, soUnits)
//					.withAuthenticators(Arrays.<NtlmAuthenticator.Factory.Named<Authenticator>>asList(new NtlmAuthenticator.Factory()))
				.withAuthenticators(Arrays.asList(new NtlmAuthenticator.Factory()))
				.withSecurityProvider(new BCSecurityProvider())
				.build();
//		client = new SMBClient(config);
//		return client;
		this.client = new SMBClient(config);
//		return new SMBClient(config);
	}
/*
	private boolean listFiles(Session session, String shareName, String folder, String searchPattern) {
		if (session == null) {
			return false;
		}
		try (DiskShare share = (DiskShare) session.connectShare(shareName)) {
			for (FileIdBothDirectoryInformation f : share.list(folder, searchPattern)) {
				System.out.println("File : " + f.getFileName());
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
*/
/*
	private Boolean listFiles(Session session, String shareName) {
		if (session == null) {
			return false;
		}
		DiskShare share = (DiskShare) session.connectShare(shareName);
		return false;
	}
*/
	@SuppressWarnings("unused")
	private boolean deleteFile(Session session, String shareName, String filePath) {
		if (session == null) {
			return false;
		}
		try {
			DiskShare share = (DiskShare) session.connectShare(shareName);
			share.rm(filePath);
			return true;
//		try (DiskShare share = (DiskShare) session.connectShare(shareName)) {
//			share.rm(filePath);
//			return true;
//		} catch (IOException e) {
//			e.printStackTrace();
		} catch (SMBApiException e) {
			e.printStackTrace();
		}
		return false;
	}

//	public void setListener(OnSMBinteraction listener) {
//		this.listener = listener;
//	}


	@SuppressLint("StaticFieldLeak")
	public class AsyncSMBlist extends AsyncTask<String, Void, DiskShare> {

		private final String TAG = AsyncSMBlist.class.getSimpleName();

		@Override
		protected DiskShare doInBackground(String... strings) {
			if (session == null) {
				Log.d(TAG, "SMB list files ERROR - no SMB session");
				return null;
			}
			return (DiskShare) session.connectShare(strings[0]);
		}

		protected void onPostExecute(DiskShare result) {
//			Log.v("FTPTask","FTP connection complete");
			listener1.OnListFiles(result);
		}
	}


	private List<FileIdBothDirectoryInformation> doSMBthings(String SERVER, String USERNAME, String PASSWORD, String WORKGROUP,
	                         String SHARE, String START_DIR) {
//		Log.d(TAG, "doSMBthings: (host)"+ SERVER+ ", (user)"+ USERNAME+", (pass)"+ PASSWORD+
//						", (domain)"+ WORKGROUP+ ", (share)"+ SHARE+ ", (dir)"+ START_DIR);
		SMBClient client = new SMBClient();
		try {
			Connection connection = client.connect(SERVER);
			if (connection != null) {
				AuthenticationContext ac = new AuthenticationContext(USERNAME, PASSWORD.toCharArray(), WORKGROUP);
				try {
					Session session = connection.authenticate(ac);
					try {
						DiskShare share = (DiskShare) session.connectShare(SHARE);
						List<FileIdBothDirectoryInformation> files = new ArrayList<>();
						files = share.list(START_DIR);
/*						List<String> files = new ArrayList<>();
						listFiles(share, START_DIR == "/" ? "" : START_DIR.replace("/", "\\"),
								files, false);*/
//						files.removeIf(name -> !name.toLowerCase().endsWith(".mp4"));
//						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//							files.forEach(System.out::println);
//						}
//						for (String file : files) {
//							System.out.println(file);
//						}
						return files;
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Log.d(TAG, "no valid SMB connection! (doSMBthings)");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static boolean isSpecialDir(String fileName) {
		return fileName.equals(".") || fileName.equals("..");
	}

	private void listFiles(DiskShare share, String path, Collection<String> files, boolean recursive) {
		List<String> dirs = new ArrayList<>();
		String extPath = path.isEmpty() ? path : path + "\\";
		for (FileIdBothDirectoryInformation f : share.list(path)) {
			if ((f.getFileAttributes() & FileAttributes.FILE_ATTRIBUTE_DIRECTORY.getValue()) != 0) {
				if (!isSpecialDir(f.getFileName())) {
					dirs.add(f.getFileName());
				}
			} else {
				files.add(extPath + f.getFileName());
			}
		}
//		dirs.forEach(dir -> listFiles(share, extPath + dir, files));
		if (!recursive) {
			files = dirs;
		} else {
			for (String dir : dirs) {
				listFiles(share, extPath + dir, files, false);
			}
		}
	}


	public void getSMBfileslist(String SERVER, String USERNAME, String PASSWORD, String WORKGROUP,
	                            String SHARE, String START_DIR, OnSMBinteraction listener) {
		filesListener = listener;
		new AsyncSMBlistFiles().execute(SERVER, USERNAME, PASSWORD, WORKGROUP, SHARE, START_DIR);
	}


	@SuppressLint("StaticFieldLeak")
	public class AsyncSMBlistFiles extends AsyncTask<String, Void, List<FileIdBothDirectoryInformation>> {

		private final String TAG = AsyncSMBlistFiles.class.getSimpleName();

		@Override
		protected List<FileIdBothDirectoryInformation> doInBackground(String... strings) {
//			if (session == null) {
//				Log.d(TAG, "SMB list files ERROR - no SMB session");
//				return null;
//			}
			return doSMBthings(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5]);
		}

		protected void onPostExecute(List<FileIdBothDirectoryInformation> files) {
//			Log.v("FTPTask","FTP connection complete");
			if (files == null) {
				Log.d(TAG, "SMB error: files list is null");
				filesListener.OnListFiles(null);
			} else if (filesListener == null) {
				Log.d(TAG, "SMB error: list files listener is null");
			} else {
				List<String> filenames = new ArrayList<>();
				for (int i = 0; i < files.size(); i++) {
					filenames.add(files.get(i).getShortName());
				}
				filesListener.OnListFilesFull(files);
			}
		}
	}


	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	private List<NetShareInfo0> listSharesMSSRVS(String SERVER, String USERNAME, String PASSWORD, String DOMAIN) {
		final SMBClient smbClient = new SMBClient();
		ArrayList<NetShareInfo0> shares = new ArrayList<>();
		try {
			final Connection smbConnection = smbClient.connect(SERVER);
			final AuthenticationContext smbAuthenticationContext = new AuthenticationContext(USERNAME,
					PASSWORD.toCharArray(), DOMAIN);
			final Session session = smbConnection.authenticate(smbAuthenticationContext);

			final RPCTransport transport = SMBTransportFactories.SRVSVC.getTransport(session);
			final ServerService serverService = new ServerService(transport);
			shares = (ArrayList<NetShareInfo0>) serverService.getShares0();//.getShares();
//			final List<NetShareInfo0> shares = serverService.getShares0();//.getShares();
			return shares;
//			for (final NetShareInfo0 share : shares) {
//				System.out.println(share);
//			}
/*
		try (final Connection smbConnection = smbClient.connect(SERVER)) {
			final AuthenticationContext smbAuthenticationContext = new AuthenticationContext(USERNAME,
					PASSWORD.toCharArray(), DOMAIN);
			final Session session = smbConnection.authenticate(smbAuthenticationContext);

			final RPCTransport transport = SMBTransportFactories.SRVSVC.getTransport(session);
			final ServerService serverService = new ServerService(transport);
			shares = (ArrayList<NetShareInfo0>) serverService.getShares0();//.getShares();
//			final List<NetShareInfo0> shares = serverService.getShares0();//.getShares();
			return shares;
//			for (final NetShareInfo0 share : shares) {
//				System.out.println(share);
//			}
*/
		} catch (SocketTimeoutException e) {
//			Log.d(TAG, "ERROR: connect timed out");
			shares.add(new NetShareInfo0("ERROR: Connection Timed-out"));
			return shares;
		} catch (NoRouteToHostException e) {
//			Log.d(TAG, "ERROR: connect timed out");
			shares.add(new NetShareInfo0("ERROR: Cannot locate device"));
			return shares;
		} catch (TransportException e) {
			shares.add(new NetShareInfo0("ERROR: Cannot communicate with device"));
			return shares;
//		} catch (Buffer.BufferException e) {
//			shares.add(new NetShareInfo0("ERROR: Communication Buffer"));
//			return shares;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public void listShares(String SERVER, String USERNAME, String PASSWORD, String WORKGROUP,
	                       OnSMBshares listener) {
		sharesListener = listener;
		new AsyncSMBlistShares().execute(SERVER, USERNAME, PASSWORD, WORKGROUP);
	}


	@SuppressLint("StaticFieldLeak")
	public class AsyncSMBlistShares extends AsyncTask<String, Void, List<NetShareInfo0>> {

		private final String TAG = AsyncSMBlistShares.class.getSimpleName();

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
		@Override
		protected List<NetShareInfo0> doInBackground(String... strings) {
//			if (session == null) {
//				Log.d(TAG, "SMB list files ERROR - no SMB session");
//				return null;
//			}
			return listSharesMSSRVS(strings[0], strings[1], strings[2], strings[3]);
		}

		protected void onPostExecute(List<NetShareInfo0> netShareInfos) {
			if (netShareInfos != null) {
//			Log.v("FTPTask","FTP connection complete");
				ArrayList<String> shares = new ArrayList<>();
				for (NetShareInfo0 share : netShareInfos) {
					shares.add(share.getNetName());
				}
				sharesListener.OnListShares(shares);
			}
		}
	}

}

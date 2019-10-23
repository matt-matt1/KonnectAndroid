package com.yumatechnical.konnectandroid.Helper.Network;

import android.os.AsyncTask;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.security.bc.BCSecurityProvider;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.auth.Authenticator;
import com.hierynomus.smbj.auth.NtlmAuthenticator;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class SMBoperation {

	private long timeout, soTimeout;
	private TimeUnit units = null, soUnits = null;
	private SMBClient client = new SMBClient();
	private Session session;
	private static final String TAG = FTPoperation.class.getSimpleName();

	public interface OnSMBinteraction {
		void OnConnection(Session result);
		void OnListFiles(DiskShare files);
	}
	private OnSMBinteraction listener;


	public SMBoperation() {
	}

	public SMBoperation(OnSMBinteraction listener) {
		this.listener = listener;
	}

	public SMBoperation(OnSMBinteraction listener, long timeout, TimeUnit units, long soTimeout, TimeUnit soUnits) {
		this.listener = listener;
		this.timeout = timeout;
		this.units = units;
		this.soTimeout = soTimeout;
		this.soUnits = soUnits;
	}


	public void connect(String serverName, String username, String password, String domain) {
		new AsyncSMBconnect().execute(serverName, username, password, domain);
	}

	public void listFiles(String shareName) {
		new AsyncSMBlist().execute(shareName);
	}


	public class AsyncSMBconnect extends AsyncTask<String, Void, Session> {

//		private SMBClient client = new SMBClient();

		@Override
		protected Session doInBackground(String... strings) {
			Session result = SMBconnect(client, strings[0], strings[1], strings[2], strings[3]);
			return result;
		}

		protected void onPostExecute(Session result) {
//			Log.v("FTPTask","FTP connection complete");
			listener.OnConnection(result);
		}
	}


	private Session SMBconnect(SMBClient client, String serverName, String username, String password, String domain) {
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

		try (Connection connection = client.connect(serverName)) {
			AuthenticationContext ac = new AuthenticationContext(username, password.toCharArray(), domain);
			Session session = connection.authenticate(ac);
			this.session = session;
			return session;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

//	private SMBClient setTimeout(long time, TimeUnit units, long soTime, TimeUnit soUnits) {
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
	private boolean deleteFile(Session session, String shareName, String filePath) {
		if (session == null) {
			return false;
		}
		try (DiskShare share = (DiskShare) session.connectShare(shareName)) {
			share.rm(filePath);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setListener(OnSMBinteraction listener) {
		this.listener = listener;
	}


	public class AsyncSMBlist extends AsyncTask<String, Void, DiskShare> {

		@Override
		protected DiskShare doInBackground(String... strings) {
			if (session == null)
				return null;
			return (DiskShare) session.connectShare(strings[0]);
		}

		protected void onPostExecute(DiskShare result) {
//			Log.v("FTPTask","FTP connection complete");
			listener.OnListFiles(result);
		}
	}

}

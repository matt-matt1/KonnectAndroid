package com.yumatechnical.konnectandroid.Helper.Network;

import android.os.AsyncTask;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okio.Timeout;


public class SMBoperation {

	private long timeout;
	private Timeout units;
	private static final String TAG = FTPoperation.class.getSimpleName();

	public interface OnSMBinteraction {
		void OnResult(Session result);
	}
	private final OnSMBinteraction listener;


	public SMBoperation(OnSMBinteraction listener) {
		this.listener = listener;
	}

	public SMBoperation(OnSMBinteraction listener, long timeout, Timeout units) {
		this.listener = listener;
		this.timeout = timeout;
		this.units = units;
	}


	public void connect(String serverName, String username, String password, String domain) {
		new AsyncSMBconnect().execute(serverName, username, password, domain);
	}


	public class AsyncSMBconnect extends AsyncTask<String, Void, Session> {

		@Override
		protected Session doInBackground(String... strings) {
			Session result = SMBconnect(strings[0], strings[1], strings[2], strings[3]);
			return result;
		}

		protected void onPostExecute(Session result) {
//			Log.v("FTPTask","FTP connection complete");
			listener.OnResult(result);
		}
	}


	private Session SMBconnect(String serverName, String username, String password, String domain) {
		SMBClient client;
		if (this.units == null) {
			SmbConfig config = SmbConfig.builder()
					.withTimeout(120, TimeUnit.SECONDS) // Timeout sets Read, Write, and Transact timeouts (default is 60 seconds)
					.withSoTimeout(180, TimeUnit.SECONDS) // Socket Timeout (default is 0 seconds, blocks forever)
					.build();
			client = new SMBClient(config);
		} else {
			client = new SMBClient();
		}

		try (Connection connection = client.connect(serverName)) {
			AuthenticationContext ac = new AuthenticationContext(username, password.toCharArray(), domain);
			Session session = connection.authenticate(ac);
			return session;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

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

}

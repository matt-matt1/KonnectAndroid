package com.yumatechnical.konnectandroid.Helper.Network;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.yumatechnical.konnectandroid.MainActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okio.Timeout;


public class LoaderSMB implements LoaderManager.LoaderCallbacks<Session> {

	private Context context;
	private long timeout;
	private Timeout units;
	private static final String TAG = LoaderSMB.class.getSimpleName();
	private static final int SMB_LOADER_ID = 24;
	private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);


	@NonNull
	@Override
	public Loader<Session> onCreateLoader(int id, @Nullable Bundle args) {
		return new Loader<Session>(context) {
//			@Override
//			protected void onStartLoading() {
//				super.onStartLoading();
//				if (args == null) {
//					return;
//				}
//				//do onPreExecute()...
//			}


			@Nullable
//			@Override
			public Session loadInBackground() {
				String serverName = args.getString("serverName");
				String username = args.getString("username");
				String password = args.getString("password");
				String domain = args.getString("domain");
				Session result = SMBconnect(serverName, username, password, domain);
				return result;
			}
		};
	}

	@Override
	public void onLoadFinished(@NonNull Loader<Session> loader, Session data) {
		if (data != null && !data.equals("")) {
			listener.OnResult(data);
		}
	}

	@Override
	public void onLoaderReset(@NonNull Loader<Session> loader) {
	}

	public interface OnSMBinteraction {
		void OnResult(Session result);
	}
	private final OnSMBinteraction listener;


	public LoaderSMB(Context context, OnSMBinteraction listener) {
		this.context = context;
		this.listener = listener;
	}

	public LoaderSMB(Context context, OnSMBinteraction listener, long timeout, Timeout units) {
		this.context = context;
		this.listener = listener;
		this.timeout = timeout;
		this.units = units;
//		Bundle bundle = new Bundle();
//		bundle.putString("units", units.toString());
//		bundle.putLong("timeout", timeout);
	}


	public void connect(String serverName, String username, String password, String domain) {
//		new AsyncSMBconnect().execute(serverName, username, password, domain);
		Bundle bundle = new Bundle();
		bundle.putString("serverName", serverName);
		bundle.putString("username", username);
		bundle.putString("password", password);
		bundle.putString("domain", domain);
//		LoaderManager loaderManager = LoaderManager.getInstance(context);//getSupportLoaderManager();
//		Loader<Session> loader = loaderManager.getLoader(SMB_LOADER_ID);
//		if (loader == null) {
//			loaderManager.initLoader(SMB_LOADER_ID, bundle, this);
//		} else {
//			loaderManager.restartLoader(SMB_LOADER_ID, bundle, this);
//		}
		logger.debug("Starting connect. Bundle: {}", bundle.toString());
	}

/*
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
*/

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

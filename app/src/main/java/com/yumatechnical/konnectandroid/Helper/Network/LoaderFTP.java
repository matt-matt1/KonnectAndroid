package com.yumatechnical.konnectandroid.Helper.Network;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import com.yumatechnical.konnectandroid.Model.ConnectionItem;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.net.SocketException;
import java.util.Locale;


public class LoaderFTP implements LoaderManager.LoaderCallbacks<FTPClient> {

	private Context context;
	private static final int FTP_LOADER_ID = 23;
	private static final String TAG = FTPoperation.class.getSimpleName();

	public interface OnFTPinteraction {
		void OnResult(FTPClient result);
	}
	private final OnFTPinteraction listener;


	public LoaderFTP(OnFTPinteraction listener) {
		this.listener = listener;
	}


	public void connectFTP(Context context, String connectionString, String username, String password, String directory) {
		this.context = context;
		Bundle bundle = new Bundle();
		bundle.putString("connstr", connectionString);
		bundle.putString("user", username);
		bundle.putString("pass", password);
		bundle.putString("dir", directory);
//		LoaderManager loaderManager = LoaderManager.getInstance(this);//context.getSupportLoaderManager();
//		Loader<FTPClient> loader = loaderManager.getLoader(FTP_LOADER_ID);
//		if (loader == null) {
//			loaderManager.initLoader(FTP_LOADER_ID, bundle, this);
//		} else {
//			loaderManager.restartLoader(FTP_LOADER_ID, bundle, this);
//		}
	}

	public void connectFTP(Context context, String server, int port, String user, String pass, String path) {
		this.context = context;
		Bundle bundle = new Bundle();
		bundle.putString("server", server);
		bundle.putInt("port", port);
		bundle.putString("user", user);
		bundle.putString("pass", pass);
		bundle.putString("dir", path);
//		LoaderManager loaderManager = context.getSupportLoaderManager();
//		Loader<FTPClient> loader = loaderManager.getLoader(FTP_LOADER_ID);
//		if (loader == null) {
//			loaderManager.initLoader(FTP_LOADER_ID, bundle, this).forceLoad();
//		} else {
//			loaderManager.restartLoader(FTP_LOADER_ID, bundle, this);
//		}
	}

	public void connectFTP(Context context, ConnectionItem connectionItem) {
		this.context = context;
		Bundle bundle = new Bundle();
		bundle.putString("token", connectionItem.getAccessToken());
		bundle.putString("host", connectionItem.getHost());
		bundle.putString("scheme", connectionItem.getScheme());
		bundle.putInt("port", connectionItem.getPort());
		bundle.putInt("id", connectionItem.getID());
		bundle.putInt("type", connectionItem.getType());
		bundle.putString("user", connectionItem.getUsername());
		bundle.putString("pass", connectionItem.getPassword());
		bundle.putString("dir", connectionItem.getPath());
//		LoaderManager loaderManager = context.getSupportLoaderManager();
//		Loader<FTPClient> loader = loaderManager.getLoader(FTP_LOADER_ID);
//		if (loader == null) {
//			loaderManager.initLoader(FTP_LOADER_ID, bundle, this);
//		} else {
//			loaderManager.restartLoader(FTP_LOADER_ID, bundle, this);
//		}
	}

/*
REPLACED WITH LOADER
	private class FtpTask extends AsyncTask<String, Void, FTPClient> {

		@Override
		protected FTPClient doInBackground(String... strings) {
			FTPClient result = connectftp(strings[0], strings[1], strings[2], strings[3]);
			return result;
		}

		protected void onPostExecute(FTPClient result) {
//			Log.v("FTPTask","FTP connection complete");
			showServerReply(result);
			listener.OnResult(result);
		}
	}
*/
	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}


	private FTPClient connectftp(String connectionString, String username, String password, String directory)
	{
		FTPClient ftp = new FTPClient();
		Log.d(TAG, "Attemping connection to "+ connectionString+ " using "+ username+ " and "+ password+ " with directory "+ directory);
		try {
			ftp.connect(connectionString);//"ftp://ftp.drivehq.com/");
			ftp.login(username, password);
			if (directory != null && !directory.equals("")) {
				ftp.changeWorkingDirectory(directory);
			}
			//  ftp.makeDirectory("200");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Log.d(TAG, "FTP error: "+ e.getLocalizedMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			Log.d(TAG, "FTP error: "+ e.getLocalizedMessage());
		}
		return ftp;
	}

	@NonNull
	@Override
	public Loader<FTPClient> onCreateLoader(int id, @Nullable Bundle args) {
		return new AsyncTaskLoader<FTPClient>(context) {
//			@Override
//			protected void onStartLoading() {
//				super.onStartLoading();
//				if (args == null) {
//					return;
//				}
//				//do onPreExecute()...
//			}

			@Nullable
			@Override
			public FTPClient loadInBackground() {
				String user = args.getString("user");
				String pass = args.getString("pass");
				String dir = args.getString("dir");
				String server = args.getString("server");
				String port = args.getString("port");
				String connStr = args.getString("connstr");
				if (connStr == null || connStr.equals("")) {
					if (server != null && !server.equals("")) {
						connStr = String.format(Locale.CANADA, "ftp://%s:%d/", server, port);
					} else {
						ConnectionItem connectionItem = new ConnectionItem(
								args.getInt("id"),
								args.getInt("type"),
								args.getString("token"),
								args.getString("scheme"),
								args.getString("user"),
								args.getString("pass"),
								args.getString("host"),
								args.getInt("port"),
								args.getString("dir"));
						connStr = String.format(Locale.CANADA, "%s://%s:%d/",
								connectionItem.getScheme(), connectionItem.getHost(), connectionItem.getPort());
						user = connectionItem.getUsername();
						pass = connectionItem.getPassword();
						dir = connectionItem.getPath();
					}
				}
				return connectftp(connStr, user, pass, dir);
			}
		};
	}

	@Override
	public void onLoadFinished(@NonNull Loader<FTPClient> loader, FTPClient data) {
		if (data != null && !data.equals("")) {
			listener.OnResult(data);
		}
	}

	@Override
	public void onLoaderReset(@NonNull Loader<FTPClient> loader) {
	}

}

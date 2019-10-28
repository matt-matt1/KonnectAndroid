package com.yumatechnical.konnectandroid.Helper.Network;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.yumatechnical.konnectandroid.Model.ConnectionItem;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Locale;


/**
 * PLEASE NOTE: This class is super-seeded by "LoaderFTP"
 * since AsyncTaskLoader is more efficient than AsyncTask
 */
//@Deprecated
public class FTPoperation {

//	private String server = "www.yourserver.net", path = "/";
//	private String connectionString, directory;
//	private int port = 21;
//	private String user = "username", pass = "password";
//	private String username, password;
//	private ConnectionItem connectionItem;
	private static final String TAG = FTPoperation.class.getSimpleName();

	public interface OnFTPinteraction {
		void OnResult(FTPClient result);
	}
	private OnFTPinteraction listener;


	public FTPoperation() {}

	public FTPoperation(OnFTPinteraction listener) {
		this.listener = listener;
	}


	@SuppressWarnings("unused")
	public void connectFTP(String connectionString, String username, String password, String directory) {
//		this.connectionString = connectionString;
//		this.username = username;
//		this.password = password;
//		this.directory = directory;
		new FtpTask().execute(connectionString, username, password, directory);
	}

	@SuppressWarnings("unused")
	public void connectFTP(String server, int port, String user, String pass, String path) {
//		this.server = server;
//		this.port = port;
//		this.user = user;
//		this.pass = pass;
//		this.path = path;
//		Uri uri = Uri.parse("ftp:").buildUpon();
//		uri.;
//		builder.set
		new FtpTask().execute(String.format(Locale.CANADA, "ftp://%s:%d/",
				server, port), user, pass, path);
	}

	@SuppressWarnings("unused")
	public void connectFTP(ConnectionItem connectionItem) {
//		this.connectionItem = connectionItem;
		new FtpTask().execute(String.format(Locale.CANADA, "%s://%s:%d/",
				connectionItem.getScheme(), connectionItem.getHost(), connectionItem.getPort()),
				connectionItem.getUsername(), connectionItem.getPassword(), connectionItem.getPath());
	}


	@SuppressLint("StaticFieldLeak")
	private class FtpTask extends AsyncTask<String, Void, FTPClient> {

		@Override
		protected FTPClient doInBackground(String... strings) {
//			FTPClient result = connectftp(strings[0], strings[1], strings[2], strings[3]);
//			return result;
			return connectftp(strings[0], strings[1], strings[2], strings[3]);
		}

		protected void onPostExecute(FTPClient result) {
//			Log.v("FTPTask","FTP connection complete");
			showServerReply(result);
			listener.OnResult(result);
		}
	}

	private static void showServerReply(FTPClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
//			for (String aReply : replies) {
//				System.out.println("SERVER: " + aReply);
//			}
			System.out.print("SERVER REPLY ("+ replies.length+ "): ");
			for (int i = 0; i < replies.length; i++) {
				System.out.println("("+ i+ ") "+ replies[i]);
			}
		}
	}


	private FTPClient connectftp(String connectionString, String username, String password, String directory)
	{
		FTPClient ftp = new FTPClient();
//		Log.d(TAG, "Attemping connection to "+ connectionString+ " using "+ username+ " and "+ password+ " with directory "+ directory);
		try {
			ftp.connect(connectionString);//"ftp://ftp.drivehq.com/");
			ftp.login(username, password);
			if (directory != null && !directory.equals("")) {
				ftp.changeWorkingDirectory(directory);
			}
			//  ftp.makeDirectory("200");
		} catch (SocketException e) {
//			e.printStackTrace();
			Log.d(TAG, "FTP error: "+ e.getLocalizedMessage());
		} catch (IOException e) {
//			e.printStackTrace();
			Log.d(TAG, "FTP error: "+ e.getLocalizedMessage());
		}
		return ftp;
	}

	public void setListener(OnFTPinteraction listener) {
		this.listener = listener;
	}
/*
	try{
            String user = "your_user_name";
            String pass ="your_pass_word";
            String sharedFolder="shared";

            String url = "smb://ip_address/" + sharedFolder + "/test.txt";
                        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(
                                null, user, pass);
                        SmbFile sfile = new SmbFile(url, auth);
    }catch(Exception e){
    }
*/
//	public static void main(String[] args) {
//		String server = "www.yourserver.net";
//		int port = 21;
//		String user = "username";
//		String pass = "password";
//		FTPClient ftpClient = new FTPClient();
//		try {
//			ftpClient.connect(server, port);
//			showServerReply(ftpClient);
//			int replyCode = ftpClient.getReplyCode();
//			if (!FTPReply.isPositiveCompletion(replyCode)) {
//				System.out.println("Operation failed. Server reply code: " + replyCode);
//				return;
//			}
//			boolean success = ftpClient.login(user, pass);
//			showServerReply(ftpClient);
//			if (!success) {
//				System.out.println("Could not login to the server");
//				return;
//			} else {
//				System.out.println("LOGGED IN SERVER");
//			}
//		} catch (IOException ex) {
//			System.out.println("Oops! Something wrong happened");
//			ex.printStackTrace();
//		}
//	}


	public interface OnFTPstatus {
		void onResult(String status);
	}
	private OnFTPstatus statusListener;


	public void getFTPstatus(FTPClient ftpClient, OnFTPstatus listener) {
		new FTPstatus(listener).execute(ftpClient);
	}

	@SuppressLint("StaticFieldLeak")
	public class FTPstatus extends AsyncTask<FTPClient, Void, String> {

		FTPstatus(OnFTPstatus listener) {
			statusListener = listener;
		}

		@Override
		protected String doInBackground(FTPClient... ftpClients) {
			try {
//				Log.d(TAG, "FTP status: "+ ftpClients[0].getStatus());
				return ftpClients[0].getStatus();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			statusListener.onResult(s);
		}
	}



	public interface OnReply {
		void reply(int code, String string);
	}
	private OnReply replyListener;


	@SuppressWarnings("unused")
	public void getReply(FTPClient ftpClient, OnReply listener) {
		new FTPreply().execute(ftpClient);
		this.replyListener = listener;
		Log.d(TAG,"getReply-launch");
	}

	@SuppressLint("StaticFieldLeak")
	public class FTPreply extends AsyncTask<FTPClient, Void, String[]> {

		private final String TAG = FTPreply.class.getSimpleName();

		@Override
		protected String[] doInBackground(FTPClient... ftpClients) {
			String isNull = (ftpClients[0] != null) ? "not null" : "null";
			Log.d(TAG,"FTPreply.getReply.doInBackground  FTPClient is "+ isNull);
			String[] strings;
			try {
//				Log.d(TAG, "FTP reply1: "+ ftpClients[0].getReply()+ ", "+ ftpClients[0].getReplyString());
				String replyCode = String.valueOf(ftpClients[0].getReply());
				Log.d(TAG,"FTPreply.getReply.doInBackground replycode = "+ replyCode);
				strings = new String[]{ replyCode, ftpClients[0].getReplyString()   };
				Log.d(TAG,"FTPreply.getReply.doInBackground strings = "+ Arrays.toString(strings));
				return strings;
			} catch (IOException e) {
				Log.d(TAG,"FTPreply.getReply.doInBackground ERROR");
				e.printStackTrace();
			}
			return new String[]{""};
		}

		@Override
		protected void onPostExecute(String[] reply) {
			Log.d(TAG, "FTPreply.getReply.onPostExecute");
			int code = 0;
			try {
				code = Integer.parseInt(reply[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			replyListener.reply(code, reply[1]);
		}

	}


	//	public Map<String, FTPFile[]> mapFTPFiles;
	public interface OnFTPlistResult {
		void listedDirs(FTPFile[] dirs);
		void listedFiles(FTPFile[] files);
	}
	private OnFTPlistResult listListener;


	@SuppressWarnings("unused")
	public void getFTPDirsAndFiles(FTPClient ftpClient, OnFTPlistResult listener) {
//		Log.d(TAG, "getFTPDirsAndFiles-launch");
		this.listListener = listener;
		new FTPlistDirs().execute(ftpClient);
		new FTPlistFiles().execute(ftpClient);
	}

	@SuppressLint("StaticFieldLeak")
	public class FTPlistDirs extends AsyncTask<FTPClient, Void, FTPFile[]> {

		private final String TAG = FTPlistDirs.class.getSimpleName();

		@Override
		protected FTPFile[] doInBackground(FTPClient... ftpClients) {
//			Log.d(TAG, "doInBackground");
			FTPFile[] dirs = null;
			try {
				dirs = ftpClients[0].listDirectories();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return dirs;
		}

		@Override
		protected void onPostExecute(FTPFile[] files) {
			if (files == null) {
				Log.d(TAG, "onPostExecute: null value");
			} else {
				Log.d(TAG, "onPostExecute: " + files.length);
				listListener.listedDirs(files);
			}
		}

	}

	@SuppressLint("StaticFieldLeak")
	public class FTPlistFiles extends AsyncTask<FTPClient, Void, FTPFile[]> {

		private final String TAG = FTPlistFiles.class.getSimpleName();

		@Override
		protected FTPFile[] doInBackground(FTPClient... ftpClients) {
			FTPFile[] files = null;
			try {
				files = ftpClients[0].listFiles();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return files;
		}

		@Override
		protected void onPostExecute(FTPFile[] files) {
			if (files == null) {
				Log.d(TAG, "onPostExecute: null value");
			} else {
				Log.d(TAG, "onPostExecute: " + files.length);
				listListener.listedFiles(files);
			}
		}

	}


}

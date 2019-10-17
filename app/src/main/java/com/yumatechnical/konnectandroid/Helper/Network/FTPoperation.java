package com.yumatechnical.konnectandroid.Helper.Network;

import android.os.AsyncTask;
import android.util.Log;

import com.yumatechnical.konnectandroid.Model.ConnectionItem;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.net.SocketException;
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
	private final OnFTPinteraction listener;


	public FTPoperation(OnFTPinteraction listener) {
		this.listener = listener;
	}


	public void connectFTP(String connectionString, String username, String password, String directory) {
//		this.connectionString = connectionString;
//		this.username = username;
//		this.password = password;
//		this.directory = directory;
		new FtpTask().execute(connectionString, username, password, directory);
	}

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

	public void connectFTP(ConnectionItem connectionItem) {
//		this.connectionItem = connectionItem;
		new FtpTask().execute(String.format(Locale.CANADA, "%s://%s:%d/",
				connectionItem.getScheme(), connectionItem.getHost(), connectionItem.getPort()),
				connectionItem.getUsername(), connectionItem.getPassword(), connectionItem.getPath());
	}


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
}

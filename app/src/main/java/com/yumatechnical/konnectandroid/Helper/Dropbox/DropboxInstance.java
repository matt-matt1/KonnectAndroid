package com.yumatechnical.konnectandroid.Helper.Dropbox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.TokenAccessType;
import com.dropbox.core.android.Auth;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.users.FullAccount;
import com.dropbox.core.v2.users.Name;
import com.yumatechnical.konnectandroid.MainActivity;
import com.yumatechnical.konnectandroid.Vars;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DropboxInstance {

	private static final String TAG = DropboxInstance.class.getSimpleName();


	public static void main(String args[]) throws DbxException {
		// Create Dropbox client
		DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
		DbxClientV2 client = new DbxClientV2(config, Vars.getDropboxKey());

		// Get current account info
		FullAccount account = client.users().getCurrentAccount();
		System.out.println(account.getName().getDisplayName());

		// Get files and folder metadata from Dropbox root directory
		ListFolderResult result = client.files().listFolder("");
		while (true) {
			for (Metadata metadata : result.getEntries()) {
				System.out.println(metadata.getPathLower());
			}

			if (!result.getHasMore()) {
				break;
			}

			result = client.files().listFolderContinue(result.getCursor());
		}

		// Upload "test.txt" to Dropbox
//		try (InputStream in = new FileInputStream("test.txt")) {
//			FileMetadata metadata = client.files().uploadBuilder("/test.txt")
//					.uploadAndFinish(in);
//		}
	}
/*
	public static String getAppInfo() {
		return "{\n\t\"key\":\""+ Vars.getDropboxKey()+ "\"\n\t\"secret\":\""+ Vars.getDropboxSecret()+ "\"\n}";
	}
*/
	public DbxAppInfo appInfo() {
//		DbxAppInfo dbxAppInfo = new DbxAppInfo(Vars.getDropboxKey(), Vars.getDropboxSecret());
		return new DbxAppInfo(Vars.getDropboxKey(), Vars.getDropboxSecret());
	}

	public void authorise(DbxAppInfo appInfo) {
		// Run through Dropbox API authorization process
		DbxRequestConfig requestConfig = new DbxRequestConfig("examples-authorize");
		DbxWebAuth webAuth = new DbxWebAuth(requestConfig, appInfo);
//		DbxWebAuth.Request.Builder builder = DbxWebAuth.newRequestBuilder()
//				.withNoRedirect().withTokenAccessType(TokenAccessType.OFFLINE);

		// TokenAccessType.OFFLINE means refresh_token + access_token. ONLINE means access_token only.
		DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder()
				.withNoRedirect()
				.withTokenAccessType(TokenAccessType.OFFLINE)
				.build();

		String authorizeUrl = webAuth.authorize(webAuthRequest);
		System.out.println("1. Go to " + authorizeUrl);
		System.out.println("2. Click \"Allow\" (you might have to log in first).");
		System.out.println("3. Copy the authorization code.");
		System.out.print("Enter the authorization code here: ");

		String code = null;
		try {
			code = new BufferedReader(new InputStreamReader(System.in)).readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (code == null) {
			System.exit(1); return;
		}
		code = code.trim();

		DbxAuthFinish authFinish;
		try {
			authFinish = webAuth.finishFromCode(code);
		} catch (DbxException ex) {
			System.err.println("Error in DbxWebAuth.authorize: " + ex.getMessage());
			System.exit(1); return;
		}

		System.out.println("Authorization complete.");
		System.out.println("- User ID: " + authFinish.getUserId());
		System.out.println("- Account ID: " + authFinish.getAccountId());
		System.out.println("- Access Token: " + authFinish.getAccessToken());
//		System.out.println("- Expires At: " + authFinish.getExpiresAt());
//		System.out.println("- Refresh Token: " + authFinish.getRefreshToken());

	}

	public void accountInfo(DbxAppInfo authInfo, String accessToken) {
		// Create a DbxClientV2, which is what you use to make API calls.
		DbxRequestConfig requestConfig = new DbxRequestConfig("examples-account-info");
		DbxClientV2 dbxClient = new DbxClientV2(requestConfig, accessToken, authInfo.getHost());

		StoneDeserializerLogger.LoggerCallback callback = (o, s) -> {
			System.out.println("This is from StoneDeserializerLogger: ");
			System.out.println(s);
		};
		StoneDeserializerLogger.registerCallback(Name.class, callback);

		// Make the /account/info API call.
		FullAccount dbxAccountInfo;
		try {
			dbxAccountInfo = dbxClient.users()
					.getCurrentAccount();
		}
		catch (DbxException ex) {
			System.err.println("Error making API call: " + ex.getMessage());
			System.exit(1); return;
		}

		System.out.println("This is from main: ");
		System.out.print(dbxAccountInfo.toStringMultiline());
	}

	public DbxRequestConfig requestConfig(String name) {
		return new DbxRequestConfig(name);
	}

	public DbxClientV2 getClient(DbxRequestConfig config, String accessToken) {
		return new DbxClientV2(config, accessToken);
	}
/*
	public FullAccount getAccount(DbxClientV2 client) throws DbxException {
		FullAccount account = null;
		try {
			account = new FullAccount(client.users().getCurrentAccount());
		} catch (DbxException e) {
			e.printStackTrace();
		}
		return account;
	}
*/
	public String displayName(FullAccount account) {
		return account.getName().getDisplayName();
	}

	public ListFolderResult listFolder(DbxClientV2 client, String folder) throws DbxException {
		return client.files().listFolder(folder);
	}

	public void printFiles(DbxClientV2 clientV2) {
		ListFolderResult result = null;
		try {
			result = listFolder(clientV2, "");
			while (true) {
				for (Metadata meta : result.getEntries()) {
					System.out.println(meta.getPathLower());
				}
				if (!result.getHasMore()) {
					break;
				}
				result = clientV2.files().listFolderContinue(result.getCursor());
			}
		} catch (DbxException e) {
			e.printStackTrace();
		}
	}

	/*
	 * example: uploadFile("/", "test.txt", new DbxClientV2(...))
	 */
	public DbxException uploadFile(String path, String filename, DbxClientV2 client) {
		String errorMessage = "error";
		try (InputStream inputStream = new FileInputStream(filename)) {
			FileMetadata metadata = client.files().uploadBuilder(path + filename).uploadAndFinish(inputStream);
			return null;
		} catch (FileNotFoundException e) {
			String string = e.getLocalizedMessage();
			if (string != null && !string.isEmpty()) {
				errorMessage = string;
			}
//			Log.d(TAG, errorMessage);
			//e.printStackTrace();
		} catch (IOException e) {
			String string = e.getLocalizedMessage();
			if (string != null && !string.isEmpty()) {
				errorMessage = string;
			}
//			Log.d(TAG, errorMessage);
			//e.printStackTrace();
		} catch (UploadErrorException e) {
			String string = e.getLocalizedMessage();
			if (string != null && !string.isEmpty()) {
				errorMessage = string;
			}
//			Log.d(TAG, errorMessage);
			//e.printStackTrace();
		} catch (DbxException e) {
			String string = e.getLocalizedMessage();
			if (string != null && !string.isEmpty()) {
				errorMessage = string;
			}
//			Log.d(TAG, errorMessage);
			//e.printStackTrace();
			return e;
		}
		return new DbxException("Dropbox upload error: "+ errorMessage);
	}

	/*
	 * example: downloadFile("/", "test.txt", new DbxClientV2(...))
	 */
	public FileOutputStream downloadFile(String path, String filename, DbxClientV2 client) throws DbxException {
		String errorMessage = "error";
		DbxDownloader<FileMetadata> downloader = client.files().download(path + filename);
		try {
			FileOutputStream outputStream = new FileOutputStream(filename);
			downloader.download(outputStream);
			downloader.close();
			return outputStream;
		} catch (DbxException e) {
			String string = e.getLocalizedMessage();
			if (string != null && !string.isEmpty()) {
				errorMessage = string;
			}
//			Log.d(TAG, errorMessage);
			//e.printStackTrace();
		} catch (FileNotFoundException e) {
			String string = e.getLocalizedMessage();
			if (string != null && !string.isEmpty()) {
				errorMessage = string;
			}
//			Log.d(TAG, errorMessage);
			//e.printStackTrace();
		} catch (IOException e) {
			String string = e.getLocalizedMessage();
			if (string != null && !string.isEmpty()) {
				errorMessage = string;
			}
//			Log.d(TAG, errorMessage);
			//e.printStackTrace();
		}
		return null;
	}

	public void getAccessToken() {
		String accessToken = Auth.getOAuth2Token();
		if (accessToken != null) {
//			SharedPreferences prefs = getSharedPreferences("com.yumatechnical.konnectandroid",
//					Context.MODE_PRIVATE);
//			prefs.edit().putString("access-token", accessToken).apply();
			Vars.setDropboxAccessToken(accessToken);

//			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//			startActivity(intent);
		}
	}

	@NonNull
	protected void getUserAccount(String accessToken) {
		DropboxInstance dropboxInstance = new DropboxInstance();
		new UserAccountTask(new DbxClientV2(dropboxInstance.requestConfig(""), accessToken),
				new UserAccountTask.TaskDelegate() {
			@Override
			public void onAccountReceived(FullAccount account) {
				//Print account's info
				Log.d("Dropbox User email", account.getEmail());
				Log.d("Dropbox User name", account.getName().getDisplayName());
				Log.d("Dropbox User acc. type", account.getAccountType().name());
//				updateUI(account);
			}
			@Override
			public void onError(Exception error) {
				Log.d("User", "Error receiving account details.");
			}
		}).execute();
	}

	@NonNull
	private void uploadImg(String accessToken) {
		//Select image to upload
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//		startActivityForResult(Intent.createChooser(intent,
//				"Upload to Dropbox"), IMAGE_REQUEST_CODE);
	}
}

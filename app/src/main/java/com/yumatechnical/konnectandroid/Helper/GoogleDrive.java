package com.yumatechnical.konnectandroid.Helper;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.yumatechnical.konnectandroid.Model.GoogleDriveFileHolder;

import java.util.Collections;

public class GoogleDrive {

	private DriveServiceHelper mDriveServiceHelper;
	public static String TAG = GoogleDrive.class.getSimpleName();
	public static String TYPE_AUDIO = "application/vnd.google-apps.audio";
	public static String TYPE_GOOGLE_DOCS = "application/vnd.google-apps.document";
	public static String TYPE_GOOGLE_DRAWING = "application/vnd.google-apps.drawing";
	public static String TYPE_GOOGLE_DRIVE_FILE = "application/vnd.google-apps.file";
//	public static String TYPE_GOOGLE_DRIVE_FOLDER = DriveFolder.MIME_TYPE;
	public static String TYPE_GOOGLE_FORMS = "application/vnd.google-apps.form";
	public static String TYPE_GOOGLE_FUSION_TABLES = "application/vnd.google-apps.fusiontable";
	public static String TYPE_GOOGLE_MY_MAPS = "application/vnd.google-apps.map";
	public static String TYPE_PHOTO = "application/vnd.google-apps.photo";
	public static String TYPE_GOOGLE_SLIDES = "application/vnd.google-apps.presentation";
	public static String TYPE_GOOGLE_APPS_SCRIPTS = "application/vnd.google-apps.script";
	public static String TYPE_GOOGLE_SITES = "application/vnd.google-apps.site";
	public static String TYPE_GOOGLE_SHEETS = "application/vnd.google-apps.spreadsheet";
	public static String TYPE_UNKNOWN = "application/vnd.google-apps.unknown";
	public static String TYPE_VIDEO = "application/vnd.google-apps.video";
	public static String TYPE_3_RD_PARTY_SHORTCUT = "application/vnd.google-apps.drive-sdk";
	public static String GDRIVE_CLIENT_ID = "326204890696-lqkln7mcnmgf0m704m9h13dtkrbic368.apps.googleusercontent.com";


	public static DriveServiceHelper sign_in(Context context) {
		GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);

		com.google.api.services.drive.Drive googleDriveService = null;
		if (account == null) {
			Log.d(TAG, "sign_in() has no account");
		} else {
			GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
					context, Collections.singleton(DriveScopes.DRIVE_FILE));
			credential.setSelectedAccount(account.getAccount());
			googleDriveService = new com.google.api.services.drive.Drive.Builder(
							AndroidHttp.newCompatibleTransport(),
							new GsonFactory(),
							credential)
							.setApplicationName("AppName")
							.build();
		}
		return new DriveServiceHelper(googleDriveService);
	}

	public static DriveServiceHelper sign_in(Context context, GoogleSignInAccount account) {
		com.google.api.services.drive.Drive googleDriveService = null;
		if (account == null) {
			Log.d(TAG, "sign_in() has no account");
		} else {
			GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
					context, Collections.singleton(DriveScopes.DRIVE_FILE));
			credential.setSelectedAccount(account.getAccount());
			googleDriveService = new com.google.api.services.drive.Drive.Builder(
							AndroidHttp.newCompatibleTransport(),
							new GsonFactory(),
							credential)
							.setApplicationName("AppName")
							.build();
		}
		return new DriveServiceHelper(googleDriveService);
	}

	public void signIn(Context context) {
		GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);

		com.google.api.services.drive.Drive googleDriveService = null;
		if (account == null) {
			Log.d(TAG, "sign_in() has no account");
		} else {
			GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
					context, Collections.singleton(DriveScopes.DRIVE_FILE));
			credential.setSelectedAccount(account.getAccount());
			googleDriveService = new com.google.api.services.drive.Drive.Builder(
							AndroidHttp.newCompatibleTransport(),
							new GsonFactory(),
							credential)
							.setApplicationName("AppName")
							.build();
		}
		mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
	}

	public void signIn(Context context, GoogleSignInAccount account) {
		com.google.api.services.drive.Drive googleDriveService = null;
		if (account == null) {
			Log.d(TAG, "sign_in() has no account");
		} else {
			GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
					context, Collections.singleton(DriveScopes.DRIVE_FILE));
			credential.setSelectedAccount(account.getAccount());
			googleDriveService = new com.google.api.services.drive.Drive.Builder(
							AndroidHttp.newCompatibleTransport(),
							new GsonFactory(),
							credential)
							.setApplicationName("AppName")
							.build();
		}
		mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
	}
/*
	public Task<GoogleDriveFileHolder> searchFile(String fileName, String mimeType) {
		return Tasks.call(mExecutor, () -> {
			FileList result = mDriveService.files().list()
					.setQ("name = '" + fileName + "' and mimeType ='" + mimeType + "'")
					.setSpaces("drive")
					.setFields("files(id, name,size,createdTime,modifiedTime,starred)")
					.execute();
			GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();
			if (result.getFiles().size() > 0) {
				googleDriveFileHolder.setId(result.getFiles().get(0).getId());
				googleDriveFileHolder.setName(result.getFiles().get(0).getName());
				googleDriveFileHolder.setModifiedTime(result.getFiles().get(0).getModifiedTime());
				googleDriveFileHolder.setSize(result.getFiles().get(0).getSize());
			}


			return googleDriveFileHolder;
		});
	}

	public Task<GoogleDriveFileHolder> searchFolder(String folderName) {
		return Tasks.call(mExecutor, () -> {

			// Retrive the metadata as a File object.
			FileList result = mDriveService.files().list()
					.setQ("mimeType = '" + DriveFolder.MIME_TYPE + "' and name = '" + folderName + "' ")
					.setSpaces("drive")
					.execute();
			GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();
			if (result.getFiles().size() > 0) {
				googleDriveFileHolder.setId(result.getFiles().get(0).getId());
				googleDriveFileHolder.setName(result.getFiles().get(0).getName());

			}
			return googleDriveFileHolder;
		});
	}

	public Task<List<GoogleDriveFileHolder>> queryFiles(@Nullable final String folderId) {
		return Tasks.call(mExecutor, new Callable<List<GoogleDriveFileHolder>>() {
					@Override
					public List<GoogleDriveFileHolder> call() throws Exception {
						List<GoogleDriveFileHolder> googleDriveFileHolderList = new ArrayList<>();
						String parent = "root";
						if (folderId != null) {
							parent = folderId;
						}

						FileList result = mDriveService.files().list().setQ("'" + parent + "' in parents").setFields("files(id, name,size,createdTime,modifiedTime,starred)").setSpaces("drive").execute();

						for (int i = 0; i < result.getFiles().size(); i++) {

							GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();
							googleDriveFileHolder.setId(result.getFiles().get(i).getId());
							googleDriveFileHolder.setName(result.getFiles().get(i).getName());
							if (result.getFiles().get(i).getSize() != null) {
								googleDriveFileHolder.setSize(result.getFiles().get(i).getSize());
							}

							if (result.getFiles().get(i).getModifiedTime() != null) {
								googleDriveFileHolder.setModifiedTime(result.getFiles().get(i).getModifiedTime());
							}

							if (result.getFiles().get(i).getCreatedTime() != null) {
								googleDriveFileHolder.setCreatedTime(result.getFiles().get(i).getCreatedTime());
							}

							if (result.getFiles().get(i).getStarred() != null) {
								googleDriveFileHolder.setStarred(result.getFiles().get(i).getStarred());
							}

							googleDriveFileHolderList.add(googleDriveFileHolder);

						}


						return googleDriveFileHolderList;


					}
				}
		);
	}
*/
}

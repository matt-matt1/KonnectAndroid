package com.yumatechnical.konnectandroid.Helper;

import android.content.Context;
import android.os.AsyncTask;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.yumatechnical.konnectandroid.MainActivity;

public class BoxSDK {

//	private final static String BOX_CLIENTID = "3cj25w8oba5gkg8oq9w1lcuuc8ecf6jk";
//	private final static String BOX_PRIMARY_TOKEN = "xBzNHrq6ZXuxQoNjZnSSrcNkI8for7QH";
//	private final static String BOX_SECONDARY_TOKEN = "GFUzapHMkldvzYch0kHdHjANcokKTzlq";

//	curl https://api.box.com/2.0/folders/0 -H \
//			"Authorization: Bearer oszkzmc9MIUqsD6PxnT3oY4M7qgB8gSt"
	public final static String DEVELOPER_TOKEN = "oszkzmc9MIUqsD6PxnT3oY4M7qgB8gSt";
	public final static String CLIENT_ID = "s3al43fdi2g5yda105kj8it91buta9ys";
	public final static String CLIENT_SECRET = "UKLM13j4haw1PQAzG6MwvtsF3qIZszre";
	public final static String REDIRECT_URI = "https://app.box.com";
	public final static String PRIMARY_KEY = "hr228h4n37GfBZ24GwuRmtozN5OOvQcE";
	public final static String SECONDARY_KEY = "S1PeQHGCwQTZ6hQGx5onxTLm3idTifQS";
/*
	BoxConfig.CLIENT_ID = "your-client-id";
	BoxConfig.CLIENT_SECRET = "your-client-secret";
// must match the redirect_uri set in your developer account if one has been set. Redirect uri should not be of type file:// or content://.
	BoxConfig.REDIRECT_URL = "your-redirect-uri";
*/
//BoxPreviewActivity.IntentBuilder builder = BoxPreviewActivity.createIntentBuilder(this, boxSession, boxFile);
	public static void betup() {
/*		BoxConfig.CLIENT_ID = BOX_CLIENT_ID;
		BoxConfig.CLIENT_SECRET = BOX_CLIENT_SECRET;
// must match the redirect_uri set in your developer account if one has been set. Redirect uri should not be of type file:// or content://.
		BoxConfig.REDIRECT_URL = BOX_REDIRECT_URI;*/
	}

	public static void signIn(Context context) {
/*		BoxSession session = new BoxSession(context);
		session.authenticate();*/
	}

	public static void listDevFiles(String dev_token) {
		new BOXAsyncTask(dev_token).execute();
	}

	private static void listFiles(String dev_token) {
		BoxAPIConnection api = new BoxAPIConnection(dev_token);
		BoxFolder rootFolder = BoxFolder.getRootFolder(api);
		for (BoxItem.Info itemInfo : rootFolder) {
			System.out.format("[%s] %s\n", itemInfo.getID(), itemInfo.getName());
		}
	}

	static class BOXAsyncTask extends AsyncTask<Void, Void, Void> {

		private String dev_token;

		BOXAsyncTask(String dev_token) {
			this.dev_token = dev_token;
		}

		@Override
		protected Void doInBackground(Void... voids) {
			listFiles(dev_token);
			return null;
		}
	}
}

package com.yumatechnical.konnectandroid.Helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxAPIException;
import com.box.sdk.BoxConfig;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;
import com.box.sdk.EncryptionAlgorithm;
import com.yumatechnical.konnectandroid.MainActivity;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static com.box.sdk.BoxConfig.readFrom;

public class BoxSDK {

	private final static String TAG = BoxSDK.class.getSimpleName();
//	private final static String BOX_CLIENTID = "3cj25w8oba5gkg8oq9w1lcuuc8ecf6jk";
//	private final static String BOX_PRIMARY_TOKEN = "xBzNHrq6ZXuxQoNjZnSSrcNkI8for7QH";
//	private final static String BOX_SECONDARY_TOKEN = "GFUzapHMkldvzYch0kHdHjANcokKTzlq";

//	curl https://api.box.com/2.0/folders/0 -H \
//			"Authorization: Bearer oszkzmc9MIUqsD6PxnT3oY4M7qgB8gSt"
	public final static String DEVELOPER_TOKEN = "vfRzMDtF7S0jfEho3v25WizXDMNoCCCm";
	public final static String CLIENT_ID = "s3al43fdi2g5yda105kj8it91buta9ys";
	public final static String CLIENT_SECRET = "UKLM13j4haw1PQAzG6MwvtsF3qIZszre";
	public final static String REDIRECT_URI = "https://app.box.com";
	public final static String PRIMARY_KEY = "hr228h4n37GfBZ24GwuRmtozN5OOvQcE";
	public final static String SECONDARY_KEY = "S1PeQHGCwQTZ6hQGx5onxTLm3idTifQS";
	public final static String ENTERPRISE_ID = "277843715";
	public final static String PUBLIC_KEY_ID = "fc6o6e72";
	public final static String PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\nProc-Type: 4,ENCRYPTED\nDEK-Info: DES-EDE3-CBC,FDAB198A0DBBEE46\n\nQNrkiXbMmHTyyQqUt0th22uRi0ngzKx3AEwWJiB9fY5pqG76NKlu3gpDXoJQF0le\nP6wPjx2PtUczBt3O04BgtqALsFUA51QapJRgsdY0pEKtLQdF0tSSwA6lD88osenl\niQiroNYj9LsPI+++k1mz7EMNaVRXKQ+futpY+R0qCArYLw39HxXv5cZXQeo2szqD\nsmI+iGIpb3Tk4Al6b11prGfVlV0O+Jdor7BmFi26c9JfaEq9cv95OWvQ+QFzZ6Ci\nRlXb6Y+0hALPg3cBZpbBck3n/IttlzVN/aOCAyEx9kF8cDVXSzFSIjOLMjkuHiLH\nDn5KJx6m6annXHqIkRQkIhUW4Mve6h2qpAYr0QZwbxneHr0k7wHmfEqTSFFhxy96\np96zQXHeTRafvdSkONiRKu7BZ0Vn3bHY0mgctdtVc+DZ1xhjoCGYtG58wgKCLoKW\nmdi4DaR1YYc8YDaXQ/ylH+YsMhOZ1Z6m6xYQG44EDh9GE8FHVRcZPF4PVh45M36p\n+VHqG/4x5obsMQlgKbDDM5BO8BUvndpBJtQ1qQtPLmj4GZJJmFYnRaS7eHBj7RNe\noZxAhxH+mY6P4lhdws0pZgmBeeA3ClMxGBhl97Dco5pk9nqGkLTSOLf6+J7XLLgW\nEB7MT7gT20vUgjptPbvfiwP7GgkvCn0fomqbHlZx9ynI7SfpQtRnzAmrD8EAr99l\nID50O4mWkLuGenM8q3c9AKd2lyCAK3M1teq/LaI1p9tq4jz8OBmczv3ZdvpOzoch\nkES/CqLEmV+YjWDGX404ex+2kpanARkaM0U8Hadzxg0+OPKJdELSa6+zWMo1hPna\n97k/kPY6H+dgQts8CwtMQ8d8c7TkxeZFppXkCzcKupnw3WXIyP6ifJ83EWgUWWzn\neDBKHjQ1T0UQRFrVYYmIRDfMxyjx5Y1nUzX44PGE4+FNR9ddhDGIfMXc4PZtznYV\npV4tC+nvb6pYb8iZf7VjqlfYymVelrClQp4E9uzeJMseeZ5MNpCvk/LN+dk4XP3M\nWWc6+0E+ObEBT9EvJEj3Tccog3fOQZQBlXLucFtZb7TFNd7cfyR4a1Z1lu/L2V2q\nhsbR5BYNGFKOKLMaGGjxUPhdMHxwHQhqnDfEG0kLHTqu+5uVO12vqUdglTfbRhfH\nNKzFLaBM/qPp1ofq0ctDprdoScOkValLqk3eqV8Myz0zj1BYCYhIr+dD1iNtc3Wc\nJtde+kqhOQJmctev7cVu8dGd7/KvElQi48XoTVDGvPZJxl+Vy1U6DJYjgMPKjrJL\np3qFpjSgV4/wZcbV6X2VS2nC45AI1s4tQBBwdIkD1XdA5ZLcf/RBBNuwRiOoN7m0\nQ8NFR/trvH23DI43nPguuBVxy3CcX2LDk4mDqUboPDmXLkRkE79EPxIE5xe1LA8t\nFKvC/uzB8DDmWwL5J5NWhITXETZ34fA7E3Zm3Lj+IUgUHjf7n39kcki7dUrvgjMw\nmQBS56r7QMqxtJzPM4+xilGVb26p2hjzD5AhoGbN8+srAb3ThbFlgRomCcTbaLN1\nCFdqT8tCDKC3iUBXWZE0YV0hvJ6Ir9IiutCYDhqHBCvebZhuG06knUomFjfFBL1P\n-----END RSA PRIVATE KEY-----\n";
	public final static String PRIVATE_KEY_PASSPH = "android";
//	public final static EncryptionAlgorithm ENCRYPTION_ALGORITHM;
/*
	BoxConfig.CLIENT_ID = "your-client-id";
	BoxConfig.CLIENT_SECRET = "your-client-secret";
// must match the redirect_uri set in your developer account if one has been set. Redirect uri should not be of type file:// or content://.
	BoxConfig.REDIRECT_URL = "your-redirect-uri";
*/
//BoxPreviewActivity.IntentBuilder builder = BoxPreviewActivity.createIntentBuilder(this, boxSession, boxFile);
	public static void etup() {
/*		BoxConfig.CLIENT_ID = BOX_CLIENT_ID;
		BoxConfig.CLIENT_SECRET = BOX_CLIENT_SECRET;
// must match the redirect_uri set in your developer account if one has been set. Redirect uri should not be of type file:// or content://.
		BoxConfig.REDIRECT_URL = BOX_REDIRECT_URI;*/
	}

	/**
	 * Load config from json file
	 * eg.  / *"/Volumes/Mac27ShareHFS/yuma/AndroidStudioProjects/KonnectAndroid2/app/"* /
	 *      "src/main/res/raw/box_config.json"
	 * @param configFile string
	 * @return
	 */
	public BoxConfig loadConfig(String configFile) {
		Reader reader = null;
		try {
			reader = new FileReader(configFile);
			return readFrom(reader);
//			BoxConfig boxConfig = BoxConfig.readFrom(reader);
//			return boxConfig;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
		try {
			for (BoxItem.Info itemInfo : rootFolder) {
				Log.d(TAG, "box has ["+ itemInfo.getID()+ "] "+ itemInfo.getName());
//				System.out.format("[%s] %s\n", itemInfo.getID(), itemInfo.getName());
			}
		} catch (BoxAPIException ex) {
			String msg = "";
			try {
				msg = ex.getMessage().substring(ex.getMessage().indexOf("["), ex.getMessage().indexOf("]"));
			} catch (Exception e) {
				msg = ex.getMessage();
			}
			Log.d(TAG, "Box Error: "+ msg);
//					"start:"+ ex.getResponse().indexOf("[")+ "end:"+ ex.getResponse().indexOf("]")+
//					" ("+ ex.getMessage()+ ")");
//		} catch (Exception e) {
//			e.printStackTrace();
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

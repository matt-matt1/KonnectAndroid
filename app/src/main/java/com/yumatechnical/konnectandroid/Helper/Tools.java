package com.yumatechnical.konnectandroid.Helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.yumatechnical.konnectandroid.MainActivity;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;
import com.yumatechnical.konnectandroid.Model.MicrosoftGraph.Error;
import com.yumatechnical.konnectandroid.Model.MicrosoftGraph.JsonError;
import com.yumatechnical.konnectandroid.Vars;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static com.yumatechnical.konnectandroid.MainActivity.jsonObject;

//import okhttp3.HttpUrl;


public class Tools extends AppCompatActivity {

	private static final String TAG = Tools.class.getSimpleName();


	/**
	 * force close/exit application
	 */
	public static void exitApplication(Activity activity) {
		activity.moveTaskToBack(true);
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			activity.finishAndRemoveTask();
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			activity.finishAffinity();
		} else {
			Process.killProcess(Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * encpyt / decrypt strings
	 *
	 * eg.
	 * SecretKey secret = generateKey();
	 * encrypt
	 * encryptString(String toEncrypt, secret))
	 * decrypt
	 * decryptString(byte[] toDecrypt, secret))
	 */
	public static SecretKey generateKey(String key)
			throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		return new SecretKeySpec(key.getBytes(), "AES");
	}

	public static byte[] encryptString(String message, SecretKey secret) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
	{
		Cipher cipher = null;
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
		return cipherText;
	}

	public static String decryptString(byte[] cipherText, SecretKey secret) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException,
			InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
	{
		Cipher cipher = null;
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret);
		String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
		return decryptString;
	}


	/**
	 * write secure data to file
	 */
/*	public boolean writeSecure(String filename, String data) {
		KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
		String masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

// Creates a file with this name, or replaces an existing file
// that has the same name. Note that the file name cannot contain
// path separators.
//		String fileToWrite = "my_sensitive_data.txt";
		try {
			EncryptedFile encryptedFile = new EncryptedFile.Builder(
					new File(directory, filename),
					context,
					masterKeyAlias,
					EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
			).build();

			// Write to a file.
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					encryptedFile.openFileOutput()));
			writer.write("MY SUPER-SECRET INFORMATION");
		} catch (GeneralSecurityException gse) {
			// Error occurred getting or creating keyset.
		} catch (IOException ex) {
			// Error occurred opening file for writing.
		}
	}
*/
	/**
	 * Hides the soft-keyboard
	 */
	/* MUST BE FROM ACTIVITY - NOT DIALOG FRAGMENT EVEN */
/*	public static void hideKeyboard(Activity activity, boolean insideClass, boolean fromContentBody, Fragment fragment, boolean openedFromBackground) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		//Find the currently focused view, so we can grab the correct window token from it.
		View view = null;
		if (activity == null) {
//		Inside a fragment class:
			if (insideClass)
				view = getView().getRootView().getWindowToken();

			if (fragment != null)
				view = (View) fragment.getView().getRootView().getWindowToken();

//			if (fromContentBody)
//				view = findViewById(android.R.id.content).getRootView().getWindowToken();
		} else {
			view = activity.getCurrentFocus();
			if (view == null) {
				//If no view currently has focus, create a new one, just so we can grab a window token from it
				view = new View(activity);
			}
		}
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		if (openedFromBackground) {
			view.clearFocus();
		}
	}*/
	public static void hideKeyboard(Context context, View view) {
		try {
			InputMethodManager editTextInput = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			editTextInput.hideSoftInputFromWindow(view.findFocus().getWindowToken(), 0);
		} catch (Exception e) {
			Log.e("hideKeyboard::Tools", "closeKeyboard: " + e);
		}
	}
	public static void hideKeyboard2(Activity activity) {
		View view = activity.findViewById(android.R.id.content);
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	public static void hideKeyboardFrom(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		} else
			Log.d(TAG, "cannot hideKeyboard because view is null");
	}

	/** Get the current line number.
	 * @return int - Current line number.
	 */
	public static int getLineNumber()
	{
		int lineNumber = 0;
		StackTraceElement[] stackTraceElement = Thread.currentThread()
				.getStackTrace();
		int currentIndex = -1;
		for (int i = 0; i < stackTraceElement.length; i++) {
			if (stackTraceElement[i].getMethodName().compareTo("getLineNumber") == 0)
			{
				currentIndex = i + 1;
				break;
			}
		}
		lineNumber = stackTraceElement[currentIndex].getLineNumber();
		return lineNumber;
	}


	/**
	 * Displays the logcat in a textview
	 *
	 * @param textView - output of log
	 */
	public static void putLogcatInTextView(TextView textView) {
		try {
			java.lang.Process process = Runtime.getRuntime().exec("logcat -d");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			StringBuilder log = new StringBuilder();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				log.append(line);
			}
			textView.setText(log.toString());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a (URL like) connection string from the given Strings
	 */
	/* !! use ConnectionItem.toConnectionString() instead !! */
/*	public static String makeConnString(String scheme, String domain, String username, String password,
	                                    String host, String sharename, String path) {
		StringBuilder output = new StringBuilder();
		if (!scheme.equals("")) {
			output.append(scheme);
			output.append(":/");
		}
		output.append("/");
		if (domain != null && !domain.equals("")) {
			output.append(domain);
			output.append(":");
		}
		if (username != null && !username.equals("")) {
			output.append(username);
			output.append(":");
			if (password != null && !password.equals("")) {
				output.append(password);
				output.append("@");
			}
		}
		if (host != null && !host.equals("")) {
			output.append(host);
			output.append("/");
		}
		if (sharename != null && !sharename.equals("")) {
			output.append(sharename);
			if (!sharename.endsWith("/"))
				output.append("/");
		}
		if (path != null && !path.equals("")) {
			output.append(path);
			if (!path.endsWith("/"))
				output.append("/");
		}
		return output.toString();
	}
*/
	/**
	 * Splits a given connection string into parts and constructs a ConnectionItem
	 * @param connStr of type String
	 * @return ConnectionItem
	 */
	public static ConnectionItem decryptConnString(String connStr) {
//		if (connStr.length() < 2)
//			return null;
		ConnectionItem connectionItem = new ConnectionItem(0, 0, "", "",
				"", "", "", "", 0, "", "");
		if (connStr.length() > 1) {
			String scheme = "";
			String[] parts = connStr.split("/");
			if (!connStr.startsWith("/") && connStr.contains("://")) {
				//is URL structure?
				connectionItem.setScheme(parts[0].substring(0, connStr.indexOf(":")));
				scheme = connectionItem.getScheme().toLowerCase();
			}
			String[] auth = parts[2].split("@");
			//divide [domain][:username[:password]] & host[:port]
			if (auth.length == 0 || auth.length > 2)
				Log.d(TAG, "Error: improper connection string - cannot determine host /& domain /& username");
			else {
				if (auth.length > 1) {
					if (!auth[1].contains(":"))
						connectionItem.setHost(auth[1].trim());
						//host only (no port)
					else {  //contains host : port
						String[] host_port = auth[1].split(":");
						//divide host[:port]
						connectionItem.setHost(host_port[0].trim());
						connectionItem.setPort(Integer.parseInt(host_port[1].trim()));
					}
					if (!auth[0].contains(":")) {
						connectionItem.setAccessToken(auth[0]);
					} else {
						String[] auths = auth[0].split(":");
						//divide [domain][:username[:password]]
						if (auths.length == 0 || auths.length > 3)
							Log.d(TAG, "Error: improper connection string - cannot determine domain &/ username");
						else {
							if (auths.length != 2) {
								connectionItem.setAccessToken(auths[0].trim());
								if (auths.length == 3) {
									connectionItem.setUsername(auths[1].trim());
									connectionItem.setPassword(auths[2].trim());
								}
							} else {
								connectionItem.setUsername(auths[0].trim());
								connectionItem.setPassword(auths[1].trim());
							}
						}
					}
				} else {
					if (!auth[0].contains(":"))
						connectionItem.setHost(auth[0].trim());
						//host only (no port)
					else {
						//contains host : port
						String[] host_port = auth[0].split(":");
						//divide host[:port]
						connectionItem.setHost(host_port[0].trim());
						connectionItem.setPort(Integer.parseInt(host_port[1].trim()));
					}
				}
				if (parts.length > 3) {
					if (!parts[3].contains(("/")))
						if (scheme.equals("smb")) {
							connectionItem.setShareName(parts[3]);
							connectionItem.setPath("/");
						} else
							connectionItem.setPath(parts[3]);
					else if (scheme.equals("smb")) {
						String[] paths = getShareNameFromPath(parts[3]);
						connectionItem.setShareName(paths[0]);
						connectionItem.setPath(paths[1]);
					}
				} else connectionItem.setPath("/");
			}
			//set type for smb & ftp
			if (scheme.equals("smb"))
				connectionItem.setType(Vars.SMB_CONN);
			else if (scheme.equals("ftp") || scheme.equals("ftps") || scheme.equals("sfpt"))
				connectionItem.setType(Vars.FTP_COMM);
		}
		return connectionItem;
	}


	/**
	 * Displays the size width and height
	 */
	private void screenDimentions() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.e("Width", "" + width);
		Log.e("height", "" + height);
	}


	/**
	 * Extracts the first segment (text before first separated)
	 *
	 * @param path : entire path as string
	 * @return String[] : [0] is the share name ; [1] is the remaining path
	 */
	public static String[] getShareNameFromPath(String path) {
		String[] separated = path.split("/");
		if (separated.length > 0) {
			ArrayList<String> list = new ArrayList<>();
			for (String pa : separated) {
				String part = pa.trim();
				if (part.equals(""))
					continue;
				list.add(part);
			}
			String shareName = list.get(0);
			list.remove(0);
			StringBuilder builder = new StringBuilder();
//			builder.append("/");
			for (String str : list) {
				builder.append("/");
				builder.append(str);
			}
			return new String[]{shareName, builder.toString()};
		} else {
			return separated;
		}
	}

	//https://stackoverflow.com/questions/3679432/images-for-alertdialog-buttons
	public static void centerImageAndTextInButton(Button button) {
		Rect textBounds = new Rect();
		//Get text bounds
		CharSequence text = button.getText();
		if (text != null && text.length() > 0) {
			TextPaint textPaint = button.getPaint();
			textPaint.getTextBounds(text.toString(), 0, text.length(), textBounds);
		}
		//Set left drawable bounds
		Drawable leftDrawable = button.getCompoundDrawables()[0];
		if (leftDrawable != null) {
			Rect leftBounds = leftDrawable.copyBounds();
			int width = button.getWidth() - (button.getPaddingLeft() + button.getPaddingRight());
			int leftOffset = (width - (textBounds.width() + leftBounds.width()) - button.getCompoundDrawablePadding()) / 2 - button.getCompoundDrawablePadding();
			leftBounds.offset(leftOffset, 0);
			leftDrawable.setBounds(leftBounds);
		}
	}


	public URL makeURLfromUri(Uri uri) {
		try {
			return new URL(uri.toString());
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
/*
	public static String buildURLwithParams(String url, ArrayList<KeyStrValueStr> params) {
		HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
		for (KeyStrValueStr par : params) {
			builder.addQueryParameter(par.getKey(), par.getValue());
		}
		return builder.build().toString();
	}
*/
/*
	public static Bitmap drawableToBitmap (Drawable drawable) {
		Bitmap bitmap = null;

		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if(bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}
		if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		}
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}
*/
	public static Bitmap drawableToBitmap (Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable)drawable).getBitmap();
		}
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static InputStream bitmapToInputStream(Bitmap bitmap) {
		int size = bitmap.getHeight() * bitmap.getRowBytes();
		ByteBuffer buffer = ByteBuffer.allocate(size);
		bitmap.copyPixelsToBuffer(buffer);
		return new ByteArrayInputStream(buffer.array());
	}


	public static int calculateNoOfColumns(Context context, float columnWidthDp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
		int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
		return noOfColumns;
	}

	public class ColumnQty {

		private int width, height, remaining;
		private DisplayMetrics displayMetrics;


		public ColumnQty(Context context, int viewId) {
			View view = View.inflate(context, viewId, null);
			view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			width = view.getMeasuredWidth();
			height = view.getMeasuredHeight();
			displayMetrics = context.getResources().getDisplayMetrics();
		}


		public int calculateNoOfColumns() {
			int numberOfColumns = displayMetrics.widthPixels / width;
			remaining = displayMetrics.widthPixels - (numberOfColumns * width);
//        System.out.println("\nRemaining\t" + remaining + "\nNumber Of Columns\t" + numberOfColumns);
			if (remaining / (2 * numberOfColumns) < 15) {
				numberOfColumns--;
				remaining = displayMetrics.widthPixels - (numberOfColumns * width);
			}
			return numberOfColumns;
		}


		public int calculateSpacing() {
			int numberOfColumns = calculateNoOfColumns();
//        System.out.println("\nNumber Of Columns\t"+ numberOfColumns+"\nRemaining Space\t"+remaining+"\nSpacing\t"+remaining/(2*numberOfColumns)+"\nWidth\t"+width+"\nHeight\t"+height+"\nDisplay DPI\t"+displayMetrics.densityDpi+"\nDisplay Metrics Width\t"+displayMetrics.widthPixels);
			return remaining / (2 * numberOfColumns);
		}
	}


	/**
	 * This method converts dp unit to equivalent pixels, depending on device density.
	 *
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static int dpToPx(float dp, Context context) {
		return (int) (dp * ((int) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 *
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float pxToDp(float px, Context context){
		return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
	}


	public static String toTitleCase(String str) {
		if (str == null) {
			return null;
		}
		boolean space = true;
		StringBuilder builder = new StringBuilder(str);
		final int len = builder.length();
		for (int i = 0; i < len; ++i) {
			char c = builder.charAt(i);
			if (space) {
				if (!Character.isWhitespace(c)) {
					builder.setCharAt(i, Character.toTitleCase(c));
					space = false;
				}
			} else if (Character.isWhitespace(c)) {
				space = true;
			} else {
				builder.setCharAt(i, Character.toLowerCase(c));
			}
		}
		return builder.toString();
	}


	public static void showMessageOKCancel(Context context, String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(context)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}
/*
	// url = file path or whatever suitable URL you want.
	public static String getMimeType(String url) {
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		}
		return type;
	}
*/
	public static String getMimeType(Context context, Uri uri) {
		String mimeType = null;
		Log.d(TAG, "uri = "+ uri);
		if (uri == null) {
			Log.d(TAG, "uri is null");
		}
		String scheme = uri.getScheme();
		if (scheme != null && scheme.equals(ContentResolver.SCHEME_CONTENT)) {
			ContentResolver cr = context.getContentResolver();
			mimeType = cr.getType(uri);
		} else {
			String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
					.toString());
			mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
					fileExtension.toLowerCase());
		}
		return mimeType;
	}

	public static String removeExtension(String s) {

		String separator = System.getProperty("file.separator");
		String filename;

		// Remove the path upto the filename.
		int lastSeparatorIndex = s.lastIndexOf(separator);
		if (lastSeparatorIndex == -1) {
			filename = s;
		} else {
			filename = s.substring(lastSeparatorIndex + 1);
		}

		// Remove the extension.
		int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex == -1)
			return filename;

		return filename.substring(0, extensionIndex);
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append((line + "\n"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}


	public static String displayVolleyError(VolleyError error) {
		String my = "error", result;
		if (error.networkResponse != null) {
			result = new String(error.networkResponse.data);
//		Log.d(TAG, "displayVolleyError() Error result: "+ result);
			try {
				jsonObject = new JSONObject(result);
//			Error err = gson.fromJson(jsonObject.toString(), Error.class);
				Error err = MainActivity.gson.fromJson(jsonObject.getJSONObject("error").toString(), Error.class);
				my = String.format(Locale.CANADA, "%d: %s (%s)",
						error.networkResponse.statusCode,
						err.getMessage(), err.getCode());
			} catch (JSONException e) {
				try {
					jsonObject = new JSONObject(result);
					JsonError jsonError = MainActivity.gson.fromJson(jsonObject.getJSONObject("error").toString(), JsonError.class);
					my = String.format(Locale.CANADA, "%d: %s (%s)",
							error.networkResponse.statusCode,
							jsonError.getMessage(), jsonError.getCode());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		} else my = error.getLocalizedMessage();
//		Toast.makeText(MainActivity.this,
//				my, Toast.LENGTH_LONG).show();
//		Log.d(TAG, "displayVolleyError() Error computed: "+ my);
		return my;
	}

}

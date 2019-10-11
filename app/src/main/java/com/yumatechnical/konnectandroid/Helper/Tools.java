package com.yumatechnical.konnectandroid.Helper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yumatechnical.konnectandroid.Model.KeyStrValueStr;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Objects;

//import okhttp3.HttpUrl;

public class Tools extends AppCompatActivity {

	private static final String TAG = Tools.class.getSimpleName();


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

}

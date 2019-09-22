package com.yumatechnical.konnectandroid;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.yumatechnical.konnectandroid.Model.FileItem;
import com.yumatechnical.konnectandroid.Model.KeyStrValueStr;

import java.util.ArrayList;
import java.util.List;

public class PutOnRight extends MainActivity {
/*
	private Cursor mData;
	private static final String TAG = PutOnRight.class.getSimpleName();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);

//		fillRight();
		new FetchContactsTask().execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// TODO (5) Remember to close your cursor!
		mData.close();
	}

	// Use an async task to do the data fetch off of the main thread.
	public class FetchContactsTask extends AsyncTask<Void, Void, Cursor> {

		int count;


		@Override
		protected void onPreExecute() {
			List<KeyStrValueStr> permissions = new ArrayList<>();
			permissions.add(new KeyStrValueStr(Manifest.permission.READ_CONTACTS, "Read Contacts"));
//			permissions.add(new KeyStrValueStr(Manifest.permission.WRITE_CONTACTS, "Write Contacts"));
			CanAccess(permissions);
		}

		@Override
		protected Cursor doInBackground(Void... params) {
			ContentResolver resolver = getContentResolver();
			Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
					null, null, null, null);
			if (cursor != null) {
				count = cursor.getCount();
			}
			return cursor;
		}

		// Invoked on UI thread
		@Override
		protected void onPostExecute(Cursor cursor) {
			super.onPostExecute(cursor);

			mData = cursor;

			if (count > 0) {
				while (cursor.moveToNext()) {
					String columnId = ContactsContract.Contacts._ID;
					int cursorIndex = cursor.getColumnIndex(columnId);
					String id = cursor.getString(cursorIndex);
					String name = cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					int numCount = Integer.parseInt(cursor.getString(cursor
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
					String add = id + ":" + name + ":(" + numCount + ")";
					rightFiles.add(new FileItem(add, null, Integer.parseInt(id), null,
							"", (numCount != 0), add));
					Log.d(TAG, add);
				}
			}
			cursor.close();
		}
	}

	/*
	void ListContacts() {
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		int count = cursor.getCount();

		if (count > 0) {
			String contactDetails = "";
			while (cursor.moveToNext()) {
				String columnId = ContactsContract.Contacts._ID;
				int cursorIndex = cursor.getColumnIndex(columnId);
				String id = cursor.getString(cursorIndex);
				String name = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				int numCount = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
				if (numCount > 0) {
					Cursor phoneCursor = resolver.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?", new String[] { id
							}, null);

					while (phoneCursor.moveToNext()) {
						String phoneNo = phoneCursor.getString(phoneCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.
										Phone.NUMBER));
						contactDetails += "Name: " + name + ", Phone No: " + phoneNo + "";
					}
					phoneCursor.close();
				}
			}
		}
	}
*/
}

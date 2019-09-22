package com.yumatechnical.konnectandroid.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yumatechnical.konnectandroid.Adapter.RightAdapter;
import com.yumatechnical.konnectandroid.Model.FileItem;
import com.yumatechnical.konnectandroid.Model.MyContact;
import com.yumatechnical.konnectandroid.Model.MyPhone;
import com.yumatechnical.konnectandroid.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Fragment for the right panel
 */
public class RightFragment extends Fragment implements AdapterView.OnClickListener {

	private static final String RIGHT_URI = "id";
	private static final String RIGHT_TEXT = "text";
	private static final String RIGHT_TITLE = "title";
	private ArrayList<FileItem> contactsList = new ArrayList<>();
	private String msg, title;
	RecyclerView recyclerView;
	TextView msgView, titleView;
	ProgressBar spinner;
	RightAdapter rightAdapter;
	RightAdapter.ListItemClickListener listener;
	ContentResolver resolver;
	Uri uri;
	private static final String TAG = RightFragment.class.getSimpleName();


	/**
	 * default constructor
	 */
	public RightFragment() {}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param title Parameter 1.
	 * @param uri Parameter 2.
	 * @param text Parameter 3.
	 * @return A new instance of fragment ContactsFragment.
	 */
	public static RightFragment newInstance(String title, String uri, String text) {
		RightFragment fragment = new RightFragment();
		Bundle args = new Bundle();
		args.putString(RIGHT_TEXT, text);
		args.putString(RIGHT_URI, uri);
		args.putString(RIGHT_TITLE, title);
		fragment.setArguments(args);
		return fragment;
	}

/*
newInstance, onCreateView, onActivityCreated
 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			msg = getArguments().getString(RIGHT_TEXT);
			title = getArguments().getString(RIGHT_TITLE);
			String string = getArguments().getString(RIGHT_URI);
			if (string != null && !string.isEmpty()) {
				uri = Uri.parse(string);
			} else {
				uri = null;
			}
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.right_panel, container, false);
	}


	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		spinner = getActivity().findViewById(R.id.pb_right);
		msgView = getActivity().findViewById(R.id.tv_right);
		titleView = getActivity().findViewById(R.id.tv_right_title);
		recyclerView = getActivity().findViewById(R.id.rv_right);
		if (title != null && !title.isEmpty()) {
			titleView.setText(title);
		}
		if (msg != null && !msg.isEmpty()) {
			msgView.setText(msg);
		} else {
			spinner.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.INVISIBLE);
			rightAdapter = new RightAdapter(null, null);
			LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
			recyclerView.setLayoutManager(layoutManager);
			recyclerView.setAdapter(rightAdapter);
			recyclerView.setOnClickListener(this);
		}
		// Initializes the loader
//		getLoaderManager().initLoader(0, null, this);
		if (title.equals(getString(R.string.contacts))) {
			if (uri == null) {
				new FetchContactsTask(-1).execute();
			} else if (uri.getPath() != null) {
				int id = Integer.parseInt(uri.getPath());
				new FetchContactsTask(id).execute();
			}
		} else if (title.equals(getString(R.string.photos))) {
			Log.d(TAG, title);
			//photos
		} else if (title.equals(getString(R.string.music))) {
			Log.d(TAG, title);
			//media
		} else {
			Log.d(TAG, title);
			//files
		}
	}


	@Override
	public void onClick(View v) {
		listener.onListItemClick(v.toString());
	}


	@SuppressLint("StaticFieldLeak")
	public class FetchContactsTask extends AsyncTask<Void, Void, Void> {

		int id;


		FetchContactsTask(int id) {
			super();
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
			spinner.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			getContacts(id);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {//done
			rightAdapter = new RightAdapter(null, null);
			recyclerView.setAdapter(rightAdapter);
			rightAdapter.setData(contactsList);
			spinner.setVisibility(View.INVISIBLE);
			recyclerView.setVisibility(View.VISIBLE);
		}

	}


	void getContacts(int ID) {
		if (contactsList != null) {
			if (contactsList.size() > 0) {
				return;
			}
		}
		resolver = getActivity().getContentResolver();
		Cursor cursor;
		String Sel = "";
		cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);
		if ((cursor != null ? cursor.getCount() : 0) > 0) {
			while (cursor.moveToNext()) {
				boolean has = false;
				String id = cursor.getString(
						cursor.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cursor.getString(cursor.getColumnIndex(
						ContactsContract.Contacts.DISPLAY_NAME));
				ArrayList<MyPhone> phoneList = new ArrayList<>();
				if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
					Cursor pCur = resolver.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
							new String[]{id}, null);
//					DatabaseUtils.dumpCursor(pCur);
					while (pCur.moveToNext()) {
						long phoneNo = 0;
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
							phoneNo = pCur.getLong(pCur.getColumnIndex(
									ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
						}
						String phoneStr = pCur.getString(pCur.getColumnIndex(
								ContactsContract.CommonDataKinds.Phone.NUMBER));
						int kind = pCur.getInt(pCur.getColumnIndex(
								ContactsContract.CommonDataKinds.Phone.DATA2));
						MyPhone myPhone = new MyPhone(phoneStr, phoneNo, kind);
						phoneList.add(myPhone);
						has = true;
/*						contactsPhones.add(new ContactsPhone(null, null, null,
                                null, null, null, null,
                                null, null, null, 0, null,
                                null, null, 0, null,
                                null, null, false, false,
                                null,
                                null, null, null, null, null,
                                null, null, null, false,
                                null, null, null, null,
                                null, null, null, null,
                                null, 0, null, 0,
                                0, null, null, null, null,
                                null, null, null, null, null,
                                0, null, null,
                                null, Integer.getInteger(id), null, null,
                                0, null, null, null, 0,
                                false, null, 0, null,
                                null, null, null, null,
                                null, null, null, null, null,
                                null, null, null, null,
                                null))*/
					}
					pCur.close();
				}
//				MyContact contact = new MyContact(Integer.parseInt(id), name, name, null,
//						has, phoneList);
				//				rightList.add(contact);
				FileItem fileItem = new FileItem(name, null, Integer.parseInt(id),
						null, "mime", has, name, phoneList);
				contactsList.add(fileItem);
//				Log.d(TAG, "adding:"+ fileItem.toString());
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		Log.d(TAG, "contactsList: "+ contactsList.toString());
	}


	@SuppressLint("StaticFieldLeak")
	public class FetchPhotosTask extends AsyncTask<Void, Void, Void> {

		int id;


		FetchPhotosTask(int id) {
			super();
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
			spinner.setVisibility(View.VISIBLE);
			recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			getPhotos(id);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {//done
			rightAdapter = new RightAdapter(null, null);
			recyclerView.setAdapter(rightAdapter);
			rightAdapter.setData(contactsList);
			spinner.setVisibility(View.INVISIBLE);
			recyclerView.setVisibility(View.VISIBLE);
		}

	}


	void getPhotos(int ID) {
		if (contactsList != null) {
			if (contactsList.size() > 0) {
				return;
			}
		}
		String[] projection = {MediaStore.MediaColumns.DATA};
		resolver = getActivity().getContentResolver();
		Cursor cursor;
		String absolutePathOfImage;
		ArrayList<FileItem> listOfAllImages = new ArrayList<>();
		ArrayList<MediaStore.Images.Media> list = new ArrayList<>();
		cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				projection, null, null, null);
		if ((cursor != null ? cursor.getCount() : 0) > 0) {
			while (cursor.moveToNext()) {
				boolean has = false;

				String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
				String id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID));
				absolutePathOfImage = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
				Uri uri = Uri.parse(absolutePathOfImage);

				FileItem fileItem = new FileItem(name, uri, Integer.parseInt(id),
						null, "mime", has, name, null);
				listOfAllImages.add(fileItem);
//				Log.d(TAG, "adding:"+ fileItem.toString());
				list.add(new MediaStore.Images.Media());
			}
		}
		if (cursor != null) {
			cursor.close();
		}
//		for (String path : listOfAllImages) {
//			list.add(new MediaStore.Images.Media(new File(path)));
//		}
		Log.d(TAG, "contactsList: "+ contactsList.toString());
	}


}

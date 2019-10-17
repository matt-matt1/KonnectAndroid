package com.yumatechnical.konnectandroid.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yumatechnical.konnectandroid.Adapter.RightAdapter;
import com.yumatechnical.konnectandroid.Helper.Network.TestInternetLoader;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.MainActivity;
import com.yumatechnical.konnectandroid.Model.FileItem;
import com.yumatechnical.konnectandroid.Model.MyPhone;
import com.yumatechnical.konnectandroid.R;
import com.yumatechnical.konnectandroid.Vars;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static com.mikepenz.iconics.Iconics.getApplicationContext;


/**
 * Fragment for the right panel
 */
public class RightFragment extends Fragment implements AdapterView.OnClickListener,
		LoaderManager.LoaderCallbacks {

	private static final String RIGHT_URI = "id";
	private static final String RIGHT_TEXT = "text";
	private static final String RIGHT_TITLE = "title";
	private ArrayList<FileItem> contactsList = new ArrayList<>(),
			listOfAllImages = new ArrayList<>(),
			listOfAllFiles = new ArrayList<>(),
			listOfMyMusic = new ArrayList<>();
	private String msg, title;
	TextView msgView, titleView;
	ProgressBar spinner;
	RightAdapter.ListItemClickListener listener;
	ContentResolver resolver;
	Uri uri;
	private static final String TAG = RightFragment.class.getSimpleName();
	private static final int LOADER_GATHER_MY_PHOTOS = 20;
	private static final int LOADER_GATHER_MY_MUSIC = 21;
	private static final int LOADER_GATHER_MY_CONTACTS = 22;
	private static final int LOADER_GATHER_MY_FILES = 23;
	private SwipeRefreshLayout swipeContainer;

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
//		timelineActivity = new TimelineActivity();
		return inflater.inflate(R.layout.right_panel, container, false);
	}

/*
	public void setupAdapter() {
		mGestureDetector = new GestureDetector(this, new MyList.SideIndexGestureListener());

//		List<FileItem> countries = Populate.populateCountries();
//		Collections.sort(countries);

		List rows = new ArrayList();
		int start = 0;
		int end = 0;
		String previousLetter = null;
		Object[] tmpIndexItem = null;
		Pattern numberPattern = Pattern.compile("[0-9]");

		for (FileItem item : wholeList) {
			String firstLetter = item.getName().substring(0, 1);

			if (numberPattern.matcher(firstLetter).matches()) {
				firstLetter = "#";
			}

			if (previousLetter != null && !firstLetter.equals(previousLetter)) {
				end = rows.size() - 1;
				tmpIndexItem = new Object[3];
				tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
				tmpIndexItem[1] = start;
				tmpIndexItem[2] = end;
				alphabet.add(tmpIndexItem);

				start = end + 1;
			}

			if (!firstLetter.equals(previousLetter)) {
				rows.add(new AlphabetListAdapter.Section(firstLetter));
				sections.put(firstLetter, start);
			}

			rows.add(new AlphabetListAdapter.Item(item.getName()));
			previousLetter = firstLetter;
		}

		if (previousLetter != null) {
			tmpIndexItem = new Object[3];
			tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
			tmpIndexItem[1] = start;
			tmpIndexItem[2] = rows.size() - 1;
			alphabet.add(tmpIndexItem);
		}

		adapter.setRows(rows);
		setListAdapter(adapter);

		updateList();
	}
*/

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			Log.d(TAG, "RightFragment-onActivityCreated:" + savedInstanceState.toString());
		}
		spinner = getActivity().findViewById(R.id.pb_right);
		msgView = getActivity().findViewById(R.id.tv_right);
		titleView = getActivity().findViewById(R.id.tv_right_title);
		((Vars)getActivity().getApplication()).recyclerView = getActivity().findViewById(R.id.rv_right);
		swipeContainer = getActivity().findViewById(R.id.sr_right);
		swipeContainer.setOnRefreshListener(() -> functionOnTitle());
		// Configure the refreshing colors
		swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		if (title != null && !title.isEmpty()) {
			titleView.setText(title);
		}
		if (msg != null && !msg.isEmpty()) {
			msgView.setText(msg);
		} else {
			spinner.setVisibility(View.VISIBLE);
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
//			myList = new MyList();
//			myList.onCreate();
//			setupAdapter();
//			listAdapter = new AlphabetListAdapter();
//			rightAdapter = new RightAdapter(null, null, null);
			((Vars)getActivity().getApplication()).rightAdapter = new RightAdapter(null, null, null);
//			LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
			GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),
					Tools.calculateNoOfColumns(getApplicationContext(),
							(1.2f * ((Vars)getApplicationContext()).getIconSize())
									+ Tools.dpToPx(40, getApplicationContext())));
//			ColumnQty columnQty = new ColumnQty(getApplicationContext(), R.layout.inner_right);
//			GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),
//					columnQty.calculateNoOfColumns());
//			recyclerView.addItemDecoration(new GridSpacing() columnQty.calculateSpacing());
//			int spanCount = 3; // 3 columns
//			int spacing = 50; // 50px
//			boolean includeEdge = true;
//			recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
			((Vars)getActivity().getApplication()).recyclerView.setLayoutManager(layoutManager);
			((Vars)getActivity().getApplication()).recyclerView.setHasFixedSize(true);
//			recyclerView.setAdapter(rightAdapter);
			((Vars)getActivity().getApplication()).recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);
//			recyclerView.setAdapter(listAdapter);
			((Vars)getActivity().getApplication()).recyclerView.setOnClickListener(this);
		}
		Bundle bundle;
		// Initializes the loader
//		getLoaderManager().initLoader(0, null, this);
		functionOnTitle();
	}

	private void functionOnTitle() {
				Log.d(TAG, "functionOnTitle:"+ title);
		if (title.equals(getString(R.string.contacts))) {
			if (uri == null) {
//				bundle = new Bundle();
//				bundle.putString("get", "contacts");
//				bundle.putInt("id", 0);
				new FetchContactsTask(-1).execute();
//				LoaderManager loaderManager = getActivity().getSupportLoaderManager();
//				Loader<String> loader = loaderManager.getLoader(TEST_CONNECT_LOADER);
//				if (loader == null) {
//					loaderManager.initLoader(TEST_CONNECT_LOADER, bundle, this);
//				} else {
//					loaderManager.restartLoader(TEST_CONNECT_LOADER, bundle, this);
//				}
			} else if (uri.getPath() != null) {
				int id = Integer.parseInt(uri.getPath());
				new FetchContactsTask(id).execute();
			}
		} else if (title.equals(getString(R.string.photos))) {
//			Log.d(TAG, title);
			String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
			String[] projection = {
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.DATA,
					MediaStore.Audio.Media.DISPLAY_NAME,
					MediaStore.Audio.Media.DURATION
			};
//			if (!Permissons.Check_STORAGE(this.getActivity())) {
//				Permissons.Request_STORAGE(this, this.MainActivity.MY_PERMISSION_);
//			} else {
				new FetchPhotosTask(-1).execute();
//			}
		} else if (title.equals(getString(R.string.music))) {
//			Log.d(TAG, title);
//			if (!Permissons.Check_STORAGE(this.getActivity())) {
//				Permissons.Request_STORAGE(this, MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE);
//			} else {
				new FetchMusicTask(-1).execute();
//			}
		} else {
			File dir = new File(Environment.getExternalStorageDirectory().getPath());
			Log.d(TAG, title+ ":"+ dir.getPath()+ ":");
			new FetchFilesTask(dir).execute();
			//files
		}
	}


	@Override
	public void onClick(View v) {
		listener.onListItemClick(v.toString());
//		Log.d(TAG, "onClick was fired");
	}

	@NonNull
	@Override
	public Loader onCreateLoader(int id, @Nullable Bundle args) {
		switch (id) {
			case LOADER_GATHER_MY_CONTACTS:
				return new TestInternetLoader(getApplicationContext());
			case LOADER_GATHER_MY_FILES:
				break;
			case LOADER_GATHER_MY_MUSIC:
				break;
			case LOADER_GATHER_MY_PHOTOS:
				break;
		}
		return null;
	}

	@Override
	public void onLoadFinished(@NonNull Loader loader, Object data) {
		int id = loader.getId();
	}

	@Override
	public void onLoaderReset(@NonNull Loader loader) {
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
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			getContacts(id);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {//done
			if (contactsList.size() > 0) {
/*				myList.setData(contactsList, new IconicsDrawable(getApplicationContext(),
						FontAwesome.Icon.faw_user_circle1)
						.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.dp(100)));*/
//				rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
				((Vars)getActivity().getApplication()).rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
					@Override
					public void onListItemClick(String item) {
//						Log.d(TAG, "ListItemClickListener:"+ item);
//						Intent intent = new Intent();
//						intent.setType("image/*");
//						intent.setAction(Intent.ACTION_GET_CONTENT);
//						startActivityForResult(Intent.createChooser(intent, "Choose picture"),
//								4);
					}

					@Override
					public void onListItemClick(FileItem item) {
//						ArrayList<String> shares = new ArrayList<>();
//						shares.add("Email");
//						shares.add("SMS/MMS");
						AlertDialog.Builder contactDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
						contactDialogBuilder.setTitle(R.string.contactMethod);
						contactDialogBuilder.setItems(R.array.contactShare, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Log.d(TAG, "share via: "+ which);
							}
						});
						contactDialogBuilder.show();
//						Uri uri = Uri.parse("contact:"+ item.getName());
						Intent intent = new Intent(Intent.ACTION_EDIT);
//						Intent intent = new Intent(Intent.ACTION_VIEW);
//						Log.d(TAG, "viewing contact: "+ uri.toString());
//						intent.putExtra(ContactsContract.Intents.Insert.EMAIL, item.getName());
						intent.setData(item.getFullPath());
						if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
							startActivity(intent);
						}
					}
				});
//				recyclerView.setAdapter(rightAAdapter);
//				((Vars)getActivity().getApplication()).recyclerView.setLayoutManager(new LinearLayoutManager());
				((Vars)getActivity().getApplication()).recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);
				((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(new IconicsDrawable(getApplicationContext(),
						FontAwesome.Icon.faw_user_circle1)
						.color(IconicsColor.colorRes(R.color.White))
						.size(IconicsSize.dp(((Vars) getApplicationContext()).getIconSize())));
				((Vars)getActivity().getApplication()).rightAdapter.setData(contactsList);
//				rightAdapter.setDefaultImage(new IconicsDrawable(getApplicationContext(),
//						FontAwesome.Icon.faw_user_circle1)
//						.color(IconicsColor.colorRes(R.color.White))
//						.size(IconicsSize.dp(((Vars) getApplicationContext()).getIconSize())));
//				rightAdapter.setData(contactsList);
				((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
				spinner.setVisibility(View.INVISIBLE);
				((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
			} else {
				spinner.setVisibility(View.INVISIBLE);
				msgView.setText(R.string.empty);
			}
		}

	}


	void getContacts(int ID) {
		if (contactsList != null) {
			if (contactsList.size() > 0) {
				return;
			}
		}
//		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
		String sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY+ " ASC";
		resolver = getActivity().getContentResolver();
		Cursor cursor;
		cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, sortOrder);
		if ((cursor != null ? cursor.getCount() : 0) > 0) {
			while (cursor.moveToNext()) {
				boolean has = false;
				String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
				long contactId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
				String name = cursor.getString(
						cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String label = cursor.getString(
						cursor.getColumnIndex("phonebook_label"));
				String sort = cursor.getString(
						cursor.getColumnIndex(ContactsContract.Contacts.SORT_KEY_PRIMARY));
//				String pic = cursor.getString(
//						cursor.getColumnIndex(ContactsContract.Contacts.Photo.openContactPhotoInputStream(
//								resolver, contactUri, perferHires)));
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
						String phoneStr = pCur.getString(
								pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						int kind = pCur.getInt(
								pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2));
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
				Drawable drawable = new IconicsDrawable(getApplicationContext(),
						FontAwesome.Icon.faw_person_booth)
						.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.TOOLBAR_ICON_SIZE);
				FileItem fileItem = new FileItem(name, null, Integer.parseInt(id),
						 null, "mime", has, name, phoneList, false, label);
				contactsList.add(fileItem);
//				Log.d(TAG, "adding:"+ fileItem.toString());
			}
		}
//		DatabaseUtils.dumpCursor(cursor);
		if (cursor != null) {
			cursor.close();
		}
//		Log.d(TAG, "contactsList: "+ contactsList.toString());
	}


	public InputStream openPhoto(long contactId) {
		Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = resolver.query(photoUri,
				new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					return new ByteArrayInputStream(data);
				}
			}
		} finally {
			cursor.close();
		}
		return null;
	}


	public InputStream openDisplayPhoto(long contactId) {
		Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
		Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
		try {
			AssetFileDescriptor fd =
					resolver.openAssetFileDescriptor(displayPhotoUri, "r");
			return fd.createInputStream();
		} catch (IOException e) {
			return null;
		}
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
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			getPhotos(id);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {//done
			if (listOfAllImages.size() > 0) {
				Drawable defaultImg = new IconicsDrawable(getApplicationContext(),
						FontAwesome.Icon.faw_image)
						.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.dp(((Vars) getApplicationContext()).getIconSize()));
//				myList.setData(listOfAllImages, defaultImg);
//				rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
				((Vars)getActivity().getApplication()).rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
					@Override
					public void onListItemClick(String item) {
					}

					@Override
					public void onListItemClick(FileItem item) {
						Intent intent = new Intent(Intent.ACTION_VIEW, item.getFullPath());
						intent.setType("image/*");
						if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
							startActivity(intent);
						}
					}
				});
//				recyclerView.setAdapter(rightAdapter);
				((Vars)getActivity().getApplication()).recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);
//				rightAdapter.setData(listOfAllImages);
				((Vars)getActivity().getApplication()).rightAdapter.setData(listOfAllImages);
				((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
//				rightAdapter.setDefaultImage(defaultImg);
				((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(defaultImg);
				spinner.setVisibility(View.INVISIBLE);
				((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
			} else {
				spinner.setVisibility(View.INVISIBLE);
				msgView.setText(R.string.empty);
			}
		}

	}


	void getPhotos(int ID) {
		if (listOfAllImages != null) {
			if (listOfAllImages.size() > 0) {
				return;
			}
		}
		String[] projection = {MediaStore.MediaColumns.DATA};
		String sortOrder = "";//MediaStore.Audio.Media.TITLE + " ASC";
		resolver = getActivity().getContentResolver();
		Cursor cursor;
		ArrayList<MediaStore.Images.Media> list = new ArrayList<>();
		cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				projection, null, null, sortOrder);
		if ((cursor != null ? cursor.getCount() : 0) > 0) {
//			Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));
			int id = 0;
			while (cursor.moveToNext()) {
				boolean has = false;

				String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
				if (path.startsWith("/")) {
					path = "file:"+ path;
				}
				Uri uri = Uri.parse(path);
				String name = Tools.removeExtension(Objects.requireNonNull(uri.getLastPathSegment()));
				String mime = Tools.getMimeType(getActivity().getApplicationContext(), uri);

//				ImageView imageView = null;
				FileItem fileItem = new FileItem(name, uri, id,
						null, mime, has, name, null, true, null);
				new DownloadImageTask(fileItem).execute(path);
				listOfAllImages.add(fileItem);
				list.add(new MediaStore.Images.Media());
				id += 1;
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		Log.d(TAG, "listOfAllImages: "+ listOfAllImages.toString());
	}


	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

//		ImageView bmImage;
		FileItem fileItem;
//		listener;


//		public DownloadImageTask(ImageView bmImage) {
		public DownloadImageTask(FileItem fileItem) {
//			this.bmImage = bmImage;
			this.fileItem = fileItem;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("DownloadImageTask Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
//			bmImage.setImageBitmap(result);
			fileItem.setBitmap(result);
//			listener.onDone();
//			re-draw list
			Log.d(TAG, "adding image to "+ fileItem.toString());

//			rightAdapter = new RightAdapter(null, null);
//			recyclerView.setAdapter(rightAdapter);
//			rightAdapter.setData(listOfAllImages);
//			spinner.setVisibility(View.INVISIBLE);
//			recyclerView.setVisibility(View.VISIBLE);
		}
	}

	@SuppressLint("StaticFieldLeak")
	public class FetchMusicTask extends AsyncTask<Void, Void, Void> {

		int id;


		FetchMusicTask(int id) {
			super();
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
			spinner.setVisibility(View.VISIBLE);
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			getMusic(id);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {//done
			if (listOfMyMusic.size() > 0) {
/*				myList.setData(listOfMyMusic, new IconicsDrawable(getApplicationContext(),
						FontAwesome.Icon.faw_music)
						.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.dp(100)));*/
//				rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
				((Vars)getActivity().getApplication()).rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
					@Override
					public void onListItemClick(String item) {
					}

					@Override
					public void onListItemClick(FileItem item) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(item.getFullPath());
						if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
							startActivity(intent);
						}
					}
				});
//				recyclerView.setAdapter(rightAdapter);
				((Vars)getActivity().getApplication()).recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);
//				rightAdapter.setData(listOfMyMusic);
				((Vars)getActivity().getApplication()).rightAdapter.setData(listOfMyMusic);
//				rightAdapter.setDefaultImage(new IconicsDrawable(getApplicationContext(),
				((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(new IconicsDrawable(getApplicationContext(),
						FontAwesome.Icon.faw_music)
						.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.dp(100)));
				spinner.setVisibility(View.INVISIBLE);
				((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
			} else {
				spinner.setVisibility(View.INVISIBLE);
				msgView.setText(R.string.empty);
			}
		}

	}


	void getMusic(int ID) {
		if (listOfMyMusic != null) {
			if (listOfMyMusic.size() > 0) {
				return;
			}
		}
		String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
		String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
		int count = 0;
		resolver = getActivity().getContentResolver();
		Cursor cursor;
		ArrayList<MediaStore.Images.Media> list = new ArrayList<>();
		cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				/*projection*/null, selection, null, sortOrder);
		if ((cursor != null ? cursor.getCount() : 0) > 0) {
//			Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));
			int id = 0;
			count = cursor.getCount();
			while (cursor.moveToNext()) {
				boolean has = false;

				String data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//				if (path.startsWith("/")) {
//					path = "file:/"+ path;
//				}
//				Uri uri = Uri.parse(path);
				String name = "", mime = "";
//				String name = Tools.removeExtension(Objects.requireNonNull(uri.getLastPathSegment()));
//				String mime = Tools.getMimeType(getActivity().getApplicationContext(), uri);

//				ImageView imageView = null;
				FileItem fileItem = new FileItem(name, uri, id,
						null, mime, has, name, null, false, "");
//				new DownloadImageTask(fileItem).execute(path);
				Log.d(TAG, "adding:"+ fileItem.toString());
				listOfMyMusic.add(fileItem);
				list.add(new MediaStore.Images.Media());
				id += 1;
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		if (listOfMyMusic.size() > 0) {
			Log.d(TAG, "listOfMyMusic: " + listOfMyMusic.toString());
		} else {
			Log.d(TAG, "listOfMyMusic is empty.");
		}
	}

	@SuppressLint("StaticFieldLeak")
	public class FetchFilesTask extends AsyncTask<Void, Void, File[]> {

		String directoryStr;
		File directory;


		FetchFilesTask(String directory) {
			super();
			this.directoryStr = directory;
			this.directory = new File(directory);
		}

		FetchFilesTask(File directory) {
			super();
			this.directory = directory;
			this.directoryStr = directory.getPath();
		}

		@Override
		protected void onPreExecute() {
			spinner.setVisibility(View.VISIBLE);
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected File[] doInBackground(Void... params) {
			File[] files = listFiles(directory);
			return files;
		}

		@Override
		protected void onPostExecute(File[] files) {
			super.onPostExecute(files);
			if (files.length > 0) {
				Log.d(TAG, "FetchFilesTask count="+ files.length);
//				rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
				((Vars)getActivity().getApplication()).rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
					@Override
					public void onListItemClick(String item) {
					}

					@Override
					public void onListItemClick(FileItem item) {
					}
				});
//				recyclerView.setAdapter(rightAdapter);
				((Vars)getActivity().getApplication()).recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);
//				ArrayList<listOfFiles = ;
				Drawable fileIcon = new IconicsDrawable(getApplicationContext(),
						FontAwesome.Icon.faw_file)
						.color(IconicsColor.colorRes(R.color.Gray)).size(IconicsSize.dp(100));
				Drawable dirIcon = new IconicsDrawable(getApplicationContext(),
						FontAwesome.Icon.faw_folder)
						.color(IconicsColor.colorRes(R.color.Gray)).size(IconicsSize.dp(100));
				for (File f : files) {
					listOfAllFiles.add(new FileItem(f.getName(), Uri.fromFile(f), 0,
							(f.isDirectory()) ? Tools.drawableToBitmap(dirIcon) : Tools.drawableToBitmap(fileIcon),
							"", f.canRead(), f.getName(), null, false, f.getAbsolutePath()));
				}
//				rightAdapter.setData(listOfAllFiles);
				((Vars)getActivity().getApplication()).rightAdapter.setData(listOfAllFiles);
//				rightAdapter.setDefaultImage(new IconicsDrawable(getApplicationContext(),
				((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(new IconicsDrawable(getApplicationContext(),
						FontAwesome.Icon.faw_file)
						.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.dp(100)));
				spinner.setVisibility(View.INVISIBLE);
				((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
			} else {
				spinner.setVisibility(View.INVISIBLE);
				msgView.setText(R.string.empty);
			}
		}

	}


	public File[] listFiles(File directory) {
		if (!directory.isDirectory()) {
			Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
			Log.d(TAG, "listFiles error for "+ directory.getPath());
			return null;
		}
		File[] files = directory.listFiles();
		Log.d(TAG, "listFiles length="+ files.length);
		return files;
	}


	private LinearLayout putFilesInLayout(File[] files) {
		LinearLayout root = new LinearLayout(getApplicationContext());
		int i = 1;
		for (File f : files)
		{
			if (f.isFile() || f.isDirectory())
			{
				try
				{
					LinearLayout layout = new LinearLayout(getApplicationContext());
					layout.setId(i);
					Button text = new Button(getApplicationContext());
					text.setText(f.getName());
					text.setMinWidth(400);
					layout.addView(text);
					root.addView(layout);
					i++;
				}
				catch(Exception e){}
			}
		}
		LinearLayout layout = new LinearLayout(getApplicationContext());
		HorizontalScrollView scroll = new HorizontalScrollView(getApplicationContext());
		scroll.addView(root);
		layout.addView(scroll);
		return layout;
//		setContentView(layout);
	}

}

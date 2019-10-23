package com.yumatechnical.konnectandroid.Fragment;
//
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yumatechnical.konnectandroid.Adapter.CustomDialogListAdapter;
import com.yumatechnical.konnectandroid.Adapter.RightAdapter;
import com.yumatechnical.konnectandroid.Helper.Network.TestInternetLoader;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.Model.FileItem;
import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.Model.MyPhone;
import com.yumatechnical.konnectandroid.R;
import com.yumatechnical.konnectandroid.Vars;

import org.apache.tika.Tika;

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
public class RightFragment extends Fragment implements LoaderManager.LoaderCallbacks {

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
	ContentResolver resolver;
	Uri uri;
	private static final String TAG = RightFragment.class.getSimpleName();
	private static final int LOADER_GATHER_MY_PHOTOS = 20;
	private static final int LOADER_GATHER_MY_MUSIC = 21;
	private static final int LOADER_GATHER_MY_CONTACTS = 22;
	private static final int LOADER_GATHER_MY_FILES = 23;
	private SwipeRefreshLayout swipeContainer;
//	private MyViewModel model;
	private Drawable fileIcon = new IconicsDrawable(getApplicationContext(),
		FontAwesome.Icon.faw_file)
		.color(IconicsColor.colorRes(R.color.Gray)).size(IconicsSize.dp(100));
	private Drawable dirIcon = new IconicsDrawable(getApplicationContext(),
			FontAwesome.Icon.faw_folder)
			.color(IconicsColor.colorRes(R.color.Gray)).size(IconicsSize.dp(100));

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
//		model = ViewModelProviders.of((FragmentActivity) getActivity()).get(MyViewModel.class);
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


	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			Log.d(TAG, "RightFragment-onActivityCreated:" + savedInstanceState.toString());
		}
		spinner = getActivity().findViewById(R.id.pb_right);
		msgView = getActivity().findViewById(R.id.tv_right);
		titleView = getActivity().findViewById(R.id.tv_right_title);
		((Vars)getActivity().getApplication()).recyclerView = getActivity().findViewById(R.id.rv_right);
//		model.recyclerView = getActivity().findViewById(R.id.rv_right);
		swipeContainer = getActivity().findViewById(R.id.sr_right);
		swipeContainer.setOnRefreshListener(() -> functionOnTitle());
		swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		if (title != null && !title.isEmpty()) {
			if (uri != null) {
				titleView.setText(String.format("%s\n%s", title, uri.toString()));
			} else {
				titleView.setText(title);
			}
		}
		if (msg != null && !msg.isEmpty()) {
			msgView.setText(msg);
		} else {
			spinner.setVisibility(View.VISIBLE);
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
//			model.recyclerView.setVisibility(View.INVISIBLE);
//			rightAdapter = new RightAdapter(null, null, null);
			((Vars)getActivity().getApplication()).rightAdapter = new RightAdapter(null, null, null);
//			model.rightAdapter = new RightAdapter(null, null, null);
			GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),
					Tools.calculateNoOfColumns(getApplicationContext(),
							(1.2f * ((Vars)getApplicationContext()).getIconSize())
//							(1.2f * model.getIconSize().getValue())
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
//			model.recyclerView.setLayoutManager(layoutManager);
			((Vars)getActivity().getApplication()).recyclerView.setHasFixedSize(true);
//			model.recyclerView.setHasFixedSize(true);
//			recyclerView.setAdapter(rightAdapter);
			((Vars)getActivity().getApplication()).recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);
//			model.recyclerView.setAdapter(model.rightAdapter);
//			recyclerView.setAdapter(listAdapter);
			/*((Vars)getActivity().getApplication()).recyclerView.setOnClickListener(this);*/
//			model.recyclerView.setOnClickListener(this);
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
//			File dir = new File(Environment.getExternalStorageDirectory().getPath());
			File dir = new File(Environment.getRootDirectory().getPath());
			Log.d(TAG, title+ ":"+ dir.toString()+ ":");
			new FetchFilesTask(dir).execute();
			//files
		}
	}

/*
 * override for LoaderManager.LoaderCallbacks
 */
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
//			model.recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			getContacts(id);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			if (contactsList.size() > 0) {
				((Vars)getActivity().getApplication()).rightAdapter.setData(contactsList);
				((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(new IconicsDrawable(
						getApplicationContext(),
						FontAwesome.Icon.faw_user_circle1)
						.color(IconicsColor.colorRes(R.color.White))
						.size(IconicsSize.dp(((Vars) getApplicationContext()).getIconSize())));
				((Vars)getActivity().getApplication()).rightAdapter.setListener((name, item, position) -> {
					AlertDialog.Builder contactDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
					contactDialogBuilder.setTitle(name);
					contactDialogBuilder.setItems(R.array.contactShare, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.d(TAG, "share via: "+ which);
						}
					});
					contactDialogBuilder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
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
				});
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
//			model.recyclerView.setVisibility(View.INVISIBLE);
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
//						.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.dp(Objects.requireNonNull(model.getIconSize().getValue())));
//				myList.setData(listOfAllImages, defaultImg);
//				rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
				((Vars)getActivity().getApplication()).rightAdapter.setListener(new RightAdapter.ListItemClickListener() {
					@Override
					public void onListItemClick(String name, FileItem item, int position) {
						if (item.getFullPath() != null) {
							Intent intent = new Intent(Intent.ACTION_VIEW, item.getFullPath());
							intent.setType("image/*");
							if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
								startActivity(intent);
							}
						}
					}
				});
/*				((Vars)getActivity().getApplication()).rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
//				model.rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
					@Override
					public void onListItemClick(String name, FileItem item, int position) {
						if (item.getFullPath() != null) {
							Intent intent = new Intent(Intent.ACTION_VIEW, item.getFullPath());
							intent.setType("image/*");
							if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
								startActivity(intent);
							}
						}
					}
				});
//				recyclerView.setAdapter(rightAdapter);
				((Vars)getActivity().getApplication()).recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);*/
//				model.recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);
//				model.recyclerView.setAdapter(model.rightAdapter);
//				rightAdapter.setData(listOfAllImages);
				((Vars)getActivity().getApplication()).rightAdapter.setData(listOfAllImages);
//				model.rightAdapter.setData(listOfAllImages);
				((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
//				model.rightAdapter.notifyDataSetChanged();
//				rightAdapter.setDefaultImage(defaultImg);
				((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(defaultImg);
//				model.rightAdapter.setDefaultImage(defaultImg);
				spinner.setVisibility(View.INVISIBLE);
				((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
//				model.recyclerView.setVisibility(View.VISIBLE);
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
//			model.recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			getMusic(id);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {//done
			if (listOfMyMusic.size() > 0) {
				((Vars)getActivity().getApplication()).rightAdapter.setListener((name, item, position) -> {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(item.getFullPath());
					if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
						startActivity(intent);
					}
				});
//				model.recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);
//				model.recyclerView.setAdapter(model.rightAdapter);
				((Vars)getActivity().getApplication()).rightAdapter.setData(listOfMyMusic);
				((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
//				model.rightAdapter.setData(listOfMyMusic);
				((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(
						new IconicsDrawable(getApplicationContext(),
//				model.rightAdapter.setDefaultImage(new IconicsDrawable(getApplicationContext(),
						FontAwesome.Icon.faw_music)
						.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.dp(100)));
				spinner.setVisibility(View.INVISIBLE);
				((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
//				model.recyclerView.setVisibility(View.VISIBLE);
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
			titleView.setText(String.format("%s\n%s", title, directory));
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
//			model.recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected File[] doInBackground(Void... params) {
			File[] files = listFiles(directory);
			return files;
		}

		@RequiresApi(api = Build.VERSION_CODES.O)
		@Override
		protected void onPostExecute(File[] files) {
			super.onPostExecute(files);
			if (files != null) {
//				Log.d(TAG, "FetchFilesTask count="+ files.length);
				Tika tika = new Tika();
				((Vars)getActivity().getApplication()).rightAdapter.setListener(new RightAdapter.ListItemClickListener() {
					@Override
					public void onListItemClick(String name, FileItem item, int position) {
						if (item.getMIME() == "directory") {
							openDirectory(item, (files.length > 0) ? files[0] : new File(directoryStr));
						} else {
							drawFileContextMenu(item);
						}
					}
				});
				((Vars)getActivity().getApplication()).recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);
//				model.recyclerView.setAdapter(model.rightAdapter);
//				if (!directoryStr.equals(Environment.getExternalStorageDirectory().toString())) {
				if (!directoryStr.equals(Environment.getRootDirectory().toString())) {
					FileItem item = new FileItem(getString(R.string.up), null, 0,
							Tools.drawableToBitmap(dirIcon), "directory", true,
							"", null, false, "..");
					listOfAllFiles.add(item);
				}
				for (File f : files) {
					String mime = "directory";
					FileItem item = new FileItem(f.getName(), Uri.fromFile(f), 0,
							Tools.drawableToBitmap(fileIcon), mime, false, f.getName(),
							null, false, f.getAbsolutePath());
					if (f.isFile()) {
						setMIME(mime, item);
					} else {
						String[] children = f.list();
						item.setHasContents(children != null && children.length > 0);
						item.setBitmap(Tools.drawableToBitmap(dirIcon));
					}
					listOfAllFiles.add(item);
					Log.d(TAG, "file item is "+ item.toString());
				}
				((Vars)getActivity().getApplication()).rightAdapter.setData(listOfAllFiles);
//				model.rightAdapter.setData(listOfAllFiles);
				((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(
						new IconicsDrawable(getApplicationContext(),
//				model.rightAdapter.setDefaultImage(new IconicsDrawable(getApplicationContext(),
						FontAwesome.Icon.faw_file)
						.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.dp(100)));
				spinner.setVisibility(View.INVISIBLE);
				((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
//				model.recyclerView.setVisibility(View.VISIBLE);
			}
		}

	}


	private void setMIME(String mime, FileItem item) {
	/*	try {
			mime = tika.detect(f);
		} catch (IOException e) {
			e.printStackTrace();*/
			mime = "";
	/*	}*/
		switch (mime) {
			case "a":
				item.setMIME("a");
				break;
			case "b":
				item.setMIME("b");
				break;
			default:
//				Log.d(TAG, "file ("+ f.toString()+ ") is mime "+ mime);
				break;
		}
	}


	private void openDirectory(FileItem item, File file) {
		String path = "";
		if (item.getLabel().equals("..")) {
			if (file != null && file.getParent() != null) {
				path = file.getParent();
				Log.d(TAG, "opening parent directory: "+ path);
			}
		} else {
			path = item.getFullPath().getPath();
			Log.d(TAG, "opening directory: " + path);
		}
		listOfAllFiles.clear();
		((Vars)getActivity().getApplication()).rightAdapter.clear();
		((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
		new FetchFilesTask(path).execute();
	}


	private void drawFileContextMenu(FileItem item) {
		boolean editFaded = false;
//		Log.d(TAG, "drawFileContextMenu index "+ index);
		AlertDialog.Builder contextmenuBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
		contextmenuBuilder.setTitle(item.getName());
		ArrayList<ListItem> items = new ArrayList<>();
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.open)), null,
				new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_ellipsis_h)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, getApplicationContext()), Tools.dpToPx(16, getApplicationContext()),
				Tools.dpToPx(18, getApplicationContext()), true, Tools.dpToPx(8, getApplicationContext()),
				false));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.rename, "")),null,
				new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_newspaper1)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, getApplicationContext()), Tools.dpToPx(16, getApplicationContext()),
				Tools.dpToPx(18, getApplicationContext()), true, Tools.dpToPx(8, getApplicationContext()),
				false));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.edit)),null,
				new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_edit1)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, getApplicationContext()), Tools.dpToPx(16, getApplicationContext()),
				Tools.dpToPx(18, getApplicationContext()), true, Tools.dpToPx(8, getApplicationContext()),
				editFaded));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.delete)),null,
				new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_eraser)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, getApplicationContext()), Tools.dpToPx(16, getApplicationContext()),
				Tools.dpToPx(18, getApplicationContext()), true, Tools.dpToPx(8, getApplicationContext()),
				false));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.copy)),null,
				new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_copy)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, getApplicationContext()), Tools.dpToPx(16, getApplicationContext()),
				Tools.dpToPx(18, getApplicationContext()), true, Tools.dpToPx(8, getApplicationContext()),
				false));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.move)),null,
				new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_arrows_alt)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, getApplicationContext()), Tools.dpToPx(16, getApplicationContext()),
				Tools.dpToPx(18, getApplicationContext()), true, Tools.dpToPx(8, getApplicationContext()),
				listOfAllFiles.size() > 1));

		final View customView = View.inflate(getActivity(), R.layout.dialog_list, null);
		customView.setPadding(Tools.dpToPx(8, getApplicationContext()), Tools.dpToPx(10, getApplicationContext()),
				Tools.dpToPx(8, getApplicationContext()), Tools.dpToPx(10, getApplicationContext()));
		contextmenuBuilder.setView(customView);

		final AlertDialog contextmenuDialog = contextmenuBuilder.create();
		contextmenuDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
				(dialog1, which) -> dialog1.cancel());
		contextmenuDialog.setCanceledOnTouchOutside(false);
		contextmenuDialog.show();
		CustomDialogListAdapter adapter = new CustomDialogListAdapter(getApplicationContext(),
				R.layout.dialog_list, items, item1 -> {
			contextmenuDialog.cancel();
			String name = item1.getName();
			fileContextmenuSelection(name, item);
		});
		ListView listView = customView.findViewById(R.id.lv_dialog_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener((parent, view, position, itemId) -> contextmenuDialog.dismiss());
	}


	private void fileContextmenuSelection(String operation, FileItem item) {
		int i = 0;
		int pos = -1;
		for (FileItem fileItem : listOfAllFiles) {
			i++;
			if (fileItem.getName().equals(item.getName())) {
				pos = i;
				break;
			}
		}
		pos--;
		final int position = pos;
		if (operation.toUpperCase().equals(getString(R.string.remove).toUpperCase())) {
				androidx.appcompat.app.AlertDialog.Builder rusureDialog = new androidx.appcompat.app.AlertDialog.Builder(getApplicationContext(), R.style.CustomDialogTheme);
				rusureDialog.setTitle(getString(R.string.rusure));
				rusureDialog.setMessage(getString(R.string.delete_conn_x, item.getName()));
				rusureDialog.setNegativeButton(getString(android.R.string.cancel), null);
				rusureDialog.setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
					listOfAllFiles.remove(item);
					((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
				});
				rusureDialog.show();
		} else if (operation.toUpperCase().equals(getString(R.string.rename, "").toUpperCase())) {
			androidx.appcompat.app.AlertDialog.Builder renameDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
			renameDialogBuilder.setTitle(getString(R.string.rename, item.getName()));
			final View customView = View.inflate(getApplicationContext(), R.layout.edit_one_field, null);
			customView.setPadding(Tools.dpToPx(8, getApplicationContext()), Tools.dpToPx(24, getApplicationContext()),
					Tools.dpToPx(8, getApplicationContext()), Tools.dpToPx(24, getApplicationContext()));
			final TextView label = customView.findViewById(R.id.tv_edit_label);
			final EditText value = customView.findViewById(R.id.et_edit_field);
			label.setText(Tools.toTitleCase(getString(R.string.name)));
			value.setHint(item.getName());
			renameDialogBuilder.setView(customView);

			final androidx.appcompat.app.AlertDialog renameDialog = renameDialogBuilder.create();
			renameDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
			renameDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
					(dialog1, which) -> dialog1.cancel());
			renameDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
					(dialog1, which) -> {
						if (position > -1 && value.getText() != null) {
							Log.d(TAG, "renaming "+ item.getName()+ " at "+ position);
							listOfAllFiles.get(position).setName(value.getText().toString());
						}
						((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
					});
			renameDialog.show();
		} else if (operation.toUpperCase().equals(getString(R.string.move).toUpperCase())) {
		} else if (operation.toUpperCase().equals(getString(R.string.connto).toUpperCase())) {
//			SelectedLeftItemIndex(index);
		} else if (operation.toUpperCase().equals(getString(R.string.edit_conn).toUpperCase())) {
				Log.d(TAG, "editing:" + item.toString());
		}
	}


	private File[] listFiles(File directory) {
		if (!directory.isDirectory()) {
			new Thread()
			{
				public void run()
				{
					getActivity().runOnUiThread(() -> Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show());
//					drawFileContextMenu();
				}
			}.start();
//			Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
			Log.d(TAG, "listFiles error for "+ directory.getPath());
			return null;
		}
		File[] files = directory.listFiles();
//		Log.d(TAG, "listFiles length="+ files.length);
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

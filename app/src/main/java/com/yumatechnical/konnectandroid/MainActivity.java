package com.yumatechnical.konnectandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.TokenAccessType;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v1.DbxEntry;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yumatechnical.konnectandroid.Adapter.CustomDialogListAdapter;
import com.yumatechnical.konnectandroid.Adapter.DialogListAdapter;
import com.yumatechnical.konnectandroid.Adapter.LeftArrayAdapter;
import com.yumatechnical.konnectandroid.Fragment.RightFragment;
import com.yumatechnical.konnectandroid.Helper.Dropbox.DropboxInstance;
import com.yumatechnical.konnectandroid.Helper.Dropbox.UploadTask;
import com.yumatechnical.konnectandroid.Helper.NetworkUtils;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.Helper.URI_to_Path;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;
import com.yumatechnical.konnectandroid.Model.KeyStrValueStr;
import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.Settings.SettingsActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
		implements SharedPreferences.OnSharedPreferenceChangeListener,
		Preference.OnPreferenceChangeListener,
		LeftArrayAdapter.OnClickListener,
		/*AddConnectionFragment.OnListFragmentInteractionListener,*/
		LoaderManager.LoaderCallbacks<String> {

	FrameLayout left, right, base;
	private static final int MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE = 88;
	private static final int MY_PERMISSION_RECORD__REQUEST_CODE = 124;
	private static final int MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE = 4;
	private static final int MY_PERMISSION_RECORD_WRITE_CONTACTS_REQUEST_CODE = 5;
	private static final int MY_PERMISSION_RECORD_READ_EXTERNAL_STORAGE_REQUEST_CODE = 8;
	private static final int MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE = 7;
	private static final int MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE = 6;
	private static final int MY_PHOTOS_ID = 1;
	private static final int MY_MUSIC_ID = 2;
	private static final int MY_CONTACTS_ID = 3;
	private static final int MY_FILES_ID = 4;
	RecyclerView recyclerViewLeft;
	ArrayList<ListItem> leftList = new ArrayList<>();
	private LeftArrayAdapter leftAdapter;
	private static final String TAG = "KonnectAndroid";
	Boolean show_hidden, save_pass, rem_local, rem_remote;
	AlertDialog.Builder builder;
	String[] colors = {"OneDrive", "Google Drive", "Dropbox", "BOX", "Cancel"};
	String[] sizes = {"red", "green", "blue", "black"};
	private BottomSheetDialog mBottomSheetDialog;
	CustomListViewDialog customDialog;
	private static DialogListAdapter adapter;
	static final int PICK_CONTACT = 1;
	private final int REQUEST_MULTIPLE_PERMISSIONS = 124;
//	private final static int IMG_SELECT_REQCODE = 4;
	private int minIconSize = 50;
	private int maxIconSize = 500;
	private int leftListDefaultLeftPadding;
	private int leftListDefaultTopPadding;
	private int leftListDefaultBottomPadding;
	private int leftListDefaultBetweenPadding;
	private boolean leftListDefaultIconBeforeText = true;
	private static final int TEST_CONNECT_LOADER = 23;
	private static final int IMAGE_REQUEST_CODE = 13;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SetupSharedPrefs();

		left = findViewById(R.id.left_frame);
		fillLeft();
		right = findViewById(R.id.right_frame);
		base = findViewById(R.id.base_frame);
		// Get current account info
//		FullAccount account = DropboxInstance.main().users().getCurrentAccount();
//		System.out.println(account.getName().getDisplayName());
	}


	void fillLeft() {
		leftListDefaultLeftPadding = Tools.dpToPx(16, this);
		leftListDefaultTopPadding = Tools.dpToPx(16, this);
		leftListDefaultBottomPadding = Tools.dpToPx(18, this);
		leftListDefaultBetweenPadding = Tools.dpToPx(8, this);
		Boolean fade_photos = false, fade_music = false, fade_contacts = false, fade_files = false;
		leftList.add(new ListItem(0, MY_PHOTOS_ID, getString(R.string.photos),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_images)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				leftListDefaultLeftPadding, leftListDefaultTopPadding, leftListDefaultBottomPadding,
				true, leftListDefaultBetweenPadding, fade_photos));
		leftList.add(new ListItem(0, MY_MUSIC_ID, getString(R.string.music),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_music)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				leftListDefaultLeftPadding, leftListDefaultTopPadding, leftListDefaultBottomPadding,
				true, leftListDefaultBetweenPadding, fade_music));
		leftList.add(new ListItem(0, MY_CONTACTS_ID, getString(R.string.contacts),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_address_book)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				leftListDefaultLeftPadding, leftListDefaultTopPadding, leftListDefaultBottomPadding,
				true, leftListDefaultBetweenPadding, fade_contacts));
		leftList.add(new ListItem(0, MY_FILES_ID, getString(R.string.files),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_file)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				leftListDefaultLeftPadding, leftListDefaultTopPadding, leftListDefaultBottomPadding,
				true, leftListDefaultBetweenPadding, fade_files));
//		recyclerViewLeft = findViewById(R.id.rv_left);
//		recyclerViewLeft.setLayoutManager(new LinearLayoutManager(this));
		ListView listView = findViewById(R.id.lv_left);
		listView.setSelector(R.drawable.list_selector);
		leftAdapter = new LeftArrayAdapter(this, 0, leftList, this);
		listView.setAdapter(leftAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	ArrayList<ListItem> fillAddConnections() {
		Boolean fade_photos = false, fade_music = false, fade_contacts = false, fade_files = false;
		ArrayList<ListItem> items = new ArrayList<>();
		items.add(new ListItem(0, 1, getString(R.string.gdrive),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_google_drive)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				fade_photos));
		items.add(new ListItem(0, 2, getString(R.string.onedrive),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_cloud)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				fade_music));
		items.add(new ListItem(0, 3, getString(R.string.dropbox),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_dropbox)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				fade_contacts));
		items.add(new ListItem(0, 4, getString(R.string.box),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_box)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				fade_files));
		items.add(new ListItem(0, 5, getString(R.string.ftp),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_server)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				fade_files));
		items.add(new ListItem(0, 6, getString(R.string.local_network),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_network_wired)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				fade_files));
		return items;
	}

	/**
	 * Methods for setting up the menu
	 **/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.visualizer_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem menuItem;

		menuItem = menu.findItem(R.id.add_connect);
		menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_plus)
				.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		menuItem = menu.findItem(R.id.settings);
		menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_cog)
				.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		menuItem = menu.findItem(R.id.resize);
		menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_arrows_alt)
				.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		Intent intent;
		switch (item.getItemId()) {
			case R.id.add_connect:
				ArrayList<ListItem> items = fillAddConnections();

				AlertDialog.Builder addConnectBuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

				final View customView = View.inflate(this, R.layout.dialog_list, null);
				customView.setPadding(Tools.dpToPx(8, this), Tools.dpToPx(24, this),
						Tools.dpToPx(8, this), Tools.dpToPx(24, this));
				addConnectBuilder.setView(customView);
//				builder.setCancelable(false);

				final AlertDialog addConnectionDialog = addConnectBuilder.create();
				addConnectionDialog.setTitle(Html.fromHtml("<font color='#111111'>"+ getString(R.string.addConnection)+ "</font>"));
				addConnectionDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
						(dialog1, which) -> dialog1.cancel());
//				addConnectionDialog.setCanceledOnTouchOutside(false);
				addConnectionDialog.show();

				CustomDialogListAdapter adapter = new CustomDialogListAdapter(this,
						R.layout.dialog_list, items, new CustomDialogListAdapter.OnClickListener() {
					@Override
					public void OnSelecttem(ListItem item1) {
						addConnectionDialog.dismiss();
						String name = item1.getName();
						int id = item1.getID();
						leftList.add(item1);
//							Log.d(TAG, "leftlist="+ leftList.toString());
						leftAdapter.notifyDataSetChanged();
						if (name.equals(MainActivity.this.getString(R.string.gdrive))) {
							Log.d(TAG, "selected GDrive:" + id);
//							ShareCompat.IntentBuilder.from(this)
//									.setChooserTitle("title")
//									.setType("mimiType")
//									.setText("textToShare");
						} else if (name.equals(MainActivity.this.getString(R.string.onedrive))) {
							Log.d(TAG, "selected Onedrive:" + id);
						} else if (name.equals(getString(R.string.dropbox))) {
//							DropboxInstance dropboxInstance = new DropboxInstance();
//							dropboxInstance.authorise(DropboxInstance.appInfo());
//							Auth.startOAuth2Authentication(getApplicationContext(), Vars.getDropboxKey());
//							^ works -requiress manifest info -switches view to black??
/*
							DbxRequestConfig requestConfig = new DbxRequestConfig("examples-authorize");
							DbxWebAuth webAuth = new DbxWebAuth(requestConfig,
									new DbxAppInfo(Vars.getDropboxKey(), Vars.getDropboxSecret()));
							DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder()
									.withNoRedirect()
//									.withTokenAccessType(TokenAccessType.OFFLINE)
									.build();
							String authorizeUrl = webAuth.authorize(webAuthRequest);
							Uri authUri = Uri.parse(authorizeUrl);
							Log.d(TAG, "Uri="+ authUri.toString());
							Intent intent = new Intent(Intent.ACTION_VIEW, authUri);
							if (intent.resolveActivity(getPackageManager()) != null) {
								startActivity(intent);
							}*/
							Uri.Builder builder = new Uri.Builder();
							builder.scheme("ftp").encodedAuthority("yumausa:yuma@192.168.1.222").path("/");
							Uri uri = builder.build();
							Log.d(TAG, "Uri="+ uri);
//							Intent intent = new Intent(Intent.ACTION_VIEW);
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							if (intent.resolveActivity(getPackageManager()) != null) {
								startActivity(intent);
							}
						} else if (name.equals(MainActivity.this.getString(R.string.ftp))) {
//							MainActivity.this.showFTPsettings("ftp", id);
							showFTPsettings("ftp", id);
						} else if (name.equals(MainActivity.this.getString(R.string.local_network))) {
//							MainActivity.this.showFTPsettings("smb", id);
							showFTPsettings("smb", id);
						}
					}
				});
				ListView listView = customView.findViewById(R.id.lv_dialog_list);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener((parent, view, position, id) -> addConnectionDialog.dismiss());
				return true;
			case R.id.settings:
/*				URL url = null;
				try {
//					url = new URL("caseontent", "contacts","people");//MalformedURLException
					url = new URL("content:contacts/people");
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				if (url != null) {
					Log.d(TAG, "URL=" + url.toString());
				}*/
				/*
//				Log.d(TAG, "dropbox is\n"+ DropboxInstance.getAppInfo());
				DropboxInstance dropbox = new DropboxInstance();
				dropbox.getClient(new DbxRequestConfig(Objects.requireNonNull(this.getClass().getCanonicalName())),
						Vars.getDropboxAccessToken());
				dropbox.authorise(new DbxAppInfo(Vars.getDropboxKey(), Vars.getDropboxSecret()));
				*/
/*				//test GET
				OKHTTPcommon.GET mytest = new OKHTTPcommon.GET();
				ArrayList<KeyStrValueStr> params = new ArrayList<>(), headers = new ArrayList<>();
				//			params.add(new KeyStrValueStr("key", "value"));
//			headers.add(new KeyStrValueStr("key", "value"));
				try {
					String response = mytest.run(
							OKHTTPcommon.buildRequestWithHeaders(
									Tools.buildURLwithParams("https://raw.github.com/square/okhttp/master/README.md",
											params),
									headers));
//					System.out.println(response);
					AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
					alert.setTitle(R.string.testConnect);
					alert.setMessage(response);
					alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
*/
/**/
				Intent settingsActivity = new Intent(this, SettingsActivity.class);
				startActivity(settingsActivity);
				/**/
				return true;
			case R.id.resize:
				final View sliderView = View.inflate(this, R.layout.size_slider, null);
				SeekBar slider = sliderView.findViewById(R.id.sb_slider);
				if (right != null) {
					maxIconSize = right.getWidth();
				}
				Log.d(TAG, "right width="+ maxIconSize);
				slider.setMax(maxIconSize);
				int factorPercent = (maxIconSize - minIconSize) / 100;
				int itemSize = ((Vars) getApplicationContext()).getIconSize();
				slider.setProgress((itemSize-minIconSize)*factorPercent);
				int initial = itemSize;
				slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
						seekBar.setProgress(progress);
						((Vars) getApplicationContext()).setIconSize((progress / factorPercent) + minIconSize);
//						if (((Fragment)RightFragment()).rightAdapter != null) {
//							((Fragment)RightFragment()).rightAdapter.notifyDataSetChanged();
//						}
					}
					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});
				AlertDialog.Builder resizeBuilder = new AlertDialog.Builder(this);
				resizeBuilder.setView(sliderView);
//				builder.setItems(colors, (dialog12, which) -> Log.d(TAG, "the user selected:"+ which));
				final AlertDialog resizeDialog = resizeBuilder.create();
				resizeDialog.setTitle(getResources().getString(R.string.icon_resize));
				resizeDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
						(dialog1, which) -> {
							dialog1.cancel();
						});
				resizeDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
						(dialog1, which) -> {
							slider.setProgress(initial);
							((Vars) getApplicationContext()).setIconSize(initial);
							dialog1.cancel();
						});
				resizeDialog.show();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showFTPsettings(String method, int id) {
		AlertDialog.Builder showFTPbuilder;
		showFTPbuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
		final View formFTP = View.inflate(this, R.layout.ftp_settings_form, null);
		formFTP.setPadding(Tools.dpToPx(8, this), Tools.dpToPx(24, this),
				Tools.dpToPx(8, this), Tools.dpToPx(24, this));
		showFTPbuilder.setView(formFTP);
//				builder.setCancelable(false);
		final AlertDialog formDialog = showFTPbuilder.create();
		formDialog.setTitle(Html.fromHtml("<font color='#111111'>"+ getString(R.string.ftpConnect)+ "</font>"));
//	get view elements
		TextView title = formFTP.findViewById(R.id.tv_ftp_title);
		ImageView rename = formFTP.findViewById(R.id.iv_ftp_rename);
		TextView hlabel = formFTP.findViewById(R.id.tv_ftp_host);
		TextView ulabel = formFTP.findViewById(R.id.tv_ftp_username);
		TextView plabel = formFTP.findViewById(R.id.tv_ftp_password);
		TextView tlabel = formFTP.findViewById(R.id.tv_ftp_port);
		EditText hname = formFTP.findViewById(R.id.et_ftp_host);
		EditText uname = formFTP.findViewById(R.id.et_ftp_username);
		EditText pword = formFTP.findViewById(R.id.et_ftp_password);
		EditText portn = formFTP.findViewById(R.id.et_ftp_port);
		EditText etitle = formFTP.findViewById(R.id.et_ftp_rename);
		ImageView spinner = formFTP.findViewById(R.id.iv_ftp_progress);
		title.setText(Tools.toTitleCase(getString(R.string.eg_ip_addr)));
//	toggle rename button
		final boolean[] openedRename = {false};
		rename.setOnClickListener(v -> {
			if (openedRename[0]) {
				openedRename[0] = false;
				etitle.setVisibility(View.GONE);
			} else {
				openedRename[0] = true;
				etitle.setVisibility(View.VISIBLE);
			}
		});
		rename.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_edit)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE));
//	set field labels
		hlabel.setText(Tools.toTitleCase(getString(R.string.server)));
		ulabel.setText(Tools.toTitleCase(getString(R.string.username)));
		plabel.setText(Tools.toTitleCase(getString(R.string.password)));
		tlabel.setText(Tools.toTitleCase(getString(R.string.port)));
		spinner.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_spinner)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE));
//  button actions
		formDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.testConnect),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog1, int which) {
//						LinearLayout layout = formFTP.findViewById(R.id.ll_ftp_main);
//						layout.setBackgroundColor(Color.GRAY);
						spinner.setVisibility(View.VISIBLE);
						Log.d(TAG, "why this dialog closes?");
//					formDialog.getButton(which).setText(FontAwesome.Icon.faw_spinner);
//						formDialog.getButton(which).setText("...");
/*						String string = method + "://" + uname.getText() + ":" + pword.getText() + "@" +
								hname.getText() + ":" + portn.getText() + "/";*/
/*						AlertDialog.Builder testBuilder = new AlertDialog.Builder(MainActivity.this);
						testBuilder.setTitle("testing...");
						testBuilder.setMessage(string);
						testBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						testBuilder.show();*/
/*						Log.d(TAG, "button TEST string=" + string);
						Bundle bundle = new Bundle();
						bundle.putString("string", string);
						bundle.putInt("which", which);
						bundle.putString("dialog", dialog1.toString());
						Uri uri = Uri.parse(string);*/
//
//					new connectTask(string, which, dialog1).execute();
//
//					LoaderManager loaderManager = getSupportLoaderManager();
//					Loader<String> loader = loaderManager.getLoader(TEST_CONNECT_LOADER);
//					if (loader == null) {
//						loaderManager.initLoader(TEST_CONNECT_LOADER, bundle, this);
//					} else {
//						loaderManager.restartLoader(TEST_CONNECT_LOADER, bundle, this);
//					}
					}
				});
		formDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
				(dialog1, which) -> {
					String string = method+ "://"+ uname.getText()+ ":"+ pword.getText()+ "@"+
							hname.getText()+ ":"+ portn.getText()+ "/";
					Uri uri = Uri.parse(string);
					String name = etitle.getText().toString().isEmpty() ? title.getText().toString() : etitle.getText().toString();
					Log.d(TAG, "button OK string="+ string);
					((Vars)this.getApplication()).addConnectionItem(new ConnectionItem(id, string));
//					leftList.add(new ListItem(0, id, name, "",null,
//							leftListDefaultLeftPadding, leftListDefaultTopPadding, leftListDefaultBottomPadding,
//							leftListDefaultIconBeforeText, leftListDefaultBetweenPadding, false));
					dialog1.cancel();
				});
		formDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
				(dialog1, which) -> dialog1.cancel());
		formDialog.setCanceledOnTouchOutside(false);
		formDialog.show();
	}


//	@SuppressLint("StaticFieldLeak")
//	public class connectTask extends AsyncTaskLoader<Void, Void, Void> {
//
//		String string;
//		int which;
//		DialogInterface dialog;
//
//
//		connectTask(String string, int which, DialogInterface dialog) {
//			this.string = string;
//			this.which = which;
//			this.dialog = dialog;
//		}
//
//
//		@Nullable
//		@Override
//		public Void loadInBackground() {
//			Log.d(TAG, "connecting to ..."+ string);
//			return null;
//		}
//	}


	void SetupSharedPrefs() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		show_hidden = sharedPreferences.getBoolean(getString(R.string.PREFS_show_hidden),
				getResources().getBoolean(R.bool.PREFS_show_hidden_default));
		save_pass = sharedPreferences.getBoolean(getString(R.string.PREFS_save_pass),
				getResources().getBoolean(R.bool.PREFS_save_pass_default));
		rem_local = sharedPreferences.getBoolean(getString(R.string.PREFS_rem_local),
				getResources().getBoolean(R.bool.PREFS_rem_local_default));
		rem_remote = sharedPreferences.getBoolean(getString(R.string.PREFS_rem_remote),
				getResources().getBoolean(R.bool.PREFS_rem_remote_default));
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(getString(R.string.PREFS_show_hidden))) {
			show_hidden = sharedPreferences.getBoolean(getString(R.string.PREFS_show_hidden),
					getResources().getBoolean(R.bool.PREFS_show_hidden_default));
		} else
		if (key.equals(getString(R.string.PREFS_save_pass))) {
			save_pass = sharedPreferences.getBoolean(getString(R.string.PREFS_save_pass),
					getResources().getBoolean(R.bool.PREFS_save_pass_default));
		} else
		if (key.equals(getString(R.string.PREFS_rem_local))) {
			rem_local = sharedPreferences.getBoolean(getString(R.string.PREFS_rem_local),
					getResources().getBoolean(R.bool.PREFS_rem_local_default));
		} else
		if (key.equals(getString(R.string.PREFS_rem_remote))) {
			rem_remote = sharedPreferences.getBoolean(getString(R.string.PREFS_rem_remote),
					getResources().getBoolean(R.bool.PREFS_rem_remote_default));
		}
//		SetupSharedPrefs();
	}


	/**
	 * onPause Cleanup audio stream
	 **/
	@Override
	protected void onPause() {
		super.onPause();
//		if (mAudioInputReader != null) {
//			mAudioInputReader.shutdown(isFinishing());
//		}
	}

	@Override
	protected void onResume() {
		super.onResume();
//		if (mAudioInputReader != null) {
//			mAudioInputReader.restart();
//		}
		DropboxInstance dropboxInstance = new DropboxInstance();
		dropboxInstance.getAccessToken();
		String accessToken = Auth.getOAuth2Token();
		if (accessToken != null) {
			Vars.setDropboxAccessToken(accessToken);
		}
	}

	@Override
	protected void onDestroy() {
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
	}


	private boolean setupPermissions(String permission_required, String message, int my_request_code) {
		if (ActivityCompat.checkSelfPermission(this,
				permission_required) != PackageManager.PERMISSION_GRANTED) {
			if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission_required)) {
				if (message != null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle(getString(R.string.no_perm));
					builder.setMessage(message);
					builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							;
						}
					});
					builder.show();
					return false;
				}
				ActivityCompat.requestPermissions(this, new String[]{permission_required},
						my_request_code);
				return false;
			}
		} else {
			Log.d(TAG, "permission has already been granted:" + message);
			return true;
		}

		/**
		 * App Permissions for Audio
		 **/
/*
		// If we don't have the record audio permission...
		if (ActivityCompat.checkSelfPermission(this,
				Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
			// And if we're on SDK M or later...
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				// Ask again, nicely, for the permissions.
				String[] permissionsWeNeed = new String[]{ Manifest.permission.RECORD_AUDIO };
				requestPermissions(permissionsWeNeed, MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE);
			}
//		} else {
			// Otherwise, permissions were granted and we are ready to go!
//			mAudioInputReader = new AudioInputReader(mVisualizerView, this);
		}
*/
		return false;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d(TAG, "The permission was granted! Start up the visualizer!");
//					mAudioInputReader = new AudioInputReader(mVisualizerView, this);

				} else {
					Toast.makeText(this, getString(R.string.no_perm_audio), Toast.LENGTH_LONG).show();
					finish();
					// The permission was denied, so we can show a message why we can't run the app
					// and then close the app.
				}
				break;
			}
			case MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE: {
				doListMusic();
				break;
			}
			case MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE: {
				doListPhotos();
				break;
			}
			case MY_PERMISSION_RECORD_WRITE_CONTACTS_REQUEST_CODE:
			case MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE: {
				doListContacts();
				break;
			}
//			case MY_PERMISSION_RECORD__REQUEST_CODE: {
//				Log.d(TAG, "onRequestPermissionsResult: " + requestCode);
//			}
			default:
				Log.d(TAG, "onRequestPermissionsResult-Unexpected value: " + requestCode);
		}
	}

	public void CanAccess(List<KeyStrValueStr> permissions)
	{
		List<String> permissionsNeeded = new ArrayList<>();
		final List<String> permissionsList = new ArrayList<>();

		for (int i = 0; i < permissions.size(); i++) {
			if (!addPermission(permissionsList, permissions.get(i).getKey())) {
				permissionsNeeded.add(permissions.get(i).getValue());
			}
		}
//		if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
//			permissionsNeeded.add("Read Contacts");
//		if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
//			permissionsNeeded.add("Write Contacts");
		if (permissionsList.size() > 0) {
			if (permissionsNeeded.size() > 0) {
				StringBuilder message = new StringBuilder("You need to grant access to " + permissionsNeeded.get(0));
				for (int i = 1; i < permissionsNeeded.size(); i++)
					message.append(", ").append(permissionsNeeded.get(i));
				Tools.showMessageOKCancel(this, message.toString(),
						(dialog, which) -> {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
								requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
										REQUEST_MULTIPLE_PERMISSIONS);
							}
						});
				return;
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
						REQUEST_MULTIPLE_PERMISSIONS);
			}
		}
	}

	private boolean addPermission(List<String> permissionsList, String permission) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
				permissionsList.add(permission);

				return shouldShowRequestPermissionRationale(permission);
			}
		}
		return true;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		return false;
	}


	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		if (resultCode != RESULT_OK || data == null) {
			return;
		}
		switch (reqCode) {
			case (PICK_CONTACT):
				if (resultCode == Activity.RESULT_OK) {
					Uri contactData = data.getData();
					Cursor c = managedQuery(contactData, null, null, null, null);
					if (c.moveToFirst()) {
						String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
						String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
						try {
							if (hasPhone.equalsIgnoreCase("1")) {
								Cursor phones = getContentResolver().query(
										ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
										null, null);
								phones.moveToFirst();
								String cNumber = phones.getString(phones.getColumnIndex("data1"));
								System.out.println("number is:" + cNumber);
//								txtphno.setText("Phone Number is: "+cNumber);
							}
							String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//							txtname.setText("Name is: "+name);
						} catch (Exception ex) {
//							st.getMessage();
						}
					}
				}
				break;
			case (MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE):
				break;
			case (MY_PERMISSION_RECORD_READ_EXTERNAL_STORAGE_REQUEST_CODE):
				break;
/*				case (4)://IMG_SELECT_REQCODE):
				Log.d(TAG, "contactImageImgView.setImageURI(data.getData());");
//					contactImageImgView.setImageURI(data.getData());
				break;*/
			case (IMAGE_REQUEST_CODE):
				DbxEntry.File file = new DbxEntry.File(URI_to_Path.getPath(getApplication(), data.getData()),
						"iconName", false, 0, "humanSize", null, null, "rev");
				DropboxInstance dropboxInstance = new DropboxInstance();
				if (file != null) {
					//Initialize UploadTask
					new UploadTask(dropboxInstance.getClient(dropboxInstance.requestConfig(Vars.getDropboxAccessToken()),
							Vars.getDropboxAccessToken()), file, getApplicationContext()).execute();
				}
				break;
		}
	}


	void getMediaList() {
//		List<KeyStrValueStr> permissions = new ArrayList<>();
//		permissions.add(new KeyStrValueStr(Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage"));
//		permissions.add(new KeyStrValueStr(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage"));
//		CanAccess(permissions);
		ContentResolver musicResolver = getContentResolver();
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor = musicResolver.query(musicUri, null, null,
				null, null);

		if (musicCursor!=null && musicCursor.moveToFirst()) {
			//get columns
			int titleCol = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
			int idCol = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
			int artistCol = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
			int albumCol = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
			//add songs to list
			do {
				long thisId = musicCursor.getLong(idCol);
				String thisTitle = musicCursor.getString(titleCol);
				String thisArtist = musicCursor.getString(artistCol);
				String thisAlbum = musicCursor.getString(albumCol);
				String add = thisId + ":" + thisTitle + ":" + thisArtist + ":" + thisAlbum;
//				List<FileItem> fileItemList = new ArrayList<>();
//				Parcel parcel;
//				FileItem fileItem = new FileItem(add, null, Integer.parseInt(String.valueOf(thisId)),
//						null, "audio/mp3", false, add);
//				parcel = FileItem.w;
//			rightList.add(parcel);
				Log.d(TAG, add);
			}
			while (musicCursor.moveToNext());
//			Log.d(TAG, String.valueOf(rightlist));
//			fillRight();
		}
		if (musicCursor != null) {
			musicCursor.close();
		}
	}


	//from LeftArrayAdapter.OnClickListener
	@Override
	public void OnClickItem(String item) {
		/*
//		new AlertDialog.Builder(getContext())
//				.setMessage(R.string.item_disabled)
////							.setPositiveButton("OK", null)
//				.setNegativeButton("Cancel", null)
//				.create()
//				.show();
//		;
		Log.d(TAG, "clicked: " + item);
		if (item.equals(getString(R.string.photos))) {
//			List<KeyStrValueStr> permissions = new ArrayList<>();
//			permissions.add(new KeyStrValueStr(Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage"));
//			permissions.add(new KeyStrValueStr(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage"));
//			AccessContact(permissions);
			if (!Permissons.Check_STORAGE(this)) {
				Permissons.Request_STORAGE(this, MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE);
			} else {
				doListPhotos();
			}
		} else
		if (item.equals(getString(R.string.music))) {
//			List<KeyStrValueStr> permissions = new ArrayList<>();
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//				permissions.add(new KeyStrValueStr(Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage"));
//			}
//			permissions.add(new KeyStrValueStr(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage"));
//			CanAccess(permissions);
//			getMediaList();
			if (!Permissons.Check_STORAGE(this)) {
				Permissons.Request_STORAGE(this, MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE);
			} else {
				doListMusic();
			}
		} else
		if (item.equals(getString(R.string.contacts))) {
//			List<KeyStrValueStr> permissions = new ArrayList<>();
//			permissions.add(new KeyStrValueStr(Manifest.permission.READ_CONTACTS, "Read Contacts"));
//			permissions.add(new KeyStrValueStr(Manifest.permission.WRITE_CONTACTS, "Write Contacts"));
//			CanAccess(permissions);
//			fillRight();
//			new FetchContactsTask().execute();
/ *			if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
					!= PackageManager.PERMISSION_GRANTED) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getString(R.string.no_perm));
				builder.setMessage(getString(R.string.read_contacts));
				builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
//					builder;
				});
				builder.setPositiveButton(R.string.req_perm, (dialog, which) -> {
					Log.d(TAG, "request permission (715)...");
				});
			}* /
			if (!Permissons.Check_READ_CONTACTS(this)) {
				Permissons.Request_READ_CONTACTS(this, MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE);
			} else {
				doListContacts();
			}
		} else
		if (item.equals(getString(R.string.files))) {
//			List<KeyStrValueStr> permissions = new ArrayList<>();
//			permissions.add(new KeyStrValueStr(Manifest.permission.READ_EXTERNAL_STORAGE, "Read External Storage"));
//			permissions.add(new KeyStrValueStr(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage"));
//			AccessContact(permissions);
			doListFiles();
		}
		*/
	}

	@Override
	public void OnClickItemItem(ListItem item) {
		switch (item.getID()) {
			case MY_PHOTOS_ID:
				if (!Permissons.Check_STORAGE(this)) {
					Permissons.Request_STORAGE(this, MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE);
				} else {
					doListPhotos();
				}
				break;
			case MY_MUSIC_ID:
				if (!Permissons.Check_STORAGE(this)) {
					Permissons.Request_STORAGE(this, MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE);
				} else {
					doListMusic();
				}
				break;
			case MY_CONTACTS_ID:
				if (!Permissons.Check_READ_CONTACTS(this)) {
					Permissons.Request_READ_CONTACTS(this, MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE);
				} else {
					doListContacts();
				}
				break;
			case MY_FILES_ID:
				break;
		}
	}

	@Override
	public boolean onItemLongClick(int position) {
		return false;
	}


	private void doListFiles() {
		FragmentManager manager = getFragmentManager();
		Fragment fragment;
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.addToBackStack(null);
		fragment = RightFragment.newInstance(getString(R.string.files), null, "");
		transaction.replace(R.id.fl_right_panel, fragment);
		transaction.commit();
	}


	private void doListMusic() {
		FragmentManager manager = getFragmentManager();
		Fragment fragment;
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.addToBackStack(null);
		fragment = RightFragment.newInstance(getString(R.string.music), null, "");
		transaction.replace(R.id.fl_right_panel, fragment);
		transaction.commit();
	}


	private void doListPhotos() {
		FragmentManager manager = getFragmentManager();
		Fragment fragment;
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.addToBackStack(null);
		fragment = RightFragment.newInstance(getString(R.string.photos), null, "");
		transaction.replace(R.id.fl_right_panel, fragment);
		transaction.commit();
	}


	private void doListContacts() {
		FragmentManager manager = getFragmentManager();
		Fragment fragment;
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.addToBackStack(null);
		fragment = RightFragment.newInstance(getString(R.string.contacts), null, "");
		transaction.replace(R.id.fl_right_panel, fragment);
		transaction.commit();
	}


	@NonNull
	@Override
	public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
		return new AsyncTaskLoader<String>(this) {
			@Override
			protected void onStartLoading() {
				super.onStartLoading();
//				if (args == null) {
//					return;
//				}
//				set loading indicator
			}

			@Nullable
			@Override
			public String loadInBackground() {
				if (args == null) {
					return null;
				}
				String string = args.getString("string");
				if (string == null || TextUtils.isEmpty(string)) {
					return null;
				}
				int which = args.getInt("which");
				String dialog = args.getString("dialog");
				try {
					URL url = new URL(string);
					return NetworkUtils.getResponseFromHttpUrl(url);
				}
				catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		};
	}

	@Override
	public void onLoadFinished(@NonNull Loader<String> loader, String data) {

	}

	@Override
	public void onLoaderReset(@NonNull Loader<String> loader) {

	}
}

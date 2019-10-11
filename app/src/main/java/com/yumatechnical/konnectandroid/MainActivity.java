package com.yumatechnical.konnectandroid;
//INUSE
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.ContactsContract;
import android.text.Html;
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
import com.dropbox.core.android.Auth;
import com.dropbox.core.v1.DbxEntry;
import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yumatechnical.konnectandroid.Adapter.CustomDialogListAdapter;
import com.yumatechnical.konnectandroid.Adapter.LeftArrayAdapter;
import com.yumatechnical.konnectandroid.Fragment.LeftItemFragment;
import com.yumatechnical.konnectandroid.Fragment.MyDialogFragment;
import com.yumatechnical.konnectandroid.Fragment.RightFragment;
import com.yumatechnical.konnectandroid.Helper.Dropbox.DropboxInstance;
import com.yumatechnical.konnectandroid.Helper.Dropbox.UploadTask;
import com.yumatechnical.konnectandroid.Helper.Network.LoaderFTP;
import com.yumatechnical.konnectandroid.Helper.Network.LocalNetwork;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.Helper.URI_to_Path;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;
import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.Settings.SettingsActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends AppCompatActivity
		implements /**/SharedPreferences.OnSharedPreferenceChangeListener,/**/
		/**/Preference.OnPreferenceChangeListener,/**/
		LeftArrayAdapter.OnListener,
		LeftItemFragment.OnLeftListFragmentInteractionListener,
		/*LoaderManager.LoaderCallbacks<String>,*/
		LocalNetwork.ConnectionInfoTask.OnNetworkConnectionInfo,
		MyDialogFragment.OnMyDialogInteraction {

	FrameLayout right, base;
	private static final int MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE = 88;
//	private static final int MY_PERMISSION_RECORD__REQUEST_CODE = 124;
	private static final int MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE = 4;
	private static final int MY_PERMISSION_RECORD_WRITE_CONTACTS_REQUEST_CODE = 5;
	private static final int MY_PERMISSION_RECORD_READ_EXTERNAL_STORAGE_REQUEST_CODE = 8;
	private static final int MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE = 7;
	private static final int MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE = 6;
	private static final int CONNECTION_TYPE_GDRIVE = 10;
	private static final int CONNECTION_TYPE_ONEDRIVE = 11;
	private static final int CONNECTION_TYPE_DROPBOX = 12;
	private static final int CONNECTION_TYPE_BOX = 13;
	private static final int CONNECTION_TYPE_FTP = 14;
	private static final int CONNECTION_TYPE_SMB = 15;
//	RecyclerView recyclerViewLeft;
	private static final String TAG = "KonnectAndroid";
	Boolean show_hidden, save_pass, rem_local, rem_remote, transfers_wifi_only;
//	private BottomSheetDialog mBottomSheetDialog;
	static final int PICK_CONTACT = 1;
//	private final static int IMG_SELECT_REQCODE = 4;
int minIconSize = 50;
	int maxIconSize = 500;
//	private static final int TEST_CONNECT_LOADER = 23;
	private static final int IMAGE_REQUEST_CODE = 13;
//	MySharedPreferencesActivity mySharedPreferencesActivity = new MySharedPreferencesActivity();
//	MyOptionsMenu myOptionsMenu = new MyOptionsMenu();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

/*		if (savedInstanceState != null) {
			((Vars)this.getApplication()).leftList =
					(ArrayList<ListItem>)savedInstanceState.getSerializable("leftList");
			((Vars)this.getApplication()).setConnectionItems(
					(ArrayList<ConnectionItem>)savedInstanceState.getSerializable("conns"));
		}*/
//		mySharedPreferencesActivity.SetupSharedPrefs();
		SetupSharedPrefs();

		new LocalNetwork.ConnectionInfoTask(getApplicationContext(), this).execute();

		setLeftList();
		right = findViewById(R.id.right_frame);
		base = findViewById(R.id.base_frame);
		// Get current account info
//		FullAccount account = DropboxInstance.main().users().getCurrentAccount();
//		System.out.println(account.getName().getDisplayName());
/*
		SMBoperation smb = new SMBoperation(new SMBoperation.OnSMBinteraction() {
			@Override
			public void OnResult(Session result) {
				if (result != null) {
					Log.d(TAG, "result=");
//					Log.d(TAG, "sessionID="+ session.getSessionId());
				}
			}
		});
		smb.connect("192.168.1.222", "yumausa", "yuma", "");
*/
	}

	private void setLeftList() {
		//setup left panel with list
//		left = findViewById(R.id.left_frame);
		FragmentManager manager = getFragmentManager();
		Fragment fragment;
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.addToBackStack(null);
		fragment = LeftItemFragment.newInstance();
		transaction.replace(R.id.left_frame, fragment);
		transaction.commit();
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

		if (!((Vars)this.getApplication()).isNetworkConnected()) {
			menuItem = menu.findItem(R.id.add_connect);
			menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_plus)
					.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.TOOLBAR_ICON_SIZE));
		}
		menuItem = menu.findItem(R.id.settings);
		menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_cog)
				.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		menuItem = menu.findItem(R.id.resize);
		menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_arrows_alt)
				.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		return true;
	}

/**/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		Intent intent;
		switch (item.getItemId()) {
			case R.id.add_connect:
				optionsMenuAddConnection();
				return true;
			case R.id.settings:
				optionsMenuSettings();
				return true;
			case R.id.resize:
//				MyOptionsMenu myOptionsMenu = new MyOptionsMenu();
//				myOptionsMenu.optionsMenuResize();
				optionsMenuResize();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
/**/
	private void optionsMenuResize() {
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
		resizeDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
				(dialog1, which) -> dialog1.cancel());
		resizeDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
				(dialog1, which) -> {
					slider.setProgress(initial);
					((Vars) getApplicationContext()).setIconSize(initial);
					dialog1.cancel();
				});
		resizeDialog.show();
	}
/**/
	private void optionsMenuSettings() {
	/*				URL url = null;
					try {
	//					url = new URL("content", "contacts","people");//MalformedURLException
						url = new URL("content:contacts/people");
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					if (url != null) {
						Log.d(TAG, "URL=" + url.toString());
					}*/
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
	}

	private void optionsMenuAddConnection() {
		ArrayList<ListItem> items = fillAddConnections();

		AlertDialog.Builder addConnectBuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

		final View customView = View.inflate(this, R.layout.dialog_list, null);
		customView.setPadding(Tools.dpToPx(8, this), Tools.dpToPx(24, this),
				Tools.dpToPx(8, this), Tools.dpToPx(24, this));
		addConnectBuilder.setView(customView);
//				addConnectBuilder.setNegativeButton(R.string.cancel, null);
//				builder.setCancelable(false);

		final AlertDialog addConnectionDialog = addConnectBuilder.create();
/*				androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
				AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance(
						R.style.CustomDialogTheme,
						Html.fromHtml("<font color='#111111'>"+ getString(R.string.addConnection)+ "</font>").toString()/ *,
						"",
						"",
						getString(R.string.cancel)* /
				);
//				alertDialogFragment.setTitle(Html.fromHtml("<font color='#111111'>"+ getString(R.string.addConnection)+ "</font>"));
/ *				alertDialogFragment.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
						new DialogListAdapter.OnClickListener() {
							@Override
							public void OnClickItem(String name) {
							}
//						}
//				(dialog1, which) -> {
//							return dialog1.cancel();
						});* /
				alertDialogFragment.show(fm, "alertDialogFragment");
/**/
		addConnectionDialog.setTitle(Html.fromHtml("<font color='#111111'>"+ getString(R.string.addConnection)+ "</font>"));
		addConnectionDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
				(dialog1, which) -> dialog1.cancel());
//				addConnectionDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), (DialogInterface.OnClickListener) null);
		addConnectionDialog.setCanceledOnTouchOutside(false);
		addConnectionDialog.show();
		/**/
		CustomDialogListAdapter adapter = new CustomDialogListAdapter(this,
				R.layout.dialog_list, items, new CustomDialogListAdapter.OnClickListener() {
			@Override
			public void OnSelecttem(ListItem item1) {
//						addConnectionDialog.dismiss();
				addConnectionDialog.cancel();
				String name = item1.getName();
				int id = ((Vars) MainActivity.this.getApplication()).getConnectionItems().size();
				item1.setID(((Vars) MainActivity.this.getApplication()).leftList.size() + 1);
				((Vars) MainActivity.this.getApplication()).leftList.add(item1);
				((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//						MainActivity.this.selectionAddConnection(name, id);
				MainActivity.this.selectionAddConnection(name, id);
			}
		});
		ListView listView = customView.findViewById(R.id.lv_dialog_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener((parent, view, position, id) -> addConnectionDialog.dismiss());
	}

	private void selectionAddConnection(String name, int id) {
		if (name.equals(MainActivity.this.getString(R.string.gdrive))) {
			Log.d(TAG, "adding a new GDrive connection:" + id);
			((Vars)getApplication()).addConnectionItem(new ConnectionItem(
					id,
					CONNECTION_TYPE_GDRIVE,
					"",
					"",
					"",
					"",
					"drive.google.com",
					80,
					"/"));
//							ShareCompat.IntentBuilder.from(this)
//									.setChooserTitle("title")
//									.setType("mimiType")
//									.setText("textToShare");
		} else if (name.equals(MainActivity.this.getString(R.string.onedrive))) {
//							Log.d(TAG, "adding a new Onedrive connection:" + id);
			((Vars)getApplication()).addConnectionItem(new ConnectionItem(
					id,
					CONNECTION_TYPE_ONEDRIVE,
					"",
					"",
					"",
					"",
					"onedrive.apple.com",
					80,
					"/"));
		} else if (name.equals(getString(R.string.dropbox))) {
//							DropboxInstance dropboxInstance = new DropboxInstance();
//							dropboxInstance.authorise(DropboxInstance.appInfo());
//							Auth.startOAuth2Authentication(getApplicationContext(), Vars.getDropboxKey());
//							^ works -requiress manifest info -switches view to black??
/**/
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
			}/**/
			((Vars)getApplication()).addConnectionItem(new ConnectionItem(
					id,
					CONNECTION_TYPE_DROPBOX,
					"",
					"",
					"",
					"",
					"onedrive.apple.com",
					80,
					"/"));
		} else if (name.equals(getString(R.string.box))) {
			Uri.Builder builder = new Uri.Builder();
			builder.scheme("smb").encodedAuthority("yumausa:yuma@192.168.1.222").path("/");
			Uri uri = builder.build();
			Log.d(TAG, "Uri="+ uri);
			/*
//							Intent intent = new Intent(Intent.ACTION_VIEW);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Log.d(TAG, getString(R.string.no_app_intent));
//								Toast.makeText(this, getResources().getString(R.string.no_app_intent), Toast.LENGTH_LONG).show();
//								AlertDialog.Builder builder1 = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
//								builder1.show();
			}
			*/
//							new NetworkUtils.AsyncGetNetworkResponce().execute();
			((Vars)getApplication()).addConnectionItem(new ConnectionItem(
					id,
					CONNECTION_TYPE_BOX,
					"",
					"",
					"",
					"",
					"onedrive.apple.com",
					80,
					"/"));
		} else if (name.equals(MainActivity.this.getString(R.string.ftp))) {
//							MainActivity.this.showFTPsettings("ftp", id);
			showFTPsettings(CONNECTION_TYPE_FTP);
		} else if (name.equals(MainActivity.this.getString(R.string.local_network))) {
//							MainActivity.this.showFTPsettings("smb", id);
			showFTPsettings(CONNECTION_TYPE_SMB);
		}
	}


	ArrayList<ListItem> fillAddConnections() {
//		Boolean fade_photos = false, fade_music = false, fade_contacts = false, fade_files = false;
//		fade_contacts = Permissons.Check_READ_CONTACTS(this);
		ArrayList<ListItem> items = new ArrayList<>();
		items.add(new ListItem(0, items.size(), getString(R.string.gdrive),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_google_drive)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				false));
		items.add(new ListItem(0, items.size(), getString(R.string.onedrive),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_cloud)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				false));
		items.add(new ListItem(0, items.size(), getString(R.string.dropbox),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_dropbox)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				false));
		items.add(new ListItem(0, items.size(), getString(R.string.box),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_box)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				false));
		items.add(new ListItem(0, items.size(), getString(R.string.ftp),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_server)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				false));
		items.add(new ListItem(0, items.size(), getString(R.string.local_network),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_network_wired)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				false));
		return items;
	}


	private void showFTPsettings(int type) {
		androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
		MyDialogFragment myDialogFragment = MyDialogFragment.newInstance(R.layout.ftp_settings_form,
				R.style.CustomDialogTheme, "Some Title", "",
				getString(R.string.testConnect), getString(android.R.string.ok), getString(android.R.string.cancel));
		myDialogFragment.show(fm, "fragment_edit_name");
/*
		AlertDialog.Builder showFTPbuilder;
		showFTPbuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
		final View formFTP = View.inflate(this, R.layout.ftp_settings_form, null);
		formFTP.setPadding(Tools.dpToPx(8, this), Tools.dpToPx(24, this),
				Tools.dpToPx(8, this), Tools.dpToPx(24, this));
		showFTPbuilder.setView(formFTP);
//				builder.setCancelable(false);
		final AlertDialog formDialog = showFTPbuilder.create();
//		AlertDialogFragment formDialog = showFTPbuilder.create();
		formDialog.setTitle(Html.fromHtml("<font color='#111111'>"+ getString(R.string.ftpConnect)+ "</font>"));
//	get view elements
		TextView title = formFTP.findViewById(R.id.tv_ftp_title);
		ImageView rename = formFTP.findViewById(R.id.iv_ftp_rename);
		TextView hlabel = formFTP.findViewById(R.id.tv_ftp_host);
		TextView ulabel = formFTP.findViewById(R.id.tv_ftp_username);
		TextView plabel = formFTP.findViewById(R.id.tv_ftp_password);
		TextView tlabel = formFTP.findViewById(R.id.tv_ftp_port);
		TextView iplabel = formFTP.findViewById(R.id.tv_ftp_init_path);
		EditText ipath = formFTP.findViewById(R.id.et_ftp_init_path);
		EditText hname = formFTP.findViewById(R.id.et_ftp_host);
		EditText uname = formFTP.findViewById(R.id.et_ftp_username);
		EditText pword = formFTP.findViewById(R.id.et_ftp_password);
		EditText portn = formFTP.findViewById(R.id.et_ftp_port);
		EditText etitle = formFTP.findViewById(R.id.et_ftp_rename);
		ImageView spinner = formFTP.findViewById(R.id.iv_ftp_progress);
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
		title.setText(Tools.toTitleCase(getString(R.string.eg_ip_addr)));
		hlabel.setText(Tools.toTitleCase(getString(R.string.server)));
		ulabel.setText(Tools.toTitleCase(getString(R.string.username)));
		plabel.setText(Tools.toTitleCase(getString(R.string.password)));
		tlabel.setText(Tools.toTitleCase(getString(R.string.port)));
		iplabel.setText(Tools.toTitleCase(getString(R.string.init_path)));
		spinner.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_spinner)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE));
//  button actions
		formDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.testConnect), null);
		formDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(android.R.string.ok), null);
		formDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel), null);
//		formDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
//				(dialog1, which) -> dialog1.cancel());
//		formDialog.setCanceledOnTouchOutside(false);
		formDialog.show();*/
	}


	@Override
	public void neutralButtonPressed() {
		Log.d(TAG, "neutralButtonPressed");
		/*
//						LinearLayout layout = formFTP.findViewById(R.id.ll_ftp_main);
//						layout.setBackgroundColor(Color.GRAY);
		spinner.setVisibility(View.VISIBLE);
		Log.d(TAG, "why this dialog closes?");
//					formDialog.getButton(which).setText(FontAwesome.Icon.faw_spinner);
//						formDialog.getButton(which).setText("...");
		String string = method + "://" + uname.getText() + ":" + pword.getText() + "@" +
				hname.getText() + ":" + portn.getText() + "/";
		AlertDialog.Builder testBuilder = new AlertDialog.Builder(MainActivity.this);
		testBuilder.setTitle("testing...");
		testBuilder.setMessage(string);
		testBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		testBuilder.show();
		Log.d(TAG, "button TEST string=" + string);
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

	@Override
	public void negativeButtonPressed() {
		Log.d(TAG, "negativeButtonPressed");
	}

	@Override
	public void positiveButtonPressed() {
		Log.d(TAG, "positiveButtonPressed");
		/*
		String string = "";
		if (type == CONNECTION_TYPE_SMB) {
			string += "smb:";
		} else {
			string += "ftp:";
		}
		string += "//"+ uname.getText()+ ":"+ pword.getText()+ "@"+
				hname.getText()+ ":"+ portn.getText()+ "/";
//					Uri uri = Uri.parse(string);
//					String name = etitle.getText().toString().isEmpty() ? title.getText().toString() : etitle.getText().toString();
		Log.d(TAG, "button OK string="+ string);
//					((Vars)this.getApplication()).addConnectionItem(new ConnectionItem(id, string));
		int connectId = ((Vars)getApplication()).getConnectionItems().size();
		((Vars)this.getApplication()).addConnectionItem(new ConnectionItem(
				connectId,
				type,
				"",
				(type == CONNECTION_TYPE_SMB) ? "smb:" : "ftp:",
				uname.getText().toString(),
				pword.getText().toString(),
				hname.getText().toString(),
				Integer.parseInt(portn.getText().toString()),
				ipath.getText().toString()));
//					leftList.add(new ListItem(0, id, name, "",null,
//							leftListDefaultLeftPadding, leftListDefaultTopPadding, leftListDefaultBottomPadding,
//							leftListDefaultIconBeforeText, leftListDefaultBetweenPadding, false));
		dialog1.cancel();*/
	}

	@Override
	public void putInOnViewCreated(View view, Bundle bundle) {
/*		showFTPsettingsActivity ftPsettingsActivity = new showFTPsettingsActivity();*/

/*		ftPsettingsActivity.connectViewElements(view);*/
/**/
		TextView title = view.findViewById(R.id.tv_ftp_title);
		ImageView rename = view.findViewById(R.id.iv_ftp_rename);
		TextView hlabel = view.findViewById(R.id.tv_ftp_host);
		TextView ulabel = view.findViewById(R.id.tv_ftp_username);
		TextView plabel = view.findViewById(R.id.tv_ftp_password);
		TextView tlabel = view.findViewById(R.id.tv_ftp_port);
		TextView iplabel = view.findViewById(R.id.tv_ftp_init_path);
		EditText ipath = view.findViewById(R.id.et_ftp_init_path);
		EditText hname = view.findViewById(R.id.et_ftp_host);
		EditText uname = view.findViewById(R.id.et_ftp_username);
		EditText pword = view.findViewById(R.id.et_ftp_password);
		EditText portn = view.findViewById(R.id.et_ftp_port);
		EditText etitle = view.findViewById(R.id.et_ftp_rename);
		ImageView spinner = view.findViewById(R.id.iv_ftp_progress);
/**/
/*		ftPsettingsActivity.setLabels();*/
/**/
		title.setText(Tools.toTitleCase(getString(R.string.eg_ip_addr)));
		hlabel.setText(Tools.toTitleCase(getString(R.string.server)));
		ulabel.setText(Tools.toTitleCase(getString(R.string.username)));
		plabel.setText(Tools.toTitleCase(getString(R.string.password)));
		tlabel.setText(Tools.toTitleCase(getString(R.string.port)));
		iplabel.setText(Tools.toTitleCase(getString(R.string.init_path)));
		spinner.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_spinner)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE));
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
/**/
//		int port;
//		try {
//			port = Integer.getInteger(portn.toString());
//		}
		String connStr = String.format(Locale.CANADA, "%s://%s:%s@%s:%d/%s",
				"", uname, pword, hname, Integer.parseInt(portn.getText().toString()), ipath);
	}

	@Override
	public void onBeforeCreate(AlertDialog.Builder alertDialogBuilder) {
		alertDialogBuilder.setTitle(Html.fromHtml("<font color='#111111'>"+ getString(R.string.ftpConnect)+ "</font>"));
	}

/**/
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
		transfers_wifi_only = sharedPreferences.getBoolean(getString(R.string.PREFS_network_transfers),
				getResources().getBoolean(R.bool.PREFS_network_transfers_default));
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
		} else
		if (key.equals(getString(R.string.PREFS_network_transfers))) {
			rem_remote = sharedPreferences.getBoolean(getString(R.string.PREFS_network_transfers),
					getResources().getBoolean(R.bool.PREFS_network_transfers_default));
		}
//		SetupSharedPrefs();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		return false;
	}
/**/

	@Override
	public void onSaveInstanceState (Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putSerializable("leftList", ((Vars)this.getApplication()).leftList);
		outState.putSerializable("conns", ((Vars)this.getApplication()).getConnectionItems());
	}

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
//		setLeftList();
/*
		AlertDialog formDialog = (AlertDialog)getDialog();
		if () {
			formDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.testConnect),
					(dialog1, which) -> {
//						LinearLayout layout = formFTP.findViewById(R.id.ll_ftp_main);
//						layout.setBackgroundColor(Color.GRAY);
						spinner.setVisibility(View.VISIBLE);
						Log.d(TAG, "why this dialog closes?");
//					formDialog.getButton(which).setText(FontAwesome.Icon.faw_spinner);
//						formDialog.getButton(which).setText("...");
/ *						String string = method + "://" + uname.getText() + ":" + pword.getText() + "@" +
							hname.getText() + ":" + portn.getText() + "/";* /
/ *						AlertDialog.Builder testBuilder = new AlertDialog.Builder(MainActivity.this);
					testBuilder.setTitle("testing...");
					testBuilder.setMessage(string);
					testBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					testBuilder.show();* /
/ *						Log.d(TAG, "button TEST string=" + string);
					Bundle bundle = new Bundle();
					bundle.putString("string", string);
					bundle.putInt("which", which);
					bundle.putString("dialog", dialog1.toString());
					Uri uri = Uri.parse(string);* /
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
					});
			formDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
					(dialog1, which) -> {
						String string = "";
						if (type == CONNECTION_TYPE_SMB) {
							string += "smb:";
						} else {
							string += "ftp:";
						}
						string += "//"+ uname.getText()+ ":"+ pword.getText()+ "@"+
								hname.getText()+ ":"+ portn.getText()+ "/";
//					Uri uri = Uri.parse(string);
//					String name = etitle.getText().toString().isEmpty() ? title.getText().toString() : etitle.getText().toString();
						Log.d(TAG, "button OK string="+ string);
//					((Vars)this.getApplication()).addConnectionItem(new ConnectionItem(id, string));
						int connectId = ((Vars)getApplication()).getConnectionItems().size();
						((Vars)this.getApplication()).addConnectionItem(new ConnectionItem(
								connectId,
								type,
								"",
								(type == CONNECTION_TYPE_SMB) ? "smb:" : "ftp:",
								uname.getText().toString(),
								pword.getText().toString(),
								hname.getText().toString(),
								Integer.parseInt(portn.getText().toString()),
								ipath.getText().toString()));
//					leftList.add(new ListItem(0, id, name, "",null,
//							leftListDefaultLeftPadding, leftListDefaultTopPadding, leftListDefaultBottomPadding,
//							leftListDefaultIconBeforeText, leftListDefaultBetweenPadding, false));
						dialog1.cancel();
					});
		}*/
	}

	@Override
	protected void onDestroy() {
		PreferenceManager.getDefaultSharedPreferences(this)
				.unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
		((Vars)getApplication()).leftList.clear();
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


	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);
		if (resultCode != RESULT_OK || data == null) {
			return;
		}
		switch (reqCode) {
			case (PICK_CONTACT):
				Uri contactData = data.getData();
				try (Cursor c = managedQuery(contactData, null, null, null, null)) {
					if (c.moveToFirst()) {
						String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
						String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
						try {
							if (hasPhone.equalsIgnoreCase("1")) {
								Cursor phones = getContentResolver().query(
										ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
										null, null);
								Objects.requireNonNull(phones).moveToFirst();
								String cNumber = phones.getString(phones.getColumnIndex("data1"));
								System.out.println("number is:" + cNumber);
	//								txtphno.setText("Phone Number is: "+cNumber);
							}
	//						String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
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
				DbxEntry.File file = new DbxEntry.File(Objects.requireNonNull(URI_to_Path.getPath(getApplication(), data.getData())),
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


	/**
	 * Draw a list of files on right panel
	 */
	void doListFiles() {
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

/*
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
//				int which = args.getInt("which");
//				String dialog = args.getString("dialog");
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
*/

	@Override
	public void onListFragmentInteraction(ListItem item) {
		Log.d(TAG, "left interaction with "+ item.toString());
	}


	@Override
	public void ContextMenuLeftItemId(int id) {
	}


	@Override
	public void SelectedLeftItemId(int id) {
		Log.d(TAG, "selected left item with id:"+ id);
		switch (id) {
			case Vars.MY_PHOTOS_ID:
				if (!Permissons.Check_STORAGE(this)) {
					Permissons.Request_STORAGE(this, MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE);
				} else {
					doListPhotos();
				}
				break;
			case Vars.MY_MUSIC_ID:
				if (!Permissons.Check_STORAGE(this)) {
					Permissons.Request_STORAGE(this, MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE);
				} else {
					doListMusic();
				}
				break;
			case Vars.MY_CONTACTS_ID:
				if (!Permissons.Check_READ_CONTACTS(this)) {
					Permissons.Request_READ_CONTACTS(this, MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE);
				} else {
					doListContacts();
				}
				break;
			case Vars.MY_FILES_ID:
				doListFiles();
				break;
			default:
//				if ();
/**/
//				FTPoperation ftPoperation = new FTPoperation(result -> {
				LoaderFTP ftPoperation = new LoaderFTP(result -> {
					if (result != null) {
						Log.d(TAG, "result=" + result.getPassiveHost());
						try {
							Log.d(TAG, "result=" + result.getStatus() + ":" + result.toString());
						} catch (IOException e) {
//				                e.printStackTrace();
							Log.d(TAG, "connection error: " + e.getLocalizedMessage());
						}
					}
				});
//		ftPoperation.connectFTP(this, "192.168.1.222", "yumausa", "yuma", "/");
//		ftPoperation.connectFTP(this, "ftp://xo3.x10hosting.com/", "yumax10h", "suguna24", "");
		ftPoperation.connectFTP(this, "https://google.com/", "", "", "");
/**/
				ConnectionItem item = ((Vars)this.getApplication()).getConnectionItemByID(id);
				if (item != null) {
					if (item.getAccessToken() != null && !item.getAccessToken().equals("")) {
						String string = "";
						if (item.getType() > 0) {
							switch (item.getType()) {
								case 3:
									string = string + "smb://";
									break;
								case 4:
									break;
							}
						}
						if (item.getUsername() != null) {
							string += item.getUsername()+ ":";
						}
						if (item.getUsername() != null) {
							string += item.getPassword()+ "@";
						}
						if (item.getHost() != null && !item.getHost().equals("")) {
							string += item.getHost();
						}
						if (item.getPort() > 0) {
							string += ":"+ item.getPort();
						}
						if (item.getPath() != null) {
							string += item.getPort();
						}
						if (string.equals("")) {
							Log.d(TAG, "no authorisation for this connection");
							break;
						}
						Log.d(TAG, "connection string = "+ string+ ".");
						break;
					} else {
						Log.d(TAG, "connection token = "+ item.getAccessToken()+ ".");
					}
				}
		}
	}


	@Override
	public void WifiConnected(boolean isConnection) {
//		if (isConnection) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(getString(R.string.warning));
//			builder.setMessage(getString(R.string.mobile_network));
//			builder.show();
//		}
	}

	@Override
	public void MobileConnected(boolean isConnection) {
		if (isConnection) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
			builder.setTitle(getString(R.string.warning));
			builder.setMessage(getString(R.string.mobile_network));
			builder.setCancelable(false);
			builder.setCursor(null, null, "");
			builder.setNegativeButton(R.string.ignore, (dialog, which) -> dialog.dismiss());
			builder.setPositiveButton(R.string.close, (dialog, which) -> {
				moveTaskToBack(true);
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
					finishAndRemoveTask();
				} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					this.finishAffinity();
				} else {
					Process.killProcess(Process.myPid());
					System.exit(1);
				}
			});
			builder.show();
		}
	}

	@Override
	public void NetworkConnected(boolean isConnected) {
		if (!isConnected) {
			((Vars)this.getApplication()).setNetworkConnected(false);
			AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
			builder.setTitle(getString(R.string.warning));
			builder.setMessage(getString(R.string.no_network));
			builder.setCancelable(false);
			builder.setCursor(null, null, "");
			builder.setNegativeButton(R.string.ignore, (dialog, which) -> dialog.dismiss());
			builder.setPositiveButton(R.string.close, (dialog, which) -> {
				moveTaskToBack(true);
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
					finishAndRemoveTask();
				} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					this.finishAffinity();
				} else {
					Process.killProcess(Process.myPid());
					System.exit(1);
				}
			});
			builder.show();
		}
	}


}

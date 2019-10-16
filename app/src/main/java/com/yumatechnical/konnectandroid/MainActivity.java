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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v1.DbxEntry;
import com.hierynomus.smbj.session.Session;
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
import com.yumatechnical.konnectandroid.Helper.Network.FTPoperation;
import com.yumatechnical.konnectandroid.Helper.Network.LoaderFTP;
//import com.yumatechnical.konnectandroid.Helper.Network.LoaderSMB;
import com.yumatechnical.konnectandroid.Helper.Network.LocalNetwork;
import com.yumatechnical.konnectandroid.Helper.Network.SMBoperation;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.Helper.URI_to_Path;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;
import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.Settings.SettingsActivity;

import java.io.IOException;
import java.util.ArrayList;
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
//	private static final int CONNECTION_ID = 108;
//	private static final int CONNECTION_TYPE = 109;
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
//	private AlertDialog alertDialog;
	private View alertViewFTP;


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
/*		CustomDialogUI customDialogUI = new CustomDialogUI();
		customDialogUI.dialog(this, "Title",
				"Message1 message2 message3 message4 message5 message6 message7 message8 message9",
				"", "", null, new CustomDialogUI.OnDialogInteraction() {
					@Override
					public void PressedNeutralButton() {
					}

					@Override
					public void PressedNegativeButton() {
					}

					@Override
					public void PressedPositiveButton() {
					}
				});*/
//				new LocalNetwork.ConnectionInfoTask(getApplicationContext(), this));
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
		menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_sliders_h)
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

		final AlertDialog addConnectionDialog = addConnectBuilder.create();
		addConnectionDialog.setTitle(Html.fromHtml("<font color='#111111'>"+ getString(R.string.addConnection)+ "</font>"));
		addConnectionDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
				(dialog1, which) -> dialog1.cancel());
		addConnectionDialog.setCanceledOnTouchOutside(false);
		addConnectionDialog.show();
		CustomDialogListAdapter adapter = new CustomDialogListAdapter(this,
				R.layout.dialog_list, items, item1 -> {
					addConnectionDialog.cancel();
					String name = item1.getName();
					int id = ((Vars) MainActivity.this.getApplication()).getConnectionItems().size();
					item1.setID(((Vars) MainActivity.this.getApplication()).leftList.size() + 1);
					((Vars) MainActivity.this.getApplication()).leftList.add(item1);
					((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
					MainActivity.this.selectionAddConnection(name, item1.getID());
				});
		ListView listView = customView.findViewById(R.id.lv_dialog_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener((parent, view, position, id) -> addConnectionDialog.dismiss());
	}

	private void selectionAddConnection(String name, int id) {
		if (name.equals(MainActivity.this.getString(R.string.gdrive))) {
			ConnectionItem item = new ConnectionItem(
					id,
					CONNECTION_TYPE_GDRIVE,
					"",
					"",
					"",
					"",
					"",
					"drive.google.com",
					80,
					"/");
//							ShareCompat.IntentBuilder.from(this)
//									.setChooserTitle("title")
//									.setType("mimiType")
//									.setText("textToShare");
			((Vars)getApplication()).addConnectionItem(item);
		} else if (name.equals(MainActivity.this.getString(R.string.onedrive))) {
//							Log.d(TAG, "adding a new Onedrive connection:" + id);
			ConnectionItem item = new ConnectionItem(
					id,
					CONNECTION_TYPE_ONEDRIVE,
					"",
					"",
					"",
					"",
					"",
					"onedrive.apple.com",
					80,
					"/");
			((Vars)getApplication()).addConnectionItem(item);
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
			ConnectionItem item = new ConnectionItem(
					id,
					CONNECTION_TYPE_DROPBOX,
					"",
					"",
					"",
					"",
					"",
					"dropbox.com",
					80,
					"/");
			((Vars)getApplication()).addConnectionItem(item);
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
			ConnectionItem item = new ConnectionItem(
					id,
					CONNECTION_TYPE_BOX,
					"",
					"",
					"",
					"",
					"",
					"box.microsoft.com",
					80,
					"/");
			((Vars)getApplication()).addConnectionItem(item);
		} else if (name.equals(MainActivity.this.getString(R.string.ftp))) {
//							MainActivity.this.showFTPsettings("ftp", id);
/*			ConnectionItem item = new ConnectionItem(
					id,
					CONNECTION_TYPE_FTP,
					"",
					"",
					"",
					"",
					"",
					"",
					21,
					"/");
			((Vars)getApplication()).addConnectionItem(item);*/
//			showFTPsettings(CONNECTION_TYPE_FTP, item);
//			showFTPsettings(item.getID());
			showFTPsettings(-1, CONNECTION_TYPE_FTP);
		} else if (name.equals(MainActivity.this.getString(R.string.local_network))) {
//							MainActivity.this.showFTPsettings("smb", id);
/*			ConnectionItem item = new ConnectionItem(
					id,
					CONNECTION_TYPE_SMB,
					"",
					"",
					"",
					"",
					"",
					"",
					0,
					"/");
			((Vars)getApplication()).addConnectionItem(item);*/
//			showFTPsettings(CONNECTION_TYPE_SMB, item);
//			showFTPsettings(item.getID());
			showFTPsettings(-1, CONNECTION_TYPE_SMB);
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
//		items.add(new ListItem(0, items.size(), getString(R.string.onedrive),null,
//				new IconicsDrawable(this, FontAwesome.Icon.faw_cloud)
//						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
//				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
//				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
//				false));
		Drawable oneDrive_d = getResources().getDrawable(R.drawable.ms_onedrive);
		Bitmap oneDrive_b = ((BitmapDrawable)oneDrive_d).getBitmap();
		Drawable oneDrive = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(oneDrive_b, 64, 64, true));
		items.add(new ListItem(0, items.size(), getString(R.string.onedrive),null,
				oneDrive, Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				false));
		items.add(new ListItem(0, items.size(), getString(R.string.dropbox),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_dropbox)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				false));
//		items.add(new ListItem(0, items.size(), getString(R.string.box),null,
//				new IconicsDrawable(this, FontAwesome.Icon.faw_box)
//						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
//				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
//				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
//				false));
		Drawable box_d = getResources().getDrawable(R.drawable.box_icon);
		Bitmap box_b = ((BitmapDrawable)box_d).getBitmap();
		Drawable box = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(box_b, 64, 64, true));
		items.add(new ListItem(0, items.size(), getString(R.string.box),null,
				box, Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				false));
//		items.add(new ListItem(0, items.size(), getString(R.string.ftp),null,
//				new IconicsDrawable(this, FontAwesome.Icon.faw_server)
//						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
//				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
//				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
//				false));
		Drawable ftp_d = getResources().getDrawable(R.drawable.ftp_connection);
		Bitmap ftp_b = ((BitmapDrawable)ftp_d).getBitmap();
		Drawable ftp = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(ftp_b, 64, 64, true));
		items.add(new ListItem(0, items.size(), getString(R.string.ftp),null,
				ftp, Tools.dpToPx(16, this), Tools.dpToPx(16, this),
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


	private void showFTPsettings(int id, int myType) {
		ConnectionItem item = new ConnectionItem();
		int type = myType;
		if (id > -1) {
			item = ((Vars) this.getApplication()).getConnectionItemByID(id);
			type = item.getType();
			Log.d(TAG, "showFTPsettings opening with item:"+ item.toString());
		}
//		Log.d(TAG, "showFTPsettings opening with item:"+ item.toString()+ ", type="+ type);
//		ArrayList<String> methods = new ArrayList<>();
//		String scheme = "";
		androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
		MyDialogFragment myDialogFragment = MyDialogFragment.newInstance();
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
//		bundle.putInt("width", 123);
//		bundle.putInt("height", 234);
//		bundle.putFloat("percent", 34.5f);
		bundle.putInt("style", R.style.CustomDialogTheme);
//		View view = View.inflate(this, R.layout.ftp_settings_form, null);
//		bundle.putInt("layout", R.layout.ftp_settings_form);
		bundle.putBoolean("cancelable", false);
		if (type == CONNECTION_TYPE_SMB) {
			bundle.putString("title", getString(R.string.local_network));
		} else {
			bundle.putString("title", getString(R.string.ftpConnect));
		}
//		bundle.putString("message", "My Message");
		bundle.putString("neutralButtonLabel", getString(R.string.testConnect));
		bundle.putString("positiveButtonLabel", getString(android.R.string.ok));
		bundle.putString("negativeButtonLabel", getString(android.R.string.cancel));

		alertViewFTP = View.inflate(this, R.layout.ftp_settings_form, null);
		if (alertViewFTP != null) {
			TextView title = alertViewFTP.findViewById(R.id.tv_ftp_title);
			TextView hlabel = alertViewFTP.findViewById(R.id.tv_ftp_host);
			TextView ulabel = alertViewFTP.findViewById(R.id.tv_ftp_username);
			TextView plabel = alertViewFTP.findViewById(R.id.tv_ftp_password);
			TextView tlabel = alertViewFTP.findViewById(R.id.tv_ftp_port);
			TextView iplabel = alertViewFTP.findViewById(R.id.tv_ftp_init_path);
			EditText ipath = alertViewFTP.findViewById(R.id.et_ftp_init_path);
			EditText hname = alertViewFTP.findViewById(R.id.et_ftp_host);
			EditText uname = alertViewFTP.findViewById(R.id.et_ftp_username);
			EditText pword = alertViewFTP.findViewById(R.id.et_ftp_password);
			EditText portn = alertViewFTP.findViewById(R.id.et_ftp_port);
			EditText etitle = alertViewFTP.findViewById(R.id.et_ftp_rename);
			ImageView spinner = alertViewFTP.findViewById(R.id.iv_ftp_progress);
			RadioGroup radios = alertViewFTP.findViewById(R.id.rg_ftp_auth);
			TextView domlabel = alertViewFTP.findViewById(R.id.tv_ftp_domain);
			EditText domain = alertViewFTP.findViewById(R.id.et_ftp_domain);
			View method = alertViewFTP.findViewById(R.id.connType);

			method.setTag(type);
			title.setText(Tools.toTitleCase(getString(R.string.title)));
			hlabel.setText(Tools.toTitleCase(getString(R.string.server)));
			domlabel.setText(Tools.toTitleCase(getString(R.string.domain)));
			ulabel.setText(Tools.toTitleCase(getString(R.string.username)));
			plabel.setText(Tools.toTitleCase(getString(R.string.password)));
			tlabel.setText(Tools.toTitleCase(getString(R.string.port)));
			iplabel.setText(Tools.toTitleCase(getString(R.string.init_path)));
			spinner.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_spinner)
					.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE));
			if (type == CONNECTION_TYPE_SMB) {
				radios.setVisibility(View.GONE);
				tlabel.setVisibility(View.GONE);
				portn.setVisibility(View.GONE);
				tlabel.setVisibility(View.GONE);
				domlabel.setVisibility(View.VISIBLE);
				domain.setVisibility(View.VISIBLE);
			} else {
				RadioButton authFTP = alertViewFTP.findViewById(R.id.rb_ftp_plain);
				RadioButton authFTPS = alertViewFTP.findViewById(R.id.rb_ftps);
				RadioButton authSFTP = alertViewFTP.findViewById(R.id.rb_sftp);
				authSFTP.setOnClickListener(v -> {
					portn.setHint("22");
				});
				authFTPS.setOnClickListener(v -> {
					portn.setHint("21");
				});
				authFTP.setOnClickListener(v -> {
					portn.setHint("21");
				});
			}
			if (item != null) {
				alertViewFTP.setTag(item.getID());
				if (item.getConnectionName() != null && !item.getConnectionName().equals("")) {
//					title.setText(item.getConnectionName());
					etitle.setText(item.getConnectionName());
				} else {
					etitle.setHint(Tools.toTitleCase(getString(R.string.eg_conn)));
				}
//				Log.d(TAG, "showFTPsettings item is "+ item.toString());////////////error below
				if (item.getPort() > 0) {// && portn != null) {
					try {
						portn.setText(String.valueOf(item.getPort()));
					} catch (NumberFormatException e) {
					}
				} else {
					portn.setText("21");
				}
				if (item.getPath() != null && !item.getPath().equals("")) {
					ipath.setText(item.getPath());
				} else {
					ipath.setHint("/");
				}
				if (item.getHost() != null && !item.getHost().equals("")) {
					hname.setText(item.getHost());
				}
				if (item.getUsername() != null && !item.getUsername().equals("")) {
					uname.setText(item.getUsername());
				}
				if (item.getPassword() != null && !item.getPassword().equals("")) {
					pword.setText(item.getPassword());
				}
				if (item.getAccessToken() != null && !item.getAccessToken().equals("")) {
					domain.setText(item.getAccessToken());
				}
			} else {
				alertViewFTP.setTag(0);
				etitle.setHint(Tools.toTitleCase(getString(R.string.eg_conn)));
				portn.setHint("21");
				ipath.setHint("/");
			}
			myDialogFragment.setArguments(bundle);
			myDialogFragment.show(fm, "fragment_edit_ftp");
		}
	}


	//from MyDialogFragment
	@Override
	public void AlertDialogNeutralButtonPressed(Button button, AlertDialog dialog, int id) {
		ConnectionItem item = new ConnectionItem();
		View view = (View) button.getParent().getParent().getParent();
		EditText ipath = view.findViewById(R.id.et_ftp_init_path);
		EditText hname = view.findViewById(R.id.et_ftp_host);
		EditText uname = view.findViewById(R.id.et_ftp_username);
		EditText pword = view.findViewById(R.id.et_ftp_password);
		EditText portn = view.findViewById(R.id.et_ftp_port);
		if (id > 0) {
			item = ((Vars) this.getApplication()).getConnectionItemByID(id);
			View method = view.findViewById(R.id.connType);
//			int type = (int)method.getTag();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			button.setBackgroundDrawable(
					new IconicsDrawable(this, FontAwesome.Icon.faw_spinner)
							.color(IconicsColor.colorRes(R.color.Teal))
							.size(IconicsSize.TOOLBAR_ICON_SIZE));
		}
//		RadioButton authFTP = alertViewFTP.findViewById(R.id.rb_ftp_plain);
		RadioButton authFTPS = alertViewFTP.findViewById(R.id.rb_ftps);
		RadioButton authSFTP = alertViewFTP.findViewById(R.id.rb_sftp);
//		TextView domlabel = alertViewFTP.findViewById(R.id.tv_ftp_domain);
		EditText domain = alertViewFTP.findViewById(R.id.et_ftp_domain);
		String connStr = "";
		connStr = (item.getType() == CONNECTION_TYPE_SMB)
				? "smb://"
				: (authSFTP.isChecked())
					? "sftp://"
					: (authFTPS.isChecked())
						? "ftps://"
						: "ftp://";
		connStr += hname.getText();
		int port;
		try {
			port = Integer.parseInt(portn.getText().toString());
			connStr += ":" + port;
		} catch (NumberFormatException e) {
		}
		if (!ipath.getText().toString().startsWith("/")) {
			connStr += "/";
		}
		connStr += ipath.getText();

		Log.d(TAG, "testing connection:"+ connStr);
		if (item.getType() == CONNECTION_TYPE_SMB) {
//			LoaderSMB client = new LoaderSMB(this, result -> {
//				Log.d(TAG, "tested connection:");
//				Log.d(TAG, "response=" + result.getSessionId()+ ":" + result.getConnection().toString());
//				dialog.dismiss();
//			});
//			client.connect(connStr, uname.getText().toString(), pword.getText().toString(), .getText().toString());
			final String connString = connStr;
			SMBoperation client = new SMBoperation(new SMBoperation.OnSMBinteraction() {
				@Override
				public void OnResult(Session result) {
					Log.d(TAG, "tested smb connection:"+ connString);
					Log.d(TAG, "smb response=" + result.getSessionId()+ ":" + result.getConnection().toString());
				}
			});
			client.connect(connStr, uname.getText().toString(), pword.getText().toString(), domain.getText().toString());
		} else {
//			LoaderFTP ftp = new LoaderFTP(result -> {
//				Log.d(TAG, "tested connection:");
//				try {
//					Log.d(TAG, "response=" + result.getStatus() + ":" + result.getReplyString());
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				dialog.dismiss();
//			});
//			ftp.connectFTP(this, connStr, uname.getText().toString(), pword.getText().toString(),
//					ipath.getText().toString());
			FTPoperation client = new FTPoperation(result -> {
				Log.d(TAG, "tested ftp connection:");
				try {
					Log.d(TAG, "ftp response=" + result.getStatus()+ ":" + result.getReplyString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			client.connectFTP(connStr, uname.getText().toString(), pword.getText().toString(), ipath.getText().toString());
		}
/*
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
	public void AlertDialogNegativeButtonPressed(Button button, AlertDialog dialog, int id) {
		dialog.dismiss();
	}

	@Override
	public void AlertDialogPositiveButtonPressed(Button button, AlertDialog dialog, int id) {
		ConnectionItem item = new ConnectionItem();
		if (id > 0) {
			item = ((Vars) this.getApplication()).getConnectionItemByID(id);
		} else {
			item.setID(((Vars)this.getApplication()).getConnectionItems().size());
		}
		View view = (View) button.getParent().getParent().getParent();
		EditText ipath = view.findViewById(R.id.et_ftp_init_path);
		EditText hname = view.findViewById(R.id.et_ftp_host);
		EditText uname = view.findViewById(R.id.et_ftp_username);
		EditText pword = view.findViewById(R.id.et_ftp_password);
		EditText portn = view.findViewById(R.id.et_ftp_port);
		EditText etitle = view.findViewById(R.id.et_ftp_rename);
		ImageView spinner = view.findViewById(R.id.iv_ftp_progress);
		RadioButton authFTPS = alertViewFTP.findViewById(R.id.rb_ftps);
		RadioButton authSFTP = alertViewFTP.findViewById(R.id.rb_sftp);
		EditText domain = alertViewFTP.findViewById(R.id.et_ftp_domain);
		View method = alertViewFTP.findViewById(R.id.connType);
		spinner.setVisibility(View.VISIBLE);
		int port = 21;
		try {
			port = Integer.parseInt(portn.getText().toString());
			if (port < 1) {
				port = 21;
			}
			item.setPort(port);
		} catch (NumberFormatException e) {
		}
		if (item.getType() < 1) {
			item.setType((int)method.getTag());
		}
		if (etitle.getText() != null) {
			item.setConnectionName(etitle.getText().toString());
		}
		if (hname.getText() != null) {
			item.setHost(hname.getText().toString());
		}
		if (uname.getText() != null) {
			item.setUsername(uname.getText().toString());
		}
		if (pword.getText() != null) {
			item.setPassword(pword.getText().toString());
		}
		if (ipath.getText() != null) {
			item.setPath(ipath.getText().toString());
		} else {
			item.setPath(ipath.getHint().toString());
		}
		if (item.getType() > 0) {
			String scheme = (item.getType() == CONNECTION_TYPE_SMB) ? "smb:"
					: (authSFTP.isChecked()) ? "sftp:"
						: (authFTPS.isChecked()) ? "ftps:"
							: "ftp:";
			item.setScheme(scheme);
		}
		if (domain.getText() != null) {
			item.setAccessToken(domain.getText().toString());
		}
		((Vars)this.getApplication()).leftList.get(item.getID()+4).setName(item.getConnectionName());
		Log.d(TAG, "saving connection: "+ item.toString());
		((Vars)this.getApplication()).addConnectionItem(item);
		spinner.setVisibility(View.INVISIBLE);
		dialog.dismiss();
	}

	@Override
	public void AlertDialogPutInOnViewCreated(View view, Bundle bundle, int id) {
	}

	@Override
	public void AlertDialogOnBeforeCreate(AlertDialog.Builder alertDialogBuilder) {
		if (alertViewFTP != null) {
			alertDialogBuilder.setView(alertViewFTP);
//			Log.d("onBeforeCreate", "alertViewFTP="+ alertViewFTP.toString());
		}
//		alertDialogBuilder.setTitle(Html.fromHtml("<font color='#111111'>"+ getString(R.string.ftpConnect)+ "</font>"));
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

	//from LeftArrayAdapter
	@Override
	public void LongPressedLeftItemId(final int id) {
		boolean editFaded = id < 5;
		Log.d(TAG, "items="+ ((Vars)this.getApplication()).getConnectionItems().toString());
		if (id > 4) {
//			Log.d(TAG, "looking for "+ id);
			final ConnectionItem item = ((Vars)this.getApplication()).getConnectionItemByID(id);
			if (item != null) {
				Log.d(TAG, "item=" + item.toString());
				if (!editFaded) {
					final int type = item.getType();
					editFaded = type != CONNECTION_TYPE_FTP && type != CONNECTION_TYPE_SMB;
				}
			}
		}
		AlertDialog.Builder contextmenuBuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
		String itemName = ((Vars)MainActivity.this.getApplication()).leftList.get(id-1).getName();
		contextmenuBuilder.setTitle(itemName);
		ArrayList<ListItem> items = new ArrayList<>();//fillAddConnections();
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.connto)), null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_ellipsis_h)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				id < 5));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.rename, "")),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_newspaper1)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				id < 5));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.edit_conn)),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_edit1)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				/*id < 5*/editFaded));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.remove)),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_eraser)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				id < 5));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.move)),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_arrows_alt)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				id < 5));

		final View customView = View.inflate(this, R.layout.dialog_list, null);
		customView.setPadding(Tools.dpToPx(8, this), Tools.dpToPx(10, this),
				Tools.dpToPx(8, this), Tools.dpToPx(10, this));
		contextmenuBuilder.setView(customView);

		final AlertDialog contextmenuDialog = contextmenuBuilder.create();
		contextmenuDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
				(dialog1, which) -> dialog1.cancel());
//				addConnectionDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), (DialogInterface.OnClickListener) null);
		contextmenuDialog.setCanceledOnTouchOutside(false);
		contextmenuDialog.show();
		CustomDialogListAdapter adapter = new CustomDialogListAdapter(this,
				R.layout.dialog_list, items, item1 -> {
					if (id > 4) {
						//						addConnectionDialog.dismiss();
						contextmenuDialog.cancel();
						String name = item1.getName();
						//				int id = ((Vars) MainActivity.this.getApplication()).getConnectionItems().size();
						//				item1.setID(((Vars) MainActivity.this.getApplication()).leftList.size() + 1);
						MainActivity.this.contextmenuSelection(name, id);
					}
				});
		ListView listView = customView.findViewById(R.id.lv_dialog_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener((parent, view, position, itemId) -> contextmenuDialog.dismiss());
	}

	void contextmenuSelection(String name, int id) {
//		Log.d(TAG, "contextmenuSelection: name="+ name+ ", id="+ id);
		if (name.toUpperCase().equals(getString(R.string.remove).toUpperCase())) {
			AlertDialog.Builder rusureDialog = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
			rusureDialog.setTitle(getString(R.string.rusure));
			String itemName = ((Vars)MainActivity.this.getApplication()).leftList.get(id-1).getName();
			rusureDialog.setMessage(getString(R.string.delete_conn_x, itemName));
			rusureDialog.setNegativeButton(getString(android.R.string.cancel), null);
			rusureDialog.setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
				((Vars) MainActivity.this.getApplication()).leftList.remove(id-1);
				((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
			});
			rusureDialog.show();
		} else if (name.toUpperCase().equals(getString(R.string.rename, "").toUpperCase())) {
			String itemName = ((Vars)MainActivity.this.getApplication()).leftList.get(id-1).getName();
			AlertDialog.Builder renameDialogBuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
			renameDialogBuilder.setTitle(getString(R.string.rename, itemName));
			final View customView = View.inflate(this, R.layout.edit_one_field, null);
			customView.setPadding(Tools.dpToPx(8, this), Tools.dpToPx(24, this),
					Tools.dpToPx(8, this), Tools.dpToPx(24, this));
			final TextView label = customView.findViewById(R.id.tv_edit_label);
			final EditText value = customView.findViewById(R.id.et_edit_field);
			label.setText(Tools.toTitleCase(getString(R.string.name)));
			value.setHint(itemName);
			renameDialogBuilder.setView(customView);

			final AlertDialog renameDialog = renameDialogBuilder.create();
			renameDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
					(dialog1, which) -> dialog1.cancel());
			renameDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
					(dialog1, which) -> {
						((Vars) MainActivity.this.getApplication()).leftList.get(id-1).setName(value.getText().toString());
						((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
					});
			renameDialog.show();
		} else if (name.toUpperCase().equals(getString(R.string.move).toUpperCase())) {
		} else if (name.toUpperCase().equals(getString(R.string.edit_conn).toUpperCase())) {
			ConnectionItem item = ((Vars) MainActivity.this.getApplication()).getConnectionItemByID(id-5);
			Log.d(TAG, "editing:"+ item.toString());
//			showFTPsettings(item.getType(), item);
			showFTPsettings(item.getID(), 0);
		}
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

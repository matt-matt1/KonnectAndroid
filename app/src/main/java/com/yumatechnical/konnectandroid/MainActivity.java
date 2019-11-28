package com.yumatechnical.konnectandroid;
//INUSE
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ammarptn.debug.gdrive.lib.GDriveDebugViewActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.box.sdk.BoxConfig;
import com.box.sdk.BoxDeveloperEditionAPIConnection;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v1.DbxEntry;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
//import com.google.api.client.json.Json;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
//import com.jakewharton.rxbinding3.view.RxView;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;
import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yumatechnical.konnectandroid.Adapter.CustomDialogListAdapter;
import com.yumatechnical.konnectandroid.Adapter.LeftArrayAdapter;
//import com.yumatechnical.konnectandroid.Adapter.SearchAdapter;
import com.yumatechnical.konnectandroid.Adapter.MSAccountsListAdapter;
import com.yumatechnical.konnectandroid.Fragment.LeftItemFragment;
import com.yumatechnical.konnectandroid.Fragment.MyDialogFragment;
import com.yumatechnical.konnectandroid.Fragment.RightFragment;
import com.yumatechnical.konnectandroid.Helper.AESUtils;
import com.yumatechnical.konnectandroid.Helper.BoxSDK;
import com.yumatechnical.konnectandroid.Helper.DriveQuickstart;
import com.yumatechnical.konnectandroid.Helper.DriveServiceHelper;
import com.yumatechnical.konnectandroid.Helper.Dropbox.DropboxInstance;
import com.yumatechnical.konnectandroid.Helper.Dropbox.UploadTask;
import com.yumatechnical.konnectandroid.Helper.GoogleDrive;
import com.yumatechnical.konnectandroid.Helper.MSALActivity;
import com.yumatechnical.konnectandroid.Helper.Network.Http;
import com.yumatechnical.konnectandroid.Helper.Network.LoaderFTP;
import com.yumatechnical.konnectandroid.Helper.Network.LocalNetwork;
import com.yumatechnical.konnectandroid.Helper.Network.SMBoperation;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.Helper.URI_to_Path;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;
import com.yumatechnical.konnectandroid.Model.GoogleDriveFileHolder;
import com.yumatechnical.konnectandroid.Model.Item;
import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.Model.MicrosoftGraph.UserInfo;
import com.yumatechnical.konnectandroid.Settings.SettingsActivity;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.yumatechnical.konnectandroid.Helper.MSALActivity.MSGRAPH_URL;
import static com.yumatechnical.konnectandroid.Helper.MSALActivity.SCOPES;
import static java.lang.StrictMath.max;


public class MainActivity extends AppCompatActivity
		implements /**/SharedPreferences.OnSharedPreferenceChangeListener,/**/
		/**/Preference.OnPreferenceChangeListener,/**/
		LeftArrayAdapter.OnListener,
		LeftItemFragment.OnLeftListFragmentInteractionListener,
		LoaderManager.LoaderCallbacks<Session>,
		LocalNetwork.ConnectionInfoTask.OnNetworkConnectionInfo,
		MyDialogFragment.OnMyDialogInteraction {

	FrameLayout left, right, base;
	Toolbar toolbar;
//	Vars vars = Vars.getInstance();
	public static final int OPERATION_SMB_LOADER = 22;
	private static final int MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE = 88;
//	private static final int MY_PERMISSION_RECORD__REQUEST_CODE = 124;
	private static final int MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE = 4;
	private static final int MY_PERMISSION_RECORD_WRITE_CONTACTS_REQUEST_CODE = 5;
	private static final int MY_PERMISSION_RECORD_READ_EXTERNAL_STORAGE_REQUEST_CODE = 8;
	private static final int MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE = 7;
	private static final int MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE = 6;
	private static final int RC_SIGN_IN = 99;
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
//	private MyViewModel model;
	private AlertDialog.Builder alertDialogBuilderActWide;
	public static SharedPreferences sharedPreferences;
	private Vars vars = Vars.getInstance();
	private MSALActivity msal = MSALActivity.getInstance();
	public static JSONObject jsonObject;
	public static Gson gson /*= new Gson()*/;
	public static JSONObject parameters = new JSONObject();
	public static GsonBuilder gsonBuilder = new GsonBuilder();
	private GoogleSignInAccount accountGoogle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "onCreate");

/*		if (savedInstanceState != null) {
			((Vars)this.getApplication()).leftList =
					(ArrayList<ListItem>)savedInstanceState.getSerializable("leftList");
			((Vars)this.getApplication()).setConnectionItems(
					(ArrayList<ConnectionItem>)savedInstanceState.getSerializable("conns"));
		}*/
//		mySharedPreferencesActivity.SetupSharedPrefs();
		SetupSharedPrefs();
		initToolbar();

		new LocalNetwork.ConnectionInfoTask(getApplicationContext(), this).execute();
//		testCustomDialog();
		left = findViewById(R.id.left_frame);
		right = findViewById(R.id.fl_right_panel);
		base = findViewById(R.id.base_frame);

		setLeftList();
//		String str = null;
//		try {
//			str = AESUtils.encrypt("]hp_V-31-21@@F=mh@r0NTwQu1mGMJ=C");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		Log.d(TAG, "encrypted oned_secret: "+ str);
//		android.defaultConfig.javaCompileOptions.annotationProcessorOptions.includeCompileClasspath = true

//		instansiateMSAL();
		/* Configure your sample app and save state for this activity */
		msal.setMsClientApp(new PublicClientApplication(
				this.getApplicationContext(),
				R.raw.auth_config));

		gsonBuilder.setDateFormat("d/M/yy H:mm");
//		gson = gsonBuilder.create();
		gson = gsonBuilder.serializeNulls().create();
		accountGoogle = GoogleSignIn.getLastSignedInAccount(this);
//		Log.d(TAG, "box config is "+ getResources().getResourceName(R.raw.box_config)+ ".");
		BoxSDK boxSDK = new BoxSDK();
		String configFilename = "./src/main/res/raw/box_config.json";
//		String configFilename = "/Volumes/Mac27ShareHFS/yuma/AndroidStudioProjects/KonnectAndroid2/app/src/main/res/raw/box_config.json";
		BoxConfig config = boxSDK.loadConfig(configFilename);
		try {
			BoxDeveloperEditionAPIConnection api =
					BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(config);
			Log.d(TAG, "box config ID is " + api.getClientID());
		} catch (Exception e) {
			try {
				BoxConfig config1 = new BoxConfig(BoxSDK.CLIENT_ID, BoxSDK.CLIENT_SECRET,
						BoxSDK.ENTERPRISE_ID, BoxSDK.PUBLIC_KEY_ID, BoxSDK.PRIVATE_KEY,
						BoxSDK.PRIVATE_KEY_PASSPH);//, BoxSDK.ENCRYPTION_ALGORITHM
				BoxDeveloperEditionAPIConnection api =
						BoxDeveloperEditionAPIConnection.getAppEnterpriseConnection(config1);
				Log.d(TAG, "box config(2) ID is "+ api.toString());
			} catch (Exception ex) {
				Log.d(TAG, "box config ID is invalid");
			}
		}
/*		JsonReader reader;
		try {
			reader = Json.createReader(new FileReader(new FileReader(configFilename));
			JsonObject obj = reader.readObject();
			System.out.println("Content: " + obj.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		BoxSDK.listDevFiles(BoxSDK.DEVELOPER_TOKEN);

		// Build a new authorized API client service.
/*		NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException | IOException e) {
			e.printStackTrace();
		}*/
		NetHttpTransport HTTP_TRANSPORT = new com.google.api.client.http.javanet.NetHttpTransport();
		Drive service = null;
		try {
			service = new Drive.Builder(HTTP_TRANSPORT,
					DriveQuickstart.JSON_FACTORY,
					DriveQuickstart.getCredentials(HTTP_TRANSPORT))
					.setApplicationName(DriveQuickstart.APPLICATION_NAME)
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Print the names and IDs for up to 10 files.
		FileList result = null;
		try {
			result = service.files().list()
					.setPageSize(10)
					.setFields("nextPageToken, files(id, name)")
					.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<com.google.api.services.drive.model.File> files = result.getFiles();
		if (files == null || files.isEmpty()) {
			System.out.println("No files found.");
		} else {
			System.out.println("Files:");
			for (com.google.api.services.drive.model.File file : files) {
				System.out.printf("%s (%s)\n", file.getName(), file.getId());
			}
		}

	}


	/**
	 * setup for MSAL - Microsoft Authentication library for Android
	 */
	private void instansiateMSAL() {
		/* Configure your sample app and save state for this activity */
		msal.setMsClientApp(new PublicClientApplication(
				this.getApplicationContext(),
				R.raw.auth_config));

		/* Attempt to get a user and acquireTokenSilent */
		msal.getMsClientApp().getAccounts(accounts -> {
			if (accounts.size() > 0) {
//			if (!accounts.isEmpty()) {
				if (accounts.size() < 2) {
					msal.getMsClientApp().acquireTokenSilentAsync(SCOPES,
							accounts.get(0),
							new AuthenticationCallback() {
								@Override
								public void onSuccess(IAuthenticationResult authenticationResult) {
									Log.d(TAG, "instansiateMSAL() onSuccess " + authenticationResult.getAccessToken());
								}

								@Override
								public void onError(MsalException exception) {
									String error = String.format("%s(Microsoft account) '%s:%s'",
											getString(R.string.error), exception.getErrorCode(),
											exception.getMessage());
									Log.d(TAG, "instansiateMSAL() " + error);
									Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
								}

								@Override
								public void onCancel() {
									Log.d(TAG, "instansiateMSAL() onCancel");
								}
							});
				} else {
					/* has more than one account */
				}
//						msal.getAuthSilentCallback());
//			} else {
//				/* No accounts */
			}
		});
	}


	/**
	 * openCloseLeftPanel
	 * opens/expands or closes/shrinks the left panel/frame
	 *
	 * @param makeOpen boolean : true to open ; false to close
	 */
	/**/
	public void openCloseLeftPanel(boolean makeOpen) {
		FrameLayout fakeLeft = findViewById(R.id.fake_left_frame);
		if (makeOpen == ((Vars)getApplication()).isLeftPanelOpen())
			return;
		Log.d(TAG, makeOpen ? "opening left panel..." : "closing left panel...");
		((Vars)getApplication()).markLeftPanelOpen(makeOpen);
		LinearLayout sides = findViewById(R.id.ll_sides);
		ViewGroup.LayoutParams layoutParams = fakeLeft.getLayoutParams();
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				int width = makeOpen
						? layoutParams.width + (int)(175 * dm.density)
						: layoutParams.width - (int)(175 * dm.density);
				fakeLeft.setLayoutParams(new LinearLayout.LayoutParams(width,
						FrameLayout.LayoutParams.MATCH_PARENT));
				sides.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		animation.setDuration(400);//no effect
		sides.startAnimation(animation);
		Log.d(TAG, "layoutParams "+ layoutParams.width+ "x"+ layoutParams.height);
	}
/**/

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

		int num = sharedPreferences.getInt(getString(R.string.num_conn), 0);
		Log.d(TAG, "setLeftList "+ getString(R.string.num_conn)+ ": "+ num);
		boolean valid = false;
		for (int i = 0; i < num; i++) {
			int id = i /*+ Vars.NUM_THIS_DEVICE*/ + 1;
			Item item;
			String name = sharedPreferences.getString(getString(R.string.display_name)+id, "");
			int connType = sharedPreferences.getInt(getString(R.string.conn_type)+id, Vars.SMB_CONN);
			String token = sharedPreferences.getString(getString(R.string.token)+id, "");
			String connStr = sharedPreferences.getString(getString(R.string.conn_str)+id, "");
			if (!name.equals("")) {
				valid = true;
				String deConnStr = "";
				ListItem listItem = ((Vars) getApplication()).getSourceItemById(connType);
				if (!connStr.equals("")) {
					try {
						deConnStr = AESUtils.decrypt(connStr);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {   //operations on a clone
					ListItem listItem1 = listItem.clone();
					listItem1.setName(name);
					listItem1.setAccessToken(token);
					listItem1.setType(connType);
					listItem1.setConnectionStr(deConnStr);
					((Vars) getApplication()).leftList.add(listItem1);
				} catch (CloneNotSupportedException cnse) { //otherwise, use the actual object
					listItem.setName(name);
					listItem.setAccessToken(token);
					listItem.setType(connType);
					listItem.setConnectionStr(deConnStr);
					((Vars) getApplication()).leftList.add(listItem);
				} finally {
					ConnectionItem connectionItem = Tools.decryptConnString(deConnStr);
					if (connectionItem.getConnectionName().equals(""))
						connectionItem.setConnectionName(name);
					if (connectionItem.getType() == 0)
						connectionItem.setType(connType);
					if (connectionItem.getAccessToken().equals(""))
						connectionItem.setAccessToken(token);
					if (connectionItem.getID() == 0)
						connectionItem.setID(i);
					((Vars) getApplication()).addConnectionItem(connectionItem);
				}
			}
		}
		if (valid)
			Vars.NUM_CONN = num;
	}


	private void initToolbar() {
		toolbar = findViewById(R.id.my_toolbar);
		toolbar.setNavigationIcon(Vars.myHamburger_icon(this));
		toolbar.setNavigationContentDescription(R.string.expand__menu);
		//^action: onOptionsItemSelected (android.R.id.home)
		setSupportActionBar(toolbar);
	}


	/**
	 * Methods for setting up the menu
	 **/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.visualizer_menu, menu);
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.visualizer_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem menuItem;

		if (!((Vars)this.getApplication()).isNetworkConnected()) {
//		if (!model.isNetworkConnected()) {
			menuItem = menu.findItem(R.id.add_connect);
			menuItem.setIcon(Vars.menu_add_icon(this));
		}
		menuItem = menu.findItem(R.id.settings);
		menuItem.setIcon(Vars.menu_settings_icon(this));

		menuItem = menu.findItem(R.id.resize);
		menuItem.setIcon(Vars.menu_resize_icon(this));

		return true;
	}

/**/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				openCloseLeftPanel(!((Vars) getApplication()).isLeftPanelOpen());
				break;
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
//		int itemSize = model.getIconSize().getValue();
		slider.setProgress((itemSize-minIconSize)*factorPercent);
		int initial = itemSize;
		slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				seekBar.setProgress(progress);
				((Vars) getApplicationContext()).setIconSize((progress / factorPercent) + minIconSize);
//				model.setIconSize((progress / factorPercent) + minIconSize);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (((Vars)getApplication()).rightAdapter != null) {
//				if (model.rightAdapter != null) {
					((Vars)getApplication()).rightAdapter.notifyDataSetChanged();
//					model.rightAdapter.notifyDataSetChanged();
				}
				if (((Vars)getApplication()).recyclerView != null) {
					((Vars) getApplication()).recyclerView.invalidate();
//					model.recyclerView.invalidate();
					((Vars) getApplication()).recyclerView.scrollBy(0, 0);
//					model.recyclerView.scrollBy(0, 0);
//					((Vars) getApplication()).recyclerView.requestLayout();
				}
			}
		});
		AlertDialog.Builder resizeBuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
		resizeBuilder.setView(sliderView);
		final AlertDialog resizeDialog = resizeBuilder.create();
		resizeDialog.setTitle(getResources().getString(R.string.icon_resize));
		resizeDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
				(dialog1, which) -> {
					((Vars) getApplication()).recyclerView.forceLayout();
					dialog1.cancel();
				});
		resizeDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
				(dialog1, which) -> {
					slider.setProgress(initial);
					((Vars) getApplicationContext()).setIconSize(initial);
//					model.setIconSize(initial);
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
//		ArrayList<ListItem> items = Vars.fillAddConnections(getApplication());
//		List<ListItem> items = Vars.fillAddConnections(getApplication());

		AlertDialog.Builder addConnectBuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

		final View customView = View.inflate(this, R.layout.dialog_list, null);
		customView.setPadding(Tools.dpToPx(8, this), Tools.dpToPx(24, this),
				Tools.dpToPx(8, this), Tools.dpToPx(24, this));
		addConnectBuilder.setView(customView);
//		ProgressBar dialogSpinner = customView.findViewById(R.id.pb_dialog_spinner);
//		dialogSpinner.setVisibility(View.INVISIBLE);

		final AlertDialog addConnectionDialog = addConnectBuilder.create();
		addConnectionDialog.setTitle(getString(R.string.addConnection));
//		addConnectionDialog.setTitle(Html.fromHtml("<font color='#111111'>"+ getString(R.string.addConnection)+ "</font>"));
		addConnectionDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
				getResources().getString(android.R.string.cancel),
				(dialog1, which) -> dialog1.cancel());
		addConnectionDialog.setCanceledOnTouchOutside(false);
		addConnectionDialog.show();
		CustomDialogListAdapter adapter = new CustomDialogListAdapter(this,
				R.layout.dialog_list, /*items*/Vars.fillAddConnections(getApplication()), item1 -> {
					addConnectionDialog.cancel();
					String name = item1.getName();
////					int id = ((Vars) MainActivity.this.getApplication()).getConnectionItems().size();
//					item1.setType(((Vars) MainActivity.this.getApplication()).leftList.size() + 1);
//					((Vars) MainActivity.this.getApplication()).leftList.add(item1);
//					((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
					MainActivity.this.selectionAddConnection(name, item1.getType(), item1);
				});
		ListView listView = customView.findViewById(R.id.lv_dialog_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener((parent, view, position, id) -> addConnectionDialog.dismiss());
	}

	private void signInGoogleAccount() {
		Toast.makeText(this, "Launching Google account sign-in for GDrive", Toast.LENGTH_LONG).show();
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();
		GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	private void selectionAddConnection(String name, int id, ListItem listItem) {
//		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if (name.equals(MainActivity.this.getString(R.string.gdrive))) {
			ConnectionItem item = new ConnectionItem(
					id,
					Vars.GDRIVE_CONN,
					/*CONNECTION_TYPE_GDRIVE,*/
					getString(R.string.gdrive),
					"",
					"",
					"",
					"",
					"drive.google.com",
					80,
					"",
					"/");
//							ShareCompat.IntentBuilder.from(this)
//									.setChooserTitle("title")
//									.setType("mimiType")
//									.setText("textToShare");
			accountGoogle = GoogleSignIn.getLastSignedInAccount(this);
//			GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
			if (accountGoogle == null) {
//			if (account == null) {
				Log.d(TAG, "sign_in() has no account");
				// Configure sign-in to request the user's ID, email address, etc.
				signInGoogleAccount();
			} else {
				alertDialogBuilderActWide = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
				alertDialogBuilderActWide.setTitle(getString(R.string.useGoogleAcc));
				alertDialogBuilderActWide.setMessage(accountGoogle.getDisplayName()+ " ("+ accountGoogle.getEmail()+ ")");
//				alertDialogBuilderActWide.setMessage(account.getDisplayName()+ " ("+ account.getEmail()+ ")");
				alertDialogBuilderActWide.setNegativeButton(android.R.string.cancel,
						(dialog, which) -> dialog.dismiss());
				alertDialogBuilderActWide.setNeutralButton(R.string.another, (dialog, which) -> {
//						account = null;
					signInGoogleAccount();
				});
				alertDialogBuilderActWide.setPositiveButton(R.string.proceed, (dialog, which) ->
						useGoodleAccount());
//						useGoodleAccount(account));
				AlertDialog accountAlert = alertDialogBuilderActWide.create();
				accountAlert.show();
//				Toast.makeText(this, "Using Google account "+ account.getDisplayName()+ " ("+
//						account.getEmail()+ ")", Toast.LENGTH_LONG).show();
			}
			((Vars)getApplication()).addConnectionItem(item);
//			model.addConnectionItem(item);
			ListItem item1 = listItem;
//			ListItem item1 = new ListItem(
//					0, ((Vars) MainActivity.this.getApplication()).leftList.size() + 1, name,
//					"", null, 0,
//					0, 0, false, 0, false);
			item1.setType(((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//			item1.setType(model.getLeftList().getValue().size()/*.getLeftListItemNextID()*/);
			((Vars) MainActivity.this.getApplication()).leftList.add(item1);
//			model.getLeftList().getValue().add(item1)/*.leftList.add(item1)*/;
			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//			model.leftAdapter.notifyDataSetChanged();
			editor.putInt(getString(R.string.num_conn)+Vars.NUM_CONN, ++Vars.NUM_CONN);
			Log.d(TAG, "saving new connection "+ Vars.NUM_CONN);
			editor.putString(getString(R.string.display_name)+Vars.NUM_CONN, name);
			editor.putInt(getString(R.string.conn_type)+Vars.NUM_CONN, Vars.GDRIVE_CONN);
			editor.putString(getString(R.string.token)+Vars.NUM_CONN, "");
			editor.putString(getString(R.string.conn_str)+Vars.NUM_CONN, "");
			editor.apply();
		} else if (name.equals(MainActivity.this.getString(R.string.onedrive))) {
//							Log.d(TAG, "adding a new Onedrive connection:" + id);
			ConnectionItem item = new ConnectionItem(
					id,
					/*CONNECTION_TYPE_ONEDRIVE,*/
					Vars.ONED_CONN,
					getString(R.string.onedrive),
					"",
					"",
					"",
					"",
					"onedrive.microsoft.com",
					80,
					"",
					"/");
			((Vars)getApplication()).addConnectionItem(item);
			PublicClientApplication msClient = new PublicClientApplication(this, R.raw.auth_config);
			msClient.acquireToken(this, SCOPES, new AuthenticationCallback() {
				@Override
				public void onSuccess(IAuthenticationResult authenticationResult) {
					Log.d(TAG, "selectionAddConnection() onSuccess authenticationResult: "+
							authenticationResult.getAccessToken());
					editor.putString(getString(R.string.token)+Vars.NUM_CONN,
							authenticationResult.getAccessToken());
					RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//										try {
//											parameters.put("key", "value");
//										} catch (Exception e) {
//											Log.d(TAG, "Failed to put parameters: " + e.toString());
//										}
					JsonObjectRequest request =
							new JsonObjectRequest(Request.Method.GET,
//														"https://apis.live.net/v5.0/me/",
//														"https://apis.live.net/v5.0/me/root/files",
									MSGRAPH_URL+ "/drive/root/children",
									parameters, response -> {
								Log.d(TAG, "Response: " + response.toString());
								try {
									jsonObject = new JSONObject(response.toString());
									UserInfo userInfo = gson.fromJson(jsonObject.toString(), UserInfo.class);
									Toast.makeText(MainActivity.this,
											"Welcome "+ userInfo.getDisplayName(),
											Toast.LENGTH_LONG).show();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}, error -> {
								String msg = Tools.displayVolleyError(error);
								doOnRightPanel(RightFragment.newInstance(item.getConnectionName(),
										null, msg));
							})
							{
								@Override
								public Map<String, String> getHeaders() {
									Map<String, String> headers = new HashMap<>();
									headers.put("Authorization",
											"Bearer "+ authenticationResult.getAccessToken());
									return headers;
								}
							};
					request.setRetryPolicy(new DefaultRetryPolicy(
							3000,
							DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
							DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
					try {
						Http.getInstance().addToRequestQueue(MainActivity.this, request, request.getUrl());//"MSKeyValue");
					} catch (Exception e) {
						queue.add(request);
					}
				}

				@Override
				public void onError(MsalException exception) {
					String error = String.format("%s(Microsoft account) '%s:%s'",
							getString(R.string.error), exception.getErrorCode(),
							exception.getMessage());
					Log.d(TAG, "selectionAddConnection() " + error);
					Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onCancel() {
				}
			});
//			MSALActivity msalActivity = new MSALActivity();
//			msalActivity.onCallGraphClicked();
//			msal.getMsClientApp().acquireToken(this, SCOPES,
//					msal.getAuthInteractiveCallback());
			Toast.makeText(this, R.string.auth_ms, Toast.LENGTH_LONG).show();
//			model.addConnectionItem(item);
			ListItem item1 = listItem;
			item1.setType(((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//			item1.setType(model.getLeftList().getValue().size()/*.getLeftListItemNextID()*/);
			((Vars) MainActivity.this.getApplication()).leftList.add(item1);
//			model.getLeftList().getValue().add(item1)/*.leftList.add(item1)*/;
			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//			model.leftAdapter.notifyDataSetChanged();
			editor.putInt(getString(R.string.num_conn), ++Vars.NUM_CONN);
			Log.d(TAG, "saving new connection "+ Vars.NUM_CONN);
			editor.putString(getString(R.string.display_name)+Vars.NUM_CONN, name);
			editor.putInt(getString(R.string.conn_type)+Vars.NUM_CONN, Vars.ONED_CONN);
			// ^ often wrongly puts in id 1 !!!
			editor.putString(getString(R.string.token)+Vars.NUM_CONN, "");
			editor.putString(getString(R.string.conn_str)+Vars.NUM_CONN, "");
			editor.apply();
		} else if (name.equals(MainActivity.this.getString(R.string.icloud))) {
							Log.d(TAG, "adding a new iCloud connection:" + id);
			ConnectionItem item = new ConnectionItem(
					id,
					/*CONNECTION_TYPE_ICLOUD*/Vars.ICLOUD_CONN,
					getString(R.string.icloud),
					"",
					"",
					"",
					"",
					"icloud.apple.com",
					80,
					"",
					"/");
			((Vars)getApplication()).addConnectionItem(item);
//			model.addConnectionItem(item);
			ListItem item1 = listItem;
			item1.setType(((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//			item1.setType(model.getLeftList().getValue().size()/*.getLeftListItemNextID()*/);
			((Vars) MainActivity.this.getApplication()).leftList.add(item1);
//			model.getLeftList().getValue().add(item1)/*.leftList.add(item1)*/;
			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//			model.leftAdapter.notifyDataSetChanged();
			editor.putInt(getString(R.string.num_conn), ++Vars.NUM_CONN);
			Log.d(TAG, "saving new connection "+ Vars.NUM_CONN);
			editor.putString(getString(R.string.display_name)+Vars.NUM_CONN, name);
			editor.putInt(getString(R.string.conn_type)+Vars.NUM_CONN, Vars.ICLOUD_CONN);
			editor.putString(getString(R.string.token)+Vars.NUM_CONN, "");
			editor.putString(getString(R.string.conn_str)+Vars.NUM_CONN, "");
			editor.apply();
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
					/*CONNECTION_TYPE_DROPBOX*/Vars.DBOX_CONN,
					getString(R.string.dropbox),
					"",
					"",
					"",
					"",
					"dropbox.com",
					80,
					"",
					"/");
			((Vars)getApplication()).addConnectionItem(item);
//			model.addConnectionItem(item);
			ListItem item1 = listItem;
			item1.setType(((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//			item1.setType(model.getLeftList().getValue().size());
			((Vars) MainActivity.this.getApplication()).leftList.add(item1);
//			model.getLeftList().getValue().add(item1);
			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//			model.leftAdapter.notifyDataSetChanged();
			editor.putInt(getString(R.string.num_conn), ++Vars.NUM_CONN);
			Log.d(TAG, "saving new connection "+ Vars.NUM_CONN);
			editor.putInt(getString(R.string.conn_type)+Vars.NUM_CONN, Vars.DBOX_CONN);
			editor.putString(getString(R.string.display_name)+Vars.NUM_CONN, name);
			editor.putString(getString(R.string.token)+Vars.NUM_CONN, "");
			editor.putString(getString(R.string.conn_str)+Vars.NUM_CONN, "");
			editor.apply();
		} else if (name.equals(getString(R.string.box))) {
/*			Uri.Builder builder = new Uri.Builder();
			builder.scheme("smb").encodedAuthority("yumausa:yuma@192.168.1.222").path("/");
			Uri uri = builder.build();
			Log.d(TAG, "Uri="+ uri);*/
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
					/*CONNECTION_TYPE_BOX*/Vars.BOX_CONN,
					getString(R.string.box),
					"",
					"",
					"",
					"",
					"box.microsoft.com",
					80,
					"",
					"/");
			((Vars)getApplication()).addConnectionItem(item);
//			model.addConnectionItem(item);
			ListItem item1 = listItem;
			item1.setType(((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//			item1.setType(model.getLeftList().getValue().size());
			((Vars) MainActivity.this.getApplication()).leftList.add(item1);
//			model.getLeftList().getValue().add(item1);
			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//			model.leftAdapter.notifyDataSetChanged();
			editor.putInt(getString(R.string.num_conn), ++Vars.NUM_CONN);
			Log.d(TAG, "saving new connection "+ Vars.NUM_CONN);
			editor.putInt(getString(R.string.conn_type)+Vars.NUM_CONN, Vars.BOX_CONN);
			editor.putString(getString(R.string.display_name)+Vars.NUM_CONN, name);
			editor.putString(getString(R.string.token)+Vars.NUM_CONN, "");
			editor.putString(getString(R.string.conn_str)+Vars.NUM_CONN, "");
			editor.apply();
		} else if (name.equals(MainActivity.this.getString(R.string.ftp))) {
//			ListItem item1 = listItem;
//			item1.setType(((Vars) MainActivity.this.getApplication()).leftList.size() + 1);
			((Vars) MainActivity.this.getApplication()).tempListItem = listItem;
//			model.tempListItem = listItem;
			((Vars) MainActivity.this.getApplication()).tempListItem.setType(
//			model.tempListItem.setType(
					((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//					model.getLeftList().getValue().size());
//			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//			showFTPsettings(-1, CONNECTION_TYPE_FTP);
			showFTPsettings(-1, Vars.FTP_COMM);
		} else if (name.equals(MainActivity.this.getString(R.string.local_network))) {
			((Vars) MainActivity.this.getApplication()).tempListItem = listItem;
//			model.tempListItem = listItem;
			((Vars) MainActivity.this.getApplication()).tempListItem.setType(
//			model.tempListItem.setType(
					((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//					model.getLeftList().getValue().size());
//			showFTPsettings(-1, CONNECTION_TYPE_SMB);
			showFTPsettings(-1, Vars.SMB_CONN);
		} else
			return;
	}


	private void showFTPsettings(int id, int myType) {
		ConnectionItem item = new ConnectionItem();
		int type = myType;
		if (id > -1) {
			item = ((Vars) this.getApplication()).getConnectionItemByID(id);
//			item = model.getConnectionItemByID(id);
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
//		if (type == CONNECTION_TYPE_SMB) {
		if (type == Vars.SMB_CONN) {
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
			TextView alabel = alertViewFTP.findViewById(R.id.tv_ftp_anon);
			TextView plabel = alertViewFTP.findViewById(R.id.tv_ftp_password);
			TextView tlabel = alertViewFTP.findViewById(R.id.tv_ftp_port);
			TextView iplabel = alertViewFTP.findViewById(R.id.tv_ftp_init_path);
			TextView shlabel = alertViewFTP.findViewById(R.id.tv_ftp_share_name);
			EditText ipath = alertViewFTP.findViewById(R.id.et_ftp_init_path);
			EditText hname = alertViewFTP.findViewById(R.id.et_ftp_host);
			EditText uname = alertViewFTP.findViewById(R.id.et_ftp_username);
			EditText pword = alertViewFTP.findViewById(R.id.et_ftp_password);
			EditText portn = alertViewFTP.findViewById(R.id.et_ftp_port);
			EditText etitle = alertViewFTP.findViewById(R.id.et_ftp_rename);
			EditText shname = alertViewFTP.findViewById(R.id.et_ftp_share_name);
			ImageView spinner = alertViewFTP.findViewById(R.id.iv_ftp_progress);
			RadioGroup radios = alertViewFTP.findViewById(R.id.rg_ftp_auth);
			TextView domlabel = alertViewFTP.findViewById(R.id.tv_ftp_domain);
			CheckBox anonuser = alertViewFTP.findViewById(R.id.cb_ftp_anon);
			EditText domain = alertViewFTP.findViewById(R.id.et_ftp_domain);
			View method = alertViewFTP.findViewById(R.id.connType);
			Button search_shares = alertViewFTP.findViewById(R.id.b_ftp_search_shares);
			ScrollView main = alertViewFTP.findViewById(R.id.sv_ftp);
			FrameLayout frame = alertViewFTP.findViewById(R.id.fl_ftp);

			method.setTag(type);
			title.setText(Tools.toTitleCase(getString(R.string.title)));
			hlabel.setText(Tools.toTitleCase(getString(R.string.server)));
			domlabel.setText(Tools.toTitleCase(getString(R.string.domain)));
			alabel.setText(Tools.toTitleCase(getString(R.string.anon_user)));
			ulabel.setText(Tools.toTitleCase(getString(R.string.username)));
			plabel.setText(Tools.toTitleCase(getString(R.string.password)));
			tlabel.setText(Tools.toTitleCase(getString(R.string.port)));
			shlabel.setText(Tools.toTitleCase(getString(R.string.share_name)));
			search_shares.setText(Tools.toTitleCase(getString(R.string.search_shares)));
//			search_shares.setWidth((main.getWidth() < 300) ? main.getWidth()-10 : 300);
			iplabel.setText(Tools.toTitleCase(getString(R.string.init_path)));
			spinner.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_spinner)
					.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				main.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						if (main.getViewTreeObserver().isAlive())
							main.getViewTreeObserver().removeOnPreDrawListener(this);
						//
						return true;
					}
				});
			}
			anonuser.setOnCheckedChangeListener((buttonView, isChecked) -> {
				if (isChecked) {
					uname.setText(R.string.anon_user);
					uname.setTextColor(Color.GRAY);
					uname.setEnabled(false);
					pword.setEnabled(false);
				} else {
					pword.setEnabled(true);
					uname.setEnabled(true);
					uname.setText("");
					uname.setTextColor(getResources().getColor(R.color.gray_iron));
				}
			});
//			if (type == CONNECTION_TYPE_SMB) {
			if (type == Vars.SMB_CONN) {
				radios.setVisibility(View.GONE);
				tlabel.setVisibility(View.GONE);
				portn.setVisibility(View.GONE);
				tlabel.setVisibility(View.GONE);
				domlabel.setVisibility(View.VISIBLE);
				domain.setVisibility(View.VISIBLE);
				shlabel.setVisibility(View.VISIBLE);
				search_shares.setVisibility(View.VISIBLE);
				shname.setVisibility(View.VISIBLE);
				hname.setText(Vars.getInstance().getPrefixIP()/*.getMyIPString()*/);
//				hname.setText(((Vars)getApplication()).getMyIPString());
			} else {
				RadioButton authFTP = alertViewFTP.findViewById(R.id.rb_ftp_plain);
				RadioButton authFTPS = alertViewFTP.findViewById(R.id.rb_ftps);
				RadioButton authSFTP = alertViewFTP.findViewById(R.id.rb_sftp);
				authSFTP.setOnClickListener(v -> portn.setHint("22"));
				authFTPS.setOnClickListener(v -> portn.setHint("21"));
				authFTP.setOnClickListener(v -> portn.setHint("21"));
				hname.setHint(Tools.toTitleCase(getString(R.string.eg_server)));
//				shlabel.setVisibility(View.GONE);
			}
/*			shname.setOnFocusChangeListener((v, hasFocus) -> {
				if (hasFocus) {
					if (!anonuser.isChecked() && (uname.getText() == null || uname.getText().toString().equals("")))
						Toast.makeText(MainActivity.this, R.string.need_user, Toast.LENGTH_SHORT).show();
					if (hname.getText() == null || hname.getText().toString().equals(""))
						Toast.makeText(MainActivity.this, R.string.need_host, Toast.LENGTH_SHORT).show();
					ArrayList<ListItem> filteredShareList = new ArrayList<>();

					final View customView = View.inflate(MainActivity.this, R.layout.dialog_list, null);
					customView.setPadding(Tools.dpToPx(8, MainActivity.this), Tools.dpToPx(10, MainActivity.this),
							Tools.dpToPx(8, MainActivity.this), Tools.dpToPx(10, MainActivity.this));

					CustomDialogListAdapter adapter =
							new CustomDialogListAdapter(MainActivity.this,
									R.layout.dialog_list, filteredShareList, item1 -> {
//								item.setShareName(item1.getName());////
								Log.d(TAG, "selected " + item1.getName());
//								shname.setText(item1.getName());
//								sharesDialog.dismiss();
							});

					ListView listView = customView.findViewById(R.id.lv_dialog_list);
					listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
					listView.setAdapter(adapter);

					SMBoperation smBoperation = new SMBoperation();
//					String host = hname.getText().toString();
//					String user = uname.getText().toString();
//					String pass = pword.getText().toString();
//					String wrkg = domain.getText().toString();
					smBoperation.listShares(
//							host, user, pass, wrkg
							hname.getText().toString(), uname.getText().toString(),
							pword.getText().toString(), domain.getText().toString(),
							shares -> {
								if (shares != null) {
									Log.d(TAG, "SMB share count: " + shares.size());
									if (shares.size() > 1) {
										markScrollbar(listView, shares.size(), adapter, MainActivity.this,
												customView.findViewById(R.id.ll_dialog_list));
									}
									for (String share : shares) {
										Log.d(TAG, "SMB share name: " + share);
										filteredShareList.add(new ListItem(0, 0, share, "",
												null, 0, 0, 0,
												false, 0, false));
									}
//									dialogSpinner.setVisibility(View.GONE);
//									Log.d(TAG, "shareList "+ shareList.size()+ ":"+ shareList.toString());
								} else {
									Log.d(TAG, "Error: Shares is null!");
								}
							});

					SearchManager searchManager = (SearchManager)
							getSystemService(Context.SEARCH_SERVICE);
//			searchMenuItem = shname.;//menu.findItem(R.id.search);
					SearchView searchView = null;// = (SearchView) searchMenuItem.getActionView();
					searchView.setSearchableInfo(searchManager.
							getSearchableInfo(getComponentName()));
					searchView.setSubmitButtonEnabled(true);
					searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
						@Override
						public boolean onQueryTextSubmit(String query) {
							return false;
						}

						@Override
						public boolean onQueryTextChange(String newText) {
							adapter.getFilter().filter(newText);
							return true;
						}
					});//setOnQueryTextListener

//					return true;

				}//hasFocus
			});//setOnFocusChangeListener
			*/
			search_shares.setOnClickListener(v -> {
				if (hname.getText() == null || hname.getText().toString().equals("")) {
					Log.d(TAG, getString(R.string.need_host));//"Need Server/Host name (or IP address to proceed");
					Toast.makeText(MainActivity.this, getString(R.string.need_host), Toast.LENGTH_SHORT).show();
				} else {
//						String host = hname.getText().toString();
				if (!anonuser.isChecked() && (uname.getText() == null || uname.getText().toString().equals(""))) {
//						Log.d(TAG, "missing username");
					Toast.makeText(MainActivity.this, getString(R.string.need_user), Toast.LENGTH_SHORT).show();
					uname.setText("guest");
				}
//						String user = uname.getText().toString();
//					if (hname.getText() == null || hname.getText().toString().equals("")) {
//						Log.d(TAG, "missing username");
//					}
//						String passwd = pword.getText().toString();
//					if (hname.getText() == null || hname.getText().toString().equals("")) {
//						Log.d(TAG, "missing username");
//					}
//						String domain = domain.getText().toString();
/*					int color = Color.TRANSPARENT;
					Drawable background = alertViewFTP.getBackground();
					if (background instanceof ColorDrawable)
						color = ((ColorDrawable) background).getColor();
					int finalColor = color;
					myDialogFragment.getView().setBackgroundColor(Color.GRAY);
//					alertViewFTP.setBackgroundColor(Color.GRAY);
					spinner.setVisibility(View.VISIBLE);*/
					alertDialogBuilderActWide = new AlertDialog.Builder(MainActivity.this,
							R.style.CustomDialogTheme);
					alertDialogBuilderActWide.setTitle(hname.getText().toString());
					alertDialogBuilderActWide.setNegativeButton(android.R.string.cancel,
							(dialog, which) -> dialog.dismiss());
//										alertDialogBuilderActWide.show();

//					final View customView = View.inflate(MainActivity.this, R.layout.dialog_list, null);
					final View customView = View.inflate(MainActivity.this, R.layout.search_share_with_list, null);
					customView.setPadding(Tools.dpToPx(8, MainActivity.this), Tools.dpToPx(10, MainActivity.this),
							Tools.dpToPx(8, MainActivity.this), Tools.dpToPx(10, MainActivity.this));
					LinearLayout layout = customView.findViewById(R.id.ll_dialog_list);
//					ListView sharesListView = customView.findViewById(R.id.lv_search_shares);

					alertDialogBuilderActWide.setView(customView);
					ProgressBar dialogSpinner = customView.findViewById(R.id.pb_dialog_spinner);
					SearchView searchView = customView.findViewById(R.id.sv_search_shares);
					searchView.setQueryHint(getString(R.string.find_share));
					layout.setVisibility(View.GONE);
					dialogSpinner.setVisibility(View.VISIBLE);

					final AlertDialog sharesDialog = alertDialogBuilderActWide.create();
					ArrayList<ListItem> shareList = new ArrayList<>();
					ArrayList<ListItem> filteredShareList = new ArrayList<>();
					CustomDialogListAdapter adapter =
							new CustomDialogListAdapter(MainActivity.this,
									R.layout.dialog_list, shareList, item1 -> {
										//										Log.d(TAG, "selected "+ item1.getName());
										Tools.hideKeyboard(MainActivity.this, customView);
										shname.setText(item1.getName());
										//										item.setShareName(shname.getText().toString());
										sharesDialog.dismiss();
	//										Tools.hideKeyboardFrom(MainActivity.this, customView);
										//TODO: make ^ work
									});
					ListView listView = customView.findViewById(R.id.lv_dialog_list);
					listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
					listView.setAdapter(adapter);
/**/
					searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
						@Override
						public boolean onQueryTextSubmit(String query) {
							filter(adapter, query, shareList, filteredShareList);
							return true;
//							return false;
						}

						@Override
						public boolean onQueryTextChange(String newText) {
//							Log.d(TAG, MainActivity.this.getLocalClassName()+ ":onQueryTextChange "+ newText);
							filter(adapter, newText, shareList, filteredShareList);
//							return false;
							return true;
						}
					});
					/**/
					sharesDialog.show();

					SMBoperation smBoperation = new SMBoperation();
					smBoperation.configSMB(false, true, true,
							true, 8*1024*1024, 300, TimeUnit.SECONDS,
							300, TimeUnit.SECONDS, null);
//					String host = hname.getText().toString();
//					String user = uname.getText().toString();
//					String pass = pword.getText().toString();
//					String wrkg = domain.getText().toString();
					smBoperation.listShares(
//							host, user, pass, wrkg
							hname.getText().toString(), uname.getText().toString(),
							pword.getText().toString(), domain.getText().toString(),
							(ArrayList<String> shares) -> {
								if (shares != null) {
//									Log.d(TAG, "SMB share count: " + shares.size());
									markScrollbar(listView, shares.size(), adapter, MainActivity.this, customView.findViewById(R.id.ll_dialog_list));
									for (String share : shares) {
//										Log.d(TAG, "SMB share name: " + share);
										shareList.add(new ListItem(0, 0, share, "",
												null, 0, 0, 0,
												false, 0, false, "", ""));
									}
									dialogSpinner.setVisibility(View.GONE);
									layout.setVisibility(View.VISIBLE);
									if (shares.size() < 2) {
										searchView.setVisibility(View.GONE);
									}
//									Log.d(TAG, "shareList "+ shareList.size()+ ":"+ shareList.toString());
								} else {
									Log.d(TAG, "Error: Shares is null!");
								}
							});
				}
			});
			if (item != null) {
				alertViewFTP.setTag(item.getID());
				if (item.getConnectionName() != null && !item.getConnectionName().equals("")) {
					etitle.setText(item.getConnectionName());
				} else {
					etitle.setHint(Tools.toTitleCase(getString(R.string.eg_conn)));
				}
				if (item.getPort() > 0) {
					try {
						portn.setText(String.valueOf(item.getPort()));
					} catch (NumberFormatException e) {
					}
//				} else {
//					portn.setText("21");
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
				if (item.getShareName() != null && !item.getShareName().equals("")) {
					shname.setText(item.getShareName());
				}
			} else {
				alertViewFTP.setTag(0);
				etitle.setHint(Tools.toTitleCase(getString(R.string.eg_conn)));
				portn.setHint("21");
				ipath.setHint("/");
			}
			myDialogFragment.setArguments(bundle);
			markScrollbar(main, 0, null, MainActivity.this, alertViewFTP.findViewById(R.id.ll_ftp_scrolloverlay));
			myDialogFragment.show(fm, "fragment_edit_ftp");
		}
	}


	private void filter(CustomDialogListAdapter adapter, String query, ArrayList<ListItem> itemsAll,
	                    ArrayList<ListItem> items) {
		//							shareList.clear();
//							adapter.setData(shareList);
		items.clear();
		adapter.setData(items);
		adapter.notifyDataSetChanged();
		if (query.equals("")) {
			adapter.setData(itemsAll);
		} else {
			for (ListItem share : itemsAll) {
				if (share.getName().toLowerCase().contains(query.toLowerCase())) {
					items.add(share);
				}
			}
			adapter.setData(items);
		}
		adapter.notifyDataSetChanged();
	}


	private static int getListViewContentsHeight(ListView listView, int number_of_elements, CustomDialogListAdapter adapter) {
		int total = 0;
		for (int i = 0; i < number_of_elements/*adapter.getCount()*/; i++) {
			View view = adapter.getView(i, null, listView);
			view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
					View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			total += view.getMeasuredHeight();
		}
		total += (listView.getDividerHeight() * max(0, adapter.getCount()-1));
		return total;
	}

	public void markScrollbar(final ViewGroup scrollView, final int number_of_elements,
	                          final CustomDialogListAdapter adapter, final Context context, ViewGroup overlay) {
		scrollView.post(() -> {
			int max = 0;
			if (scrollView instanceof ScrollView) {
				max = Math.max(0, scrollView.getChildAt(0).getHeight() - (scrollView.getHeight()
						- scrollView.getPaddingBottom() - scrollView.getPaddingTop()));
			} else if (scrollView instanceof ListView) {
				max = getListViewContentsHeight((ListView) scrollView, number_of_elements, adapter);
			}
			if (max == 0) {
				Log.d(TAG, "scrollview contents all fit");
			} else {
				ImageView up = new ImageView(context);
				up.setImageDrawable(Vars.scoll_up_icon(context));
				overlay.addView(up);
				ImageView mid = new ImageView(context);
				mid.setImageDrawable(Vars.scroll_mid_icon(context));
				overlay.addView(mid);
				ImageView down = new ImageView(context);
				down.setImageDrawable(Vars.scroll_down_icon(context));
				overlay.addView(down);
				overlay.setVisibility(View.VISIBLE);
			}
		});
	}


	//from MyDialogFragment
	@Override
	public void AlertDialogNeutralButtonPressed(Button button, AlertDialog dialog, int id) {
		ConnectionItem item;
		View view = (View) button.getParent().getParent().getParent();
		EditText ipath = view.findViewById(R.id.et_ftp_init_path);
		EditText hname = view.findViewById(R.id.et_ftp_host);
		EditText uname = view.findViewById(R.id.et_ftp_username);
		EditText pword = view.findViewById(R.id.et_ftp_password);
		EditText portn = view.findViewById(R.id.et_ftp_port);
//		EditText share = view.findViewById(R.id.et_ftp_share_name);
		ProgressBar spinner = view.findViewById(R.id.pb_ftp_spinner);
		LinearLayout layout = view.findViewById(R.id.ll_ftp_response);
		TextView response = view.findViewById(R.id.tv_ftp_response);
		ImageView rimage = view.findViewById(R.id.iv_ftp_response);
		ScrollView main = view.findViewById(R.id.sv_ftp);
		LinearLayout indicators = view.findViewById(R.id.ll_ftp_scrolloverlay);

		indicators.setVisibility(View.INVISIBLE);
		button.setVisibility(View.INVISIBLE);
		main.setVisibility(View.INVISIBLE);
		spinner.setVisibility(View.VISIBLE);
		int type;
		if (id > -1) {
			item = ((Vars) this.getApplication()).getConnectionItemByID(id);
//			item = model.getConnectionItemByID(id);
			type = item.getType();
		} else {
			View method = view.findViewById(R.id.connType);
			type = (int)method.getTag();
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			button.setBackgroundDrawable(
					new IconicsDrawable(this, FontAwesome.Icon.faw_spinner)
							.color(IconicsColor.colorRes(R.color.Teal))
							.size(IconicsSize.TOOLBAR_ICON_SIZE));
		}
//		RadioButton authFTP = alertViewFTP.findViewById(R.id.rb_ftp_plain);
//		RadioButton authFTPS = alertViewFTP.findViewById(R.id.rb_ftps);
//		RadioButton authSFTP = alertViewFTP.findViewById(R.id.rb_sftp);
//		TextView domlabel = alertViewFTP.findViewById(R.id.tv_ftp_domain);
		EditText domain = alertViewFTP.findViewById(R.id.et_ftp_domain);
		String connStr = "";
//		connStr = (type == CONNECTION_TYPE_SMB)
//				? ""
//				: (authSFTP.isChecked())
//					? "sftp://"
//					: (authFTPS.isChecked())
//						? "ftps://"
//						: "ftp://";
		connStr += hname.getText();
		int port;
//		if (type != CONNECTION_TYPE_SMB) {
		if (type != Vars.SMB_CONN) {
			try {
				port = Integer.parseInt(portn.getText().toString());
				connStr += ":" + port;
			} catch (NumberFormatException e) {
			}
			if (!ipath.getText().toString().startsWith("/")) {
				connStr += "/";
			}
			connStr += ipath.getText();
		}

		Log.d(TAG, "testing connection:"+ connStr);
//		if (type == CONNECTION_TYPE_SMB) {
		if (type == Vars.SMB_CONN) {
//			LoaderSMB client = new LoaderSMB(this, result -> {
//				Log.d(TAG, "tested connection:");
//				Log.d(TAG, "response=" + result.getSessionId()+ ":" + result.getConnection().toString());
//				dialog.dismiss();
//			});
//			client.connect(connStr, uname.getText().toString(), pword.getText().toString(), .getText().toString());
			final String connString = connStr;
			SMBoperation smb = new SMBoperation();
			smb.configSMB(false, true, true,
					true, 8*1024*1024, 300, TimeUnit.SECONDS,
					300, TimeUnit.SECONDS, null);
			smb.listShares(hname.getText().toString(), uname.getText().toString(), pword.getText().toString(),
					domain.getText().toString(), shares -> {
						spinner.setVisibility(View.INVISIBLE);
						if (shares != null && shares.size() > 0) {
//								Log.d(TAG, "smb shares="+ shares.forEach(toString()));
							response.setText(String.format(Locale.CANADA, "%s.\n%s",
									getString(R.string.conn_good), getString(R.string.save_OK)));
							rimage.setImageDrawable(Vars.success_icon(MainActivity.this));
							response.setVisibility(View.VISIBLE);
							rimage.setVisibility(View.VISIBLE);
							layout.setVisibility(View.VISIBLE);
						} else {
							Toast.makeText(MainActivity.this,
									getString(R.string.conn_bad)+ "."+ getString(R.string.adjust_sett),
									Toast.LENGTH_SHORT).show();
							main.setVisibility(View.VISIBLE);
							button.setVisibility(View.VISIBLE);
						}
					});
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
/*			FTPoperation client = new FTPoperation(result -> {
				button.setVisibility(View.GONE);
				spinner.setVisibility(View.GONE);
				try {
					Log.d(TAG, "ftp response=" + result.getStatus()+ ":" + result.getReplyString());
					response.setText(String.format("%s.\n%s", getString(R.string.conn_good), getString(R.string.save_OK)));
					rimage.setImageDrawable(new IconicsDrawable(MainActivity.this, FontAwesome.Icon.faw_check)
							.color(IconicsColor.colorRes(R.color.springGreen)).size(IconicsSize.TOOLBAR_ICON_SIZE));
					response.setVisibility(View.VISIBLE);
					rimage.setVisibility(View.VISIBLE);
					layout.setVisibility(View.VISIBLE);
				} catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(MainActivity.this,
							getString(R.string.conn_bad)+ "."+ getString(R.string.adjust_sett),
							Toast.LENGTH_SHORT).show();
				}
			});
			client.connectFTP(connStr, uname.getText().toString(), pword.getText().toString(),
					ipath.getText().toString());*/
			FTPClient ftpClient = new FTPClient();
			try {
				ftpClient.connect(InetAddress.getByName(hname.getText().toString()));
				ftpClient.login(uname.getText().toString(), pword.getText().toString());
				ftpClient.changeWorkingDirectory(ipath.getText().toString());
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				File file = new File("filename");
				BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
				ftpClient.enterLocalPassiveMode();
				ftpClient.storeFile("test.txt", bufferedInputStream);
				bufferedInputStream.close();
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void AlertDialogNegativeButtonPressed(Button button, AlertDialog dialog, int id) {
		dialog.dismiss();
	}

	@Override
	public void AlertDialogPositiveButtonPressed(Button button, AlertDialog dialog, int id) {
		Log.d(TAG, "AlertDialogPositiveButtonPressed");
		ConnectionItem item = new ConnectionItem();
		if (id > -1) {
			item = ((Vars) this.getApplication()).getConnectionItemByID(id);
//			item = model.getConnectionItemByID(id);
		} else {
			item.setID(((Vars)this.getApplication()).getConnectionItems().size());
//			item.setType(model.getConnectionItems().getValue().size());
		}
		View view = (View) button.getParent().getParent().getParent();
		EditText ipath = view.findViewById(R.id.et_ftp_init_path);
		EditText hname = view.findViewById(R.id.et_ftp_host);
		EditText uname = view.findViewById(R.id.et_ftp_username);
		EditText pword = view.findViewById(R.id.et_ftp_password);
		EditText portn = view.findViewById(R.id.et_ftp_port);
		EditText etitle = view.findViewById(R.id.et_ftp_rename);
		EditText share = view.findViewById(R.id.et_ftp_share_name);
		ImageView spinner = view.findViewById(R.id.iv_ftp_progress);
		RadioButton authFTPS = view.findViewById(R.id.rb_ftps);
		RadioButton authSFTP = view.findViewById(R.id.rb_sftp);
		EditText domain = view.findViewById(R.id.et_ftp_domain);
		View method = view.findViewById(R.id.connType);
//		RadioButton authFTPS = alertViewFTP.findViewById(R.id.rb_ftps);
//		RadioButton authSFTP = alertViewFTP.findViewById(R.id.rb_sftp);
//		EditText domain = alertViewFTP.findViewById(R.id.et_ftp_domain);
//		View method = alertViewFTP.findViewById(R.id.connType);
		spinner.setVisibility(View.VISIBLE);
		int port = 0;
		try {
			port = Integer.parseInt(portn.getText().toString());
//			if (port < 1) {
//				port = 21;
//			}
			item.setPort(port);
		} catch (NumberFormatException e) {
		}
		if (item.getType() < 1) {
			item.setType((int)method.getTag());
		}
		if (etitle.getText() != null && !etitle.getText().toString().equals("")) {
			item.setConnectionName(etitle.getText().toString());
		} else if (hname.getText() != null && share.getText() != null) {
			String ipid = hname.getText().toString();
			if (hname.getText().toString().contains(".")) {
				ipid = ipid.substring(ipid.lastIndexOf("."));
			}
			item.setConnectionName(String.format(Locale.CANADA, "%s::%s",
					ipid, share.getText().toString()));
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
		if (share.getText() != null) {
			item.setShareName(share.getText().toString());
		}
		if (ipath.getText() != null) {
			item.setPath(ipath.getText().toString());
		} else {
			item.setPath(ipath.getHint().toString());
		}
		String scheme = "";
		if (item.getType() > 0) {
//			String scheme = (item.getType() == CONNECTION_TYPE_SMB) ? "smb"
			scheme = (item.getType() == Vars.SMB_CONN) ? "smb"
					: (authSFTP.isChecked()) ? "sftp"
						: (authFTPS.isChecked()) ? "ftps"
							: "ftp";
			item.setScheme(scheme);
		}
		if (domain.getText() != null) {
			item.setAccessToken(domain.getText().toString());
		}
		if (item.getConnectionName() != null && !item.getConnectionName().equals("")
				&& ((Vars) MainActivity.this.getApplication()).tempListItem != null
				&& !item.getConnectionName().equals(
						((Vars) MainActivity.this.getApplication()).tempListItem.getName())) {
//				!item.getConnectionName().equals(model.tempListItem.getName())) {
//			Log.d(TAG, "leftList="+ ((Vars)this.getApplication()).leftList.toString());
//			Log.d(TAG, "id="+ id+ ", itemId="+ item.getType());
//			((Vars) this.getApplication()).leftList.get(item.getType()+5).setName(item.getConnectionName());
			if (id < 0) {
				((Vars) MainActivity.this.getApplication()).tempListItem.setName(item.getConnectionName());
//				model.tempListItem.setName(item.getConnectionName());
			} else {
				((Vars) MainActivity.this.getApplication()).leftList.get(id+ 5).setName(item.getConnectionName());
//				model.getLeftList().getValue().get(id+ 5).setName(item.getConnectionName());
			}
		}
		if (id < 0) {
			((Vars) MainActivity.this.getApplication()).leftList.add(
//			model.getLeftList().getValue().add(
					((Vars) MainActivity.this.getApplication()).tempListItem
//					model.tempListItem
			);
			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//			model.leftAdapter.notifyDataSetChanged();
		}
		ConnectionItem connectionItem = new ConnectionItem(0, 0, item.getConnectionName(),
				domain.getText().toString(), scheme, uname.getText().toString(), pword.getText().toString(),
				hname.getText().toString(),
				(portn.getText() == null || portn.getText().toString().equals("")) ? 0
						: Integer.parseInt(portn.getText().toString()),
				share.getText().toString(), ipath.getText().toString());
		String connStr = connectionItem.toConnectionString();
/*		String connStr = Tools.makeConnString(scheme, domain.getText().toString(), uname.getText().toString(),
				pword.getText().toString(), hname.getText().toString(), share.getText().toString(),
				ipath.getText().toString());*/
/*		String auth = "";
		if (!domain.getText().toString().equals("")) {
			auth += domain.getText().toString();
			auth += ":";
		}
		Uri.Builder builder = new Uri.Builder();
		builder.scheme(item.getScheme())
				.encodedAuthority(item.getUsername()+ ":"+ item.getPassword()+ "@"+
						item.getHost()+ ":"+ item.getPort())
				.path(item.getShareName()+ item.getPath())
				.build();
//		Uri uri = builder.build();
		Log.d(TAG, "uri = "+ builder.toString());
		String connStr = Tools.makeConnString((domain==null) ? "" : domain.getText().toString(),
				(uname==null) ? "" : uname.getText().toString(),
				(pword==null) ? "" : pword.getText().toString(),
				(hname==null) ? "" : hname.getText().toString(),
				(share==null) ? "" : share.getText().toString(),
				(ipath==null) ? "" : ipath.getText().toString());*/
		Log.d(TAG, "connection string = "+ connStr);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if (id < 0) {
			editor.putInt(getString(R.string.num_conn), ++Vars.NUM_CONN);
			id = Vars.NUM_CONN;
			Log.d(TAG, "saving new connection "+ Vars.NUM_CONN);
			editor.putInt(getString(R.string.conn_type) + Vars.NUM_CONN, item.getType() > 0 ?
//				item.getType() == CONNECTION_TYPE_FTP ?
					item.getType() == Vars.FTP_COMM ?
							Vars.FTP_COMM : Vars.SMB_CONN : Vars.SMB_CONN);
		}
		Log.d(TAG, "AlertDialogPositiveButtonPressed editor: id = "+ id);
		editor.putString(getString(R.string.display_name) + id, item.getConnectionName());
		Log.d(TAG, "AlertDialogPositiveButtonPressed editor: "+
				getString(R.string.display_name)+ " = "+ item.getConnectionName());
		editor.putString(getString(R.string.token) + id, "");
		String encString = "";
		try {
			encString = AESUtils.encrypt(connStr);
			Log.d(TAG, "AlertDialogPositiveButtonPressed editor: "+
					getString(R.string.conn_str)+ " = "+ connStr+ " ("+ encString+ ")");
			editor.putString(getString(R.string.conn_str) + id, encString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		editor.apply();
		Log.d(TAG, "AlertDialogPositiveButtonPressed saving connection: "+ item.toString());
		((Vars)this.getApplication()).addConnectionItem(item);
//		model.addConnectionItem(item);
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
/*
	private void initSourceList() {
		((Vars)getApplication()).addSourceItem(new ListItem(

		))
	}
/**/
	void SetupSharedPrefs() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
/**/
		SharedPreferences.Editor editor = sharedPreferences.edit();
//		editor.remove(getString(R.string.display_name)+"2").apply();
//		editor.remove(getString(R.string.conn_type)+"2").apply();
//		editor.remove(getString(R.string.token)+"2").apply();
//		editor.remove(getString(R.string.conn_str)+"2").apply();
/*		editor.remove("number of connections");
		editor.remove("connection string2");
		editor.remove("connection_id2");
		editor.remove("connection_id1");
		editor.remove("connection string1");*/
//		editor.putString(getString(R.string.token)+"1", "");
//		editor.putInt(getString(R.string.conn_type)+"1", Vars.SMB_CONN);
//		editor.putString(getString(R.string.display_name)+"1", "Ultra-WIN::2019").apply();
//		editor.putInt("connection_type2", Vars.ONED_CONN);
//		editor.putInt(getString(R.string.conn_type)+"1", Vars.SMB_CONN).apply();
//		editor.putString(getString(R.string.token)+"1", "").apply();
//		editor.putString(getString(R.string.conn_str)+"1", "C5683199190A0C10C970387DF16EF66F6B82DB723499E96EEAFD9D55A6C92C48D5D65C6B470443F48A7374D1876AA73A").apply();
//		editor.putInt(getString(R.string.num_conn), 1).apply();
//		sharedPreferences.edit().putInt(getString(R.string.conn_type)+"2", Vars.ONED_CONN).apply();
//		sharedPreferences.edit().putString(getString(R.string.token)+"1", "").apply();
		editor.apply();
/**/
//		sharedPreferences.edit().clear().apply();
		Map<String, ?> map = sharedPreferences.getAll();
		Log.d(TAG, "DUMP ALL sharedPreferences: (line:"+ Tools.getLineNumber()+ ")");
		Log.d(TAG, "---------------------------");
		for (String str : map.keySet()) {
			Object value = map.get(str);
			if (value == null)
				Log.d(TAG, "sharedPreferences \""+ str+ "\" = null");
			else
				Log.d(TAG, "sharedPreferences \""+ str+ "\" = \""+ value.toString()+ "\"");
		}
		Log.d(TAG, "---------------------------");
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
		Log.d(TAG, "onSaveInstanceState ("+ Tools.getLineNumber()+ ")");
/*		outState.putSerializable("leftList", ((Vars)this.getApplication()).leftList);
//		outState.putSerializable("leftList", model.getLeftList().getValue());
		outState.putSerializable("conns", ((Vars)this.getApplication()).getConnectionItems());
//		outState.putSerializable("conns", model.getConnectionItems().getValue());
*/
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
//		if (mAudioInputReader != null) {
//			mAudioInputReader.shutdown(isFinishing());
//		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
//		if (alertDialogBuilderActWide != null) {
//			AlertDialog alertDialog = alertDialogBuilderActWide.create();
//			if (alertDialog.isShowing())
//				alertDialog.dismiss();
//		}
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
		Log.d(TAG, "onDestroy");
		if (alertDialogBuilderActWide != null) {
			AlertDialog dialog = alertDialogBuilderActWide.create();
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
		PreferenceManager.getDefaultSharedPreferences(this)
				.unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
//		((Vars)getApplication()).leftList.clear();
//		model.getLeftList().getValue().clear();
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
//					doOnRightPanel(RightFragment.newInstance(getString(R.string.no_perm_audio), null, ""));
				} else {
					Toast.makeText(this, getString(R.string.no_perm_audio), Toast.LENGTH_LONG).show();
					finish();
					// The permission was denied, so we can show a message why we can't run the app
					// and then close the app.
				}
				break;
			}
			case MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE: {
//				doListMusic();
				doOnRightPanel(RightFragment.newInstance(getString(R.string.music), null, ""));
				break;
			}
			case MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE: {
//				doListPhotos();
				doOnRightPanel(RightFragment.newInstance(getString(R.string.photos), null, ""));
				break;
			}
			case MY_PERMISSION_RECORD_WRITE_CONTACTS_REQUEST_CODE:
			case MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE: {
//				doListContacts();
				doOnRightPanel(RightFragment.newInstance(getString(R.string.contacts), null, ""));
				break;
			}
//			case MY_PERMISSION_RECORD__REQUEST_CODE: {
//				Log.d(TAG, "onRequestPermissionsResult: " + requestCode);
//			}
			default:
				Log.d(TAG, "onRequestPermissionsResult-Unexpected value: "+ requestCode+
						Tools.getLineNumber()+ ")");
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
			case (RC_SIGN_IN):
				Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
				try {
					accountGoogle = task.getResult(ApiException.class);
//					GoogleSignInAccount account = task.getResult(ApiException.class);
					if (accountGoogle == null) {
//					if (account == null) {
						Log.w(TAG, "getGooleAccount() signInResult:failed.");
					} /*else useGoodleAccount(account);*/
				} catch (ApiException e) {
					// Please refer to the GoogleSignInStatusCodes class reference for more information.
					Log.w(TAG, "getGooleAccount() signInResult:failed code=" + e.getStatusCode());
				}
				break;
		}
	}

	private void useGoodleAccount() {
//	private void useGoodleAccount(GoogleSignInAccount account) {
		GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
				this, Collections.singleton(DriveScopes.DRIVE_FILE));
		credential.setSelectedAccount(accountGoogle.getAccount());
//		credential.setSelectedAccount(account.getAccount());
		com.google.api.services.drive.Drive googleDriveService =
				new com.google.api.services.drive.Drive.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), credential)
						.setApplicationName(getString(R.string.app_name))
						.build();
		DriveServiceHelper driveServiceHelper = new DriveServiceHelper(googleDriveService);

		List<GoogleDriveFileHolder> list = new ArrayList<>();
//				list = driveServiceHelper.queryFiles();
/*
		Toast.makeText(this, "Launching Ammarptn file explorer for GDrive", Toast.LENGTH_LONG).show();
		Intent openActivity = new Intent(this, GDriveDebugViewActivity.class);
		startActivity(openActivity);
*/
		// ^ Ammarptn file explorer for GDrive
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
/*
		Log.d(TAG, "LongPressedLeftItemIndex on "+ index);
		boolean editFaded = index < 5;
		Log.d(TAG, "items="+ ((Vars)this.getApplication()).getConnectionItems().toString());
		if (index > 4) {
//			Log.d(TAG, "looking for "+ id);
			final ConnectionItem item = ((Vars)this.getApplication()).getConnectionItemByID(index-5);
			if (item != null) {
				Log.d(TAG, "item=" + item.toString());
				if (!editFaded) {
					final int type = item.getType();
					editFaded = type != CONNECTION_TYPE_FTP && type != CONNECTION_TYPE_SMB;
				}
			}
		}
		AlertDialog.Builder contextmenuBuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
//		ListItem leftListItem = ((Vars)MainActivity.this.getApplication()).getLeftListItemByID(id);
		ListItem leftListItem = ((Vars)MainActivity.this.getApplication()).leftList.get(index);
//		Log.d(TAG, "LongPressedLeftItemId leftListItem="+ leftListItem.toString());
		String itemName = leftListItem.getName();
//		String itemName = ((Vars)this.getApplication()).leftList.get(index).getName();
		contextmenuBuilder.setTitle(itemName);
		ArrayList<ListItem> items = new ArrayList<>();//fillAddConnections();
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.connto)), null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_ellipsis_h)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				false));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.rename, "")),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_newspaper1)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				index < 5));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.edit_conn)),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_edit1)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				editFaded));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.remove)),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_eraser)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				index < 5));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.move)),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_arrows_alt)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				((Vars)MainActivity.this.getApplication()).leftList.size() < 6));

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
			if (index > 4) {
				//						addConnectionDialog.dismiss();
				contextmenuDialog.cancel();
				String name = item1.getName();
				//				int id = ((Vars) MainActivity.this.getApplication()).getConnectionItems().size();
				//				item1.setType(((Vars) MainActivity.this.getApplication()).leftList.size() + 1);
				MainActivity.this.contextmenuSelection(name, index);
			}
		});
		ListView listView = customView.findViewById(R.id.lv_dialog_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener((parent, view, position, itemId) -> contextmenuDialog.dismiss());
*/
	}

	@Override
	public void LongPressedLeftItemIndex(int index) {
		Log.d(TAG, "LongPressedLeftItemIndex on "+ index);
		boolean editFaded = index < Vars.NUM_THIS_DEVICE;
//		Log.d(TAG, "items="+ model.getConnectionItems().getValue().toString());
		if (index > (Vars.NUM_THIS_DEVICE-1)) {
			Log.d(TAG, "items="+ ((Vars)this.getApplication()).getConnectionItems().toString());
//			Log.d(TAG, "looking for "+ id);
			final ConnectionItem item = ((Vars)this.getApplication()).getConnectionItemByID(index-Vars.NUM_THIS_DEVICE);
//			final ConnectionItem item = model.getConnectionItemByID(index-4);
			if (item != null) {
				Log.d(TAG, "item=" + item.toString());
				if (!editFaded) {
					final int type = item.getType();
//					editFaded = type != CONNECTION_TYPE_FTP && type != CONNECTION_TYPE_SMB;
					editFaded = type != Vars.FTP_COMM && type != Vars.SMB_CONN;
				}
			}
		}
		alertDialogBuilderActWide = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
//		AlertDialog.Builder contextmenuBuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
//		ListItem leftListItem = ((Vars)MainActivity.this.getApplication()).getLeftListItemByID(id);
		ListItem leftListItem = ((Vars)MainActivity.this.getApplication()).leftList.get(index);
//		ListItem leftListItem = model.getLeftList().getValue().get(index);
//		Log.d(TAG, "LongPressedLeftItemId leftListItem="+ leftListItem.toString());
		String itemName = leftListItem.getName();
//		String itemName = ((Vars)this.getApplication()).leftList.get(index).getName();
		alertDialogBuilderActWide.setTitle(itemName);
//		contextmenuBuilder.setTitle(itemName);
		ArrayList<ListItem> items = new ArrayList<>();//fillAddConnections();
		items.add(((Vars)getApplication()).getSourceItemById(Vars.CONN_TO));
		items.add(((Vars)getApplication()).getSourceItemById(Vars.RENAME_CONN));
		items.add(((Vars)getApplication()).getSourceItemById(Vars.EDIT_CONN));
		items.add(((Vars)getApplication()).getSourceItemById(Vars.REMOVE_CONN));
		items.add(((Vars)getApplication()).getSourceItemById(Vars.MOVE_CONN));
/*		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.connto)), null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_ellipsis_h)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				false));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.rename, "")),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_newspaper1)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				index <= (Vars.NUM_THIS_DEVICE-1)));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.edit_conn)),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_edit1)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				editFaded));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.remove)),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_eraser)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				index <= (Vars.NUM_THIS_DEVICE-1)));
		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.move)),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_arrows_alt)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, this), Tools.dpToPx(16, this),
				Tools.dpToPx(18, this), true, Tools.dpToPx(8, this),
				index < (Vars.NUM_THIS_DEVICE-1) && ((Vars)MainActivity.this.getApplication()).leftList.size() < Vars.NUM_THIS_DEVICE));
//				index < 4 && model.getLeftList().getValue().size() < 5));
*/
		final View customView = View.inflate(this, R.layout.dialog_list, null);
		customView.setPadding(Tools.dpToPx(8, this), Tools.dpToPx(10, this),
				Tools.dpToPx(8, this), Tools.dpToPx(10, this));
		alertDialogBuilderActWide.setView(customView);
//		contextmenuBuilder.setView(customView);

		final AlertDialog contextmenuDialog = alertDialogBuilderActWide.create();
//		final AlertDialog contextmenuDialog = contextmenuBuilder.create();
		contextmenuDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
				(dialog1, which) -> dialog1.cancel());
//				addConnectionDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), (DialogInterface.OnClickListener) null);
		contextmenuDialog.setCanceledOnTouchOutside(false);
		contextmenuDialog.show();
		CustomDialogListAdapter adapter = new CustomDialogListAdapter(this,
				R.layout.dialog_list, items, item1 -> {
	//			if (index > 4) {
					//						addConnectionDialog.dismiss();
					contextmenuDialog.cancel();
					String name = item1.getName();
					//				int id = ((Vars) MainActivity.this.getApplication()).getConnectionItems().size();
					//				item1.setType(((Vars) MainActivity.this.getApplication()).leftList.size() + 1);
					MainActivity.this.contextmenuSelection(name, index);
	//			}
				});
		ListView listView = customView.findViewById(R.id.lv_dialog_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener((parent, view, position, itemId) -> contextmenuDialog.dismiss());
	}


	void contextmenuSelection(String name, int index) {
		Log.d(TAG, "contextmenuSelection: name="+ name+ ", index="+ index);
//		ListItem leftListItem = ((Vars)MainActivity.this.getApplication()).getLeftListItemByID(index+1);
		ListItem leftListItem = ((Vars)MainActivity.this.getApplication()).leftList.get(index);
//		ListItem leftListItem = model.getLeftListItemByID(index);
//		int connId = leftListItem.getType() - Vars.NUM_CONN;
		int connId = max(0, index - Vars.NUM_THIS_DEVICE);
		if (leftListItem != null)
			Log.d(TAG, "contextmenuSelection: item="+ leftListItem.toString());
		else
			Log.d(TAG, "contextmenuSelection: item is null!");
		if (name.toUpperCase().equals(getString(R.string.delete).toUpperCase())) {
//		if (name.toUpperCase().equals(getString(R.string.remove).toUpperCase())) {
			if (index > 3) {
				AlertDialog.Builder rusureDialog = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
				rusureDialog.setTitle(getString(R.string.rusure));
//			String itemName = ((Vars)MainActivity.this.getApplication()).leftList.get(id-1).getName();
				String itemName = leftListItem.getName();
				rusureDialog.setMessage(getString(R.string.delete_conn_x, itemName));
				rusureDialog.setNegativeButton(getString(android.R.string.cancel), null);
				rusureDialog.setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
//				((Vars) MainActivity.this.getApplication()).leftList.remove(id-1);
					((Vars) MainActivity.this.getApplication()).leftList.remove(index);
//					((Vars) MainActivity.this.getApplication()).leftList.remove(leftListItem.getType() - 1);
//				model.removeLeftListItem(leftListItem.getType()-1);
					((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//				model.leftAdapter.notifyDataSetChanged();
					if (index > 4) {
//					if (leftListItem.getType() > 4) {
						((Vars) MainActivity.this.getApplication()).removeConnectionItem(connId);
//					model.removeConnectionItem(leftListItem.getType() - 5);
//						((Vars) MainActivity.this.getApplication()).rightAdapter.notifyDataSetChanged();
						Vars.NUM_CONN--;
						sharedPreferences.edit().putInt(getString(R.string.num_conn), Vars.NUM_CONN).apply();
						sharedPreferences.edit().remove(getString(R.string.conn_str)+ (connId+1)).apply();
//						sharedPreferences.edit().remove(getString(R.string.conn_str)+leftListItem.getType()).apply();
						sharedPreferences.edit().remove(getString(R.string.token)+ (connId+1)).apply();
//						sharedPreferences.edit().remove(getString(R.string.token)+leftListItem.getType()).apply();
						sharedPreferences.edit().remove(getString(R.string.display_name)+ (connId+1)).apply();
//						sharedPreferences.edit().remove(getString(R.string.display_name)+leftListItem.getType()).apply();
						sharedPreferences.edit().remove(getString(R.string.conn_type)+ (connId+1)).apply();
//						sharedPreferences.edit().remove(getString(R.string.conn_type)+leftListItem.getType()).apply();
					}
				});
				rusureDialog.show();
			}
		} else if (name.toUpperCase().equals(getString(R.string.rename, "").toUpperCase())) {
//			String itemName = ((Vars)MainActivity.this.getApplication()).leftList.get(index-1).getName();
			String itemName = leftListItem.getName();
//			String itemName = model.getLeftList().getValue().get(index-1).getName();
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
						((Vars) MainActivity.this.getApplication()).leftList.get(index).setName(value.getText().toString());
//						model.getLeftList().getValue().get(index-1).setName(value.getText().toString());
						((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//						model.leftAdapter.notifyDataSetChanged();
						sharedPreferences.edit().putString(
								getString(R.string.display_name)+connId, value.getText().toString())
								.apply();
					});
			renameDialog.show();
		} else if (name.toUpperCase().equals(getString(R.string.move).toUpperCase())) {
		} else if (name.toUpperCase().equals(getString(R.string.connto).toUpperCase())) {
//			SelectedLeftItemId(id);
			SelectedLeftItemIndex(index);
		} else if (name.toUpperCase().equals(getString(R.string.edit).toUpperCase())) {
			if (index > (Vars.NUM_THIS_DEVICE-1)) {
				ConnectionItem item = ((Vars) MainActivity.this.getApplication()).getConnectionItemByID(index-5);
//			ConnectionItem item = model.getConnectionItemByID(index-5);
				Log.d(TAG, "editing:" + item.toString());
//			showFTPsettings(item.getType(), item);
				showFTPsettings(item.getID(), 0);
			}
		}
	}


	@Override
	public void SelectedLeftItemId(int id) {
		/*
		switch (index) {
			case Vars.MY_PHOTOS_ID:
				if (!Permissons.Check_STORAGE(this)) {
//					Permissons.Request_STORAGE(this, MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					}
				} else {
					doListPhotos();
				}
				break;
			case Vars.MY_MUSIC_ID:
				if (!Permissons.Check_STORAGE(this)) {
//					Permissons.Request_STORAGE(this, MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					}
				} else {
					doListMusic();
				}
				break;
			case Vars.MY_CONTACTS_ID:
				if (!Permissons.Check_READ_CONTACTS(this)) {
//					Permissons.Request_READ_CONTACTS(this, MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE);
					}
				} else {
					doListContacts();
				}
				break;
			case Vars.MY_FILES_ID:
				doListFiles();
				break;
			default:
//				if ();
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
				ConnectionItem item = ((Vars)this.getApplication()).getConnectItem(index);
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
		*/
	}

	/**
	 * Draw a list of files on right panel
	 */
/*	void doListFiles() {
		FragmentManager manager = getFragmentManager();
		Fragment fragment;
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.addToBackStack(null);
		fragment = RightFragment.newInstance(getString(R.string.files), "/", "");
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

	private void doListContacts() {
		FragmentManager manager = getFragmentManager();
		Fragment fragment;
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.addToBackStack(null);
		fragment = RightFragment.newInstance(getString(R.string.contacts), null, "");
		transaction.replace(R.id.fl_right_panel, fragment);
		transaction.commit();
	}

	private void doListHosts() {
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
*/
	private void doOnRightPanel(Fragment fragment) {
		FragmentManager manager = getFragmentManager();
//		Fragment fragment;
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.addToBackStack(null);
//		fragment = RightFragment.newInstance(getString(R.string.photos), null, "");
		transaction.replace(R.id.fl_right_panel, fragment);
		transaction.commit();
	}


	@Override
	public void SelectedLeftItemIndex(int index) {
		Log.d(TAG, "selected left item with index:"+ index);
		ListItem listItem = ((Vars)this.getApplication()).leftList.get(index);
		Log.d(TAG, "selected left item:"+ listItem.toString());
		switch (listItem.getType()) {
			case Vars.MY_PHOTOS_ID:
				if (!Permissons.Check_STORAGE(this)) {
//					Permissons.Request_STORAGE(this, MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					}
				} else {
					doOnRightPanel(RightFragment.newInstance(getString(R.string.photos), null, ""));
				}
				break;
			case Vars.MY_MUSIC_ID:
				if (!Permissons.Check_STORAGE(this)) {
//					Permissons.Request_STORAGE(this, MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					}
				} else {
					doOnRightPanel(RightFragment.newInstance(getString(R.string.music), null, ""));
				}
				break;
			case Vars.MY_CONTACTS_ID:
				if (!Permissons.Check_READ_CONTACTS(this)) {
//					Permissons.Request_READ_CONTACTS(this, MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE);
					}
				} else {
					doOnRightPanel(RightFragment.newInstance(getString(R.string.contacts), null, ""));
				}
				break;
			case Vars.MY_FILES_ID:
				doOnRightPanel(RightFragment.newInstance(getString(R.string.files), "/", ""));
				break;
			case Vars.MY_LOCAL_HOSTS:
				doOnRightPanel(RightFragment.newInstance(getString(R.string.my_network), null, ""));
				break;
			case Vars.ONED_CONN:
				doOnRightPanel(RightFragment.newInstance(getString(R.string.onedrive),
						String.valueOf(index), ""));
				break;
			case Vars.GDRIVE_CONN:
//				GoogleSignInAccount account =
				useGoodleAccount();
				break;
			default:
				if (index >= Vars.NUM_THIS_DEVICE)
					handle(index);
				else
					Log.d(TAG, "index is not in bounds");
		}
	}

	private void handle(int index) {
		Log.d(TAG, "handle("+ index+ ")");
		ConnectionItem item1 /*= new ConnectionItem()*/;
		int itemId = index - Vars.NUM_THIS_DEVICE /*- 1*/;
		if (itemId < 0 || itemId > ((Vars)getApplication()).getConnectionItems().size()) {
				Log.d(TAG, "itemId is not in bounds: "+ itemId);
				return;
			}
//		Log.d(TAG, "getting connection item: "+ itemId);
		item1 = ((Vars) this.getApplication()).getConnectItem(itemId);
		if (item1 != null) {
			Log.d(TAG, "selected connection item: "+ item1.toString());
		} else {
			Log.d(TAG, "handle() Error getting selected item - null!");
			return;
		}
		final ConnectionItem item = item1;
//		Log.d(TAG, "Connection Items:"+ ((Vars)this.getApplication()).getConnectionItems().toString());
		switch (item.getType())
		{
			case Vars.SMB_CONN:
				handleSMB(item);
				break;
			case Vars.FTP_COMM:
				handleFTP(item);
				break;
			case Vars.BOX_CONN:
				break;
			case Vars.ICLOUD_CONN:
				break;
			case Vars.DBOX_CONN:
				break;
		}
	}

	private void handleFTP(ConnectionItem item) {
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
//					ftPoperation.connectFTP(this, "https://google.com/", "", "", "");
		ftPoperation.connectFTP(this, item);
//		} else {
//			//handle gdrive, onedrive, ...
//			handle(item);
//		}
		/**/
//				ConnectionItem item = ((Vars)this.getApplication()).getConnectItem(index-4);
/*				if (item != null) {
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
				}*/
	}


	private void handleSMB(ConnectionItem item) {
		Log.d(TAG, "selectedLeftItem handleSMB() connStr: "+ item.toConnectionString());
		doOnRightPanel(RightFragment.newInstance(getString(R.string.smb_share),
				item.toConnectionString(),
				""));
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
//			if (alertDialogBuilderActWide != null) {
				alertDialogBuilderActWide = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
//			AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
				alertDialogBuilderActWide.setTitle(getString(R.string.warning));
				alertDialogBuilderActWide.setMessage(getString(R.string.mobile_network));
				alertDialogBuilderActWide.setCancelable(false);
				alertDialogBuilderActWide.setCursor(null, null, "");
				alertDialogBuilderActWide.setNegativeButton(R.string.ignore, (dialog, which) -> dialog.dismiss());
				alertDialogBuilderActWide.setPositiveButton(R.string.close, (dialog, which) -> {
					Tools.exitApplication(this);
/*					moveTaskToBack(true);
					if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
						finishAndRemoveTask();
					} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						this.finishAffinity();
					} else {
						Process.killProcess(Process.myPid());
						System.exit(1);
					}*/
				});
//				alertDialogBuilderActWide.show();
			AlertDialog alertDialog = alertDialogBuilderActWide.create();
			alertDialog.show();
//			}
		}
	}

	@Override
	public void NetworkConnected(boolean isConnected) {
		if (!isConnected) {
			((Vars)this.getApplication()).setNetworkConnected(false);
//			model.setNetworkConnected(false);
			alertDialogBuilderActWide = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
			alertDialogBuilderActWide.setTitle(getString(R.string.warning));
			alertDialogBuilderActWide.setMessage(getString(R.string.no_network));
			alertDialogBuilderActWide.setCancelable(false);
			alertDialogBuilderActWide.setCursor(null, null, "");
			alertDialogBuilderActWide.setNegativeButton(R.string.ignore, (dialog, which) -> dialog.dismiss());
			alertDialogBuilderActWide.setPositiveButton(R.string.close, (dialog, which) -> {
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
//			alertDialogBuilderActWide.show();
			AlertDialog dialog = alertDialogBuilderActWide.create();
			dialog.show();
		}
	}


	@NonNull
	@Override
	public Loader<Session> onCreateLoader(int id, @Nullable Bundle args) {
		return new AsyncTaskLoader<Session>(this) {
			Session chachedSession = null;


			@Nullable
			@Override
			public Session loadInBackground() {
				SMBClient client = new SMBClient();

				try (Connection connection = client.connect(args.getString("host"))) {
					AuthenticationContext ac = new AuthenticationContext(args.getString("username"),
							args.getString("password").toCharArray(),
							args.getString("domain"));
					Session session = connection.authenticate(ac);

					// Connect to Share
					try (DiskShare share = (DiskShare) session.connectShare(args.getString("domain"))) {
						for (FileIdBothDirectoryInformation f : share.list(args.getString("domain"),
								args.getString("domain"))) {
							System.out.println("File : " + f.getFileName());
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
/*				try {
					NetworkUtils.getResponseFromHttpUrl(new URL("ftp:", "xo3.x10hosting.com", 0, "/"))
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}*/
				return null;
			}

			@Override
			protected void onStartLoading() {
				if (chachedSession != null) {
					//To skip loadInBackground call
					deliverResult(chachedSession);
				} else {
					if (args != null) {
						forceLoad();
					}
				}
			}

			@Override
			public void deliverResult(@Nullable Session data) {
				chachedSession = data;
				super.deliverResult(data);
			}
		};
	}

	@Override
	public void onLoadFinished(@NonNull Loader<Session> loader, Session data) {
	}

	@Override
	public void onLoaderReset(@NonNull Loader<Session> loader) {
	}

	private void makeOperationSMB(String host, String domain, String username, String password,
	                              String shareName, String path) {
		Bundle bundle = new Bundle();
		if (host.equals("")) {
			return;
		} else {
			bundle.putString("host", host.trim());
		}
//		bundle.putInt("port", 0);
		if (!domain.equals("")) {
			bundle.putString("domain", domain.trim());
		}
		if (!username.equals("")) {
			bundle.putString("username", username.trim());
		}
		if (!password.equals("")) {
			bundle.putString("password", password.trim());
		}
		if (!shareName.equals("")) {
			bundle.putString("shareName", shareName.trim());
		}
		if (!path.equals("")) {
			bundle.putString("path", path.trim());
		} else {
			bundle.putString("path", "/");
		}
		LoaderManager loaderManager = getSupportLoaderManager();
//		LoaderManager loaderManager = LoaderManager.getInstance(this).initLoader(OPERATION_SMB_LOADER, bundle, this).forceLoad();
		Loader<Session> loader = loaderManager.getLoader(OPERATION_SMB_LOADER);
		if (loader == null) {
			loaderManager.initLoader(OPERATION_SMB_LOADER, bundle, this);
		} else {
			loaderManager.restartLoader(OPERATION_SMB_LOADER, bundle, this);
		}
	}

	private void makeOperationSMB(ConnectionItem item) {
		Bundle bundle = new Bundle();
		if (item == null) {
			return;
		}
		if (item.getHost().equals("")) {
			return;
		} else {
			bundle.putString("host", item.getHost().trim());
		}
//		bundle.putInt("port", 0);
		if (!item.getAccessToken().equals("")) {
			bundle.putString("domain", item.getAccessToken().trim());
		}
		if (!item.getUsername().equals("")) {
			bundle.putString("username", item.getUsername().trim());
		}
		if (!item.getPassword().equals("")) {
			bundle.putString("password", item.getPassword().trim());
		}
		if (!item.getPath().equals("") && !item.getPath().equals("/")) {
			ArrayList<String> list = new ArrayList<>();
			String[] separated = item.getPath().split("/");
			for (String p : separated) {
				if (!p.equals(""))
					continue;
				list.add(p.trim());
			}
			if (list.size() > 0) {
				bundle.putString("shareName", list.get(0));
			}
			StringBuilder path = new StringBuilder();
			for (int i = 1; i < list.size(); i++) {
				path.append(list.get(i)).append("/");
			}
			bundle.putString("path", path.toString());
		}
		LoaderManager loaderManager = getSupportLoaderManager();
		Loader<Session> loader = loaderManager.getLoader(OPERATION_SMB_LOADER);
		if (loader == null) {
			loaderManager.initLoader(OPERATION_SMB_LOADER, bundle, this);
		} else {
			loaderManager.restartLoader(OPERATION_SMB_LOADER, bundle, this);
		}
	}
/*
	//SearchView.OnQueryTextListener
	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	//SearchView.OnQueryTextListener
	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}
*/

	/* MS OneDrive */
//	final MSAAuthenticator msaAuthenticator = new MSAAuthenticator() {
//		@Override
//		public String getClientId() {
//			return getString(R.string.msa_client_id);
//		}
//
//		@Override
//		public String[] getScopes() {
//			return new String[] { getString(R.string.directory_tenant_id) };
//		}
//	}
//
//	final ADALAuthenticator adalAuthenticator = new ADALAuthenticator() {
//		@Override
//		public String getClientId() {
//			return getString(R.string.adal_client_id);
//		}
//
//		@Override
//		protected String getRedirectUrl() {
//			return getString(R.string.aad_redirectUriGraph);
//		}
//	}
}

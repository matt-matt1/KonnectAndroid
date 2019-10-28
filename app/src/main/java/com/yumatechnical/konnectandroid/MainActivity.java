package com.yumatechnical.konnectandroid;
//INUSE
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v1.DbxEntry;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
//import com.jakewharton.rxbinding3.view.RxView;
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
import com.yumatechnical.konnectandroid.Helper.Network.LocalNetwork;
import com.yumatechnical.konnectandroid.Helper.Network.SMBoperation;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.Helper.URI_to_Path;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;
import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.Settings.SettingsActivity;

import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity
		implements /**/SharedPreferences.OnSharedPreferenceChangeListener,/**/
		/**/Preference.OnPreferenceChangeListener,/**/
		LeftArrayAdapter.OnListener,
		LeftItemFragment.OnLeftListFragmentInteractionListener,
		LoaderManager.LoaderCallbacks<Session>,
		LocalNetwork.ConnectionInfoTask.OnNetworkConnectionInfo,
		MyDialogFragment.OnMyDialogInteraction {

	FrameLayout left, right, base;
	public static final int OPERATION_SMB_LOADER = 22;
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
//	private AlertDialog alertDialog;
	private View alertViewFTP;
//	private MyViewModel model;
	private AlertDialog.Builder alertDialogBuilderActWide;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "onCreate");

		modelOperation();
/*		if (savedInstanceState != null) {
			((Vars)this.getApplication()).leftList =
					(ArrayList<ListItem>)savedInstanceState.getSerializable("leftList");
			((Vars)this.getApplication()).setConnectionItems(
					(ArrayList<ConnectionItem>)savedInstanceState.getSerializable("conns"));
		}*/
//		mySharedPreferencesActivity.SetupSharedPrefs();
		SetupSharedPrefs();

		new LocalNetwork.ConnectionInfoTask(getApplicationContext(), this).execute();
		testCustomDialog();
		left = findViewById(R.id.left_frame);
		right = findViewById(R.id.fl_right_panel);
		base = findViewById(R.id.base_frame);
		setLeftList();
		// Get current account info
//		FullAccount account = DropboxInstance.main().users().getCurrentAccount();
//		System.out.println(account.getName().getDisplayName());
/**/
		testSMB();
//		testFTP();
/**/
		getSupportLoaderManager().initLoader(OPERATION_SMB_LOADER, null, this);

//		screenDimentions();
//		openCloseLeftPanel(false);
//		openCloseLeftPanel(true);
	}

	private void screenDimentions() {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.e("Width", "" + width);
		Log.e("height", "" + height);
	}

	private void testFTP() {
		FTPoperation ftPoperation = new FTPoperation();
		ftPoperation.connectFTP("xo3.x10hosting.com",
				"yumax10h",
				"suguna24",
//				"/public_html");
				"/");
//				"");
//		ftPoperation.connectFTP("yumatechnical.com",
//				"yumatechnicalcom",
//				"Suguna24!@",
//				"");
		ftPoperation.setListener(result -> {
//			Log.d(TAG, "FTP connection complete");
			if (result != null) {
				ftPoperation.getFTPstatus(result, status -> Log.d(TAG, "FTP status: "+ status));

				ftPoperation.getFTPDirsAndFiles(result, new FTPoperation.OnFTPlistResult() {
					@Override
					public void listedDirs(FTPFile[] dirs) {
						for (FTPFile dir : dirs) {
							Log.d(TAG, "FTP dir: "+ dir.getName());
						}
					}

					@Override
					public void listedFiles(FTPFile[] files) {
						for (FTPFile file : files) {
							Log.d(TAG, "FTP file: "+ file.getName());
						}
					}
				});
/*
				try {
					Log.d(TAG, "FTP list: "+ result.list());
				} catch (IOException e) {
					e.printStackTrace();
				}
*/
			}
		});
	}

	private void testSMB() {
	/*		LoaderSMB smb = new LoaderSMB(this, new LoaderSMB.OnSMBinteraction() {
				@Override
				public void OnResult(Session result) {
					Log.d(TAG, "result="+ result.getSessionId()+ ":"+ result.getConnection().getRemoteHostname());
				}
			});*/
/*		SMBoperation smb = new SMBoperation(result -> {
			if (result != null) {
				Log.d(TAG, "result="+ result.getSessionId()+ ":"+ result.getConnection().getRemoteHostname());
//					Log.d(TAG, "sessionID="+ session.getSessionId());
			}
		});
		smb.connect("192.168.1.222", "yumausa", "yuma", "");*/
		class DoThreadSMB implements Runnable {
			@Override
			public void run() {
				android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

				SMBClient client = new SMBClient();

				try (
						Connection connection = client.connect("SERVERNAME")) {
					AuthenticationContext ac = new AuthenticationContext("USERNAME", "PASSWORD".toCharArray(), "DOMAIN");
					Session session = connection.authenticate(ac);

					// Connect to Share
					try (DiskShare share = (DiskShare) session.connectShare("SHARENAME")) {
						for (FileIdBothDirectoryInformation f : share.list("FOLDER", "*.TXT")) {
							System.out.println("File : " + f.getFileName());
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
//				Thread.currentThread();
			}
		}
//		DoThreadSMB smb = new DoThreadSMB();
/****/
		SMBoperation smBoperation = new SMBoperation();
//		smBoperation.configSMB(false, true, true,
//				true, 8*1024*1024, 300, TimeUnit.SECONDS,
//				300, TimeUnit.SECONDS, null);
/*		smBoperation.setTimeout(120, TimeUnit.SECONDS, 100, TimeUnit.SECONDS);
		smBoperation.connect("192.168.1.222", "yumausa", "yuma", "");
		smBoperation.listFiles("pop");
		smBoperation.setListener(new SMBoperation.OnSMBinteraction() {
			@Override
			public void OnConnection(Session result) {
			}

			@Override
			public void OnListFiles(DiskShare share) {
				if (share != null) {
					for (FileIdBothDirectoryInformation file : share.list("/")) {
						Log.d(TAG, "SMB file: " + file.getShortName());
					}
				} else {
					Log.d(TAG, "SMB filelist is null");
				}
			}
		});*/
		/****/
//		smBoperation.getSMBfileslist("192.168.1.222", "yumausa", "yuma",
//				"", "pop", "/", new SMBoperation.OnSMBinteraction2() {
//					@Override
//					public void OnListFiles2(List<String> files) {
//						if (files != null) {
//							for (String file : files) {
//								Log.d(TAG, "SMB file: " + file);
//							}
//						} else {
//							Log.d(TAG, "recieved null!");
//						}
//					}
//				});
//		BELOW STATEMENT WORKS WELL
//		smBoperation.listShares("192.168.1.222", "yumausa", "yuma",
//				"", new SMBoperation.OnSMBshares() {
//					@Override
//					public void OnListShares(ArrayList<String> shares) {
//						if (shares != null) {
//							Log.d(TAG, "SMB share count: "+ shares.size());
////							int cnt = 0;
//							for (String share : shares) {
////								Log.d(TAG, "SMB share name("+ (++cnt)+ "): " + share);
//								Log.d(TAG, "SMB share name: " + share);
//							}
//						} else {
//							Log.d(TAG, "Error: Shares is null!");
//						}
//					}
//				});
//		BELOW STATEMENT WORKS WELL
//		smBoperation.getSMBfileslist("192.168.1.222", "yumausa", "yuma",
//				"", "2019", "", new SMBoperation.OnSMBinteraction() {
//					@Override
//					public void OnListFiles(List<String> files) {
//						if (files != null) {
//							for (String file : files) {
//								Log.d(TAG, "SMB file: " + file);
//							}
//						} else {
//							Log.d(TAG, "recieved null!");
//						}
//					}
//				});
/*		smBoperation.listShares("192.168.1.29", "yuma", "yuma",
				"", new SMBoperation.OnSMBshares() {
					@Override
					public void OnListShares(ArrayList<String> shares) {
						if (shares != null) {
							Log.d(TAG, "SMB share count: "+ shares.size());
//							int cnt = 0;
							for (String share : shares) {
//								Log.d(TAG, "SMB share name("+ (++cnt)+ "): " + share);
								Log.d(TAG, "SMB share name: " + share);
							}
						} else {
							Log.d(TAG, "Error: Shares is null!");
						}
					}
				});*/
//		smBoperation.getSMBfileslist("192.168.1.29", "yuma", "yuma",
//				"", "time", "", new SMBoperation.OnSMBinteraction() {
//					@Override
//					public void OnListFiles(List<String> files) {
//						if (files != null) {
//							for (String file : files) {
//								Log.d(TAG, "SMB file: " + file);
//							}
//						} else {
//							Log.d(TAG, "recieved null!");
//						}
//					}
//				});
//		smb.run();
/*		String string = "/share/folder1/folder2/file.extension";
		String[] separated = Tools.getShareNameFromPath(string);
		Log.d(TAG, "getShareNameFromPath( "+ string+ " )--> "+ separated[0]+ " : "+ separated[1]);*/
	}

	private void testCustomDialog() {
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
	}


	private void modelOperation() {
		//		final MyViewModel viewModel = new ViewModelProvider(this).get(MyViewModel.class);
//		viewModel.getUser().getUser().observe()
//		MyViewModel model = ViewModelProviders.of(this).get(MyViewModel.class);
/*		SMBViewModel smbViewModel = ViewModelProviders.of(this).get(SMBViewModel.class);
		ArrayList<FileItem> fileItems = new ArrayList<>();
		smbViewModel.getData().observe(this, new Observer<ArrayList<FileIdBothDirectoryInformation>>() {
			@Override
			public void onChanged(ArrayList<FileIdBothDirectoryInformation> fileIdBothDirectoryInformations) {
				for (FileIdBothDirectoryInformation file : fileIdBothDirectoryInformations) {
//					boolean hasContent = file.isDirectory();
					fileItems.add(new FileItem(file.getFileName(), null, fileIdBothDirectoryInformations.size(),
							null, file.getShortName(), true, file.getShortName(),
							null, false, file.getShortName()));
				}
			}
		});*/
/*		model.getLeftList().observe(this, new Observer<ArrayList<ListItem>>() {
			@Override
			public void onChanged(ArrayList<ListItem> leftList) {
				// update UI
			}
		});*/
//		model.setIconSize(100);
	}

	/**
	 * openCloseLeftPanel
	 * opens/expands or closes/shrinks the left panel/frame
	 *
	 * @param makeOpen boolean : true to open ; false to close
	 */
	public void openCloseLeftPanel(boolean makeOpen) {
		FrameLayout fakeLeft = findViewById(R.id.fake_left_frame);
		if (makeOpen == ((Vars)getApplication()).isLeftPanelOpen())
			return;
		Log.d(TAG, makeOpen ? "opening left panel..." : "closing left panel...");
		((Vars)getApplication()).markLeftPanelOpen(makeOpen);
		LinearLayout sides = findViewById(R.id.ll_sides);
//				int leftWidth = (makeOpen) ? Tools.dpToPx(300, this) :
//						Tools.dpToPx(65, this);
		ViewGroup.LayoutParams layoutParams = fakeLeft.getLayoutParams();
		Animation animation = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
//				ViewGroup.LayoutParams layoutParams = fakeLeft.getLayoutParams();
//				int width = makeOpen
//						? layoutParams.width + 20//468
//						: layoutParams.width - 21;//468;
				int width = makeOpen
						? layoutParams.width + 468
						: layoutParams.width - 468;
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
//		inflater.inflate(R.menu.hamburger, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem menuItem;

		menuItem = menu.findItem(R.id.hamburger);
		menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_bars)
				.color(IconicsColor.colorRes(R.color.white)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		if (!((Vars)this.getApplication()).isNetworkConnected()) {
//		if (!model.isNetworkConnected()) {
			menuItem = menu.findItem(R.id.add_connect);
			menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_plus)
					.color(IconicsColor.colorRes(R.color.white)).size(IconicsSize.TOOLBAR_ICON_SIZE));
		}
		menuItem = menu.findItem(R.id.settings);
		menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_sliders_h)
				.color(IconicsColor.colorRes(R.color.white)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		menuItem = menu.findItem(R.id.resize);
		menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_arrows_alt)
				.color(IconicsColor.colorRes(R.color.white)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		return true;
	}

/**/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		Intent intent;
		switch (item.getItemId()) {
			case R.id.hamburger:
				openCloseLeftPanel(((Vars)getApplication()).isLeftPanelOpen() ? false : true);
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
				if (((Vars)getApplication()).rightAdapter != null) {
//				if (model.rightAdapter != null) {
					((Vars)getApplication()).rightAdapter.notifyDataSetChanged();
//					model.rightAdapter.notifyDataSetChanged();
					((Vars)getApplication()).recyclerView.invalidate();
//					model.recyclerView.invalidate();
					((Vars)getApplication()).recyclerView.scrollBy(0, 0);
//					model.recyclerView.scrollBy(0, 0);
				}
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
		final AlertDialog resizeDialog = resizeBuilder.create();
		resizeDialog.setTitle(getResources().getString(R.string.icon_resize));
		resizeDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
				(dialog1, which) -> dialog1.cancel());
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
		ArrayList<ListItem> items = fillAddConnections();

		AlertDialog.Builder addConnectBuilder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

		final View customView = View.inflate(this, R.layout.dialog_list, null);
		customView.setPadding(Tools.dpToPx(8, this), Tools.dpToPx(24, this),
				Tools.dpToPx(8, this), Tools.dpToPx(24, this));
		addConnectBuilder.setView(customView);
//		ProgressBar dialogSpinner = customView.findViewById(R.id.pb_dialog_spinner);
//		dialogSpinner.setVisibility(View.INVISIBLE);

		final AlertDialog addConnectionDialog = addConnectBuilder.create();
		addConnectionDialog.setTitle(Html.fromHtml("<font color='#111111'>"+ getString(R.string.addConnection)+ "</font>"));
		addConnectionDialog.setButton(AlertDialog.BUTTON_NEGATIVE,
				getResources().getString(android.R.string.cancel),
				(dialog1, which) -> dialog1.cancel());
		addConnectionDialog.setCanceledOnTouchOutside(false);
		addConnectionDialog.show();
		CustomDialogListAdapter adapter = new CustomDialogListAdapter(this,
				R.layout.dialog_list, items, item1 -> {
					addConnectionDialog.cancel();
					String name = item1.getName();
////					int id = ((Vars) MainActivity.this.getApplication()).getConnectionItems().size();
//					item1.setID(((Vars) MainActivity.this.getApplication()).leftList.size() + 1);
//					((Vars) MainActivity.this.getApplication()).leftList.add(item1);
//					((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
					MainActivity.this.selectionAddConnection(name, item1.getID(), item1);
				});
		ListView listView = customView.findViewById(R.id.lv_dialog_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener((parent, view, position, id) -> addConnectionDialog.dismiss());
	}

	private void selectionAddConnection(String name, int id, ListItem listItem) {
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
					"",
					"/");
//							ShareCompat.IntentBuilder.from(this)
//									.setChooserTitle("title")
//									.setType("mimiType")
//									.setText("textToShare");
			((Vars)getApplication()).addConnectionItem(item);
//			model.addConnectionItem(item);
			ListItem item1 = listItem;
//			ListItem item1 = new ListItem(
//					0, ((Vars) MainActivity.this.getApplication()).leftList.size() + 1, name,
//					"", null, 0,
//					0, 0, false, 0, false);
			item1.setID(((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//			item1.setID(model.getLeftList().getValue().size()/*.getLeftListItemNextID()*/);
			((Vars) MainActivity.this.getApplication()).leftList.add(item1);
//			model.getLeftList().getValue().add(item1)/*.leftList.add(item1)*/;
			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//			model.leftAdapter.notifyDataSetChanged();
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
					"onedrive.microsoft.com",
					80,
					"",
					"/");
			((Vars)getApplication()).addConnectionItem(item);
//			model.addConnectionItem(item);
			ListItem item1 = listItem;
			item1.setID(((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//			item1.setID(model.getLeftList().getValue().size()/*.getLeftListItemNextID()*/);
			((Vars) MainActivity.this.getApplication()).leftList.add(item1);
//			model.getLeftList().getValue().add(item1)/*.leftList.add(item1)*/;
			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//			model.leftAdapter.notifyDataSetChanged();
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
					"",
					"/");
			((Vars)getApplication()).addConnectionItem(item);
//			model.addConnectionItem(item);
			ListItem item1 = listItem;
			item1.setID(((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//			item1.setID(model.getLeftList().getValue().size());
			((Vars) MainActivity.this.getApplication()).leftList.add(item1);
//			model.getLeftList().getValue().add(item1);
			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//			model.leftAdapter.notifyDataSetChanged();
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
					"",
					"/");
			((Vars)getApplication()).addConnectionItem(item);
//			model.addConnectionItem(item);
			ListItem item1 = listItem;
			item1.setID(((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//			item1.setID(model.getLeftList().getValue().size());
			((Vars) MainActivity.this.getApplication()).leftList.add(item1);
//			model.getLeftList().getValue().add(item1);
			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//			model.leftAdapter.notifyDataSetChanged();
		} else if (name.equals(MainActivity.this.getString(R.string.ftp))) {
//			ListItem item1 = listItem;
//			item1.setID(((Vars) MainActivity.this.getApplication()).leftList.size() + 1);
			((Vars) MainActivity.this.getApplication()).tempListItem = listItem;
//			model.tempListItem = listItem;
			((Vars) MainActivity.this.getApplication()).tempListItem.setID(
//			model.tempListItem.setID(
					((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//					model.getLeftList().getValue().size());
//			((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
			showFTPsettings(-1, CONNECTION_TYPE_FTP);
		} else if (name.equals(MainActivity.this.getString(R.string.local_network))) {
			((Vars) MainActivity.this.getApplication()).tempListItem = listItem;
//			model.tempListItem = listItem;
			((Vars) MainActivity.this.getApplication()).tempListItem.setID(
//			model.tempListItem.setID(
					((Vars) MainActivity.this.getApplication()).getLeftListItemNextID());
//					model.getLeftList().getValue().size());
			showFTPsettings(-1, CONNECTION_TYPE_SMB);
		}
	}


	ArrayList<ListItem> fillAddConnections() {
//		Boolean fade_photos = false, fade_music = false, fade_contacts = false, fade_files = false;
//		fade_contacts = Permissons.Check_READ_CONTACTS(this);
		ArrayList<ListItem> items = new ArrayList<>();
		items.add(new ListItem(0, items.size(), getString(R.string.gdrive),null,
				Vars.gdrive_icon(this), Vars.pxFrom16dp(this), Vars.pxFrom16dp(this),
				Vars.pxFrom18dp(this), true, Vars.pxFrom8dp(this),
				false));

		items.add(new ListItem(0, items.size(), getString(R.string.onedrive),null,
				Vars.onedrive_icon2(this), Vars.pxFrom16dp(this), Vars.pxFrom16dp(this),
				Vars.pxFrom18dp(this), true, Vars.pxFrom8dp(this),
				false));

		items.add(new ListItem(0, items.size(), getString(R.string.dropbox),null,
				Vars.dropbox_icon(this), Vars.pxFrom16dp(this), Vars.pxFrom16dp(this),
				Vars.pxFrom18dp(this), true, Vars.pxFrom8dp(this),
				false));

		items.add(new ListItem(0, items.size(), getString(R.string.box),null,
				Vars.box_icon2(this), Vars.pxFrom16dp(this), Vars.pxFrom16dp(this),
				Vars.pxFrom18dp(this), true, Vars.pxFrom8dp(this),
				false));

		items.add(new ListItem(0, items.size(), getString(R.string.ftp),null,
				new IconicsDrawable(this, FontAwesome.Icon.faw_server)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Vars.pxFrom16dp(this), Vars.pxFrom16dp(this),
				Vars.pxFrom18dp(this), true, Vars.pxFrom8dp(this),
				false));

		items.add(new ListItem(0, items.size(), getString(R.string.local_network),null,
				Vars.local_network_icon(this),
				Vars.pxFrom16dp(this), Vars.pxFrom16dp(this),
				Vars.pxFrom18dp(this), true, Vars.pxFrom8dp(this),
				false));
		return items;
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
			EditText domain = alertViewFTP.findViewById(R.id.et_ftp_domain);
			View method = alertViewFTP.findViewById(R.id.connType);
			Button search_shares = alertViewFTP.findViewById(R.id.b_ftp_search_shares);
			ScrollView main = alertViewFTP.findViewById(R.id.sv_ftp);
			FrameLayout frame = alertViewFTP.findViewById(R.id.fl_ftp);

			method.setTag(type);
			title.setText(Tools.toTitleCase(getString(R.string.title)));
			hlabel.setText(Tools.toTitleCase(getString(R.string.server)));
			domlabel.setText(Tools.toTitleCase(getString(R.string.domain)));
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
			if (type == CONNECTION_TYPE_SMB) {
				radios.setVisibility(View.GONE);
				tlabel.setVisibility(View.GONE);
				portn.setVisibility(View.GONE);
				tlabel.setVisibility(View.GONE);
				domlabel.setVisibility(View.VISIBLE);
				domain.setVisibility(View.VISIBLE);
				shlabel.setVisibility(View.VISIBLE);
				search_shares.setVisibility(View.VISIBLE);
				shname.setVisibility(View.VISIBLE);
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
			search_shares.setOnClickListener(v -> {
				if (hname.getText() == null || hname.getText().toString().equals("")) {
					Log.d(TAG, "missing host address");
				} else {
//						String host = hname.getText().toString();
				if (uname.getText() == null || uname.getText().toString().equals("")) {
//						Log.d(TAG, "missing username");
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

					final View customView = View.inflate(MainActivity.this, R.layout.dialog_list, null);
					customView.setPadding(Tools.dpToPx(8, MainActivity.this), Tools.dpToPx(10, MainActivity.this),
							Tools.dpToPx(8, MainActivity.this), Tools.dpToPx(10, MainActivity.this));
					alertDialogBuilderActWide.setView(customView);
					ProgressBar dialogSpinner = customView.findViewById(R.id.pb_dialog_spinner);
					dialogSpinner.setVisibility(View.VISIBLE);

					final AlertDialog sharesDialog = alertDialogBuilderActWide.create();
					ArrayList<ListItem> shareList = new ArrayList<>();
					CustomDialogListAdapter adapter =
							new CustomDialogListAdapter(MainActivity.this,
									R.layout.dialog_list, shareList, new CustomDialogListAdapter.OnClickListener() {
								@Override
								public void OnSelectItem(ListItem item1) {
									Log.d(TAG, "selected "+ item1.getName());
									shname.setText(item1.getName());
									sharesDialog.dismiss();
								}
							});
					ListView listView = customView.findViewById(R.id.lv_dialog_list);
					listView.setAdapter(adapter);
					sharesDialog.show();

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
									for (String share : shares) {
										Log.d(TAG, "SMB share name: " + share);
										shareList.add(new ListItem(0, 0, share, "",
												null, 0, 0, 0,
												false, 0, false));
									}
									dialogSpinner.setVisibility(View.GONE);
/*									if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
										alertViewFTP.setBackground(background);
//										myDialogFragment.getView().setBackground(background);
									} else {
										alertViewFTP.setBackgroundColor(finalColor);
//										myDialogFragment.getView().setBackgroundColor(finalColor);
									}*/
									Log.d(TAG, "shareList "+ shareList.size()+ ":"+ shareList.toString());
/*										AlertDialog.Builder builderSingle = new AlertDialog.Builder(
											getApplicationContext());
//										builderSingle.setIcon(R.drawable.ic_launcher);
									builderSingle.setTitle("Select One Name:-");

									builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										}
									});
									CustomDialogListAdapter adapter = new CustomDialogListAdapter(
											getApplicationContext(), 0, shareList, null
									);
									builderSingle.setAdapter(adapter, new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											ListItem listItem = adapter.getItem(which);
											AlertDialog.Builder builderInner = new AlertDialog.Builder(
											getApplicationContext());
* /
											builderInner.setMessage(listItem.getName());
											builderInner.setTitle("Your Selected Item is");
											builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog,int which) {
													dialog.dismiss();
												}
											});
											builderInner.show();
										}
									});
									builderSingle.show();
*/
/**/
									/**/
								} else {
									Log.d(TAG, "Error: Shares is null!");
								}
							});
				}
			});
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
//			main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//				@Override
//				public void onGlobalLayout() {
//					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//						main.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//					}
//					Log.d(TAG, "showFTPsettings - scroll width, height: "+ main.getWidth()+ ", "+ main.getHeight());
//				}
//			});
//			main.post(() -> Log.d(TAG, "showFTPsettings - scroll width, height: "+ main.getWidth()+ ", "+ main.getHeight()));
			main.post(new Runnable() {
				@Override
				public void run() {
					int max = Math.max(0, main.getChildAt(0).getHeight() - (main.getHeight()
							- main.getPaddingBottom() - main.getPaddingTop()));
//					Log.d(TAG, "showFTPsettings - scroll contents height: "+ max);
					if (max == 0) {
						Log.d(TAG, "showFTPsettings - scroll fits");
					} else {
						LinearLayout overlay = alertViewFTP.findViewById(R.id.ll_ftp_scrolloverlay);
						ImageView ol_image = alertViewFTP.findViewById(R.id.iv_ftp_scroll),
								ol_image2 = alertViewFTP.findViewById(R.id.iv_ftp_scroll2);
						ol_image.setImageDrawable(new IconicsDrawable(MainActivity.this,
								FontAwesome.Icon.faw_hand_point_up)
								.color(IconicsColor.colorRes(R.color.lightGray))
								.size(IconicsSize.TOOLBAR_ICON_SIZE));
						ol_image2.setImageDrawable(new IconicsDrawable(MainActivity.this,
								FontAwesome.Icon.faw_hand_point_down)
								.color(IconicsColor.colorRes(R.color.lightGray))
								.size(IconicsSize.TOOLBAR_ICON_SIZE));
						overlay.setVisibility(View.VISIBLE);
					}
				}
			});
/*			RxView.layoutChanges(main).take(1)
					.subscribe(aVoid -> {
						// width and height have been calculated here
					});*/
//			frame.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//				@Override
//				public void onGlobalLayout() {
//					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//						frame.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//					}
//					Log.d(TAG, "showFTPsettings - frame height: "+ frame.getHeight());
//				}
//			});
//			frame.post(() -> Log.d(TAG, "showFTPsettings - frame height: "+ frame.getHeight()));
//			Log.d(TAG, "showFTPsettings - scroll stretched to max: "+ main.isFillViewport());
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//				Log.d(TAG, "showFTPsettings - scroll getFitsSystemWindows: "+ main.getFitsSystemWindows());
//			}
			myDialogFragment.show(fm, "fragment_edit_ftp");
//			Log.d(TAG, "showFTPsettings - scroll width, height, frame height: "+
//					main.getWidth()+ ", "+ main.getHeight()+ ", "+ frame.getHeight());
/*			Log.d(TAG, "showFTPsettings - scroll contents height: "+
					Math.max(0, main.getChildAt(0).getHeight() - (main.getHeight()
							- main.getPaddingBottom() - main.getPaddingTop())));*/
		}
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
		ProgressBar spinner = view.findViewById(R.id.pb_ftp_spinner);
		LinearLayout layout = view.findViewById(R.id.ll_ftp_response);
		TextView response = view.findViewById(R.id.tv_ftp_response);
		ImageView rimage = view.findViewById(R.id.iv_ftp_response);
		ScrollView main = view.findViewById(R.id.sv_ftp);

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
		if (type != CONNECTION_TYPE_SMB) {
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
		if (type == CONNECTION_TYPE_SMB) {
//			LoaderSMB client = new LoaderSMB(this, result -> {
//				Log.d(TAG, "tested connection:");
//				Log.d(TAG, "response=" + result.getSessionId()+ ":" + result.getConnection().toString());
//				dialog.dismiss();
//			});
//			client.connect(connStr, uname.getText().toString(), pword.getText().toString(), .getText().toString());
			final String connString = connStr;
/*			SMBoperation client = new SMBoperation(new SMBoperation.OnSMBinteraction() {
				@Override
				public void OnConnection(Session result) {
//					Log.d(TAG, "tested smb connection:" + connString);
					button.setVisibility(View.GONE);
					spinner.setVisibility(View.GONE);
					if (result != null) {
//						Log.d(TAG, "smb response=" + result.getSessionId() + ":" + result.getConnection().toString());
						response.setText(getString(R.string.conn_good)+ ".\n"+ getString(R.string.save_OK));
						rimage.setImageDrawable(new IconicsDrawable(MainActivity.this, FontAwesome.Icon.faw_check)
								.color(IconicsColor.colorRes(R.color.springGreen)).size(IconicsSize.TOOLBAR_ICON_SIZE));
						response.setVisibility(View.VISIBLE);
						rimage.setVisibility(View.VISIBLE);
						layout.setVisibility(View.VISIBLE);
					} else {
						Toast.makeText(MainActivity.this,
								getString(R.string.conn_bad)+ "."+ getString(R.string.adjust_sett),
								Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void OnListFiles(DiskShare files) {
				}
			});
			client.connect(connStr, uname.getText().toString(), pword.getText().toString(), domain.getText().toString());
*/
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
			client.connectFTP(connStr, uname.getText().toString(), pword.getText().toString(), ipath.getText().toString());
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
//			item.setID(model.getConnectionItems().getValue().size());
		}
		View view = (View) button.getParent().getParent().getParent();
		EditText ipath = view.findViewById(R.id.et_ftp_init_path);
		EditText hname = view.findViewById(R.id.et_ftp_host);
		EditText uname = view.findViewById(R.id.et_ftp_username);
		EditText pword = view.findViewById(R.id.et_ftp_password);
		EditText portn = view.findViewById(R.id.et_ftp_port);
		EditText etitle = view.findViewById(R.id.et_ftp_rename);
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
//		if (item.getType() > 0) {
//			String scheme = (item.getType() == CONNECTION_TYPE_SMB) ? "smb:"
//					: (authSFTP.isChecked()) ? "sftp:"
//						: (authFTPS.isChecked()) ? "ftps:"
//							: "ftp:";
//			item.setScheme(scheme);
//		}
		if (domain.getText() != null) {
			item.setAccessToken(domain.getText().toString());
		}
		if (item.getConnectionName() != null && !item.getConnectionName().equals("") &&
				!item.getConnectionName().equals(((Vars) MainActivity.this.getApplication()).tempListItem.getName())) {
//				!item.getConnectionName().equals(model.tempListItem.getName())) {
//			Log.d(TAG, "leftList="+ ((Vars)this.getApplication()).leftList.toString());
//			Log.d(TAG, "id="+ id+ ", itemId="+ item.getID());
//			((Vars) this.getApplication()).leftList.get(item.getID()+5).setName(item.getConnectionName());
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
//		Log.d(TAG, "saving connection: "+ item.toString());
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
		Log.d(TAG, "onSaveInstanceState");
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
				//				item1.setID(((Vars) MainActivity.this.getApplication()).leftList.size() + 1);
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
			final ConnectionItem item = ((Vars)this.getApplication()).getConnectionItemByID(index-4);
//			final ConnectionItem item = model.getConnectionItemByID(index-4);
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
//		ListItem leftListItem = model.getLeftList().getValue().get(index);
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

		final View customView = View.inflate(this, R.layout.dialog_list, null);
		customView.setPadding(Tools.dpToPx(8, this), Tools.dpToPx(10, this),
				Tools.dpToPx(8, this), Tools.dpToPx(10, this));
		contextmenuBuilder.setView(customView);

		final AlertDialog contextmenuDialog = contextmenuBuilder.create();
		contextmenuDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
				(dialog1, which) -> dialog1.cancel());
//				addConnectionDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), (DialogInterface.OnClickListener) null);
		contextmenuDialog.setCanceledOnTouchOutside(false);
		contextmenuDialog.show();
		CustomDialogListAdapter adapter = new CustomDialogListAdapter(this,
				R.layout.dialog_list, items, new CustomDialogListAdapter.OnClickListener() {
			@Override
			public void OnSelectItem(ListItem item1) {
//			if (index > 4) {
				//						addConnectionDialog.dismiss();
				contextmenuDialog.cancel();
				String name = item1.getName();
				//				int id = ((Vars) MainActivity.this.getApplication()).getConnectionItems().size();
				//				item1.setID(((Vars) MainActivity.this.getApplication()).leftList.size() + 1);
				MainActivity.this.contextmenuSelection(name, index);
//			}
			}
		});
		ListView listView = customView.findViewById(R.id.lv_dialog_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener((parent, view, position, itemId) -> contextmenuDialog.dismiss());
	}

	void contextmenuSelection(String name, int index) {
		Log.d(TAG, "contextmenuSelection: name="+ name+ ", index="+ index);
		ListItem leftListItem = ((Vars)MainActivity.this.getApplication()).getLeftListItemByID(index+1);
//		ListItem leftListItem = model.getLeftListItemByID(index);
		if (name.toUpperCase().equals(getString(R.string.remove).toUpperCase())) {
			if (index > 3) {
				AlertDialog.Builder rusureDialog = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
				rusureDialog.setTitle(getString(R.string.rusure));
//			String itemName = ((Vars)MainActivity.this.getApplication()).leftList.get(id-1).getName();
				String itemName = leftListItem.getName();
				rusureDialog.setMessage(getString(R.string.delete_conn_x, itemName));
				rusureDialog.setNegativeButton(getString(android.R.string.cancel), null);
				rusureDialog.setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
//				((Vars) MainActivity.this.getApplication()).leftList.remove(id-1);
					((Vars) MainActivity.this.getApplication()).leftList.remove(leftListItem.getID() - 1);
//				model.removeLeftListItem(leftListItem.getID()-1);
					((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//				model.leftAdapter.notifyDataSetChanged();
					if (leftListItem.getID() > 4) {
						((Vars) MainActivity.this.getApplication()).removeConnectionItem(leftListItem.getID() - 5);
//					model.removeConnectionItem(leftListItem.getID() - 5);
					}
				});
				rusureDialog.show();
			}
		} else if (name.toUpperCase().equals(getString(R.string.rename, "").toUpperCase())) {
			String itemName = ((Vars)MainActivity.this.getApplication()).leftList.get(index-1).getName();
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
						((Vars) MainActivity.this.getApplication()).leftList.get(index-1).setName(value.getText().toString());
//						model.getLeftList().getValue().get(index-1).setName(value.getText().toString());
						((Vars) MainActivity.this.getApplication()).leftAdapter.notifyDataSetChanged();
//						model.leftAdapter.notifyDataSetChanged();
					});
			renameDialog.show();
		} else if (name.toUpperCase().equals(getString(R.string.move).toUpperCase())) {
		} else if (name.toUpperCase().equals(getString(R.string.connto).toUpperCase())) {
//			SelectedLeftItemId(id);
			SelectedLeftItemIndex(index);
		} else if (name.toUpperCase().equals(getString(R.string.edit_conn).toUpperCase())) {
			if (index > (Vars.NUM_THIS_DEVICE-1)) {
				ConnectionItem item = ((Vars) MainActivity.this.getApplication()).getConnectionItemByID(index-4);
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
	void doListFiles() {
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


	private void doListPhotos() {
		FragmentManager manager = getFragmentManager();
		Fragment fragment;
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.addToBackStack(null);
		fragment = RightFragment.newInstance(getString(R.string.photos), null, "");
		transaction.replace(R.id.fl_right_panel, fragment);
		transaction.commit();
	}


	private void doOnRightPanel(Fragment fragment) {
		FragmentManager manager = getFragmentManager();
//		Fragment fragment;
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.addToBackStack(null);
//		fragment = RightFragment.newInstance(getString(R.string.photos), null, "");
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


	@Override
	public void SelectedLeftItemIndex(int index) {
//		Log.d(TAG, "leftItems:"+ ((Vars)this.getApplication()).leftList.toString());
//		Log.d(TAG, "Connection Items:"+ ((Vars)this.getApplication()).getConnectionItems().toString());
//		Log.d(TAG, "leftItems:"+ model.getLeftList().getValue().toString());
		Log.d(TAG, "selected left item with index:"+ index);
		ListItem listItem = ((Vars)this.getApplication()).leftList.get(index);
//		ListItem listItem = model.getLeftList().getValue().get(index);
		ConnectionItem item = new ConnectionItem();
//		int itemId = -99;
		int itemId = index - Vars.NUM_THIS_DEVICE+1;
		if (index > (Vars.NUM_THIS_DEVICE-1)) {
			if (0 > itemId || (((Vars) this.getApplication()).getConnectionItems() != null &&
					itemId > ((Vars) this.getApplication()).getConnectionItems().size())) {
				Log.d(TAG, "itemId is not in bounds: "+ itemId);
				return;
			}
			Log.d(TAG, "getting connection item: "+ itemId);
			item = ((Vars) this.getApplication()).getConnectItem(itemId-1);
			if (item != null) {
				Log.d(TAG, "selected item: "+ item.toString());
			} else {
				Log.d(TAG, "Error getting selected item - null!");
			}
//		ConnectionItem item = model.getConnectItem(index-4);
		}
		switch (listItem.getID()) {
			case Vars.MY_PHOTOS_ID:
//			case model.MY_PHOTOS_ID:
				if (!Permissons.Check_STORAGE(this)) {
//					Permissons.Request_STORAGE(this, MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_PHOTOS_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					}
				} else {
					doOnRightPanel(RightFragment.newInstance(getString(R.string.photos), null, ""));
//					doListPhotos();
				}
				break;
			case Vars.MY_MUSIC_ID:
//			case model.MY_MUSIC_ID:
				if (!Permissons.Check_STORAGE(this)) {
//					Permissons.Request_STORAGE(this, MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE);
					}
				} else {
					doOnRightPanel(RightFragment.newInstance(getString(R.string.music), null, ""));
//					doListMusic();
				}
				break;
			case Vars.MY_CONTACTS_ID:
//			case model.MY_CONTACTS_ID:
				if (!Permissons.Check_READ_CONTACTS(this)) {
//					Permissons.Request_READ_CONTACTS(this, MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSION_RECORD_READ_CONTACTS_REQUEST_CODE);
					}
				} else {
					doOnRightPanel(RightFragment.newInstance(getString(R.string.contacts), null, ""));
//					doListContacts();
				}
				break;
			case Vars.MY_FILES_ID:
//			case model.MY_FILES_ID:
//				doListFiles();
				doOnRightPanel(RightFragment.newInstance(getString(R.string.files), "/", ""));
				break;
			case Vars.MY_LOCAL_HOSTS:
				doOnRightPanel(RightFragment.newInstance(getString(R.string.my_network), null, ""));
//				doListHosts();
				break;
			default:
				if (item == null) {
					Log.d(TAG, "selected left item accioated connection item is null (item id:"+ itemId);
					return;
				}
				if (item.getType() == CONNECTION_TYPE_SMB) {
					SMBoperation smb = new SMBoperation();
					smb.configSMB(false, true, true,
							true, 8*1024*1024, 300, TimeUnit.SECONDS,
							300, TimeUnit.SECONDS, null);
//					SMBoperation smb = new SMBoperation(result -> {
//						if (result != null) {
//							Log.d(TAG, "result="+ result.getSessionId()+ ":"+ result.getConnection().getRemoteHostname());
//						}
//					});
//					smb.connect(item.getHost(), item.getUsername(), item.getPassword(), item.getAccessToken());
					smb.getSMBfileslist(item.getHost(), item.getUsername(), item.getPassword(),
							item.getAccessToken(), item.getShareName(), item.getPath(), new SMBoperation.OnSMBinteraction() {
								@Override
								public void OnListFiles(List<String> files) {
									if (files != null) {
										for (String file : files) {
											Log.d(TAG, "SMB file: " + file);
										}
									} else {
										Log.d(TAG, "recieved null!");
									}
								}
							});
				} else if (item.getType() == CONNECTION_TYPE_FTP) {
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
				} else {
					//handle gdrive, onedrive, ...
					handle();
				}
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
	}

	private void handle() {
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

}

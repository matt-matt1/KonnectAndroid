package com.yumatechnical.konnectandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.druk.rx2dnssd.BonjourService;
import com.github.druk.rx2dnssd.Rx2Dnssd;
import com.github.druk.rx2dnssd.Rx2DnssdEmbedded;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.yumatechnical.konnectandroid.Fragment.RightFragment;
import com.yumatechnical.konnectandroid.Helper.CustomDialogUI;
import com.yumatechnical.konnectandroid.Helper.Network.FTPoperation;
import com.yumatechnical.konnectandroid.Helper.Network.LocalNetwork;
import com.yumatechnical.konnectandroid.Helper.Network.SMBoperation;
import com.yumatechnical.konnectandroid.Helper.Tools;

import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class InitActivity extends AppCompatActivity {

//	Boolean fade_photos = false, fade_music = false, fade_contacts = false, fade_files = false,
//			fade_hosts = false;
	private ConstraintLayout layout;
	private static final String TAG = InitActivity.class.getSimpleName();
//	public static final List<ListItem> sourceList = new ArrayList<>();
//	public static final List<ListItem> addConnList = new ArrayList<>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);

		layout = findViewById(R.id.cl_init_main);
//		modelOperation();
		// Get current account info
//		FullAccount account = DropboxInstance.main().users().getCurrentAccount();
//		System.out.println(account.getName().getDisplayName());
/*		DisplayMetrics dm = this.getResources().getDisplayMetrics();
//		int densityDpi = dm.densityDpi;
		Log.d(TAG, "density is "+ dm.density+ " ("+ dm.densityDpi+ " dpi)");*/
//		testSMB();
		testFTP();
		//getSupportLoaderManager().initLoader(OPERATION_SMB_LOADER, null, this);

		ScrollView scrollView = new ScrollView(this);
		TextView logView = new TextView(this);
		scrollView.addView(logView);
		layout.addView(scrollView);
		class DoLogcat implements Runnable {

			@Override
			public void run() {
				Tools.putLogcatInTextView(logView);
			}
		}
		DoLogcat doLogcat = new DoLogcat();
		doLogcat.run();
		((Vars)getApplication()).initSourceList(this);
//		initSources(this);
//		((Vars)getApplication()).addItem(new Item(Vars.MY_PHOTOS_ID, "", "", ""));
		((Vars)getApplication()).leftList.add(((Vars)getApplication()).getSourceItemById(Vars.MY_PHOTOS_ID));
//		((Vars)getApplication()).addItem(new Item(Vars.MY_MUSIC_ID, "", "", ""));
		((Vars)getApplication()).leftList.add(((Vars)getApplication()).getSourceItemById(Vars.MY_MUSIC_ID));
//		((Vars)getApplication()).addItem(new Item(Vars.MY_CONTACTS_ID, "", "", ""));
		((Vars)getApplication()).leftList.add(((Vars)getApplication()).getSourceItemById(Vars.MY_CONTACTS_ID));
//		((Vars)getApplication()).addItem(new Item(Vars.MY_FILES_ID, "", "", ""));
		((Vars)getApplication()).leftList.add(((Vars)getApplication()).getSourceItemById(Vars.MY_FILES_ID));
//		((Vars)getApplication()).addItem(new Item(Vars.MY_LOCAL_HOSTS, "", "", ""));
		((Vars)getApplication()).leftList.add(((Vars)getApplication()).getSourceItemById(Vars.MY_LOCAL_HOSTS));

		startActivity(new Intent(this, MainActivity.class));
	}

/*
	public ListItem getSourceItemById(int id) {
		for (ListItem item : Vars.sourceList) {
			if (id == item.getID())
				return item;
		}
		return null;
	}
*/
/*
	void initSources(Context context) {
		int leftListDefaultLeftPadding = Tools.dpToPx(16, context);
		int leftListDefaultTopPadding = Tools.dpToPx(16, context);
		int leftListDefaultBottomPadding = Tools.dpToPx(18, context);
		int leftListDefaultBetweenPadding = Tools.dpToPx(8, context);

		sourceList.add(new ListItem(0, MY_PHOTOS_ID, context.getString(R.string.photos),
				null, myPhotos_icon2(context), leftListDefaultLeftPadding,
				leftListDefaultTopPadding, leftListDefaultBottomPadding, true,
				leftListDefaultBetweenPadding, fade_photos, "", ""));
		sourceList.add(new ListItem(0, MY_MUSIC_ID, context.getString(R.string.music),
				null, myMusic_icon(context), leftListDefaultLeftPadding,
				leftListDefaultTopPadding, leftListDefaultBottomPadding, true,
				leftListDefaultBetweenPadding, fade_music, "", ""));
		sourceList.add(new ListItem(0, MY_CONTACTS_ID, context.getString(R.string.contacts),
				null, myContacts_icon(context), leftListDefaultLeftPadding,
				leftListDefaultTopPadding, leftListDefaultBottomPadding, true,
				leftListDefaultBetweenPadding, fade_contacts, "", ""));
		sourceList.add(new ListItem(0, MY_FILES_ID, context.getString(R.string.files),
				null, myFiles_icon(context), leftListDefaultLeftPadding,
				leftListDefaultTopPadding, leftListDefaultBottomPadding, true,
				leftListDefaultBetweenPadding, fade_files, "", ""));
		sourceList.add(new ListItem(0, MY_LOCAL_HOSTS, context.getString(R.string.my_network),
				null, myLocalHosts_icon(context), leftListDefaultLeftPadding,
				leftListDefaultTopPadding, leftListDefaultBottomPadding, true,
				leftListDefaultBetweenPadding, fade_hosts,"", ""));

		sourceList.add(new ListItem(0, GDRIVE_CONN, context.getString(R.string.gdrive),
				null, gdrive_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, DBOX_CONN, context.getString(R.string.dropbox),
				null, dropbox_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, BOX_CONN, context.getString(R.string.box),
				null, box_icon2(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, ONED_CONN, context.getString(R.string.onedrive),
				null, onedrive_icon2(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, ICLOUD_CONN, context.getString(R.string.icloud),
				null, icloud_icon(context), pxFrom16dp(context),
				pxFrom16dp(context), pxFrom18dp(context), true, pxFrom8dp(context),
				false, "", ""));
		sourceList.add(new ListItem(0, FTP_COMM, context.getString(R.string.ftp),
				null, ftp_icon(context), pxFrom16dp(context),
				pxFrom16dp(context), pxFrom18dp(context), true, pxFrom8dp(context),
				false, "", ""));
//		sourceList.add(new ListItem(0, FTP_COMM, context.getString(R.string.ftp),
//				null, network_server_icon(context), pxFrom16dp(context),
//				pxFrom16dp(context), pxFrom18dp(context), true, pxFrom8dp(context),
//				false, "", ""));
		sourceList.add(new ListItem(0, SMB_CONN, context.getString(R.string.local_network),
				null, local_network_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));

		sourceList.add(new ListItem(0, CONN_TO, Tools.toTitleCase(context.getString(R.string.connto)),
				null, conn_to_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, EDIT_CONN, Tools.toTitleCase(context.getString(R.string.edit)),
				null, edit_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, RENAME_CONN, Tools.toTitleCase(context.getString(R.string.rename, "")),
				null, rename_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, REMOVE_CONN, Tools.toTitleCase(context.getString(R.string.delete)),
				null, remove_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
		sourceList.add(new ListItem(0, MOVE_CONN, Tools.toTitleCase(context.getString(R.string.move)),
				null, move_icon(context), pxFrom16dp(context), pxFrom16dp(context),
				pxFrom18dp(context), true, pxFrom8dp(context), false,
				"", ""));
	}
*/

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
//				ftPoperation.getFTPstatus(result, status -> Log.d(TAG, "[MainActivity::testFTP] FTP status: "+ status.trim()+ " (line:"+ Tools.getLineNumber()+ ")"));
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
/*
		DNSSD dnssd = new DNSSDBindable(this);
		DNSSDService registerService;
		try {
			registerService = dnssd.register("service_name", "_rxdnssd._tcp", 123,
					new RegisterListener() {

						@Override
						public void serviceRegistered(DNSSDRegistration registration, int flags,
						                              String serviceName, String regType, String domain) {
							Log.i(TAG, "DNSSD: Register successfully ");
						}

						@Override
						public void operationFailed(DNSSDService service, int errorCode) {
							Log.e(TAG, "DNSSD: error " + errorCode);
						}
					});
		} catch (DNSSDException e) {
			Log.e(TAG, "DNSSD: error", e);
		}
		DNSSDService browseService;
		try {
			browseService = dnssd.browse("_rxdnssd._tcp", new BrowseListener() {

				@Override
				public void serviceFound(DNSSDService browser, int flags, int ifIndex,
				                         final String serviceName, String regType, String domain) {
					Log.i(TAG, "DNSSD: Found " + serviceName);
				}

				@Override
				public void serviceLost(DNSSDService browser, int flags, int ifIndex,
				                        String serviceName, String regType, String domain) {
					Log.i(TAG, "DNSSD: Lost " + serviceName);
				}

				@Override
				public void operationFailed(DNSSDService service, int errorCode) {
					Log.e("TAG", "DNSSD: error: " + errorCode);
				}
			});
		} catch (DNSSDException e) {
			Log.e(TAG, "DNSSD: error", e);
		}
*/
/*
		BonjourService bs = new BonjourService.Builder(0, 0, Build.DEVICE, "_rxdnssd._tcp", null).port(123).build();
		Object registerDisposable;
		//Rx2Dnssd rxdnssd = new Rx2DnssdBindable(context);
		Rx2Dnssd rxdnssd = new Rx2DnssdEmbedded(this);
		registerDisposable = rxdnssd.register(bs)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(bonjourService -> {
					Log.i(TAG, "Rx2DNSSD: Register successfully " + bonjourService.toString());
				}, throwable -> {
					Log.e(TAG, "Rx2DNSSD: error", throwable);
				});
		Object browseDisposable;
		browseDisposable = rxdnssd.browse("_http._tcp", "local.")
				.compose(rxdnssd.resolve())
				.compose(rxdnssd.queryRecords())
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(bonjourService -> {
					Log.d("TAG", "Rx2DNSSD: "+ bonjourService.toString());
//					AbstractQueue<Object> mServiceAdapter = null;
/ *					if (bonjourService.isLost()) {
						mServiceAdapter.remove(bonjourService);
					} else {
						mServiceAdapter.add(bonjourService);
					}* /
				}, throwable -> Log.e(TAG, "error", throwable));
		*/
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
		CustomDialogUI customDialogUI = new CustomDialogUI();
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
				});
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

/*
	public enum SourceList {

		private List<ListItem> sources;

		SourceList() {
			sources = new ArrayList<ListItem>();
		}

		public List<ListItem> getSources() {
			return sources;
		}
	}
*/
}

package com.yumatechnical.konnectandroid.Fragment;
//
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.mssmb2.SMBApiException;
import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.rapid7.client.dcerpc.mssrvs.dto.NetShareInfo0;
import com.yumatechnical.konnectandroid.Adapter.CustomDialogListAdapter;
import com.yumatechnical.konnectandroid.Adapter.RightAdapter;
import com.yumatechnical.konnectandroid.Helper.Network.LocalNetwork;
import com.yumatechnical.konnectandroid.Helper.Network.SMBoperation;
import com.yumatechnical.konnectandroid.Helper.Network.TestInternetLoader;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;
import com.yumatechnical.konnectandroid.Model.FileItem;
import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.Model.MyPhone;
import com.yumatechnical.konnectandroid.Model.SMBConnection;
import com.yumatechnical.konnectandroid.R;
import com.yumatechnical.konnectandroid.SMB;
import com.yumatechnical.konnectandroid.Vars;

import org.apache.tika.Tika;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.mikepenz.iconics.Iconics.getApplicationContext;


/**
 * Fragment for the right panel
 */
public class RightFragment extends Fragment implements LoaderManager.LoaderCallbacks {

	private static final String RIGHT_URI = "id";
	private static final String RIGHT_TEXT = "text";
	private static final String RIGHT_TITLE = "title";
	private ArrayList<FileItem> listOfThings = new ArrayList<>();
	private String msg, title;
	private TextView msgView, titleView, spinnerText;
	private ProgressBar spinnerPB, spinner8;
	private FrameLayout spinner;
//	private int pStatus = 0;
//	private Handler handler = new Handler();
	private ContentResolver resolver;
	private Uri uri;
	private static final String TAG = RightFragment.class.getSimpleName();
	private static final int LOADER_GATHER_MY_PHOTOS = 20;
	private static final int LOADER_GATHER_MY_MUSIC = 21;
	private static final int LOADER_GATHER_MY_CONTACTS = 22;
	private static final int LOADER_GATHER_MY_FILES = 23;
	private SwipeRefreshLayout swipeContainer;
//	private MyViewModel model;
	/*private*/ AsyncTask task = null;
//	/*private*/ MyAsyncTask task = null;

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
		spinnerPB = getActivity().findViewById(R.id.pb_progress);
		spinner8 = getActivity().findViewById(R.id.pb_progress_infinite);
		spinner = getActivity().findViewById(R.id.fl_progress_right);
		spinnerText = getActivity().findViewById(R.id.tv_progress);
		spinnerPB.setProgress(0);
		spinnerText.setText("0%");
		spinnerPB.setSecondaryProgress(100);
		spinnerPB.setMax(100);
		spinnerPB.setProgressDrawable(getResources().getDrawable(R.drawable.circular));
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
//			if (uri != null) {
//				titleView.setText(String.format("%s\n%s", title, uri.toString()));
//			} else {
				titleView.setText(title);
//			}
		}
		if (msg != null && !msg.isEmpty()) {
			msgView.setText(msg);
		} else {
//			spinner.setVisibility(View.VISIBLE);
			spinner8.setVisibility(View.VISIBLE);
/*			new Thread(new Runnable() {
				@Override
				public void run() {
					while (pStatus < 100) {
						pStatus += 1;
						handler.post(new Runnable() {
							@Override
							public void run() {
								spinnerPB.setProgress(pStatus);
								spinnerText.setText(pStatus + "%");
							}
						});
						try {
							// Sleep for 200 milliseconds.=16
							Thread.sleep(100); //thread will take approx 3 seconds to finish,change its value according to your needs
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			*/
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
		if (uri != null) {
			Log.d(TAG, "functionOnTitle: "+ title + ", uri: "+ uri.toString());
		} else {
			Log.d(TAG, "functionOnTitle: "+ title);
		}
//		if (uri != null) {
//			titleView.setText(String.format("%s\n%s", title, uri.toString()));
//		}
		if (title.equals(getString(R.string.contacts))) {
//			if (uri != null) {
//				titleView.setText(String.format("%s\n%s", title, uri.toString()));
//			}
			if (uri == null) {
//				bundle = new Bundle();
//				bundle.putString("get", "contacts");
//				bundle.putInt("id", 0);
				if (task != null)
					task.cancel(true);
				task = new MyAsyncTask(null, Vars.MY_CONTACTS_ID, -1, null).execute();
//				task = new FetchContactsTask(-1).execute();
//				LoaderManager loaderManager = getActivity().getSupportLoaderManager();
//				Loader<String> loader = loaderManager.getLoader(TEST_CONNECT_LOADER);
//				if (loader == null) {
//					loaderManager.initLoader(TEST_CONNECT_LOADER, bundle, this);
//				} else {
//					loaderManager.restartLoader(TEST_CONNECT_LOADER, bundle, this);
//				}
			} else if (uri.getPath() != null) {
				int id = Integer.parseInt(uri.getPath());
				if (task != null)
					task.cancel(true);
//				task = new FetchContactsTask(id).execute();
				task = new MyAsyncTask(null, Vars.MY_CONTACTS_ID, id, null).execute();
			}
		} else if (title.equals(getString(R.string.photos))) {
//			if (uri != null) {
//				titleView.setText(String.format("%s\n%s", title, uri.toString()));
//			}
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
			if (task != null)
				task.cancel(true);
				task = new MyAsyncTask(null, Vars.MY_PHOTOS_ID, -1, null).execute();
//				task = new FetchPhotosTask(-1).execute();
//			}
		} else if (title.equals(getString(R.string.music))) {
//			if (uri != null) {
//				titleView.setText(String.format("%s\n%s", title, uri.toString()));
//			}
//			Log.d(TAG, title);
//			if (!Permissons.Check_STORAGE(this.getActivity())) {
//				Permissons.Request_STORAGE(this, MY_PERMISSION_MUSIC_READ_EXTERNAL_STORAGE_REQUEST_CODE);
//			} else {
			if (task != null)
				task.cancel(true);
			task = new MyAsyncTask(null, Vars.MY_MUSIC_ID, -1, null).execute();
//			task = new FetchMusicTask(-1).execute();
//			}
		} else if (title.equals(getString(R.string.my_network))) {
			if (uri != null) {
				titleView.setText(String.format("%s\n%s", title, uri.toString()));
			}
//			new LocalNetwork.NetworkSniffTask(getApplicationContext()).execute();
			if (task != null)
				task.cancel(true);
			task = new MyAsyncTask(getApplicationContext(), Vars.MY_LOCAL_HOSTS, -1, null).execute();
//			task = new LocalHostsTask(getApplicationContext()).execute();
//			new FetchLocalHosts().execute();
		} else if (title.equals(getString(R.string.smb_share))) {
//			getSMBFiles();
			ConnectionItem item = Tools.decryptConnString(uri.toString());
			uri = Uri.parse(item.getPath());
			if (uri != null) {
				titleView.setText(String.format("%s\n%s", title, uri.toString()));
			}
//			new SMBConnTask(path).execute(host, domain, user, pass, share);
			if (item != null) {
//				Log.d(TAG, "functionOnTitle: getting SMB for (path)" + item.getPath()+
//						" on (host)"+ item.getHost()+ ", (domain)"+ item.getAccessToken()+
//						", (user)"+ item.getUsername()+ ", (pass)"+ item.getPassword()+
//						", (share)"+ item.getShareName());
				SMB smb = new SMB();
				if (task != null)
					task.cancel(true);
				smb.setListener(new SMB.OnSMBResult() {
					@Override
					public void listedFiles(List<Object> files, int connectionIndex) {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								fetchedSMBFiles(files, connectionIndex);
							}
						});
					}
				});
/*				smb.configSMB(false, true, true,
						true, 8 * 1024 * 1024, 300, TimeUnit.SECONDS,
						300, TimeUnit.SECONDS, null);*/
				int connectionIndex = smb.getList(-1, item.getHost(), item.getAccessToken(),
						item.getUsername(), item.getPassword(), item.getShareName(), item.getPath());
				Log.d(TAG, "SMB connectionIndex: "+ connectionIndex);
//				task = new SMBConnTask(item.getPath()).execute(item.getHost(), item.getAccessToken(),
//						item.getUsername(), item.getPassword(), item.getShareName());
				task = new MyAsyncTask(null, Vars.SMB_CONN, connectionIndex, uri).execute();
			}
		} else {
			if (uri != null) {
				titleView.setText(String.format("%s\n%s", title, uri.toString()));
			}
//			File dir = new File(Environment.getExternalStorageDirectory().getPath());
			File dir = new File(Environment.getRootDirectory().getPath());
			Log.d(TAG, title+ ":"+ dir.toString()+ ":");
			if (task != null)
				task.cancel(true);
			task = new MyAsyncTask(null, Vars.MY_FILES_ID, -1, dir).execute();
//			task = new FetchFilesTask(dir).execute();
		}
	}


	public void startSMB() {
		SMB smb = new SMB();
		if (task != null)
			task.cancel(true);
		smb.setListener((files, connectionIndex) -> getActivity().runOnUiThread(()
				-> fetchedSMBFiles(files, connectionIndex)));
/*				smb.configSMB(false, true, true,
						true, 8 * 1024 * 1024, 300, TimeUnit.SECONDS,
						300, TimeUnit.SECONDS, null);*/
//		int connectionIndex = smb.getList(-1, item.getHost(), item.getAccessToken(),
//				item.getUsername(), item.getPassword(), item.getShareName(), item.getPath());
	}


	/**/
	@SuppressLint("StaticFieldLeak")
	public class MyAsyncTask extends AsyncTask<Void, String[], Void> {

		private int id, opId;
		private Object data;
		private WeakReference<Context> mContextRef;
		private File[] files = new File[0];


		MyAsyncTask(Context context, int operationId, int id, Object data) {
			super();
			opId = operationId;
			this.id = id;
			this.data = data;
			mContextRef = new WeakReference<>(context);
		}

		@Override
		protected void onPreExecute() {
			if (id == Vars.MY_LOCAL_HOSTS)
				spinner.setVisibility(View.VISIBLE);
			spinner8.setVisibility(View.VISIBLE);
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
//			model.recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (!isCancelled())
				if (opId == Vars.MY_CONTACTS_ID) {
					getContacts(id);
				} else if (opId == Vars.MY_LOCAL_HOSTS) {
					getLocalHosts(mContextRef);
				} else if (opId == Vars.MY_PHOTOS_ID) {
					getPhotos(id);
				} else if (opId == Vars.MY_MUSIC_ID) {
					getMusic(id);
				} else if (opId == Vars.SMB_CONN) {
					startSMB();
				} else if (opId == Vars.MY_FILES_ID) {
//					File[] files = new File[0];
					files = listFiles((File) data);
				}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			msg = getString(R.string.empty);
			if (listOfThings.size() > 0) {
				if (opId == Vars.MY_CONTACTS_ID) {
					postContacts();
				} else if (opId == Vars.MY_LOCAL_HOSTS) {
					postLocalHosts();
//				} else if (opId == Vars.SMB_CONN) {
//					fetchedSMBFiles(listOfSMBFiles, id);
				} else if (opId == Vars.MY_PHOTOS_ID) {
					postPhotos();
				} else if (opId == Vars.MY_MUSIC_ID) {
					postMusic();
				} else if (opId == Vars.MY_FILES_ID) {
					postFiles(files, (String) data);
					int i = 1;
					for (File file : files) {
						listOfThings.add(new FileItem(file.getName(), null/*new Uri(file.getAbsolutePath())*/, i++,
								null, "", true, file.getName(),
								null, false, file.getAbsolutePath()));
					}
				}
				((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
				if (id == Vars.MY_LOCAL_HOSTS)
					spinner.setVisibility(View.INVISIBLE);
				spinner8.setVisibility(View.INVISIBLE);
				((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
			} else {
				if (id == Vars.MY_LOCAL_HOSTS)
					spinner.setVisibility(View.INVISIBLE);
				spinner8.setVisibility(View.INVISIBLE);
				msgView.setText(msg/*R.string.empty*/);
			}
		}

		@Override
		protected void onProgressUpdate(String[]... values) {
			if (id == Vars.MY_LOCAL_HOSTS) {
				for (int i = 0; i < values.length; i++) {
					String[] string = values[i];
					switch (string[0]) {
						case "title":
							titleView.setText(String.format("%s\n(%s)", title, string[1]));
							break;
						case "host":
							String name = string[1];
							String label = string[1];
							try {
								String ip = string[3];
								String mac = string[5];
								String host = string[1];
								if (ip.equals(((Vars) getApplicationContext()).getMyIPString())) {
									host = "My " + string[1];
									ip = "* " + string[3];
								}
								name = host+ " ("+ ip+ ")["+ mac+ "]";
//							name = host + " (" + ip + ")";
								label = ip;
//						} catch (Exception e) {
//							listofLocalHosts.add(new FileItem(string[1], null, 0,
//									Tools.drawableToBitmap(Vars.network_device_icon(getApplicationContext())),
//									"", false, string[1], null,
//									false, string[1]));
							} finally {
								listOfThings.add(new FileItem(name, null, 0,
//								listofLocalHosts.add(new FileItem(name, null, 0,
										Tools.drawableToBitmap(Vars.network_device_icon(getApplicationContext())),
										"", false, string[1], null,
										false, label));
							}
							((Vars) getActivity().getApplication()).rightAdapter.setData(listOfThings);
//							((Vars) getActivity().getApplication()).rightAdapter.setData(listofLocalHosts);
							((Vars) getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
							break;
						case "percent":
							try {
								int percent = Integer.parseInt(string[1]);
//							Log.d(TAG, "raw percent: "+ percent);
								percent = (percent * 100) / 255;
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//								spinnerPB.setProgress(percent / 255 * 100, true);
									spinnerPB.setProgress(percent, true);
//								Log.d(TAG, "setProgress: "+ percent);
								} else {
//								spinnerPB.setProgress(percent / 255 * 100);
									spinnerPB.setProgress(percent);
//								Log.d(TAG, "LocalHostsTask:onProgressUpdate error");
								}
								spinnerText.setText(String.format("%d%%", percent));
//							Log.d(TAG, "now percent: "+ percent);
							} catch (Exception e) {
								Log.d(TAG, "LocalHostsTask:onProgressUpdate error");
								e.printStackTrace();
							}
							break;
						default:
					}
				}
			}
//			super.onProgressUpdate(values);
		}
	}


	public void postContacts() {
		((Vars)getActivity().getApplication()).rightAdapter.setData(listOfThings);
//		((Vars)getActivity().getApplication()).rightAdapter.setData(contactsList);
		((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(
				Vars.user_icon(getApplicationContext()));
		((Vars)getActivity().getApplication()).rightAdapter.setListener(new RightAdapter.OnListItemClickListener() {
			@Override
			public void pressListItem(String name, FileItem item, int position) {
				AlertDialog.Builder contactDialogBuilder = new AlertDialog.Builder(getActivity(),
						R.style.CustomDialogTheme);
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
			}

			@Override
			public void longPressListItem(String name, FileItem item, int position) {
				AlertDialog.Builder contactDialogBuilder = new AlertDialog.Builder(getActivity(),
						R.style.CustomDialogTheme);
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
			}
		});
	}


	/**/
	private void fetchedSMBFiles(final List<Object> files, final int connIndex) {
/*		SMB smb = new SMB();
//		SMBConnection connection = new SMBConnection();
		if (connIndex < 0)
			Log.d(TAG, "fetchedSMBFiles: connIndex less than 0");
		SMBConnection connection = smb.getConnection(connIndex);*/
/*		if (files != null) {*/
			msg = getActivity().getString(R.string.empty);
		listOfThings.clear();
//			listOfSMBFiles.clear();
		((Vars)getApplicationContext()).rightAdapter.notifyDataSetChanged();
			for (Object obj : files) {
				if (obj instanceof FileIdBothDirectoryInformation) {    // files in a share
					FileIdBothDirectoryInformation file = (FileIdBothDirectoryInformation) obj;
					if (file.getFileName().equals("."))
						continue;
//					if (file.getFileName().equals("..") /*&& is / dir*/ )
//						continue;
//					Log.d(TAG, "SMB file: " + file.getFileName());
					File file1 = new File(file.getFileName());
					listOfThings.add(new FileItem(file.getFileName(), null, 0,
//					listOfSMBFiles.add(new FileItem(file.getFileName(), null, 0,
							(file1.isDirectory()) ? Tools.drawableToBitmap(Vars.folder_icon(getApplicationContext()))
									: Tools.drawableToBitmap(Vars.file_icon(getApplicationContext())),
							"", (file1.getParent() == null), file.getShortName(), null,
							false, file.getFileName()));
				} else if (obj instanceof NetShareInfo0) {  // list of shares on server
					NetShareInfo0 share = (NetShareInfo0) obj;
					Log.d(TAG, "SMB share: " + share.getNetName());
					if (share.getNetName().endsWith("$"))
						continue;
					listOfThings.add(new FileItem(share.getNetName(), null, 0,
//					listOfSMBFiles.add(new FileItem(share.getNetName(), null, 0,
							Tools.drawableToBitmap(Vars.folder_icon(getApplicationContext())),
							"", true, share.toString(), null,
							false, share.toString()));
				} else {//if (obj instanceof SMBApiException) {    // error
//					String msg = "";
					if (obj instanceof SMBApiException) {
						SMBApiException error = (SMBApiException) obj;
						msg = error.getLocalizedMessage();
					} else if (obj instanceof String)
						msg = (String) obj;
					Log.d(TAG, "SMB Error: "+ msg);
//					listOfSMBFiles.add(new FileItem(msg, null, 0, null,
//							"", false, msg, null, false, msg));
				}
			}
			if (listOfThings.size() > 0) {
//			if (listOfSMBFiles.size() > 0) {
				spinner8.setVisibility(View.INVISIBLE);
				((Vars)getActivity().getApplication()).rightAdapter.setData(listOfThings);
//				((Vars)getActivity().getApplication()).rightAdapter.setData(listOfSMBFiles);
				for (FileItem item : listOfThings) {
//				for (FileItem item : listOfSMBFiles) {
					item.setHasContents(true);
				}
				((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
				((Vars)getActivity().getApplication()).rightAdapter.setListener(new RightAdapter.OnListItemClickListener() {
					@Override
					public void pressListItem(String name, FileItem item, int position) {
						if (true/*isDirectory*/) {
							SMBConnection connection = Vars.getInstance().getConnection(connIndex);
							String newPath = "";
							String str /*= ""*/;
							Log.d(TAG, "pressListItem: item="+ item.toString()+
									", position="+ position+ ", connection="+ connection.toString());
							if (name.equals("..")) {
								int pos = Math.max(0, connection.getPath().lastIndexOf("\\"));
								newPath = connection.getPath().substring(0, pos);
//								Log.d(TAG, "pressListItemUP: item="+ item.toString()+
//										", position="+ position+ ", connection="+ connection.toString()+
//										", newPath="+ newPath);
								if (newPath.equals("")) {
									if (connection.getPath().equals("")) {
//									if (position == -1 || position == 0) {
//									if (connection.getShare().equals("")) {
										Vars.getInstance().setConnectionDiskshareNull(connIndex);
										Vars.getInstance().setConnectionShareNull(connIndex);
										str = String.format("%s\n%s", title, connection.getServer());
									} else {
										str = String.format("%s\n/", title);
									}
								} else {
//										str = String.format("%s\n/", title);
									str = String.format("%s\n/%s", title,
											newPath.replace("\\", "/"));
								}
//								}
							} else {
								if (position == 0 /*&& connection.getShare() != null
										&& connection.getShare().equals("")*/) {
									connection.setShare(name);
									str = String.format("%s\n/", title);
								} else {
									if (connection.getPath() != null && !connection.getPath().equals("")) {
										newPath += connection.getPath();
										newPath += "\\";
									}
									newPath += name;
									str = String.format("%s\n/%s", title,
											newPath.replace("\\", "/"));
								}
							}
//							Log.d(TAG, "fetchedSMBFiles::pressListItem: uri is "+ str);
							titleView.setText(str);
							spinner8.setVisibility(View.VISIBLE);
							SMB smb = new SMB();
							if (task != null)
								task.cancel(true);
							smb.setListener(new SMB.OnSMBResult() {
								@Override
								public void listedFiles(List<Object> files, int connectionIndex) {
									getActivity().runOnUiThread(new Runnable() {
										@Override
										public void run() {
											spinner8.setVisibility(View.INVISIBLE);
											fetchedSMBFiles(files, connIndex);
										}
									});
								}
							});
/*				smb.configSMB(false, true, true,
						true, 8 * 1024 * 1024, 300, TimeUnit.SECONDS,
						300, TimeUnit.SECONDS, null);*/
							smb.getList(connIndex, connection.getServer(), connection.getDomain(),
									connection.getUsername(), connection.getPassword(),
									connection.getShare(), newPath);
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
								}
							});
						}
					}

					@Override
					public void longPressListItem(String name, FileItem item, int position) {
						//TODO: Do somthing - SMBConnTask
						AlertDialog.Builder contactDialogBuilder = new AlertDialog.Builder(getActivity(),
								R.style.CustomDialogTheme);
						contactDialogBuilder.setTitle(name);
						contactDialogBuilder.setItems(R.array.contactShare, (dialog, which) ->
								Log.d(TAG, "share via: "+ which));
						contactDialogBuilder.setNegativeButton(android.R.string.cancel, (dialog, which) ->
								dialog.cancel());
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
				((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
			} else {
				spinner8.setVisibility(View.INVISIBLE);
				msgView.setText(msg);
			}
/*		} else {
			spinner8.setVisibility(View.INVISIBLE);
			msgView.setText(R.string.empty);
		}*/
	}

	private void getSMBFiles(String host, String user, String pass, String domain, String share, String path) {
		SMBoperation smb = new SMBoperation();
		smb.configSMB(false, true, true,
				true, 8*1024*1024, 300, TimeUnit.SECONDS,
				300, TimeUnit.SECONDS, null);
//		smb.getSMBfileslist(host, user, pass, domain, share, path, files -> {
//			fetchedSMBFiles(files);
//		});
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

/*
	public class LocalHostsTask extends AsyncTask<Void, String[], Void> {

		private WeakReference<Context> mContextRef;

		public LocalHostsTask(Context context) {
			mContextRef = new WeakReference<>(context);
		}

		@Override
		protected void onPreExecute() {
			spinner.setVisibility(View.VISIBLE);
			spinner8.setVisibility(View.VISIBLE);
//			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... voids) {
			if (!isCancelled()) {
				getLocalHosts(mContextRef);
			}
			return null;
		}

		@SuppressLint("DefaultLocale")
		@Override
		protected void onProgressUpdate(String[]... values) {
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			if (listOfThings.size() > 0) {
//			if (listofLocalHosts.size() > 0) {
				postLocalHosts();
			} else {
				spinner.setVisibility(View.INVISIBLE);
				spinner8.setVisibility(View.INVISIBLE);
				msgView.setText(R.string.no_network);//TODO wrong message I think...
			}
		}

	}
*/

	public void getLocalHosts(WeakReference<Context> reference) {
		Log.d(TAG, "Let's sniff the network");
		try {
			Context context = reference.get();
			if (context != null) {
				ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetwork;
				if (cm != null) {
					activeNetwork = cm.getActiveNetworkInfo();
					Log.d(TAG, "activeNetwork: " + activeNetwork);
				}
				WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

				WifiInfo connectionInfo;
				if (wm != null) {
					connectionInfo = wm.getConnectionInfo();
					int ipInt = connectionInfo.getIpAddress();
					if (ipInt == 0) {
						Log.d(TAG, "cannot work on emulator");
						return;
					}
					Log.d(TAG, "ip as int: " + ipInt);
					String ipString = InetAddress.getByAddress(
							ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array())
							.getHostAddress();

					Log.d(TAG, "ipString: " + ipString);
					((Vars) getApplicationContext()).setMyIPString(ipString);

					String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
					Log.d(TAG, "prefix: " + prefix);
//					publishProgress(new String[]{   "title", prefix});

					LocalNetwork.makeArrayFromARPTable();
					for (int i = 0; i < 255; i++) {
//						if (!isCancelled()) {
							String testIp = prefix + i;
//							publishProgress(new String[]{"percent", String.valueOf(i)});

//									boolean reachable = address.isReachable(1000);
//									boolean reachable = LocalNetwork.isReachable2(testIp, 58, 200);
							boolean reachable = LocalNetwork.testAddress(testIp);
							if (reachable) {
								InetAddress address = InetAddress.getByName(testIp);
								String hostName = address.getCanonicalHostName();
								String mac = LocalNetwork.getMacAddressFromARPTable(testIp);
								String vendor = getMacVendor(mac);
								if (testIp.equals(ipString))
									mac = LocalNetwork.getMyMacAddr();
								Log.i(TAG, "Host: "+ hostName+ "("+ testIp + ")("+
										mac+ ")("+ vendor+ ") is reachable!");
//										Log.i(TAG, "Host: " + hostName + "(" + testIp + ") is reachable!");
//								publishProgress(new String[]{   "host", hostName,
//										"ip", testIp, "mac", mac});
							}
//						}
					}
				}
			}
		} catch (Throwable t) {
			Log.e(TAG, "Well that's not good.", t);
		}
	}


	public void postLocalHosts() {
		((Vars)getActivity().getApplication()).rightAdapter.setData(listOfThings);
//		((Vars)getActivity().getApplication()).rightAdapter.setData(listofLocalHosts);
//				for (FileItem item : listofLocalHosts) {
//					item.setHasContents(true);
//				}
		for (int i = 0; i < listOfThings.size(); i++) {
//		for (int i = 0; i < listofLocalHosts.size(); i++) {
			listOfThings.get(i).setHasContents(true);
//			listofLocalHosts.get(i).setHasContents(true);
		}
		((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
		spinner.setVisibility(View.INVISIBLE);
		spinner8.setVisibility(View.INVISIBLE);
		((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
	}


	public String getMacVendor(String mac) {
		String base ="https://api.macvendors.com/";
		try {
			StringBuilder result = new StringBuilder();
			URL url = new URL(base + mac);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			return result.toString();
		} catch (FileNotFoundException e) {
			// MAC not found
			return "N/A";
		} catch (IOException e) {
			// Error during lookup, either network or API.
			return null;
		}
			/*
			RequestQueue queue = Volley.newRequestQueue(this);
			String url ="https://api.macvendors.com/";
			url += mac;

			StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							// Display the first 500 characters of the response string.
//							textView.setText("Response is: "+ response.substring(0,500));
						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					textView.setText("That didn't work!");
				}
			});

// Add the request to the RequestQueue.
			queue.add(stringRequest);
*/
	}

/*
	@SuppressLint("StaticFieldLeak")
	public class FetchContactsTask extends AsyncTask<Void, Void, Void> {

		int id;

		FetchContactsTask(int id) {
			super();
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
//			spinner.setVisibility(View.VISIBLE);
			spinner8.setVisibility(View.VISIBLE);
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
//			model.recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (!isCancelled())
				getContacts(id);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			if (contactsList.size() > 0) {
				((Vars)getActivity().getApplication()).rightAdapter.setData(contactsList);
				((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(
						Vars.user_icon(getApplicationContext()));
				((Vars)getActivity().getApplication()).rightAdapter.setListener(new RightAdapter.OnListItemClickListener() {
					@Override
					public void pressListItem(String name, FileItem item, int position) {
						AlertDialog.Builder contactDialogBuilder = new AlertDialog.Builder(getActivity(),
								R.style.CustomDialogTheme);
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
					}

					@Override
					public void longPressListItem(String name, FileItem item, int position) {
						AlertDialog.Builder contactDialogBuilder = new AlertDialog.Builder(getActivity(),
								R.style.CustomDialogTheme);
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
					}
				});
				((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
//				spinner.setVisibility(View.INVISIBLE);
				spinner8.setVisibility(View.INVISIBLE);
				((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
			} else {
//				spinner.setVisibility(View.INVISIBLE);
				spinner8.setVisibility(View.INVISIBLE);
				msgView.setText(R.string.empty);
			}
		}

	}
*/

	void getContacts(int ID) {
//		if (listOfThings != null) {
//			if (listOfThings.size() > 0) {
//				return;
//			}
//		}
//		if (contactsList != null) {
//			if (contactsList.size() > 0) {
//				return;
//			}
//		}
//		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
		String sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY+ " ASC";
		resolver = getActivity().getContentResolver();
		Cursor cursor;
		cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, sortOrder);
		if ((cursor != null ? cursor.getCount() : 0) > 0) {
			while (cursor.moveToNext() && !task.isCancelled()) {
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
//				Drawable drawable = Vars.contact_icon(getApplicationContext());
				FileItem fileItem = new FileItem(name, null, Integer.parseInt(id),
						 null, "mime", has, name, phoneList, false, label);
				listOfThings.add(fileItem);
//				contactsList.add(fileItem);
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

/*
	@SuppressLint("StaticFieldLeak")
	public class FetchPhotosTask extends AsyncTask<Void, Void, Void> {

		int id;

		FetchPhotosTask(int id) {
			super();
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
//			spinner.setVisibility(View.VISIBLE);
			spinner8.setVisibility(View.VISIBLE);
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
//			model.recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (!isCancelled())
				getPhotos(id);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {//done
//			if (listOfAllImages.size() > 0) {
			if (listOfThings.size() > 0) {
				postPhotos();
			} else {
				spinner8.setVisibility(View.INVISIBLE);
//				spinner.setVisibility(View.INVISIBLE);
				msgView.setText(R.string.empty);
			}
		}

	}
*/

	void getPhotos(int ID) {
//		if (listOfThings != null || listOfThings.size() > 0) {
//			return;
//		}
//		if (listOfThings != null) {
//			if (listOfThings.size() > 0) {
//				return;
//			}
//		}
		String[] projection = {MediaStore.MediaColumns.DATA};
		String sortOrder = "";//MediaStore.Audio.Media.TITLE + " ASC";
		resolver = getActivity().getContentResolver();
		Cursor cursor;
//		ArrayList<MediaStore.Images.Media> list = new ArrayList<>();
		cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				projection, null, null, sortOrder);
		if ((cursor != null ? cursor.getCount() : 0) > 0) {
//			Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));
			int id = 0;
			while (cursor.moveToNext() && !task.isCancelled()) {
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
//				if (task != null)
//					task.cancel(true);
				new DownloadImageTask(fileItem).execute(path);
//				task = new DownloadImageTask(fileItem).execute(path);
				listOfThings.add(fileItem);
//				list.add(new MediaStore.Images.Media());
				id += 1;
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		Log.d(TAG, "listOfThings: "+ listOfThings.toString());
	}


	public void postPhotos() {
		//				Drawable defaultImg = Vars.img_icon(getApplicationContext());
//						.color(IconicsColor.colorRes(R.color.White)).size(IconicsSize.dp(Objects.requireNonNull(model.getIconSize().getValue())));
//				myList.setData(listOfAllImages, defaultImg);
//				rightAdapter = new RightAdapter(null, null, new RightAdapter.ListItemClickListener() {
		((Vars)getActivity().getApplication()).rightAdapter.setListener(new RightAdapter.OnListItemClickListener() {
			@Override
			public void pressListItem(String name, FileItem item, int position) {
				if (item.getFullPath() != null) {
					Intent intent = new Intent(Intent.ACTION_VIEW, item.getFullPath());
					intent.setType("image/*");
					if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
						startActivity(intent);
					}
				}
			}

			@Override
			public void longPressListItem(String name, FileItem item, int position) {
				//TODO: Do somthing - FetchPhotosTask
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
		((Vars)getActivity().getApplication()).rightAdapter.setData(listOfThings);
//		((Vars)getActivity().getApplication()).rightAdapter.setData(listOfAllImages);
//				model.rightAdapter.setData(listOfAllImages);
		((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
//				model.rightAdapter.notifyDataSetChanged();
//				rightAdapter.setDefaultImage(defaultImg);
		((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(
				Vars.img_icon(getApplicationContext()));
//				model.rightAdapter.setDefaultImage(defaultImg);
//				spinner.setVisibility(View.INVISIBLE);
		spinner8.setVisibility(View.INVISIBLE);
		((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
//				model.recyclerView.setVisibility(View.VISIBLE);
		((Vars)getActivity().getApplication()).recyclerView.requestLayout();
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
			if (!isCancelled()) {
				try {
					InputStream in = new java.net.URL(urldisplay).openStream();
					mIcon11 = BitmapFactory.decodeStream(in);
				} catch (Exception e) {
					Log.e("DownloadImageTask Error", e.getMessage());
					e.printStackTrace();
				}
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

/*
	@SuppressLint("StaticFieldLeak")
	public class FetchMusicTask extends AsyncTask<Void, Void, Void> {

		int id;

		FetchMusicTask(int id) {
			super();
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
//			spinner.setVisibility(View.VISIBLE);
			spinner8.setVisibility(View.INVISIBLE);
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
//			model.recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (!isCancelled())
				getMusic(id);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {//done
			if (listOfThings.size() > 0) {
//			if (listOfMyMusic.size() > 0) {
				postMusic();
			} else {
//				spinner.setVisibility(View.INVISIBLE);
				spinner8.setVisibility(View.INVISIBLE);
				msgView.setText(R.string.empty);
			}
		}

	}
*/

	void getMusic(int ID) {
//		if (listOfThings != null) {
//			if (listOfThings.size() > 0) {
//				return;
//			}
//		}
//		if (listOfMyMusic != null) {
//			if (listOfMyMusic.size() > 0) {
//				return;
//			}
//		}
		String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
		String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
		int count = 0;
		resolver = getActivity().getContentResolver();
		Cursor cursor;
//		ArrayList<MediaStore.Images.Media> list = new ArrayList<>();
		cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				/*projection*/null, selection, null, sortOrder);
		if ((cursor != null ? cursor.getCount() : 0) > 0) {
//			Log.d(TAG, DatabaseUtils.dumpCursorToString(cursor));
			int id = 0;
			count = cursor.getCount();
			while (cursor.moveToNext() && !task.isCancelled()) {
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
				listOfThings.add(fileItem);
//				listOfMyMusic.add(fileItem);
//				list.add(new MediaStore.Images.Media());
				id += 1;
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		if (listOfThings.size() > 0) {
//		if (listOfMyMusic.size() > 0) {
			Log.d(TAG, "listOfMyMusic: " + listOfThings.toString());
//			Log.d(TAG, "listOfMyMusic: " + listOfMyMusic.toString());
		} else {
			Log.d(TAG, "listOfMyMusic is empty.");
		}
	}


	public void postMusic() {
		((Vars)getActivity().getApplication()).rightAdapter.setListener(new RightAdapter.OnListItemClickListener() {
			@Override
			public void pressListItem(String name, FileItem item, int position) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(item.getFullPath());
				if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
					startActivity(intent);
				}
			}

			@Override
			public void longPressListItem(String name, FileItem item, int position) {
				//TODO: Do somthing - FetchMusicTask
			}
		});
//				model.recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);
//				model.recyclerView.setAdapter(model.rightAdapter);
		((Vars)getActivity().getApplication()).rightAdapter.setData(listOfThings);
//		((Vars)getActivity().getApplication()).rightAdapter.setData(listOfMyMusic);
		((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
//				model.rightAdapter.setData(listOfMyMusic);
		((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(
				Vars.def_music_icon(getApplicationContext()));
//				spinner.setVisibility(View.INVISIBLE);
		spinner8.setVisibility(View.INVISIBLE);
		((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
//				model.recyclerView.setVisibility(View.VISIBLE);
	}

/*
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
//			spinner.setVisibility(View.VISIBLE);
			spinner8.setVisibility(View.VISIBLE);
			titleView.setText(String.format("%s\n%s", title, directory));
			((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.INVISIBLE);
//			model.recyclerView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected File[] doInBackground(Void... params) {
			File[] files = new File[0];
			if (!isCancelled())
				files = listFiles(directory);
			return files;
		}

		@RequiresApi(api = Build.VERSION_CODES.O)
		@Override
		protected void onPostExecute(File[] files) {
			super.onPostExecute(files);
			if (files != null) {
				postFiles(files, directoryStr);
			} else {
				msgView.setText(R.string.error);
				spinner8.setVisibility(View.INVISIBLE);
			}
		}

	}
*/

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


	public void postFiles(File[] files, String directoryStr) {
		//				Log.d(TAG, "FetchFilesTask count="+ files.length);
		Tika tika = new Tika();
		((Vars)getActivity().getApplication()).rightAdapter.setListener(
				new RightAdapter.OnListItemClickListener() {
					@Override
					public void pressListItem(String name, FileItem item, int position) {
						if (item.getMIME() == "directory") {
							openDirectory(item, (files.length > 0) ? files[0] : new File(directoryStr));
						} else {
							drawFileContextMenu(item);
						}
					}

					@Override
					public void longPressListItem(String name, FileItem item, int position) {
						//TODO: Do somthing - FetchFilesTask
						androidx.appcompat.app.AlertDialog.Builder contextmenuBuilder =
								new androidx.appcompat.app.AlertDialog.Builder(getApplicationContext(),
										R.style.CustomDialogTheme);
						ListItem leftListItem = ((Vars) getActivity().getApplication()).leftList.get(position);
						String itemName = leftListItem.getName();
						contextmenuBuilder.setTitle(itemName);
						ArrayList<ListItem> items = new ArrayList<>();//fillAddConnections();
						items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.connto)), null,
								new IconicsDrawable(getActivity(), FontAwesome.Icon.faw_ellipsis_h)
										.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
								Tools.dpToPx(16, getApplicationContext()),
								Tools.dpToPx(16, getApplicationContext()),
								Tools.dpToPx(18, getApplicationContext()), true,
								Tools.dpToPx(8, getApplicationContext()),
								false, "", ""));

						final View customView = View.inflate(getActivity(), R.layout.dialog_list, null);
						customView.setPadding(Tools.dpToPx(8, getApplicationContext()),
								Tools.dpToPx(10, getApplicationContext()),
								Tools.dpToPx(8, getApplicationContext()),
								Tools.dpToPx(10, getApplicationContext()));
						contextmenuBuilder.setView(customView);

						final androidx.appcompat.app.AlertDialog contextmenuDialog = contextmenuBuilder.create();
						contextmenuDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
								(dialog1, which) -> dialog1.cancel());
						contextmenuDialog.setCanceledOnTouchOutside(false);
						contextmenuDialog.show();
						CustomDialogListAdapter adapter = new CustomDialogListAdapter(getActivity(),
								R.layout.dialog_list, items, new CustomDialogListAdapter.OnClickListener() {
							@Override
							public void OnSelectItem(ListItem item1) {
								contextmenuDialog.cancel();
								String name = item1.getName();
//								MainActivity.this.contextmenuSelection(name, index);
							}
						});
						ListView listView = customView.findViewById(R.id.lv_dialog_list);
						listView.setAdapter(adapter);
						listView.setOnItemClickListener((parent, view, position1, itemId) -> contextmenuDialog.dismiss());
					}
				});
		((Vars)getActivity().getApplication()).recyclerView.setAdapter(((Vars)getActivity().getApplication()).rightAdapter);
//				model.recyclerView.setAdapter(model.rightAdapter);
//				if (!directoryStr.equals(Environment.getExternalStorageDirectory().toString())) {
		if (!directoryStr.equals(Environment.getRootDirectory().toString())) {
//					FileItem item = new FileItem(getString(R.string.up), null, 0,
//							Tools.drawableToBitmap(dirIcon), "directory", true,
//							"", null, false, "..");
			FileItem item = new FileItem(getString(R.string.up), null, 0,
					Tools.drawableToBitmap(Vars.folder_icon(getApplicationContext())),
					"directory", true, "", null,
					false, "..");
			listOfThings.add(item);
//					listOfAllFiles.add(item);
		}
		for (File f : files) {
			String mime = "directory";
			FileItem item = new FileItem(f.getName(), Uri.fromFile(f), 0,
//							Tools.drawableToBitmap(fileIcon), mime, false, f.getName(),
//							null, false, f.getAbsolutePath());
					Tools.drawableToBitmap(Vars.file_icon(getApplicationContext())), mime,
					false, f.getName(), null, false,
					f.getAbsolutePath());
			if (f.isFile()) {
				setMIME(mime, item);
			} else {
				String[] children = f.list();
				item.setHasContents(children != null && children.length > 0);
//						item.setBitmap(Tools.drawableToBitmap(dirIcon));
				item.setBitmap(Tools.drawableToBitmap(Vars.folder_icon(getApplicationContext())));
			}
			listOfThings.add(item);
//					listOfAllFiles.add(item);
			Log.d(TAG, "file item is "+ item.toString());
		}
		((Vars)getActivity().getApplication()).rightAdapter.setData(listOfThings);
//				((Vars)getActivity().getApplication()).rightAdapter.setData(listOfAllFiles);
//				model.rightAdapter.setData(listOfAllFiles);
		((Vars)getActivity().getApplication()).rightAdapter.setDefaultImage(
				Vars.file_icon(getApplicationContext()));
//				spinner.setVisibility(View.INVISIBLE);
		spinner8.setVisibility(View.INVISIBLE);
		((Vars)getActivity().getApplication()).recyclerView.setVisibility(View.VISIBLE);
//				model.recyclerView.setVisibility(View.VISIBLE);
//				((Vars)getActivity().getApplication()).recyclerView.requestLayout();
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
		listOfThings.clear();
//		listOfAllFiles.clear();
		((Vars)getActivity().getApplication()).rightAdapter.clear();
		((Vars)getActivity().getApplication()).rightAdapter.notifyDataSetChanged();
		if (task != null)
			task.cancel(true);
		task = new MyAsyncTask(null, Vars.MY_FILES_ID, 0, path).execute();
//		task = new FetchFilesTask(path).execute();
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
				false, "", ""));

		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.rename, "")),null,
				new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_newspaper1)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, getApplicationContext()), Tools.dpToPx(16, getApplicationContext()),
				Tools.dpToPx(18, getApplicationContext()), true, Tools.dpToPx(8, getApplicationContext()),
				false, "", ""));

		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.edit)),null,
				new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_edit1)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, getApplicationContext()), Tools.dpToPx(16, getApplicationContext()),
				Tools.dpToPx(18, getApplicationContext()), true, Tools.dpToPx(8, getApplicationContext()),
				editFaded, "", ""));

		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.delete)),null,
				new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_eraser)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, getApplicationContext()), Tools.dpToPx(16, getApplicationContext()),
				Tools.dpToPx(18, getApplicationContext()), true, Tools.dpToPx(8, getApplicationContext()),
				false, "", ""));

		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.copy)),null,
				new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_copy)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, getApplicationContext()), Tools.dpToPx(16, getApplicationContext()),
				Tools.dpToPx(18, getApplicationContext()), true, Tools.dpToPx(8, getApplicationContext()),
				false, "", ""));

		items.add(new ListItem(0, items.size(), Tools.toTitleCase(getString(R.string.move)),null,
				new IconicsDrawable(getApplicationContext(), FontAwesome.Icon.faw_arrows_alt)
						.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE),
				Tools.dpToPx(16, getApplicationContext()), Tools.dpToPx(16, getApplicationContext()),
				Tools.dpToPx(18, getApplicationContext()), true, Tools.dpToPx(8, getApplicationContext()),
				listOfThings.size() > 1, "", ""));
//				listOfAllFiles.size() > 1, "", ""));

		final View customView = View.inflate(getActivity(), R.layout.dialog_list, null);
		customView.setPadding(Tools.dpToPx(8, getApplicationContext()), Tools.dpToPx(10, getApplicationContext()),
				Tools.dpToPx(8, getApplicationContext()), Tools.dpToPx(10, getApplicationContext()));
		contextmenuBuilder.setView(customView);

		final AlertDialog contextmenuDialog = contextmenuBuilder.create();
		contextmenuDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE,
				getResources().getString(android.R.string.cancel),
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
		for (FileItem fileItem : listOfThings) {
//		for (FileItem fileItem : listOfAllFiles) {
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
					listOfThings.remove(item);
//					listOfAllFiles.remove(item);
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
							listOfThings.get(position).setName(value.getText().toString());
//							listOfAllFiles.get(position).setName(value.getText().toString());
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

/*
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
*/
}

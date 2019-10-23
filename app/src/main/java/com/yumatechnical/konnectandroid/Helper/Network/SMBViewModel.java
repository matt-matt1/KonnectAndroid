package com.yumatechnical.konnectandroid.Helper.Network;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.FileObserver;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;

import java.io.IOException;
import java.util.ArrayList;


public class SMBViewModel extends AndroidViewModel {

	private final MyLiveData data;
//	private final MutableLiveData<ArrayList<FileIdBothDirectoryInformation>> data = new MutableLiveData<>();
	private ConnectionItem item = new ConnectionItem();
	private String fileType = "*.TXT";


	public SMBViewModel(Application application, ConnectionItem connectionItem) {
		super(application);
		item = connectionItem;
//		loadData();
		data = new MyLiveData(application, connectionItem);
	}

	public LiveData<ArrayList<FileIdBothDirectoryInformation>> getData() {
		return data;
	}

	public class MyLiveData extends LiveData<ArrayList<FileIdBothDirectoryInformation>> {
		private final Context context;
		private final FileObserver fileObserver;

		public MyLiveData(Context context, ConnectionItem connectionItem) {
			this.context = context;
			item = connectionItem;
			fileObserver = new FileObserver(item.toConnectionString()) {
				@Override
				public void onEvent(int event, String path) {
					loadData();
				}
			};
			loadData();
		}

		@Override
		protected void onActive() {
			fileObserver.startWatching();
		}

		@Override
		protected void onInactive() {
			fileObserver.stopWatching();
		}

		private void loadData() {
			new AsyncTask<Void,Void,ArrayList<FileIdBothDirectoryInformation>>() {
				@Override
				protected ArrayList<FileIdBothDirectoryInformation> doInBackground(Void... voids) {
	//				List<Session> data = new ArrayList<>();
					ArrayList<FileIdBothDirectoryInformation> files = new ArrayList<>();
					SMBClient client = new SMBClient();

					try (Connection connection = client.connect(item.getHost())) {
						AuthenticationContext ac = new AuthenticationContext(item.getUsername(),
								item.getPassword().toCharArray(),
								item.getAccessToken());
						Session session = connection.authenticate(ac);

	//					data.add(session);

						// Connect to Share
						String[] separated = Tools.getShareNameFromPath(item.getPath());
						try (DiskShare share = (DiskShare) session.connectShare(separated[0])) {
							for (FileIdBothDirectoryInformation f : share.list(separated[1], fileType)) {
	//							System.out.println("File : " + f.getFileName());
								files.add(f);
							}
						}
	//					return files;
					} catch (IOException e) {
						e.printStackTrace();
					}
					return files;
	//				return data;
				}

				@Override
				protected void onPostExecute(ArrayList<FileIdBothDirectoryInformation> responseData) {
					data.setValue(responseData);
	//				this.data.setValue(data);
				}
			}.execute();
		}

	}

}
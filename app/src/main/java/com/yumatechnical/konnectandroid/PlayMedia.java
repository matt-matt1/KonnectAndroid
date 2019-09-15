package com.yumatechnical.konnectandroid;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PlayMedia extends AppCompatActivity {

	private Cursor mData;
	private ArrayList<Song> songList = new ArrayList<>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final String toPackage = null;
		final Uri uri = null;
		final int mobileFlags = 0;
//		grantUriPermission(toPackage, uri, mobileFlags);
		new WordFetchTask().execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mData.close();
	}

	public class WordFetchTask extends AsyncTask<Void, Void, Cursor> {

		@Override
		protected Cursor doInBackground(Void... voids) {
			ContentResolver musicResolver = getContentResolver();
			Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			Cursor cursor = musicResolver.query(musicUri, null, null,
					null, null);
			return cursor;
		}

		@Override
		protected void onPostExecute(Cursor cursor) {
			super.onPostExecute(cursor);
			if (cursor!=null && cursor.moveToFirst()) {
				//get columns
				int titleColumn = cursor.getColumnIndex
						(android.provider.MediaStore.Audio.Media.TITLE);
				int idColumn = cursor.getColumnIndex
						(android.provider.MediaStore.Audio.Media._ID);
				int artistColumn = cursor.getColumnIndex
						(android.provider.MediaStore.Audio.Media.ARTIST);
				//add songs to list
				do {
					long thisId = cursor.getLong(idColumn);
					String thisTitle = cursor.getString(titleColumn);
					String thisArtist = cursor.getString(artistColumn);
					Log.d("WordFetchTask Song:", thisId + ":" + thisTitle + ":" + thisArtist);
					songList.add(new Song(thisId, thisTitle, thisArtist));
				}
				while (cursor.moveToNext());
			}
		}

	}
/*
	public class MyService extends Service implements MediaPlayer.OnPreparedListener,
			MediaPlayer.OnErrorListener {
		private static final String ACTION_PLAY = "com.example.action.PLAY";
		MediaPlayer mediaPlayer = null;
		WifiManager.WifiLock wifiLock = null;

		public void startWifiLock(WifiManager.WifiLock wifiLock) {
//			this.wifiLock = wifiLock;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
				wifiLock = ((WifiManager) Objects.requireNonNull(getApplicationContext()
						.getSystemService(Context.WIFI_SERVICE)))
						.createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
			}
			if (wifiLock != null) {
				wifiLock.acquire();
			}
		}

		public void initMediaPlayer() {
			// ...initialize the MediaPlayer here...
			mediaPlayer.setOnErrorListener(this);
		}

		public int onStartCommand(Intent intent, int flags, int startId) {
//        ...
			if (intent.getAction().equals(ACTION_PLAY)) {
//				mediaPlayer = ... // initialize it here
				mediaPlayer.setOnPreparedListener(this);
				mediaPlayer.prepareAsync(); // prepare async to not block main thread
			}
			return mediaPlayer.getAudioSessionId();
		}

		@Nullable
		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}

		/ ** Called when MediaPlayer is ready * /
		public void onPrepared(MediaPlayer player) {
			player.start();
		}

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			// ... react appropriately ...
			// The MediaPlayer has moved to the Error state, must be reset!
			return false;
		}

		void setWakeMode(MediaPlayer mediaPlayer) {
			mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			if (mediaPlayer != null)
				mediaPlayer.release();
			if (wifiLock != null)
				wifiLock.release();
		}

	}
*/
/*
	public void getFromContentProvider(ContentResolver contentResolver) {
//		ContentResolver contentResolver = getContentResolver();
		Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor cursor = contentResolver.query(uri, null, null, null, null);
		if (cursor == null) {
			// query failed, handle error.
		} else if (!cursor.moveToFirst()) {
			// no media on the device
		} else {
			int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
			int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
			do {
				long thisId = cursor.getLong(idColumn);
				String thisTitle = cursor.getString(titleColumn);
				// ...process entry...
			} while (cursor.moveToNext());
		}
	}
	public void getFromContentProvider2(long id, MediaPlayer mediaPlayer, Context context) {
		Uri contentUri = ContentUris.withAppendedId(
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(context, contentUri);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ...prepare and start...
	}

	public void playLocal(Context context, Uri uri) {
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(context, uri);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void playRemote(String url) {
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare(); // might take long! (for buffering, etc)
			mediaPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/
	class Song {
		private long id;
		private String title;
		private String artist;

		private Song(long songID, String songTitle, String songArtist) {
			id=songID;
			title=songTitle;
			artist=songArtist;
		}

		public long getID(){return id;}
		public String getTitle(){return title;}
		public String getArtist(){return artist;}
	}

}

package com.yumatechnical.konnectandroid.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyService extends Service {

	MediaPlayer mp;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		mp = MediaPlayer.create(getApplicationContext(), R.raw.song);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mp != null) {
			mp.start();
		}
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		mp.release();
		super.onDestroy();
	}

}

package com.yumatechnical.konnectandroid.Helper;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestSMBconnectionLoader extends AsyncTaskLoader {

	private String uri;


	public TestSMBconnectionLoader(Context context, String uri) {
		super(context);
		this.uri = uri;
	}

	@Override
	protected void onStartLoading(){
		forceLoad();
	}

	@Override
	public Object loadInBackground() {
		try {
//			SmbFile
//			NtlmPasswordAuthentication
			HttpURLConnection httpURLConnection = (HttpURLConnection)(new URL(uri).openConnection());
//			httpURLConnection.setRequestProperty("User-Agent", "Test");
//			httpURLConnection.setRequestProperty("Connection", "close");
			httpURLConnection.setConnectTimeout(10000);
			httpURLConnection.connect();
			return (httpURLConnection.getResponseCode() == 200);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return false;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
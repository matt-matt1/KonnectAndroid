package com.yumatechnical.konnectandroid.Helper.Dropbox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dropbox.core.android.Auth;
import com.yumatechnical.konnectandroid.MainActivity;
import com.yumatechnical.konnectandroid.Vars;


public class LoginActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				Auth.startOAuth2Authentication(getApplicationContext(), Vars.getDropboxKey());
	}

	@Override
	protected void onResume() {
		super.onResume();
		getAccessToken();
	}

	public void getAccessToken() {
		String accessToken = Auth.getOAuth2Token();
		if (accessToken != null) {
			SharedPreferences prefs = getSharedPreferences("com.yumatechnical.konnectandroid",
					Context.MODE_PRIVATE);
			prefs.edit().putString("access-token", accessToken).apply();
			Vars.setDropboxAccessToken(accessToken);

			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
		}
	}
}
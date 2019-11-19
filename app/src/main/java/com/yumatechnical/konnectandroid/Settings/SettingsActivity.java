package com.yumatechnical.konnectandroid.Settings;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.R;

public class SettingsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.PreferencesTheme);
		setContentView(R.layout.activity_settings);
		//loads SettingFragment
		setupActionBar();
	}

	private void setupActionBar() {
		ViewGroup rootView = findViewById(R.id.action_bar_root);

/*		Button button = findViewById(R.id.b_settings);
		button.setText(getString(R.string.remove_settings));
		button.setOnClickListener(v -> {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
					SettingsActivity.this);
			sharedPreferences.edit().clear().apply();
			Tools.exitApplication(this);
		});*/

		if (rootView != null) {
			View view = getLayoutInflater().inflate(R.layout.toolbar, rootView, false);
			rootView.addView(view, 0);
			Toolbar toolbar = findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
		}
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

}

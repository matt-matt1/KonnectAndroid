package com.yumatechnical.konnectandroid;

import android.content.SharedPreferences;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;


public class MySharedPreferencesActivity extends MainActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener,
		Preference.OnPreferenceChangeListener {

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


}

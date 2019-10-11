package com.yumatechnical.konnectandroid.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.yumatechnical.konnectandroid.R;

public class SettingFragment extends PreferenceFragmentCompat
		implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		addPreferencesFromResource(R.xml.prefs_visual);
		SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
		PreferenceScreen preferenceScreen = getPreferenceScreen();
		int count = preferenceScreen.getPreferenceCount();
		for (int i = 0; i < count; i++) {
			Preference preference = preferenceScreen.getPreference(i);
			if (!(preference instanceof CheckBoxPreference)) {
				String value = sharedPreferences.getString(preference.getKey(), "");
//				preference.setSummary(value);
				setPreferenceSummary(preference, value);
			}
		}
//		Preference preference = findPreference(getString(R.string.pref_size_key));
//		preference.setOnPreferenceChangeListener(this);
	}

	private void setPreferenceSummary(Preference preference, String value) {
		if (preference instanceof ListPreference) {
			ListPreference listPreference = (ListPreference)preference;
			int index = ((ListPreference) preference).findIndexOfValue(value);
			if (index <= 0) {
				preference.setSummary(listPreference.getEntries()[index]);
			}
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Preference preference = findPreference(key);
		if (preference != null) {
			if (!(preference instanceof CheckBoxPreference)) {
				String value = sharedPreferences.getString(preference.getKey(), "");
//				preference.setSummary(value);
				setPreferenceSummary(preference, value);
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}


	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		Toast error = Toast.makeText(getContext(), "Please select a number between 0.1 and 3", Toast.LENGTH_SHORT);

//		String sizeKey = getString(R.string.pref_size_key);
//		if (preference.getKey().equals(sizeKey)) {
			String stringSize = (String) newValue;
			try {
				float size = Float.parseFloat(stringSize);
				if (size > 3 || size <= 0) {
					error.show();
					return false;
				}
			} catch (NumberFormatException nfe) {
				error.show();
				return false;
			}
//		}
		return true;
	}
}

package com.yumatechnical.konnectandroid;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yumatechnical.konnectandroid.Adapter.CustomDialogListAdapter;
import com.yumatechnical.konnectandroid.Helper.Tools;
import com.yumatechnical.konnectandroid.Model.ConnectionItem;
import com.yumatechnical.konnectandroid.Model.ListItem;
import com.yumatechnical.konnectandroid.Model.MyViewModel;
import com.yumatechnical.konnectandroid.Settings.SettingsActivity;

import java.util.ArrayList;

public class MyOptionsMenu extends MainActivity {

	private static final String TAG = MyOptionsMenu.class.getSimpleName();
	private MyViewModel model;

	/**
	 * Methods for setting up the menu
	 **/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		model = ViewModelProviders.of(this).get(MyViewModel.class);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.visualizer_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem menuItem;

//		if (!((Vars)this.getApplication()).isNetworkConnected()) {
		if (!model.isNetworkConnected()) {
			menuItem = menu.findItem(R.id.add_connect);
			menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_plus)
					.color(IconicsColor.colorRes(R.color.white)).size(IconicsSize.TOOLBAR_ICON_SIZE));
		}
		menuItem = menu.findItem(R.id.settings);
		menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_cog)
				.color(IconicsColor.colorRes(R.color.white)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		menuItem = menu.findItem(R.id.resize);
		menuItem.setIcon(new IconicsDrawable(this, FontAwesome.Icon.faw_arrows_alt)
				.color(IconicsColor.colorRes(R.color.white)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		return true;
	}

/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		Intent intent;
		switch (item.getItemId()) {
			case R.id.add_connect:
				optionsMenuAddConnection();
				return true;
			case R.id.settings:
				optionsMenuSettings();
				return true;
			case R.id.resize:
				optionsMenuResize();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
*/

	void optionsMenuResize() {
		final View sliderView = View.inflate(this, R.layout.size_slider, null);
		SeekBar slider = sliderView.findViewById(R.id.sb_slider);
		if (right != null) {
			maxIconSize = right.getWidth();
		}
		Log.d(TAG, "right width="+ maxIconSize);
		slider.setMax(maxIconSize);
		int factorPercent = (maxIconSize - minIconSize) / 100;
//		int itemSize = ((Vars) getApplicationContext()).getIconSize();
		int itemSize = model.getIconSize().getValue();
		slider.setProgress((itemSize-minIconSize)*factorPercent);
		int initial = itemSize;
		slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				seekBar.setProgress(progress);
//				((Vars) getApplicationContext()).setIconSize((progress / factorPercent) + minIconSize);
				model.setIconSize((progress / factorPercent) + minIconSize);
//						if (((Fragment)RightFragment()).rightAdapter != null) {
//							((Fragment)RightFragment()).rightAdapter.notifyDataSetChanged();
//						}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		AlertDialog.Builder resizeBuilder = new AlertDialog.Builder(this);
		resizeBuilder.setView(sliderView);
//				builder.setItems(colors, (dialog12, which) -> Log.d(TAG, "the user selected:"+ which));
		final AlertDialog resizeDialog = resizeBuilder.create();
		resizeDialog.setTitle(getResources().getString(R.string.icon_resize));
		resizeDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(android.R.string.ok),
				(dialog1, which) -> dialog1.cancel());
		resizeDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(android.R.string.cancel),
				(dialog1, which) -> {
					slider.setProgress(initial);
//					((Vars) getApplicationContext()).setIconSize(initial);
					model.setIconSize(initial);
					dialog1.cancel();
				});
		resizeDialog.show();
	}

}

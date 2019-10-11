package com.yumatechnical.konnectandroid.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.yumatechnical.konnectandroid.Helper.Network.TestInternetLoader;
import com.yumatechnical.konnectandroid.R;

public class LoadSMBfragment extends Fragment implements LoaderManager.LoaderCallbacks {

	private ProgressDialog dialog;
//	AlertDialog dialog;
	String uri;


	public LoadSMBfragment(String uri) {
		this.uri = uri;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
//		final View view = inflater.inflate(R.layout.fragment_changing_wallpaper, container, false);
//		return view;
	}

	@NonNull
	@Override
	public Loader onCreateLoader(int id, @Nullable Bundle args) {
		if (id == 0 || id == 2) {
			if (id == 0)
				dialog.setMessage("Checking Connectivity...");
			if (id == 2)
				dialog.setMessage("Loading Settings...");
			dialog.show();
//			return new TestSMBconnectionLoader(getContext(), uri);
			return new TestInternetLoader(getContext());
		}
		return null;
	}

	@Override
	public void onLoadFinished(@NonNull Loader loader, Object data) {
		int id = loader.getId();
		if (id == 0 || id == 2) {
			boolean check = (Boolean) data;
			if (check) {
				if (dialog.isShowing()) {dialog.dismiss();}
			}
			else{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
				alertDialog.setTitle("Info");
				alertDialog.setMessage(R.string.failed_connect);
				alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
				alertDialog.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
							}
						});
				alertDialog.show();
			}
		}
	}

	@Override
	public void onLoaderReset(@NonNull Loader loader) {
	}
}

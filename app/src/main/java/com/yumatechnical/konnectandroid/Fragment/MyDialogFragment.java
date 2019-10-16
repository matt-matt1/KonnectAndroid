package com.yumatechnical.konnectandroid.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.yumatechnical.konnectandroid.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * from : https://guides.codepath.com/android/using-dialogfragment
 */
public class MyDialogFragment extends DialogFragment {

	private int width, height;
	private float percent;
	private boolean useCustomWidth = false, useCustomHeight = false, useCustomPercent = false,
			useCustomWidthPercent = false, useCustomHeightPercent = false, useFullScreen = false;
//	private String title, message, neutralButtonLabel, positiveButtonLabel, negativeButtonLabel;
//	private @StyleRes int style;
	public interface OnMyDialogInteraction {
		void AlertDialogNeutralButtonPressed(Button view, AlertDialog dialog, int id);
		void AlertDialogNegativeButtonPressed(Button view, AlertDialog dialog, int id);
		void AlertDialogPositiveButtonPressed(Button view, AlertDialog dialog, int id);
		void AlertDialogPutInOnViewCreated(View view, Bundle bundle, int id);
		// ^Get field from view - eg:
//		mEditText = view.findViewById(R.id.txt_your_name);
	// Show soft keyboard automatically and request focus to field
//		mEditText.requestFocus();
//		Objects.requireNonNull(getDialog().getWindow()).setSoftInputMode(
//				LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		void AlertDialogOnBeforeCreate(AlertDialog.Builder alertDialogBuilder);
	}
//	private View view;
	private int id;


	public MyDialogFragment() {}


	public static MyDialogFragment newInstance() {
		MyDialogFragment frag = new MyDialogFragment();
		return frag;
	}

	public static MyDialogFragment newInstance(@LayoutRes int layout, @StyleRes int style,
	                                           String title, String message,
	                                           String neutralButtonLabel,
	                                           String positiveButtonLabel,
	                                           String negativeButtonLabel) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putString("neutralButtonLabel", neutralButtonLabel);
		args.putString("positiveButtonLabel", positiveButtonLabel);
		args.putString("negativeButtonLabel", negativeButtonLabel);
		args.putInt("style", style);
		args.putInt("layout", layout);
		args.putString("message", message);
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}


	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		@LayoutRes int layout = Objects.requireNonNull(getArguments()).getInt("layout",
				R.layout.ftp_settings_form);
		return Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(layout, container);
	}


	@Override
	public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (getArguments() != null) {
			this.width = getArguments().getInt("width", 0);
			if (this.width > 0) {
				useCustomWidth = true;
			}
			this.height = getArguments().getInt("height", 0);
			if (this.height > 0) {
				useCustomHeight = true;
			}
			this.percent = getArguments().getFloat("percent", 0);
			if (this.percent > 0) {
				useCustomPercent = true;
			}
			if (getArguments().getBoolean("fullScreen", false)) {
				useFullScreen = true;
			}
		}
//		this.view = view;
		OnMyDialogInteraction listener = (OnMyDialogInteraction) getActivity();
		Objects.requireNonNull(listener).AlertDialogPutInOnViewCreated(view, savedInstanceState, id);
	}


	@NotNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
		if (getArguments() != null) {
			if (getArguments().containsKey("id")) {
				this.id = getArguments().getInt("id");
			}
			if (getArguments().containsKey("style")) {
				@StyleRes int style = Objects.requireNonNull(getArguments()).getInt("style", 0);
				alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), style);
			} else {
				alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
			}
			if (getArguments().containsKey("title")) {
				String title = Objects.requireNonNull(getArguments()).getString("title");
				alertDialogBuilder.setTitle(title);
			}
			if (getArguments().containsKey("message")) {
				String message = Objects.requireNonNull(getArguments()).getString("message");
				alertDialogBuilder.setMessage(message);
			}
			if (getArguments().containsKey("layout")) {
				@LayoutRes int layout = Objects.requireNonNull(getArguments()).getInt("layout");
				alertDialogBuilder.setView(layout);
			}
			if (getArguments().containsKey("cancelable")) {
				if (getArguments().getBoolean("cancelable")) {
					alertDialogBuilder.setCancelable(true);
				} else {
					alertDialogBuilder.setCancelable(false);
				}
			}
		}
		OnMyDialogInteraction listener = (OnMyDialogInteraction) getActivity();
		if (listener != null) {
			listener.AlertDialogOnBeforeCreate(alertDialogBuilder);
		}
		AlertDialog dialog = alertDialogBuilder.create();
		if (getArguments().containsKey("neutralButtonLabel")) {
			String neutralButtonLabel = Objects.requireNonNull(getArguments()).getString("neutralButtonLabel");
			dialog.setButton(AlertDialog.BUTTON_NEUTRAL, neutralButtonLabel, (DialogInterface.OnClickListener) null);
		}
		if (getArguments().containsKey("negativeButtonLabel")) {
			String negativeButtonLabel = Objects.requireNonNull(getArguments()).getString("negativeButtonLabel");
			dialog.setButton(AlertDialog.BUTTON_NEGATIVE, negativeButtonLabel, (DialogInterface.OnClickListener) null);
		}
		if (getArguments().containsKey("positiveButtonLabel")) {
			String positiveButtonLabel = Objects.requireNonNull(getArguments()).getString("positiveButtonLabel");
			dialog.setButton(AlertDialog.BUTTON_POSITIVE, positiveButtonLabel, (DialogInterface.OnClickListener) null);
		}
		return dialog;
	}


	public void setHeight(int height) {
		this.height = height;
		useCustomHeight = true;
	}

	public void setWidth(int width) {
		this.width = width;
		useCustomWidth = true;
	}

	public void setPercent(float percent) {
		this.percent = percent;
		useCustomPercent = true;
	}


	@Override
	public void onResume() {
		if (useCustomWidth && useCustomHeight) {
			Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
		} else if (useCustomHeightPercent || useCustomWidthPercent) {
			Window window = Objects.requireNonNull(getDialog()).getWindow();
			Point size = new Point();
			Display display = Objects.requireNonNull(window).getWindowManager().getDefaultDisplay();
			display.getSize(size);
			if (useCustomWidthPercent) {
				window.setLayout((int) (size.x * percent), LayoutParams.WRAP_CONTENT);
			}
			window.setGravity(Gravity.CENTER);
		} else if (useFullScreen) {
			LayoutParams params = Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).getAttributes();
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.MATCH_PARENT;
			Objects.requireNonNull(getDialog().getWindow()).setAttributes(params);
		}
		OnMyDialogInteraction listener = (OnMyDialogInteraction) getActivity();
		AlertDialog dialog = (AlertDialog) getDialog();
		if (dialog != null && getArguments() != null && getArguments().containsKey("neutralButtonLabel")) {
			dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
				if (listener != null) {
					listener.AlertDialogNeutralButtonPressed((Button)v, dialog, id);
				}
			});
		}
		if (dialog != null && getArguments() != null && getArguments().containsKey("negativeButtonLabel")) {
			dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
				if (listener != null) {
					listener.AlertDialogNegativeButtonPressed((Button)v, dialog, id);
				}
			});
		}
		if (dialog != null && getArguments() != null && getArguments().containsKey("positiveButtonLabel")) {
			dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
				if (listener != null) {
					listener.AlertDialogPositiveButtonPressed((Button)v, dialog, id);
				}
			});
		}
		super.onResume();
	}

}
package com.yumatechnical.konnectandroid.Fragment;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

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
	private String title, message, neutralButtonLabel, positiveButtonLabel, negativeButtonLabel;
	private @StyleRes int style;
//	private EditText mEditText;
	public interface OnMyDialogInteraction {
//		void AddToView();
//		void setButton(int which, String label);
		void neutralButtonPressed();
		void negativeButtonPressed();
		void positiveButtonPressed();
		void putInOnViewCreated(View view, Bundle bundle);
		// ^Get field from view - eg:
//		mEditText = view.findViewById(R.id.txt_your_name);
	// Show soft keyboard automatically and request focus to field
//		mEditText.requestFocus();
//		Objects.requireNonNull(getDialog().getWindow()).setSoftInputMode(
//				LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		void onBeforeCreate(AlertDialog.Builder alertDialogBuilder);
	}
//	private OnMyDialogInteraction listener;


	public MyDialogFragment() {}


	public static MyDialogFragment newInstance(String title) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(String title,
	                                                 int width, int height) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putInt("width", width);
		args.putInt("height", height);
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(String title,
	                                                 boolean fullScreen) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putBoolean("fullScreen", fullScreen);
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(String title, float percent) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putFloat("percent", percent);
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(@LayoutRes int layout, String title) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putInt("layout", layout);
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(@LayoutRes int layout, String title,
	                                                 int width, int height) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putInt("width", width);
		args.putInt("height", height);
		args.putInt("layout", layout);
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(@LayoutRes int layout, String title,
	                                                 boolean fullScreen) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putBoolean("fullScreen", false);
		args.putInt("layout", layout);
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(@LayoutRes int layout, String title, float percent) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putFloat("percent", percent);
		args.putInt("layout", layout);
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(String title, String message,
	                                                 String neutralButtonLabel,
	                                                 String positiveButtonLabel,
	                                                 String negativeButtonLabel,
	                                           OnMyDialogInteraction listener) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		args.putString("neutralButtonLabel", neutralButtonLabel);
		args.putString("positiveButtonLabel", positiveButtonLabel);
		args.putString("negativeButtonLabel", negativeButtonLabel);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(String title, String message,
	                                                 int width, int height) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putInt("width", width);
		args.putInt("height", height);
		args.putString("title", title);
		args.putString("message", message);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(String title, String message,
	                                                 boolean fullScreen) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putBoolean("fullScreen", false);
		args.putString("title", title);
		args.putString("message", message);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(String title, String message, float percent) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putFloat("percent", percent);
		args.putString("title", title);
		args.putString("message", message);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(@LayoutRes int layout, String title, String message) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putInt("layout", layout);
		args.putString("title", title);
		args.putString("message", message);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(@LayoutRes int layout, String title, String message,
	                                                 int width, int height) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putInt("width", width);
		args.putInt("height", height);
		args.putInt("layout", layout);
		args.putString("title", title);
		args.putString("message", message);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(@LayoutRes int layout, String title, String message,
	                                                 boolean fullScreen) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putBoolean("fullScreen", fullScreen);
		args.putInt("layout", layout);
		args.putString("message", message);
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	public static MyDialogFragment newInstance(@LayoutRes int layout, String title, String message,
	                                                 float percent) {
		MyDialogFragment frag = new MyDialogFragment();
		Bundle args = new Bundle();
		args.putFloat("percent", percent);
		args.putInt("layout", layout);
		args.putString("message", message);
		args.putString("title", title);
		frag.setArguments(args);
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
//		return inflater.inflate(layout, container);
//		^Note: There is currently a bug in the support library that causes styles not to show up properly. Thus:
		return Objects.requireNonNull(getActivity()).getLayoutInflater().inflate(layout, container);
	}

	@Override
	public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (getArguments().getInt("width", 0) > 0) {
			this.width = getArguments().getInt("width");
			useCustomWidth = true;
		}
		if (getArguments().getInt("height", 0) > 0) {
			this.height = getArguments().getInt("height");
			useCustomHeight = true;
		}
		if (getArguments().getFloat("percent", 0) > 0) {
			this.percent = getArguments().getFloat("percent");
			useCustomPercent = true;
		}
		if (getArguments().getBoolean("fullScreen", false)) {
			useFullScreen = true;
		}
		message = Objects.requireNonNull(getArguments()).getString("message", "");
		Objects.requireNonNull(getDialog()).setTitle(message);
		title = Objects.requireNonNull(getArguments()).getString("title", "");
		Objects.requireNonNull(getDialog()).setTitle(title);
		OnMyDialogInteraction listener = (OnMyDialogInteraction) getActivity();
		listener.putInOnViewCreated(view, savedInstanceState);
	}

	@NotNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alertDialogBuilder;
		title = Objects.requireNonNull(getArguments()).getString("title");
		message = Objects.requireNonNull(getArguments()).getString("message");
		style = Objects.requireNonNull(getArguments()).getInt("style", 0);
		neutralButtonLabel = Objects.requireNonNull(getArguments()).getString("neutralButtonLabel");
		negativeButtonLabel = Objects.requireNonNull(getArguments()).getString("negativeButtonLabel");
		positiveButtonLabel = Objects.requireNonNull(getArguments()).getString("positiveButtonLabel");
		if (title == null || title.equals("")) {
			Dialog dialog = super.onCreateDialog(savedInstanceState);
			// request a window without the title
			Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
//			alertDialogBuilder = dialog.create();//dialog without titlebar
			return dialog;
		} else {
			if (style != 0) {
				alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), style);
			} else {
				alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
			}
			alertDialogBuilder.setTitle(title);
//			alertDialogBuilder.setMessage("Are you sure?");
//			alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// on success
//				}
//			});
//			alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					if (dialog != null /*&& dialog.isShowing()*/) {
//						dialog.dismiss();
//					}
//				}
//			});
//			return alertDialogBuilder.create();
		}
		if (message != null && !message.equals("")) {
			message = Objects.requireNonNull(getArguments()).getString("message");
//			alertDialogBuilder.setMessage("Are you sure?");
			alertDialogBuilder.setMessage(message);
		}
		if (neutralButtonLabel != null && !neutralButtonLabel.equals("")) {
			alertDialogBuilder.setNeutralButton(negativeButtonLabel, null);
		}
		if (negativeButtonLabel != null && !negativeButtonLabel.equals("")) {
			alertDialogBuilder.setNeutralButton(negativeButtonLabel, null);
		}
		if (positiveButtonLabel != null && !positiveButtonLabel.equals("")) {
			alertDialogBuilder.setNeutralButton(positiveButtonLabel, null);
		}
//		alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// on success
//			}
//		});
//		alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				if (dialog != null /*&& dialog.isShowing()*/) {
//					dialog.dismiss();
//				}
//			}
//		});
		OnMyDialogInteraction listener = (OnMyDialogInteraction) getActivity();
		listener.onBeforeCreate(alertDialogBuilder);
		return alertDialogBuilder.create();
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
//		int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
//		int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
		if (useCustomWidth && useCustomHeight) {
			Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setLayout(width, height);
			// Call super onResume after sizing
		} else if (useCustomHeightPercent || useCustomWidthPercent) {
			// Store access variables for window and blank point
			Window window = Objects.requireNonNull(getDialog()).getWindow();
			Point size = new Point();
			// Store dimensions of the screen in `size`
			Display display = Objects.requireNonNull(window).getWindowManager().getDefaultDisplay();
			display.getSize(size);
			// Set the width of the dialog proportional to 75% of the screen width
			if (useCustomWidthPercent) {
				window.setLayout((int) (size.x * percent), LayoutParams.WRAP_CONTENT);
			}
			window.setGravity(Gravity.CENTER);
			// Call super onResume after sizing
		} else if (useFullScreen) {
			// Get existing layout params for the window
			LayoutParams params = Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).getAttributes();
			// Assign window properties to fill the parent
			params.width = LayoutParams.MATCH_PARENT;
			params.height = LayoutParams.MATCH_PARENT;
			Objects.requireNonNull(getDialog().getWindow()).setAttributes(params);
			// Call super onResume after sizing
		}
		OnMyDialogInteraction listener = (OnMyDialogInteraction) getActivity();
		if (neutralButtonLabel != null && !neutralButtonLabel.equals("")) {
			listener.neutralButtonPressed();
		}
		if (negativeButtonLabel != null && !negativeButtonLabel.equals("")) {
			listener.negativeButtonPressed();
		}
		if (positiveButtonLabel != null && !positiveButtonLabel.equals("")) {
			listener.positiveButtonPressed();
		}
		super.onResume();
	}

}
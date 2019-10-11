package com.yumatechnical.konnectandroid.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.yumatechnical.konnectandroid.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * use: new AlertDialogFragment().show(getSupportFragmentManager(), "alertdialog_sample");
 */
public class AlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

	private String title, message, btnPosLabel, btnNegLabel;
	private Spanned spannedTitle,  spannedMessage;
	private DialogInterface.OnClickListener btnPos, btnNeg;
	private Activity activity;
	private @LayoutRes int layout = 0;

	public interface AlertDialogCallback<T> {
		void alertDialogCallback(T ret);
	}
	public interface AlertDialogListener {
		void OnButtonClick(int button);
	}
	private AlertDialogListener listener;


	public AlertDialogFragment() {}


	public static AlertDialogFragment newInstance() {
		AlertDialogFragment frag = new AlertDialogFragment();
		return frag;
	}

	public static AlertDialogFragment newInstance(String title) {
		AlertDialogFragment frag = new AlertDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}

	public static AlertDialogFragment newInstance(@LayoutRes int layoutResourceId, String title) {
		AlertDialogFragment frag = new AlertDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putInt("layout", layoutResourceId);
		frag.setArguments(args);
		return frag;
	}

	public static AlertDialogFragment newInstance(@LayoutRes int layoutResourceId, String title, String message) {
		AlertDialogFragment frag = new AlertDialogFragment();
		Bundle args = new Bundle();
		args.putInt("layout", layoutResourceId);
		args.putString("title", title);
		args.putString("message", message);
		frag.setArguments(args);
		return frag;
	}
/*
	public static AlertDialogFragment newInstance(@LayoutRes int layout, String title, String message) {
		AlertDialogFragment frag = new AlertDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		frag.setArguments(args);
		return frag;
	}
*/
	//	public static AlertDialogFragment newInstance(Activity activity, @LayoutRes int layout) {
	public static AlertDialogFragment newInstance(String title, String message, String btnPosLabel,
	                                              String btnNegLabel) {
		AlertDialogFragment frag = new AlertDialogFragment();
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		args.putString("btnPosLabel", btnPosLabel);
		args.putString("btnNegLabel", btnNegLabel);
		frag.setArguments(args);
//		this.activity = activity;
//		this.layout = layout;
		return frag;
	}

//	public AlertDialogFragment(String title, String message, String btnPosLabel, String btnNegLabel,
//	                           DialogInterface.OnClickListener btnPos, DialogInterface.OnClickListener bbtnNeg) {
//		this.title = title;
//		this.message = message;
//		this.btnPosLabel = btnPosLabel;
//		this.btnNegLabel = btnNegLabel;
//		this.btnPos = btnPos;
//		this.btnNeg = bbtnNeg;
//	}


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (layout != 0) {
			return getActivity().getLayoutInflater().inflate(layout, container);
		} else {
			return super.onCreateView(inflater, container, savedInstanceState);
		}
	}

	public void setLayout(@LayoutRes int layoutResourceId) {
		this.layout = layoutResourceId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitle(Spanned title) {
		this.spannedTitle = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(Spanned message) {
		this.spannedMessage = message;
	}

	public void setButton(int buttonId, String string, DialogInterface.OnClickListener o) {
		switch (buttonId) {
			case AlertDialog.BUTTON_NEUTRAL:
				break;
			case AlertDialog.BUTTON_NEGATIVE:
				setNegativeButton(string, o);
			case AlertDialog.BUTTON_POSITIVE:
				setPositiveButton(string, o);
		}
	}

	public void setPositiveButton(String btnPosLabel, DialogInterface.OnClickListener onClickBtnPos) {
		this.btnPosLabel = btnPosLabel;
		this.btnPos = onClickBtnPos;
	}

	public void setNegativeButton(String btnNegLabel, DialogInterface.OnClickListener onClickBtnNeg) {
		this.btnNegLabel = btnNegLabel;
		this.btnNeg = onClickBtnNeg;
	}


	@NotNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		if (spannedTitle == null || !spannedTitle.toString().isEmpty()) {
			if (title == null || title.isEmpty()) {
				if (savedInstanceState != null) {
					title = getArguments().getString("title", "tItLe");
				} else {
					title = "dtitle";
				}
			}
//		} else {
//			title = spannedTitle;
//		}
		if (message == null) {
			if (savedInstanceState != null) {
				message = getArguments().getString("message", "");
			} else {
				message = "dmessage";
			}
		}
		if (btnPosLabel == null || btnPosLabel.isEmpty()) {
			if (savedInstanceState != null) {
				btnPosLabel = getArguments().getString("btnPosLabel", "Positive");
			} else {
				btnPosLabel = "";
			}
		}
		if (btnNegLabel == null) {
			if (savedInstanceState != null) {
				btnNegLabel = getArguments().getString("btnNegLabel", "");
			} else {
				btnNegLabel = "";
			}
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
		if (spannedTitle != null && !spannedTitle.equals("")) {
			builder.setTitle(spannedTitle);
		} else if (getArguments() != null) {
			builder.setTitle(getArguments().getString("title", "title"));
		} else if (title != null && !title.equals("")) {
			builder.setTitle(title);
		} else {
			builder.setTitle("title");
		}
		return (builder.setTitle(title).setMessage(message)
				.setPositiveButton(btnPosLabel, btnPos)
				.setNegativeButton(btnNegLabel, btnNeg)
				.create());
	}

	@Override
	public void onCancel(@NotNull DialogInterface dialog) {
		super.onCancel(dialog);
	}

	@Override
	public void onDismiss(@NotNull DialogInterface dialog) {
		super.onDismiss(dialog);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
//		Toast.makeText(getActivity(), "which is " + which, Toast.LENGTH_LONG).show();
		listener.OnButtonClick(which);
	}

	@Override
	public void onResume() {
		super.onResume();
		AlertDialog alertDialog = (AlertDialog) getDialog();
		Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Do your stuff here
			}
		});
	}
}
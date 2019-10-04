package com.yumatechnical.konnectandroid.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * use: new AlertDialogFragment().show(getSupportFragmentManager(), "alertdialog_sample");
 */
public class AlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

	private String title, message, btnPosLabel, btnNegLabel;
	private DialogInterface.OnClickListener btnPos, btnNeg;


//	public AlertDialogFragment(String title, String message, String btnPosLabel, String btnNegLabel,
//	                           DialogInterface.OnClickListener btnPos, DialogInterface.OnClickListener bbtnNeg) {
//		this.title = title;
//		this.message = message;
//		this.btnPosLabel = btnPosLabel;
//		this.btnNegLabel = btnNegLabel;
//		this.btnPos = btnPos;
//		this.btnNeg = bbtnNeg;
//	}


	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
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
		if (title == null || title.isEmpty()) {
			title = "tItLe";
		}
		if (message == null) {
			message = "";
		}
		if (btnPosLabel == null || btnPosLabel.isEmpty()) {
			btnPosLabel = "Positive";
		}
		if (btnNegLabel == null) {
			btnNegLabel = "";
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
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
		Toast.makeText(getActivity(), "which is " + which, Toast.LENGTH_LONG).show();
	}

}
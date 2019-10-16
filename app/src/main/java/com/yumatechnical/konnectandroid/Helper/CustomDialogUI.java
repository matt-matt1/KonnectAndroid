package com.yumatechnical.konnectandroid.Helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yumatechnical.konnectandroid.R;


public class CustomDialogUI {

	private Dialog dialog;
//	private Vibrator vib;
	private RelativeLayout rl;

	public interface OnDialogInteraction {
		void PressedNeutralButton();
		void PressedNegativeButton();
		void PressedPositiveButton();
	}
//	private OnDialogInteraction listener;


	public void dialog(final Context context, String title, String message,
	                   String negativeButtonLabel, String positiveButtonLabel,
	                   String neutralButtonLabel, OnDialogInteraction listener/*, final Runnable task*/) {
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		dialog.setCancelable(false);
		TextView m = dialog.findViewById(R.id.message);
		TextView t = dialog.findViewById(R.id.title);
		final Button n = dialog.findViewById(R.id.button2);
		final Button p = dialog.findViewById(R.id.next_button);
//		rl = dialog.findViewById(R.id.rlmain);
		rl = dialog.findViewById(R.id.rl_dialog_background);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			rl.setClipToOutline(true);
		}
		t.setText(bold(title));
		m.setText(message);
		dialog.show();
		if (negativeButtonLabel != null) {
			n.setText(negativeButtonLabel.equals("")
					? bold(context.getString(android.R.string.cancel))
					: negativeButtonLabel);
		}
		if (positiveButtonLabel != null) {
			p.setText(positiveButtonLabel.equals("")
					? bold(context.getString(android.R.string.ok))
					: positiveButtonLabel);
		}
		if (neutralButtonLabel != null) {
			p.setText(neutralButtonLabel.equals("")
					? bold(context.getString(R.string.neutral))
					: neutralButtonLabel);
		}
		// color(context,rl);
//		vib = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
		n.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listener.PressedNegativeButton();
//				vib.vibrate(15);
				dialog.dismiss();
			}
		});
		p.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listener.PressedPositiveButton();
//				vib.vibrate(20);
				dialog.dismiss();
//				task.run();
			}
		});
	}
	//customize text style bold italic....
	public SpannableString bold(String s) {
		SpannableString spanString = new SpannableString(s);
		spanString.setSpan(new StyleSpan(Typeface.BOLD), 0,
				spanString.length(), 0);
		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		// spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0,
		// spanString.length(), 0);
		return spanString;
	}
}

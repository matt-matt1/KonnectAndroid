package com.yumatechnical.konnectandroid.Helper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yumatechnical.konnectandroid.R;


public class showFTPsettingsActivity extends AppCompatActivity {

//	private TextView title;
//	private ImageView rename;
	private TextView hlabel, ulabel, plabel, tlabel;
	private EditText title, hname, uname, pword, portn, etitle;
	private ImageView spinner;
	private RadioButton radioFTP, radioFTPS, radioSFTP;


	public void connectViewElements(View view) {
//		title = view.findViewById(R.id.tv_ftp_title);
//		rename = view.findViewById(R.id.iv_ftp_rename);
		hlabel = view.findViewById(R.id.tv_ftp_host);
		ulabel = view.findViewById(R.id.tv_ftp_username);
		plabel = view.findViewById(R.id.tv_ftp_password);
		tlabel = view.findViewById(R.id.tv_ftp_port);
		hname = view.findViewById(R.id.et_ftp_host);
		uname = view.findViewById(R.id.et_ftp_username);
		pword = view.findViewById(R.id.et_ftp_password);
		portn = view.findViewById(R.id.et_ftp_port);
		etitle = view.findViewById(R.id.et_ftp_rename);
		spinner = view.findViewById(R.id.iv_ftp_progress);
		radioFTP = view.findViewById(R.id.rb_ftp_plain);
		radioSFTP = view.findViewById(R.id.rb_sftp);
		radioFTPS = view.findViewById(R.id.rb_ftps);
	}


	public void setLabels() {
//		title.setText(Tools.toTitleCase(getString(R.string.eg_ip_addr)));
		title.setHint(Tools.toTitleCase(getString(R.string.eg_conn)));
		hlabel.setText(Tools.toTitleCase(getString(R.string.server)));
		ulabel.setText(Tools.toTitleCase(getString(R.string.username)));
		plabel.setText(Tools.toTitleCase(getString(R.string.password)));
		tlabel.setText(Tools.toTitleCase(getString(R.string.port)));
		spinner.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_spinner)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE));
//		rename.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_edit)
//				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		final boolean[] openedRename = {false};
//		rename.setOnClickListener(v -> {
//			if (openedRename[0]) {
//				openedRename[0] = false;
//				etitle.setVisibility(View.GONE);
//			} else {
//				openedRename[0] = true;
//				etitle.setVisibility(View.VISIBLE);
//			}
//		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		View view = R.layout.ftp_settings_form;
		setContentView(R.layout.ftp_settings_form);
		connectViewElements(this.findViewById(android.R.id.content).getRootView());
		setLabels();
	}
}

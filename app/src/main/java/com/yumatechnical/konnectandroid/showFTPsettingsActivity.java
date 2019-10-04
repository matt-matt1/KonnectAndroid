package com.yumatechnical.konnectandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.iconics.IconicsColor;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.IconicsSize;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yumatechnical.konnectandroid.Helper.Tools;

public class showFTPsettingsActivity extends AppCompatActivity {

	private TextView title;
	private ImageView rename;
	private TextView hlabel;
	private TextView ulabel;
	private TextView plabel;
	private TextView tlabel;
	private EditText hname;
	private EditText uname;
	private EditText pword;
	private EditText portn;
	private EditText etitle;
	private ImageView spinner;


	private void connectViewElements() {
		title = findViewById(R.id.tv_ftp_title);
		rename = findViewById(R.id.iv_ftp_rename);
		hlabel = findViewById(R.id.tv_ftp_host);
		ulabel = findViewById(R.id.tv_ftp_username);
		plabel = findViewById(R.id.tv_ftp_password);
		tlabel = findViewById(R.id.tv_ftp_port);
		hname = findViewById(R.id.et_ftp_host);
		uname = findViewById(R.id.et_ftp_username);
		pword = findViewById(R.id.et_ftp_password);
		portn = findViewById(R.id.et_ftp_port);
		etitle = findViewById(R.id.et_ftp_rename);
		spinner = findViewById(R.id.iv_ftp_progress);
	}


	private void setLabels() {
		title.setText(Tools.toTitleCase(getString(R.string.eg_ip_addr)));
		hlabel.setText(Tools.toTitleCase(getString(R.string.server)));
		ulabel.setText(Tools.toTitleCase(getString(R.string.username)));
		plabel.setText(Tools.toTitleCase(getString(R.string.password)));
		tlabel.setText(Tools.toTitleCase(getString(R.string.port)));
		spinner.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_spinner)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE));
		rename.setImageDrawable(new IconicsDrawable(this, FontAwesome.Icon.faw_edit)
				.color(IconicsColor.colorRes(R.color.Teal)).size(IconicsSize.TOOLBAR_ICON_SIZE));

		final boolean[] openedRename = {false};
		rename.setOnClickListener(v -> {
			if (openedRename[0]) {
				openedRename[0] = false;
				etitle.setVisibility(View.GONE);
			} else {
				openedRename[0] = true;
				etitle.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ftp_settings_form);
		connectViewElements();
		setLabels();
	}
}

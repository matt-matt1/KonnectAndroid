package com.yumatechnical.konnectandroid;

import android.net.Uri;

import com.dropbox.core.v2.paper.Cursor;

import java.util.Date;

public class SharesnameContractContract extends Cursor {

	/**
	 * The authority of the notes content provider - this must match the authorities field
	 * specified in the AndroidManifest.xml provider section
	 */
	public static final String AUTHORITY = "com.yumatechnical.konnectandroid.SharenameSearchableActivity";

	/**
	 * The content URI for the top-level content provider
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	public SharesnameContractContract(String value, Date expiration) {
		super(value, expiration);
	}
}

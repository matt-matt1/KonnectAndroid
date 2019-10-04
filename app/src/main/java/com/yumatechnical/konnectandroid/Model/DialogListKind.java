package com.yumatechnical.konnectandroid.Model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DialogListKind {

	public static final int Normal = 0;
	public static final int Radios = 1;
	public static final int Checkboxes = 2;

	public DialogListKind(@DialogListKind.Season int season) {
		System.out.println("Season :" + season);
	}

	@IntDef({Normal,
			Radios,
			Checkboxes
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Season {
	}

	public static void main(String[] args) {
//		ContactsKind contactsKind = new ContactsKind(Home);
	}
}

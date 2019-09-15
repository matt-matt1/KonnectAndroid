package com.yumatechnical.konnectandroid.Model;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


//as per https://android.jlelse.eu/android-performance-avoid-using-enum-on-android-326be0794dc3
public class ContactsKind {

	public static final int Custom = 0;
	public static final int Home = 1;
	public static final int Mobile = 2;
	public static final int Work = 3;
	public static final int WorkFax = 4;
	public static final int HomeFax = 5;
	public static final int Pager = 6;
	public static final int Other = 7;
	public static final int Callback = 8;
	public static final int Car = 9;
	public static final int CompanyMain = 10;
	public static final int ISDN = 11;
	public static final int Main = 12;
	public static final int OtherFax = 13;
	public static final int Radio = 14;
	public static final int Telex = 15;
	public static final int TTY_TDD = 16;
	public static final int WorkMobile = 17;
	public static final int WorkPager = 18;
	public static final int Assistant = 19;
	public static final int MMS = 20;

	public ContactsKind(@Season int season) {
		System.out.println("Season :" + season);
	}

	@IntDef({Custom,
			Home,
			Mobile,
			Work,
			WorkFax,
			HomeFax,
			Pager,
			Other,
			Callback,
			Car,
			CompanyMain,
			ISDN,
			Main,
			OtherFax,
			Radio,
			TTY_TDD,
			WorkMobile,
			WorkPager,
			Assistant,
			Telex,
			MMS
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Season {
	}

	public static void main(String[] args) {
//		ContactsKind contactsKind = new ContactsKind(Home);
	}
}

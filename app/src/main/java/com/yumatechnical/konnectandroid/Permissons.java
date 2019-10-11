package com.yumatechnical.konnectandroid;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*
 * example:
 * if(!Permissons.Check_STORAGE(MainActivity.this))
 *{
 *   //if not permisson granted so request permisson with request code
 *   Permissons.Request_STORAGE(MainActivity.this,22);
 *}
 */
public class Permissons {

	//Request Permisson
	public static void Request_STORAGE(Activity act,int code)
	{
		ActivityCompat.requestPermissions(act, new
				String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},code);
	}
	public static void Request_CAMERA(Activity act,int code)
	{
		ActivityCompat.requestPermissions(act, new
				String[]{Manifest.permission.CAMERA},code);
	}
	public static void Request_FINE_LOCATION(Activity act,int code)
	{
		ActivityCompat.requestPermissions(act, new
				String[]{Manifest.permission.ACCESS_FINE_LOCATION},code);
	}
	public static void Request_READ_SMS(Activity act,int code)
	{
		ActivityCompat.requestPermissions(act, new
				String[]{Manifest.permission.READ_SMS},code);
	}
	public static void Request_READ_CONTACTS(Activity act,int code)
	{
		ActivityCompat.requestPermissions(act, new
				String[]{Manifest.permission.READ_CONTACTS},code);
	}
	public static void Request_READ_CALENDAR(Activity act,int code)
	{
		ActivityCompat.requestPermissions(act, new
				String[]{Manifest.permission.READ_CALENDAR},code);
	}
	public static void Request_RECORD_AUDIO(Activity act,int code)
	{
		ActivityCompat.requestPermissions(act, new
				String[]{Manifest.permission.RECORD_AUDIO},code);
	}
	public static void Request_this(Activity activity, int code, @NonNull String manifest_permission) {
		ActivityCompat.requestPermissions(activity, new String[]{manifest_permission}, code);
	}

	//Check Permisson
	public static boolean Check_STORAGE(Activity act)
	{
		int result = ContextCompat.checkSelfPermission(act,android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
		return result == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean Check_CAMERA(Activity act)
	{
		int result = ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA);
		return result == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean Check_FINE_LOCATION(Activity act)
	{
		int result = ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION);
		return result == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean Check_READ_SMS(Activity act)
	{
		int result = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_SMS);
		return result == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean Check_READ_CONTACTS(Activity act)
	{
		int result = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_CONTACTS);
		return result == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean Check_READ_CALENDAR(Activity act)
	{
		int result = ContextCompat.checkSelfPermission(act, Manifest.permission.READ_CALENDAR);
		return result == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean Check_RECORD_AUDIO(Activity act)
	{
		int result = ContextCompat.checkSelfPermission(act, Manifest.permission.RECORD_AUDIO);
		return result == PackageManager.PERMISSION_GRANTED;
	}
	public static boolean Check_this(Activity activity, @NonNull String manifest_permission) {
		int result = ContextCompat.checkSelfPermission(activity, manifest_permission);
		return result == PackageManager.PERMISSION_GRANTED;
	}

}

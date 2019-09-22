package com.yumatechnical.konnectandroid.Model;

import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
import android.net.Uri;
//import android.os.Build;
//import android.os.Parcel;
//import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FileItem /*implements Parcelable*/ {

/*	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public FileItem createFromParcel(Parcel in) {
			return new FileItem(in);
		}

		public FileItem[] newArray(int size) {
			return new FileItem[size];
		}
	};*/
	private String name;
	private Uri fullPath;
	private int ID;
	private Bitmap bitmap;
	private String MIME;
	private Boolean hasContents;
	private String sortKey;
	private ArrayList<MyPhone> phoneArrayList;

	public FileItem(String name, Uri fullPath, int ID, Bitmap bitmap, String MIME,
	                Boolean hasContents, String sortKey, ArrayList<MyPhone> phoneArrayList) {
		this.name = name;
		this.fullPath = fullPath;
		this.ID = ID;
		this.bitmap = bitmap;
		this.MIME = MIME;
		this.hasContents = hasContents;
		this.sortKey = sortKey;
		this.phoneArrayList = phoneArrayList;
	}

/*	protected FileItem(Parcel in) {
		this.name = in.readString();
		this.fullPath = in.readParcelable(Uri.class.getClassLoader());
		this.ID = in.readInt();
		this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
		this.MIME = in.readString();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			this.hasContents = in.readBoolean();
		}
		this.sortKey = in.readString();
	}
*/
	public String getName() {
		return name;
	}

	public Uri getFullPath() {
		return fullPath;
	}

	public int getID() {
		return ID;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public String getMIME() {
		return MIME;
	}

	public Boolean getHasContents() {
		return hasContents;
	}

	public String getSortKey() {
		return sortKey;
	}

	public ArrayList<MyPhone> getPhoneArrayList() {
		return phoneArrayList;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFullPath(Uri fullPath) {
		this.fullPath = fullPath;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public void setMIME(String MIME) {
		this.MIME = MIME;
	}


	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public void setHasContents(Boolean hasContents) {
		this.hasContents = hasContents;
	}

	public void setPhoneArrayList(ArrayList<MyPhone> phoneArrayList) {
		this.phoneArrayList = phoneArrayList;
	}

	@NonNull
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@NonNull
	@Override
	public String toString() {
		return "FileItem: ID="+ getID()+ ", NAME="+ getName()+ ", BITMAP="+ getBitmap()+ ", SORT_KEY="+
				getSortKey()+ ", PATH="+ getFullPath()+ ", HAS_CONTENTS="+ getHasContents()+
				", PHONE_LIST="+ getPhoneArrayList().toString()+ "\n";
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
/*
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeParcelable(fullPath, flags);
		dest.writeInt(ID);
//		dest.writeValue(drawable);
		dest.writeParcelable(bitmap, flags);
		dest.writeString(MIME);
	}
*/
}

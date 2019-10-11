package com.yumatechnical.konnectandroid.Model;

import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
import android.net.Uri;
//import android.os.Build;
//import android.os.Parcel;
//import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;

@Parcel
public class FileItem /*implements Parcelable*/ {

/*	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public FileItem createFromParcel(Parcel in) {
			return new FileItem(in);
		}

		public FileItem[] newArray(int size) {
			return new FileItem[size];
		}
	};*/
	String name;
	Uri fullPath;
	int ID;
	Bitmap bitmap;
	String MIME;
	Boolean hasContents;
	String sortKey;
	ArrayList<MyPhone> phoneArrayList;
	Boolean hideName;
	String label;


//	public FileItem() {}
	@ParcelConstructor
	public FileItem(String name, Uri fullPath, int ID, Bitmap bitmap, String MIME, Boolean hasContents,
	                String sortKey, ArrayList<MyPhone> phoneArrayList, Boolean hideName, String label) {
		this.name = name;
		this.fullPath = fullPath;
		this.ID = ID;
		this.bitmap = bitmap;
		this.MIME = MIME;
		this.hasContents = hasContents;
		this.sortKey = sortKey;
		this.phoneArrayList = phoneArrayList;
		this.hideName = hideName;
		this.label = label;
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

	public Boolean isHideName() {
		return hideName;
	}

	public Boolean getHideName() {
		return hideName;
	}

	public String getLabel() {
		return label;
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

	public void setHideName(Boolean hideName) {
		this.hideName = hideName;
	}

	public void setLabel(String label) {
		this.label = label;
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
		String rtn = "ID="+ getID()+ ", ";
		if (getName() != null) { rtn += "NAME="+ getName()+ ", "; }
		if (getBitmap() != null) { rtn += "BITMAP="+ getBitmap()+ ", "; }
		if (getMIME() != null) { rtn += "MIME="+ getMIME()+ ", "; }
		if (getSortKey() != null) { rtn += "SORT_KEY="+ getSortKey()+ ", "; }
		if (getFullPath() != null) { rtn += "PATH="+ getFullPath()+ ", "; }
		if (getHasContents() != null) { rtn += "HAS_CONTENTS="+ getHasContents()+ ", "; }
		if (getPhoneArrayList() != null && getPhoneArrayList().size() > 0) { rtn += "PHONE_LIST="+ getPhoneArrayList().toString()+ ", "; }
		if (isHideName() != null) { rtn += "HIDE_AME="+ isHideName()+ ", "; }
		if (getLabel() != null) { rtn += "LABEL="+ getLabel(); }
		rtn += "\n";
		return "FileItem: "+ rtn;
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

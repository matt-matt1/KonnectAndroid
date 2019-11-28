package com.yumatechnical.konnectandroid.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Item implements Parcelable {

//	Integer priority;
	Integer ID;
	String name;
//	String iconAsString;
	//	Drawable drawable;
//	Integer leftPadding;
//	Integer topPadding;
//	Integer botPadding;
//	Integer iconBeforeText;
//	Integer iconTextPadding;
//	Integer faded;
	String accessToken;
	String connectionStr;


	protected Item(Parcel in) {
//		priority = in.readInt();
		ID = in.readInt();
		name = in.readString();
//		iconAsString = in.readString();
//		leftPadding = in.readInt();
//		topPadding = in.readInt();
//		botPadding = in.readInt();
//		iconBeforeText = in.readInt();
//		iconTextPadding = in.readInt();
//		faded = in.readInt();
		accessToken = in.readString();
		connectionStr = in.readString();
	}

	//	public ListItem() {}
//	@ParcelConstructor
	public Item(/*int priority,*/ int ID, String name, /*String iconAsString,*/ /*Drawable drawable,*/ /*int leftPadding,
	                int topPadding, int botPadding, Boolean iconBeforeText, int iconTextPadding, Boolean faded,*/
	                String accessToken, String connectionStr) {
//		this.priority = priority;
		this.ID = ID;
		this.name = name;
//		this.iconAsString = iconAsString;
//		this.drawable = drawable;
//		this.leftPadding = leftPadding;
//		this.topPadding = topPadding;
//		this.botPadding = botPadding;
//		this.iconBeforeText = (iconBeforeText == true) ? 1 : 0;
//		this.iconTextPadding = iconTextPadding;
//		this.faded = (faded == true) ? 1 : 0;
		this.accessToken = accessToken;
		this.connectionStr = connectionStr;
	}
	/*
		protected ListItem(Parcel in) {
			if (in.readByte() == 0) {
				priority = null;
			} else {
				priority = in.readInt();
			}
			if (in.readByte() == 0) {
				type = null;
			} else {
				type = in.readInt();
			}
			name = in.readString();
			iconAsString = in.readString();
			if (in.readByte() == 0) {
				leftPadding = null;
			} else {
				leftPadding = in.readInt();
			}
			if (in.readByte() == 0) {
				topPadding = null;
			} else {
				topPadding = in.readInt();
			}
			if (in.readByte() == 0) {
				botPadding = null;
			} else {
				botPadding = in.readInt();
			}
			byte tmpIconBeforeText = in.readByte();
			iconBeforeText = tmpIconBeforeText == 0 ? null : tmpIconBeforeText == 1;
			if (in.readByte() == 0) {
				iconTextPadding = null;
			} else {
				iconTextPadding = in.readInt();
			}
			byte tmpFaded = in.readByte();
			faded = tmpFaded == 0 ? null : tmpFaded == 1;
			accessToken = in.readString();
			connectionStr = in.readString();
		}
	*/
	public static final Creator<Item> CREATOR = new Creator<Item>() {
		@Override
		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}

		@Override
		public Item[] newArray(int size) {
			return new Item[size];
		}
	};

//	public int getPriority() {
//		return priority;
//	}

	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

//	public String getIconAsString() {
//		return iconAsString;
//	}

//	public Drawable getDrawable() {
//		return drawable;
//	}

//	public int getLeftPadding() {
//		return leftPadding;
//	}
//
//	public int getTopPadding() {
//		return topPadding;
//	}
//
//	public int getBotPadding() {
//		return botPadding;
//	}

//	public Boolean getIconBeforeText() {
//		return iconBeforeText == 1;
//	}
//
//	public int getIconTextPadding() {
//		return iconTextPadding;
//	}
//
//	public Boolean getFaded() {
//		return faded == 1;
//	}
//
//	public void setPriority(int priority) {
//		this.priority = priority;
//	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public void setIconAsString(String iconAsString) {
//		this.iconAsString = iconAsString;
//	}

//	public void setDrawable(Drawable drawable) {
//		this.drawable = drawable;
//	}

//	public void setLeftPadding(int leftPadding) {
//		this.leftPadding = leftPadding;
//	}
//
//	public void setTopPadding(int topPadding) {
//		this.topPadding = topPadding;
//	}
//
//	public void setBotPadding(int botPadding) {
//		this.botPadding = botPadding;
//	}
//
//	public void setIconBeforeText(Boolean iconBeforeText) {
//		this.iconBeforeText = (iconBeforeText == true) ? 1 : 0;
//	}
//
//	public void setIconTextPadding(int iconTextPadding) {
//		this.iconTextPadding = iconTextPadding;
//	}
//
//	public void setFaded(Boolean faded) {
//		this.faded = (faded == true) ? 1 : 0;
//	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@NonNull
	@Override
	public String toString() {
		return "ListItem{ "+ "NAME="+ getName()+ /*", PRIORITY="+ getPriority()+*/ ", type="+ getID()+ /*", ICON_AS_STRING="+ getIconAsString()+*/
				/*", DRAWABLE="+ getDrawable()+*/ /*", PADDING_LEFT="+ getLeftPadding()+ ", PADDING_TOP="+ getTopPadding()+
				", PADDING_BOTTOM="+ getBotPadding()+ ", IS_ICON_BEFORE_TEXT="+ getIconBeforeText()+
				", PADDING_BETEEN_ICON_AND_TEXT="+ getIconTextPadding()+ ", IS_FADED="+ getFaded()+*/ "}\n";
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		return super.equals(obj);
	}

	@NonNull
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
/*		if (priority == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);* /
		dest.writeInt(priority);/ *
		}
		if (type == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);*/
		dest.writeInt(ID);/*
		}*/
		dest.writeString(name);/*
		dest.writeString(iconAsString);
		if (leftPadding == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);* /
		dest.writeInt(leftPadding);/ *
		}
		if (topPadding == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);* /
		dest.writeInt(topPadding);/ *
		}
		if (botPadding == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);* /
		dest.writeInt(botPadding);/ *
		}
		dest.writeByte((byte) (iconBeforeText == null ? 0 : iconBeforeText ? 1 : 2));* /
		dest.writeInt(iconBeforeText);/ *
		if (iconTextPadding == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);* /
		dest.writeInt(iconTextPadding);/ *
		}
		dest.writeByte((byte) (faded == null ? 0 : faded ? 1 : 2));* /
		dest.writeInt(faded);*/
		dest.writeString(accessToken);
		dest.writeString(connectionStr);
	}
/*
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		if (priority == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(priority);
		}
		if (type == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(type);
		}
		dest.writeString(name);
		dest.writeString(iconAsString);
		if (leftPadding == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(leftPadding);
		}
		if (topPadding == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(topPadding);
		}
		if (botPadding == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(botPadding);
		}
		dest.writeByte((byte) (iconBeforeText == null ? 0 : iconBeforeText ? 1 : 2));
		if (iconTextPadding == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(iconTextPadding);
		}
		dest.writeByte((byte) (faded == null ? 0 : faded ? 1 : 2));
		dest.writeString(accessToken);
		dest.writeString(connectionStr);
	}
	*/

}

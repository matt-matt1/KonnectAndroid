package com.yumatechnical.konnectandroid.Model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//@Parcel
public class ListItem implements Cloneable, Parcelable {

	Integer priority;
	Integer type;
	String name;
	String iconAsString;
	Drawable drawable;
	Integer leftPadding;
	Integer topPadding;
	Integer botPadding;
	Integer iconBeforeText;
	Integer iconTextPadding;
	Integer faded;
	String accessToken;
	String connectionStr;

/*
	protected ListItem(Parcel in) {
		priority = in.readInt();
		type = in.readInt();
		name = in.readString();
		iconAsString = in.readString();
		leftPadding = in.readInt();
		topPadding = in.readInt();
		botPadding = in.readInt();
		iconBeforeText = in.readInt();
		iconTextPadding = in.readInt();
		faded = in.readInt();
//		accessToken = in.readString();
//		connectionStr = in.readString();
	}
*/
//	public ListItem() {}
//	@ParcelConstructor
	public ListItem(int priority, int ID, String name, String iconAsString, Drawable drawable, int leftPadding,
	                int topPadding, int botPadding, Boolean iconBeforeText, int iconTextPadding, Boolean faded,
	                String accessToken, String connectionStr) {
		this.priority = priority;
		this.type = ID;
		this.name = name;
		this.iconAsString = iconAsString;
		this.drawable = drawable;
		this.leftPadding = leftPadding;
		this.topPadding = topPadding;
		this.botPadding = botPadding;
		this.iconBeforeText = (iconBeforeText == true) ? 1 : 0;
		this.iconTextPadding = iconTextPadding;
		this.faded = (faded == true) ? 1 : 0;
		this.accessToken = accessToken;
		this.connectionStr = connectionStr;
	}

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
		if (in.readByte() == 0) {
			iconBeforeText = null;
		} else {
			iconBeforeText = in.readInt();
		}
		if (in.readByte() == 0) {
			iconTextPadding = null;
		} else {
			iconTextPadding = in.readInt();
		}
		if (in.readByte() == 0) {
			faded = null;
		} else {
			faded = in.readInt();
		}
		accessToken = in.readString();
		connectionStr = in.readString();
	}

	public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
		@Override
		public ListItem createFromParcel(Parcel in) {
			return new ListItem(in);
		}

		@Override
		public ListItem[] newArray(int size) {
			return new ListItem[size];
		}
	};

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
	*//*
	public static final Parcelable.Creator<ListItem> CREATOR = new Parcelable.Creator<ListItem>() {
		@Override
		public ListItem createFromParcel(Parcel in) {
			return new ListItem(in);
		}

		@Override
		public ListItem[] newArray(int size) {
			return new ListItem[size];
		}
	};
*/
	public int getPriority() {
		return priority;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getIconAsString() {
		return iconAsString;
	}

	public Drawable getDrawable() {
		return drawable;
	}

	public int getLeftPadding() {
		return leftPadding;
	}

	public int getTopPadding() {
		return topPadding;
	}

	public int getBotPadding() {
		return botPadding;
	}

	public Boolean getIconBeforeText() {
		return iconBeforeText == 1;
	}

	public int getIconTextPadding() {
		return iconTextPadding;
	}

	public Boolean getFaded() {
		return faded == 1;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getConnectionStr() {
		return connectionStr;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIconAsString(String iconAsString) {
		this.iconAsString = iconAsString;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	public void setLeftPadding(int leftPadding) {
		this.leftPadding = leftPadding;
	}

	public void setTopPadding(int topPadding) {
		this.topPadding = topPadding;
	}

	public void setBotPadding(int botPadding) {
		this.botPadding = botPadding;
	}

	public void setIconBeforeText(Boolean iconBeforeText) {
		this.iconBeforeText = (iconBeforeText == true) ? 1 : 0;
	}

	public void setIconTextPadding(int iconTextPadding) {
		this.iconTextPadding = iconTextPadding;
	}

	public void setFaded(Boolean faded) {
		this.faded = (faded) ? 1 : 0;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setConnectionStr(String connectionStr) {
		this.connectionStr = connectionStr;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@NonNull
	@Override
	public String toString() {
		return "ListItem: NAME="+ getName()+ ", PRIORITY="+ getPriority()+ ", type="+ getType()+
				", ICON_AS_STRING="+ getIconAsString()+ ", DRAWABLE="+ getDrawable()+
				", PADDING_LEFT="+ getLeftPadding()+ ", PADDING_TOP="+ getTopPadding()+
				", PADDING_BOTTOM="+ getBotPadding()+ ", IS_ICON_BEFORE_TEXT="+ getIconBeforeText()+
				", PADDING_BETEEN_ICON_AND_TEXT="+ getIconTextPadding()+ ", IS_FADED="+ getFaded()+
				", TOKEN="+ getAccessToken()+ ", CONN_STR="+ getConnectionStr()+ "\n";
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
	public ListItem clone() throws CloneNotSupportedException {
		ListItem cloned = (ListItem) super.clone();
//		cloned.getDrawable() = this.getDrawable().clone();
		return cloned;
	}

	@Override
	public int describeContents() {
		return 0;
	}

/**/
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
//		dest.writeByte((byte) (iconBeforeText == null ? 0 : iconBeforeText ? 1 : 2));
		if (iconTextPadding == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(iconTextPadding);
		}
//		dest.writeByte((byte) (faded == null ? 0 : faded ? 1 : 2));
		dest.writeString(accessToken);
		dest.writeString(connectionStr);
	}
	/**/

	public static <T extends Parcelable> T copy(T orig) {
		Parcel p = Parcel.obtain();
		orig.writeToParcel(p, 0);
		p.setDataPosition(0);
		T copy = null;
		try {
			copy = (T) orig.getClass().getDeclaredConstructor(new Class[]{Parcel.class}).newInstance(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return copy;
	}

}

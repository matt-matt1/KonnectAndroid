package com.yumatechnical.konnectandroid.Model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

//@Parcel
public class ListItem {

	int priority;
	int ID;
	String name;
	String iconAsString;
	Drawable drawable;
	int leftPadding;
	int topPadding;
	int botPadding;
	Boolean iconBeforeText;
	int iconTextPadding;
	Boolean faded;


//	public ListItem() {}
//	@ParcelConstructor
	public ListItem(int priority, int ID, String name, String iconAsString, Drawable drawable, int leftPadding,
	                int topPadding, int botPadding, Boolean iconBeforeText, int iconTextPadding, Boolean faded) {
		this.priority = priority;
		this.ID = ID;
		this.name = name;
		this.iconAsString = iconAsString;
		this.drawable = drawable;
		this.leftPadding = leftPadding;
		this.topPadding = topPadding;
		this.botPadding = botPadding;
		this.iconBeforeText = iconBeforeText;
		this.iconTextPadding = iconTextPadding;
		this.faded = faded;
	}

	public int getPriority() {
		return priority;
	}

	public int getID() {
		return ID;
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
		return iconBeforeText;
	}

	public int getIconTextPadding() {
		return iconTextPadding;
	}

	public Boolean getFaded() {
		return faded;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setID(int ID) {
		this.ID = ID;
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
		this.iconBeforeText = iconBeforeText;
	}

	public void setIconTextPadding(int iconTextPadding) {
		this.iconTextPadding = iconTextPadding;
	}

	public void setFaded(Boolean faded) {
		this.faded = faded;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@NonNull
	@Override
	public String toString() {
		return "ListItem: NAME="+ getName()+ ", PRIORITY="+ getPriority()+ ", ID="+ getID() +", ICON_AS_STRING="+ getIconAsString()+
				", DRAWABLE="+ getDrawable()+ ", PADDING_LEFT="+ getLeftPadding()+ ", PADDING_TOP="+ getTopPadding()+
				", PADDING_BOTTOM="+ getBotPadding()+ ", IS_ICON_BEFORE_TEXT="+ getIconBeforeText()+
				", PADDING_BETEEN_ICON_AND_TEXT="+ getIconTextPadding()+ ", IS_FADED="+ getFaded()+ "\n";
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

}

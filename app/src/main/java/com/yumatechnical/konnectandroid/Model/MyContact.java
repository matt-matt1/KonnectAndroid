package com.yumatechnical.konnectandroid.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.List;

public class MyContact {

	private int id;
	private String name;
	private String sort_key;
	private String label;
	private Boolean has_number;
	private List<MyPhone> phoneList;

	public MyContact(int id, String name, String sort_key, String label, Boolean has_number, List<MyPhone> phones) {
		this.id = id;
		this.name = name;
		this.sort_key = sort_key;
		this.label = label;
		this.has_number = has_number;
		this.phoneList = phones;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSort_key() {
		return sort_key;
	}

	public String getLabel() {
		return label;
	}

	public Boolean getHas_number() {
		return has_number;
	}

	public List<MyPhone> getPhoneList() {
		return phoneList;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSort_key(String sort_key) {
		this.sort_key = sort_key;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setHas_number(Boolean has_number) {
		this.has_number = has_number;
	}

	public void setPhoneList(List<MyPhone> phoneList) {
		this.phoneList = phoneList;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	@NonNull
	@Override
	public String toString() {
		return super.toString();
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

	public static Comparator<MyContact> sortBySortKey = ((MyContact o1, MyContact o2) ->
			o1.getSort_key().toUpperCase().compareTo(o2.getSort_key().toUpperCase()));

//	Comparator<MyContact> compareBySortKey = new Comparator<MyContact>() {
//		@Override
//		public int compare(MyContact o1, MyContact o2) {
//			return o1.getSort_key().compareTo(o2.getSort_key());
//		}
//	};

//	public MyContact compa
}

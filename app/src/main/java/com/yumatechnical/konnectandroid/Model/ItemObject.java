package com.yumatechnical.konnectandroid.Model;

public class ItemObject {
	private int id;
	private String word;
	private String meaning;
	public ItemObject(int id, String word) {
		this.id = id;
		this.word = word;
	}
	public ItemObject(int id, String word, String meaning) {
		this.id = id;
		this.word = word;
		this.meaning = meaning;
	}
	public int getId() {
		return id;
	}
	public String getWord() {
		return word;
	}
	public String getMeaning() {
		return meaning;
	}
}

package com.yumatechnical.konnectandroid.Model.MicrosoftGraph;

public class Audio {
	//{
	//  "album": "string",
	//  "albumArtist": "string",
	//  "artist": "string",
	//  "bitrate": 128,
	//  "composers": "string",
	//  "copyright": "string",
	//  "disc": 0,
	//  "discCount": 0,
	//  "duration": 567,
	//  "genre": "string",
	//  "hasDrm": false,
	//  "isVariableBitrate": false,
	//  "title": "string",
	//  "track": 1,
	//  "trackCount": 16,
	//  "year": 2014
	//}

	private String album;
	private String albumArtist;
	private String artist;
	private int bitrate;// 128,
	private String composers;
	private String copyright;
	private int disc;// 0,
	private int discCount;// 0,
	private int duration;// 567,
	private String genre;
	private boolean hasDrm;// false,
	private boolean isVariableBitrate;// false,
	private String title;
	private int track;// 1,
	private int trackCount;// 16,
	private int year;// 2014


	Audio(String album, String albumArtist, String artist, int bitrate, String composers, String copyright, int disc, int discCount, int duration, String genre, boolean hasDrm, boolean isVariableBitrate, String title, int track, int trackCount, int year) {
		this.album = album;
		this.albumArtist = albumArtist;
		this.artist = artist;
		this.bitrate = bitrate;
		this.composers = composers;
		this.copyright = copyright;
		this.disc = disc;
		this.discCount = discCount;
		this.duration = duration;
		this.genre = genre;
		this.hasDrm = hasDrm;
		this.isVariableBitrate = isVariableBitrate;
		this.title = title;
		this.track = track;
		this.trackCount = trackCount;
		this.year = year;
	}


	public String getAlbum() {
		return album;
	}

	public String getAlbumArtist() {
		return albumArtist;
	}

	public String getArtist() {
		return artist;
	}

	public int getBitrate() {
		return bitrate;
	}

	public String getComposers() {
		return composers;
	}

	public String getCopyright() {
		return copyright;
	}

	public int getDisc() {
		return disc;
	}

	public int getDiscCount() {
		return discCount;
	}

	public int getDuration() {
		return duration;
	}

	public String getGenre() {
		return genre;
	}

	public boolean isHasDrm() {
		return hasDrm;
	}

	public boolean isVariableBitrate() {
		return isVariableBitrate;
	}

	public String getTitle() {
		return title;
	}

	public int getTrack() {
		return track;
	}

	public int getTrackCount() {
		return trackCount;
	}

	public int getYear() {
		return year;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public void setComposers(String composers) {
		this.composers = composers;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public void setDisc(int disc) {
		this.disc = disc;
	}

	public void setDiscCount(int discCount) {
		this.discCount = discCount;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setHasDrm(boolean hasDrm) {
		this.hasDrm = hasDrm;
	}

	public void setVariableBitrate(boolean variableBitrate) {
		isVariableBitrate = variableBitrate;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTrack(int track) {
		this.track = track;
	}

	public void setTrackCount(int trackCount) {
		this.trackCount = trackCount;
	}

	public void setYear(int year) {
		this.year = year;
	}

}

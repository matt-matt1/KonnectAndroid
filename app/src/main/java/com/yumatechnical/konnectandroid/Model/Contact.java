package com.yumatechnical.konnectandroid.Model;

import android.net.Uri;

import java.sql.Timestamp;

public class Contact {

	public String sort_key;                         //Test..
	public Uri photo_uri;                           //null..
	public Boolean send_to_voicemail;               //0..
	public String contact_status;                   //null..
	public String contact_status_label;             //null..
	public Boolean pinned;                          //0..
	public String display_name;                     //Test..
	public String phonebook_label_alt;              //T..
	public int phonebook_bucket;                    //20..
	public String contact_status_res_package;       //null..
	public String photo_id;                         //null..
	public Boolean in_default_directory;            //1..
	public String custom_ringtone;                  //null..
	public int _id;                                 //1..
	public int times_contacted;                     //0..
	public String phonebook_label;                  //T..
	public String lookup;                           //1560i288887ce0edaad59..
	public String display_name_alt;                 //Test..
	public String phonetic_name;                    //null..
	public int last_time_contacted;                 //0..
	public Timestamp contact_last_updated_timestamp;   //1568391266676..
	public Boolean has_phone_number;                //1..
	public Boolean in_visible_group;                //1..
	public Boolean is_user_profile;                 //0..
	public int display_name_source;                 //40..
	public String photo_file_id;                    //null..
	public String contact_status_ts;                //null..
	public int phonebook_bucket_alt;                //20..
	public String sort_key_alt;                     //Test..
	public String contact_presence;                 //null..
	public Boolean starred;                         //0..
	public String photo_thumb_uri;                  //null..
	public String contact_status_icon;              //null..
	public String contact_chat_capability;          //null..
	public Boolean phonetic_name_style;             //0..
	public int name_raw_contact_id;                 //1..


	public Contact(String sort_key, Uri photo_uri, Boolean send_to_voicemail, String contact_status, String contact_status_label, Boolean pinned, String display_name, String phonebook_label_alt, int phonebook_bucket, String contact_status_res_package, String photo_id, Boolean in_default_directory, String custom_ringtone, int _id, int times_contacted, String phonebook_label, String lookup, String display_name_alt, String phonetic_name, int last_time_contacted, Timestamp contact_last_updated_timestamp, Boolean has_phone_number, Boolean in_visible_group, Boolean is_user_profile, int display_name_source, String photo_file_id, String contact_status_ts, int phonebook_bucket_alt, String sort_key_alt, String contact_presence, Boolean starred, String photo_thumb_uri, String contact_status_icon, String contact_chat_capability, Boolean phonetic_name_style, int name_raw_contact_id) {
		this.sort_key = sort_key;
		this.photo_uri = photo_uri;
		this.send_to_voicemail = send_to_voicemail;
		this.contact_status = contact_status;
		this.contact_status_label = contact_status_label;
		this.pinned = pinned;
		this.display_name = display_name;
		this.phonebook_label_alt = phonebook_label_alt;
		this.phonebook_bucket = phonebook_bucket;
		this.contact_status_res_package = contact_status_res_package;
		this.photo_id = photo_id;
		this.in_default_directory = in_default_directory;
		this.custom_ringtone = custom_ringtone;
		this._id = _id;
		this.times_contacted = times_contacted;
		this.phonebook_label = phonebook_label;
		this.lookup = lookup;
		this.display_name_alt = display_name_alt;
		this.phonetic_name = phonetic_name;
		this.last_time_contacted = last_time_contacted;
		this.contact_last_updated_timestamp = contact_last_updated_timestamp;
		this.has_phone_number = has_phone_number;
		this.in_visible_group = in_visible_group;
		this.is_user_profile = is_user_profile;
		this.display_name_source = display_name_source;
		this.photo_file_id = photo_file_id;
		this.contact_status_ts = contact_status_ts;
		this.phonebook_bucket_alt = phonebook_bucket_alt;
		this.sort_key_alt = sort_key_alt;
		this.contact_presence = contact_presence;
		this.starred = starred;
		this.photo_thumb_uri = photo_thumb_uri;
		this.contact_status_icon = contact_status_icon;
		this.contact_chat_capability = contact_chat_capability;
		this.phonetic_name_style = phonetic_name_style;
		this.name_raw_contact_id = name_raw_contact_id;
	}

}

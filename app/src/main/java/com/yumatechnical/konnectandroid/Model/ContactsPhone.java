package com.yumatechnical.konnectandroid.Model;

import android.net.Uri;

import androidx.core.content.PermissionChecker;

import java.sql.Timestamp;

public class ContactsPhone {
	/*
sort_key=Test
photo_uri=null
status_label=null
status_res_package=null
status_ts=null
display_name=Test
last_time_used=null
mimetype=vnd.android.cursor.item/phone_v2
phonebook_label_alt=T
data6=null
version=10.
photo_id=null
data3=null
custom_ringtone=null
times_contacted=0
account_type_and_data_set=com.google
data15=null
data7=null
dirty=0
raw_contact_is_user_profile=0
data_set=null
phonebook_label=T
data10=null
res_package=null
data11=null
account_type=com.google
lookup=1560i288887ce0edaad59
display_name_alt=Test
phonetic_name=null
last_time_contacted=0
contact_last_updated_timestamp=1568391266676
data13=null
in_visible_group=1
data9=null
chat_capability=null
carrier_presence=0
data_sync1=null
sort_key_alt=Test
contact_presence=null
data_version=1
phonetic_name_style=0
name_raw_contact_id=1.
raw_contact_id=1.
send_to_voicemail=0
data4=null
data12=null
contact_status=null
contact_status_label=null
pinned=0
status_icon=null
data1=000.9.
status=null
phonebook_bucket=20
data_sync2=null
contact_status_res_package=null
in_default_directory=1
_id=2..
hash_id=xeh1RjdQTl6/ho78kVrgnLi6HDs=
is_super_primary=0
contact_id=1.
data5=null
is_primary=0
data8=null
data_sync4=11
has_phone_number=1
photo_file_id=null
display_name_source=40
data_sync3=null
data14=null
backup_id=null
contact_status_ts=null
phonebook_bucket_alt=20
mode=null
data2=2.
group_sourceid=null
starred=0
photo_thumb_uri=null
times_used=null
contact_status_icon=null
contact_chat_capability=null
account_name=yumatechnical@gmail.com
sourceid=288887ce0edaad59
	 */

	public String sort_key;
	public Object photo_uri;
	public Object status_label;
	public Object status_res_package;
	public Object status_ts;
	public String display_name;
	public Object last_time_used;
	public String mimetype;
	public String phonebook_label_alt;
//	public Object data6;
	public int version;
	public Object photo_id;
//	public Object data3;
	public Object custom_ringtone;
	public int times_contacted;
	public String account_type_and_data_set;
//	public Object data15;
//	public Object data7;
	public Boolean dirty;
	public Boolean raw_contact_is_user_profile;
//	public Object data_set;
	public String phonebook_label;
//	public Object data10;
	public Object res_package;
//	public Object data11;
	public String account_type;
	public String lookup;
	public String display_name_alt;
	public Object phonetic_name;
	public Boolean last_time_contacted;
	public Timestamp contact_last_updated_timestamp;
//	public Object data13;
	public Boolean in_visible_group;
//	public Object data9;
	public Object chat_capability;
	public Boolean carrier_presence;
//	public Object data_sync1;
	public String sort_key_alt;
	public Object contact_presence;
//	public int data_version;
	public Boolean phonetic_name_style;
	public int name_raw_contact_id;
	public int raw_contact_id;
	public Boolean send_to_voicemail;
//	public Object data4;
//	public Object data12;
	public Object contact_status;
	public Object contact_status_label;
	public Boolean pinned;
	public Object status_icon;
//	public Object data1;
	public Object status;
	public int phonebook_bucket;
//	public Object data_sync2;
	public Object contact_status_res_package;
	public Boolean in_default_directory;
	public int _id;
	public String hash_id;
	public Boolean is_super_primary;
	public int contact_id;
//	public Object data5;
	public Boolean is_primary;
//	public Object data8;
//	public int data_sync4;
	public Boolean has_phone_number;
	public Object photo_file_id;
	public int display_name_source;
//	public Object data_sync3;
//	public Object data14;
	public Object backup_id;
	public Object contact_status_ts;
	public int phonebook_bucket_alt;
	public Object mode;
//	public int data2;
	public Object group_sourceid;
	public Boolean starred;
	public Uri photo_thumb_uri;
	public Object times_used;
	public Object contact_status_icon;
	public Object contact_chat_capability;
	public String account_name;
	public String sourceid;

	public ContactsPhone(String sort_key, Object photo_uri, Object status_label, PermissionChecker status_res_package,
	                     Object status_ts, String display_name, Object last_time_used, String mimetype,
	                     String phonebook_label_alt, /*Object data6,*/ int version, Object photo_id,
	                     /*Object data3,*/ Object custom_ringtone, int times_contacted,
	                     String account_type_and_data_set, /*Object data15,*/ /*Object data7,*/ Boolean dirty,
	                     Boolean raw_contact_is_user_profile, /*Object data_set,*/ String phonebook_label,
	                     /*Object data10,*/ Object res_package, /*Object data11,*/ String account_type,
	                     String lookup, String display_name_alt, Object phonetic_name,
	                     Boolean last_time_contacted, Timestamp contact_last_updated_timestamp,
	                     /*Object data13,*/ Boolean in_visible_group, /*Object data9,*/ Object chat_capability,
	                     Boolean carrier_presence, /*Object data_sync1,*/ String sort_key_alt,
	                     PermissionChecker contact_presence, /*int data_version,*/ Boolean phonetic_name_style,
	                     int name_raw_contact_id, int raw_contact_id, Boolean send_to_voicemail,
	                     /*Object data4,*/ /*Object data12,*/ Object contact_status, Object contact_status_label,
	                     Boolean pinned, Object status_icon, /*Object data1,*/ Object status, int phonebook_bucket,
	                     /*Object data_sync2,*/ Object contact_status_res_package, Boolean in_default_directory,
	                     int _id, String hash_id, Boolean is_super_primary, int contact_id,
	                     /*Object data5,*/ Boolean is_primary, /*Object data8,*/ /*int data_sync4,*/
	                     Boolean has_phone_number, Object photo_file_id, int display_name_source,
	                     /*Object data_sync3,*/ /*Object data14,*/ Object backup_id, Object contact_status_ts,
	                     int phonebook_bucket_alt, String mode, /*int data2,*/ Object group_sourceid,
	                     Boolean starred, Uri photo_thumb_uri, Object times_used, Object contact_status_icon,
	                     Object contact_chat_capability, String account_name, String sourceid) {
		this.sort_key = sort_key;
		this.photo_uri = photo_uri;
		this.status_label = status_label;
		this.status_res_package = status_res_package;
		this.status_ts = status_ts;
		this.display_name = display_name;
		this.last_time_used = last_time_used;
		this.mimetype = mimetype;
		this.phonebook_label_alt = phonebook_label_alt;
//		this.data6 = data6;
		this.version = version;
		this.photo_id = photo_id;
//		this.data3 = data3;
		this.custom_ringtone = custom_ringtone;
		this.times_contacted = times_contacted;
		this.account_type_and_data_set = account_type_and_data_set;
//		this.data15 = data15;
//		this.data7 = data7;
		this.dirty = dirty;
		this.raw_contact_is_user_profile = raw_contact_is_user_profile;
//		this.data_set = data_set;
		this.phonebook_label = phonebook_label;
//		this.data10 = data10;
		this.res_package = res_package;
//		this.data11 = data11;
		this.account_type = account_type;
		this.lookup = lookup;
		this.display_name_alt = display_name_alt;
		this.phonetic_name = phonetic_name;
		this.last_time_contacted = last_time_contacted;
		this.contact_last_updated_timestamp = contact_last_updated_timestamp;
//		this.data13 = data13;
		this.in_visible_group = in_visible_group;
//		this.data9 = data9;
		this.chat_capability = chat_capability;
		this.carrier_presence = carrier_presence;
//		this.data_sync1 = data_sync1;
		this.sort_key_alt = sort_key_alt;
		this.contact_presence = contact_presence;
//		this.data_version = data_version;
		this.phonetic_name_style = phonetic_name_style;
		this.name_raw_contact_id = name_raw_contact_id;
		this.raw_contact_id = raw_contact_id;
		this.send_to_voicemail = send_to_voicemail;
//		this.data4 = data4;
//		this.data12 = data12;
		this.contact_status = contact_status;
		this.contact_status_label = contact_status_label;
		this.pinned = pinned;
		this.status_icon = status_icon;
//		this.data1 = data1;
		this.status = status;
		this.phonebook_bucket = phonebook_bucket;
//		this.data_sync2 = data_sync2;
		this.contact_status_res_package = contact_status_res_package;
		this.in_default_directory = in_default_directory;
		this._id = _id;
		this.hash_id = hash_id;
		this.is_super_primary = is_super_primary;
		this.contact_id = contact_id;
//		this.data5 = data5;
		this.is_primary = is_primary;
//		this.data8 = data8;
//		this.data_sync4 = data_sync4;
		this.has_phone_number = has_phone_number;
		this.photo_file_id = photo_file_id;
		this.display_name_source = display_name_source;
//		this.data_sync3 = data_sync3;
//		this.data14 = data14;
		this.backup_id = backup_id;
		this.contact_status_ts = contact_status_ts;
		this.phonebook_bucket_alt = phonebook_bucket_alt;
		this.mode = mode;
//		this.data2 = data2;
		this.group_sourceid = group_sourceid;
		this.starred = starred;
		this.photo_thumb_uri = photo_thumb_uri;
		this.times_used = times_used;
		this.contact_status_icon = contact_status_icon;
		this.contact_chat_capability = contact_chat_capability;
		this.account_name = account_name;
		this.sourceid = sourceid;
	}
}

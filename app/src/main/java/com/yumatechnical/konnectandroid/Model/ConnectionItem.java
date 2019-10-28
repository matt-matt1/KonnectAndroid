package com.yumatechnical.konnectandroid.Model;

import android.net.Uri;

import androidx.annotation.NonNull;

import org.parceler.Parcel;
//import org.parceler.ParcelConstructor;

import java.net.MalformedURLException;
import java.net.URL;

@Parcel
public class ConnectionItem {

	int ID;
	int type;
	String connectionName;
	String accessToken;
	String scheme;
	String username;
	String password;
	String host;
	int port;
	String shareName;
	String path;


	@SuppressWarnings("unused")
	public ConnectionItem() {}
//	@ParcelConstructor
	@SuppressWarnings("unused")
	public ConnectionItem (int ID, int type, String connectionName, String accessToken, String scheme,
	                       String username, String password, String host, int port, String shareName, String path) {
		this.ID = ID;
		this.type = type;
		this.connectionName = connectionName;
		this.accessToken = accessToken;
		this.scheme = scheme;
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.shareName = shareName;
		this.path = path;
	}


	@SuppressWarnings("unused")
	public int getID() {
		return ID;
	}

	@SuppressWarnings("unused")
	public void setID(int ID) {
		this.ID = ID;
	}

	@SuppressWarnings("unused")
	public int getType() {
		return type;
	}

	@SuppressWarnings("unused")
	public void setType(int type) {
		this.type = type;
	}

	@SuppressWarnings("unused")
	public String getConnectionName() {
		return connectionName;
	}

	@SuppressWarnings("unused")
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	@SuppressWarnings("unused")
	public String getAccessToken() {
		return accessToken;
	}

	@SuppressWarnings("unused")
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@SuppressWarnings("unused")
	public String getScheme() {
		return scheme;
	}

	@SuppressWarnings("unused")
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	@SuppressWarnings("unused")
	public String getUsername() {
		return username;
	}

	@SuppressWarnings("unused")
	public void setUsername(String username) {
		this.username = username;
	}

	@SuppressWarnings("unused")
	public String getPassword() {
		return password;
	}

	@SuppressWarnings("unused")
	public void setPassword(String password) {
		this.password = password;
	}

	@SuppressWarnings("unused")
	public String getHost() {
		return host;
	}

	@SuppressWarnings("unused")
	public void setHost(String host) {
		this.host = host;
	}

	@SuppressWarnings("unused")
	public int getPort() {
		return port;
	}

	@SuppressWarnings("unused")
	public void setPort(int port) {
		this.port = port;
	}

	@SuppressWarnings("unused")
	public String getShareName() {
		return shareName;
	}

	@SuppressWarnings("unused")
	public void setShareName(String shareName) {
		this.shareName = shareName;
	}

	@SuppressWarnings("unused")
	public String getPath() {
		return path;
	}

	@SuppressWarnings("unused")
	public void setPath(String path) {
		this.path = path;
	}

	@NonNull
	@Override
	public String toString() {
//		return super.toString();
		return "ConnectionItem("+ getID()+ ", TYPE="+ getType()+ ", NAME="+ getConnectionName()+
				", TOKEN="+ getAccessToken()+ ", SCHEME="+ getScheme()+ ", USER="+ getUsername()+
				", PASS="+ getPassword()+ ", HOST="+ getHost()+ ", PORT="+ getPort()+
				", SHARE="+ getShareName()+ ", PATH="+ getPath()+ ")";
	}

	public String toConnectionString() {
		StringBuilder output = new StringBuilder();
		if (getScheme() != null && !getScheme().equals("")) {
			output.append(getScheme());
			output.append("/");
		}
		if (getUsername() != null && !getUsername().equals("")) {
			output.append(getUsername());
			output.append(":");
			if (getPassword() != null && !getPassword().equals("")) {
				output.append(getPassword());
			}
			output.append("@");
		}
		if (getHost() != null && !getHost().equals("")) {
			output.append(getHost());
			if (getPort() > 0) {
				output.append(":");
				output.append(getPort());
			}
			output.append("/");
		}
		output.append("/");
		if (getPath() != null && !getPath().equals("")) {
			if (!getPath().startsWith("/")) {
				output.append("/");
			}
			output.append(getPath());
		}
		try {
			URL url = new URL(output.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return output.toString();
	}

	@SuppressWarnings("unused")
	public String toConnectionUri() {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme(getScheme());
		builder.encodedAuthority(getUsername()+ ":"+ getPassword()+ "@"+ getHost());
		String path = "";
		if (getShareName() != null && !getShareName().equals("")) {
			path += getShareName()+ "/";
		}
		builder.path(path+ getPath());
		return builder.build().toString();
	}

}

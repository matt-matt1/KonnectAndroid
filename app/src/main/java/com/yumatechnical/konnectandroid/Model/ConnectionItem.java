package com.yumatechnical.konnectandroid.Model;

import android.net.Uri;

import androidx.annotation.NonNull;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

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
	String path;


	public ConnectionItem() {}
//	@ParcelConstructor
	public ConnectionItem (int ID, int type, String connectionName, String accessToken, String scheme,
	                       String username, String password, String host, int port, String path) {
		this.ID = ID;
		this.type = type;
		this.connectionName = connectionName;
		this.accessToken = accessToken;
		this.scheme = scheme;
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.path = path;
	}


	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@NonNull
	@Override
	public String toString() {
//		return super.toString();
		return "ConnectionItem("+ getID()+ ", type="+ getType()+ ", name="+ getConnectionName()+
				", token="+ getAccessToken()+ ", scheme="+ getScheme()+ ", user="+ getUsername()+
				", pass="+ getPassword()+ ", host="+ getHost()+ ", port="+ getPort()+ ", path="+ getPath()+ ")";
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

	public String toConnectionUri() {
		Uri.Builder builder = new Uri.Builder();
		builder.scheme(getScheme());
		builder.encodedAuthority(getUsername()+ ":"+ getPassword()+ "@"+ getHost());
		builder.path(getPath());
		return builder.build().toString();
	}

}

package com.yumatechnical.konnectandroid.Model;

import androidx.annotation.NonNull;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

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
}
